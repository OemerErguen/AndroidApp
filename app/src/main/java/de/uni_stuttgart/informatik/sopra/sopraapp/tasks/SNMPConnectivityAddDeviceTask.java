package de.uni_stuttgart.informatik.sopra.sopraapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.snmp4j.smi.OID;

import java.util.List;
import java.util.Locale;

import de.uni_stuttgart.informatik.sopra.sopraapp.CockpitMainActivity;
import de.uni_stuttgart.informatik.sopra.sopraapp.CockpitPreferenceManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.CockpitStateManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.fragment.DeviceMonitorViewFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.SnmpConnection;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.SnmpManager;

/**
 * check snmp connectivity and add device to app
 * <p>
 * if query response is null no connection possible
 */
public class SNMPConnectivityAddDeviceTask extends AsyncTask<Void, Void, Boolean> {

    public static final String TAG = SNMPConnectivityAddDeviceTask.class.getName();

    private final ThreadLocal<Context> context = new ThreadLocal<>();
    private final DeviceMonitorViewFragment deviceMonitorViewFragment;
    private final int connectionTestTimeout;
    private final int connectionTestRetries;
    private DeviceConfiguration usedDeviceConfiguration;
    private LinearLayout progressRow;
    private boolean doesConnectionExist = false;
    private int connectionTestTotal = 0;
    private SnmpConnection connector;

    /**
     * constructor
     *
     * @param context
     * @param deviceMonitorViewFragment
     * @param progressRow
     */
    public SNMPConnectivityAddDeviceTask(Context context, DeviceConfiguration deviceConfiguration,
                                         DeviceMonitorViewFragment deviceMonitorViewFragment, LinearLayout progressRow) {
        this.context.set(context);
        this.usedDeviceConfiguration = deviceConfiguration;
        this.deviceMonitorViewFragment = deviceMonitorViewFragment;
        this.progressRow = progressRow;
        connectionTestTotal = SnmpManager.getInstance().getTotalConnectionTestCount();

        CockpitPreferenceManager preferenceManager = new CockpitPreferenceManager(context);
        this.connectionTestRetries = preferenceManager.getConnectionTestRetries();
        this.connectionTestTimeout = preferenceManager.getConnectionTestTimeout();
    }

    /**
     * background device test tasks to prepare communication
     *
     * @param voids
     * @return
     */
    @Override
    protected Boolean doInBackground(Void... voids) {
        SnmpManager snmpManager = SnmpManager.getInstance();
        if (snmpManager.doesConnectionExist(usedDeviceConfiguration.getUniqueDeviceId())) {
            Log.d(TAG, "connection does already exist");
            doesConnectionExist = true;
            return false;
        }
        Log.d(TAG, "connection is new");

        // for v3 connections do a (possibly large) connection test to get correct auth and privProtocol
        if (usedDeviceConfiguration.getSnmpVersionEnum() == DeviceConfiguration.SNMP_VERSION.v3) {
            Log.d(TAG, "start connection check v3");
            publishProgress();
            Pair<OID, OID> firstCombination;
            if (usedDeviceConfiguration.isConnectionTestNeeded()) {
                Log.d(TAG, "run connection test");
                List<Pair<OID, OID>> workingSecuritySettings =
                        SnmpManager.getInstance().testConnections(usedDeviceConfiguration, new Runnable() {
                            @Override
                            public void run() {
                                publishProgress();
                            }
                        }, connectionTestTimeout, connectionTestRetries);
                if (workingSecuritySettings.isEmpty()) {
                    Log.d(TAG, "no auth and privProtocol matched");
                    return false;
                }
                firstCombination = workingSecuritySettings.get(0);
            } else {
                Log.d(TAG, "skip connection test");
                firstCombination = new Pair<>(usedDeviceConfiguration.getAuthProtocol(), usedDeviceConfiguration.getPrivProtocol());
            }
            // TODO use strongest
            Log.d(TAG, "selected authProtocol: " + firstCombination.first + " and privProtocol: " + firstCombination.second);
            usedDeviceConfiguration.setAuthProtocol(firstCombination.first);
            usedDeviceConfiguration.setPrivProtocol(firstCombination.second);
        }
        connector = SnmpManager.getInstance().getConnection(usedDeviceConfiguration);
        if (connector == null) {
            Log.w(TAG, "no connection available");
            return false;
        }
        // sysDesct is the test oid
        if (!connector.canPing(usedDeviceConfiguration)) {
            return false;
        }
        return true;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        Log.d(TAG, "progress update");
        TextView infoTextView = this.progressRow.findViewById(R.id.connection_attempt_count_label);
        infoTextView.setText(String.format(getCurrentLocale(context.get()), "%s %d/%d",
                context.get().getString(R.string.connection_attempt_label),
                SnmpManager.getInstance().getCurrentConnectionTestsDoneCount(),
                connectionTestTotal)
        );
    }

    /**
     * method for current locale
     *
     * @param context
     * @return
     */
    @SuppressWarnings({"deprecation"})
    protected Locale getCurrentLocale(Context context) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            //noinspection deprecation
            return context.getResources().getConfiguration().locale;
        } else {
            return context.getResources().getConfiguration().getLocales().get(0);
        }
    }

    @Override
    protected void onPostExecute(Boolean s) {
        super.onPostExecute(s);
        if (s == null || !s) {
            Log.e(TAG, "Connection test NOT successful! Could not connect!");
            if (doesConnectionExist) {
                Toast.makeText(context.get(),
                        R.string.connection_already_exist, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context.get(),
                        R.string.connection_test_not_successful_label, Toast.LENGTH_LONG).show();
            }
            AsyncTask.execute(() -> {
                SnmpManager.getInstance().removeConnection(usedDeviceConfiguration);
            });
        } else {
            Log.i(TAG, "Connection test successful");
            Toast.makeText(context.get(),
                    context.get().getString(R.string.connection_test_successful_label), Toast.LENGTH_SHORT).show();

            DeviceManager.getInstance().add(usedDeviceConfiguration, false);
            // refresh ui on list change!
            deviceMonitorViewFragment.getRecyclerView().getAdapter().notifyDataSetChanged();

            CockpitMainActivity cockpitMainActivity = (CockpitMainActivity) context.get();
            cockpitMainActivity.checkNoData();
        }
        progressRow.setVisibility(View.GONE);
        TextView infoTextView = this.progressRow.findViewById(R.id.connection_attempt_count_label);
        infoTextView.setText(context.get().getString(R.string.connection_attempt_label));
        CockpitStateManager.getInstance().setConnecting(false);
        CockpitStateManager.getInstance().setConnectionTask(null);
    }

    public void setProgressRow(LinearLayout progressRow) {
        this.progressRow = progressRow;
        publishProgress();
    }
}