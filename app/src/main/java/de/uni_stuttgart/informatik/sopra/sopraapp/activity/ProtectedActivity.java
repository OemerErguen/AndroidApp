package de.uni_stuttgart.informatik.sopra.sopraapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.Observer;
import java.util.concurrent.ThreadPoolExecutor;

import de.uni_stuttgart.informatik.sopra.sopraapp.CockpitMainActivity;
import de.uni_stuttgart.informatik.sopra.sopraapp.CockpitPreferenceManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.CockpitStateManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.service.CockpitStateService;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.SnmpManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.util.BooleanObservable;

/**
 * this interface adds security mechanism of the app to an activity.
 * We use an interface to spread our security mechanisms through the app.
 * This class should be called a trait.
 *
 * Usage:
 *  - {@link #initObservables(Activity, AlertHelper, OnSecurityStateChangeListener)} in #onCreate of your activity.
 *  - and add this to your activity:
 *     @Override
 *     protected void onStart() {
 *         super.onStart();
 *         startProtection(this);
 *     }
 *
 *     @Override
 *     protected void onRestart() {
 *         super.onRestart();
 *         restartTrigger(this);
 *     }
 *
 */
public interface ProtectedActivity {

    static final String LOG_TAG = ProtectedActivity.class.getName();

    /**
     * magic init method to setup alert window handling for an activity
     *
     * @param activity
     * @param alertHelper
     * @param listener
     */
    default void initObservables(@NonNull Activity activity, @NonNull AlertHelper alertHelper,
                                 @Nullable OnSecurityStateChangeListener listener) {
        Log.d(LOG_TAG, "observables inited for " + this.getClass().getSimpleName());
        BooleanObservable isNetworkSecureObservable = CockpitStateManager.getInstance().getNetworkSecurityObservable();
        BooleanObservable isInTimeoutObservable = CockpitStateManager.getInstance().getIsInTimeoutsObservable();
        BooleanObservable isInSessionTimeoutObservable = CockpitStateManager.getInstance().getIsInSessionTimeoutObservable();
        // ensure only one observer at the same time
        if (activity instanceof CockpitMainActivity) {
            isNetworkSecureObservable.deleteObservers();
            isInTimeoutObservable.deleteObservers();
            isInSessionTimeoutObservable.deleteObservers();
        }

        isNetworkSecureObservable.addObserver(getNetworkSecurityObserver(activity, alertHelper, listener));
        isInTimeoutObservable.addObserver(getConnectionTimeoutObserver(alertHelper));
        isInSessionTimeoutObservable.addObserver(getIsInSessionTimeoutObserver(alertHelper));

        checkSessionTimeout(activity);
    }

    /**
     * method to check session timeout state
     *
     * @param activity
     */
    default void checkSessionTimeout(@NonNull Activity activity) {
        CockpitPreferenceManager cockpitPreferenceManager = new CockpitPreferenceManager(activity);

        cockpitPreferenceManager.checkSessionTimeout();
    }

    /**
     * call this in #onStop of activities!
     *
     * @param activity
     */
    default void stopProtection(Activity activity) {
        Log.d(LOG_TAG, "stop cockpit service in " + this.getClass().getName());
        Intent stateServiceIntent = new Intent(activity, CockpitStateService.class);
        activity.stopService(stateServiceIntent);
    }

    /**
     * call this in #onStart of activities!
     *
     * @param activity
     */
    default void startProtection(Activity activity) {
        Log.d(LOG_TAG, "start cockpit service in " + this.getClass().getSimpleName());
        Intent stateServiceIntent = new Intent(activity, CockpitStateService.class);
        activity.startService(stateServiceIntent);
    }

    /**
     * should be called in #onRestart
     *
     * @param activity
     */
    default void restartTrigger(Activity activity) {
        Log.d(LOG_TAG, "restart trigger called");
        startProtection(activity);
        checkSessionTimeout(activity);
    }

    /**
     * this method is called when use has clicked "retry connection" after connection timeout
     */
    public void restartQueryCall();

    /**
     * Generic method of network security observer
     *
     * @param activity
     * @param alertHelper
     * @param listener
     * @return
     */
    default Observer getNetworkSecurityObserver(Activity activity, AlertHelper alertHelper, OnSecurityStateChangeListener listener) {
        return (booleanObservable, arg) -> {
            boolean isNetworkSecureState = ((BooleanObservable) booleanObservable).getValue();
            Log.d(LOG_TAG, "observable changed! new value: " + isNetworkSecureState);
            if (((BooleanObservable) booleanObservable).getValue()) {
                Toast.makeText(activity,
                        "Im sicheren Netzwerk!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity,
                        "Kein sicheres Netzwerk!", Toast.LENGTH_SHORT).show();
            }

            if (!isNetworkSecureState) {
                // immediately stop running connection tasks
                if (listener != null) {
                    listener.onInsecureState();
                }

                ThreadPoolExecutor threadPoolExecutor = SnmpManager.getInstance().getThreadPoolExecutor();
                if (threadPoolExecutor.getActiveCount() > 0) {
                    List<Runnable> runnables = threadPoolExecutor.shutdownNow();
                    Log.d(LOG_TAG, "stopped thread pool with jobs: " + runnables.toString());
                }
                alertHelper.showNotSecureAlert();
            } else {
                if (listener != null) {
                    listener.onSecureState();
                }
                alertHelper.closeAllSecurityAlerts();
            }
        };
    }

    /**
     * generic method of network timeout observer
     *
     * @param alertHelper
     * @return
     */
    default Observer getConnectionTimeoutObserver(AlertHelper alertHelper) {
        return (observable, arg) -> {
            boolean isInTimeouts = ((BooleanObservable) observable).getValue();
            Log.d(LOG_TAG, "timeout observable changed: " + isInTimeouts);
            if (isInTimeouts) {
                alertHelper.showDeviceTimeoutDialog();
            } else {
                alertHelper.closeAllTimeoutAlerts();
            }
        };
    }

    /**
     * generic method of app session timeout observer
     *
     * @param alertHelper
     * @return
     */
    default Observer getIsInSessionTimeoutObserver(AlertHelper alertHelper) {
        return (observable, arg) -> {
            boolean isInSessionTimeout = ((BooleanObservable) observable).getValue();
            Log.d(LOG_TAG, "session timeout observable changed: " + isInSessionTimeout);
            if (isInSessionTimeout) {
                ThreadPoolExecutor threadPoolExecutor = SnmpManager.getInstance().getThreadPoolExecutor();
                if (threadPoolExecutor.getActiveCount() > 0) {
                    List<Runnable> runnables = threadPoolExecutor.shutdownNow();
                    Log.d(LOG_TAG, "stopped thread pool with jobs: " + runnables.toString());
                }
                DeviceManager.getInstance().removeAllItems();
                alertHelper.showSessionTimeoutDialog();
            } else {
                alertHelper.closeAllTimeoutAlerts();
            }
        };
    }

    /**
     * helper interface to hook into network security alert actions
     */
    interface OnSecurityStateChangeListener {
        public void onInsecureState();

        public void onSecureState();
    }
}
