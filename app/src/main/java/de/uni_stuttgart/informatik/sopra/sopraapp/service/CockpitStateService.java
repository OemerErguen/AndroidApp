package de.uni_stuttgart.informatik.sopra.sopraapp.service;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import de.uni_stuttgart.informatik.sopra.sopraapp.CockpitStateManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.network.NetworkChangeReceiver;
import de.uni_stuttgart.informatik.sopra.sopraapp.network.WifiNetworkManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.util.BooleanObservable;

/**
 * observes network security
 */
public class CockpitStateService extends android.app.Service {
    public static final String TAG = CockpitStateService.class.getName();
    private BooleanObservable isNetworkSecureObservable;
    private WifiNetworkManager wifiNetworkManager;
    private NetworkChangeReceiver networkChangeReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // we don't bind sth
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "starting cockpit state service");
        // register broadcast receivers with this context
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        wifiNetworkManager = WifiNetworkManager.getInstance(this);
        isNetworkSecureObservable = CockpitStateManager.getInstance().getNetworkSecurityObservable();
        networkChangeReceiver = new NetworkChangeReceiver(wifiNetworkManager, isNetworkSecureObservable);
        registerReceiver(networkChangeReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "finishing cockpit state service");
        if (networkChangeReceiver != null) {
            unregisterReceiver(networkChangeReceiver);
        }
    }
}
