package de.uni_stuttgart.informatik.sopra.sopraapp.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import de.uni_stuttgart.informatik.sopra.sopraapp.CockpitMainActivity;
import de.uni_stuttgart.informatik.sopra.sopraapp.util.BooleanObservable;

/**
 * this {@link BroadcastReceiver} child class receives network change events
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    private static final String TAG = NetworkChangeReceiver.class.getName();
    private final WifiNetworkManager wifiNetworkManager;
    private final BooleanObservable isNetworkSecure;

    /**
     * constructor
     *
     * @param wifiNetworkManager
     * @param isNetworkSecureObservable
     */
    public NetworkChangeReceiver(WifiNetworkManager wifiNetworkManager, BooleanObservable isNetworkSecureObservable) {
        this.wifiNetworkManager = wifiNetworkManager;
        isNetworkSecure = isNetworkSecureObservable;
    }

    /**
     * most important method of this class
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("app", "Network connectivity change");

        wifiNetworkManager.updateMode();
        boolean wasSet = false;
        if (intent != null) {
            NetworkInfo netInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (netInfo != null && ConnectivityManager.TYPE_WIFI == netInfo.getType()) {
                String ssid = netInfo.getExtraInfo();
                Log.d(TAG, "Detected WiFi SSID: " + ssid);
                if (isValidSsid(ssid)) {
                    Log.d(TAG, "isNetworkSecure: " + wifiNetworkManager.isNetworkSecure());
                    isNetworkSecure.setValue(wifiNetworkManager.isNetworkSecure());
                    isNetworkSecure.notifyObservers();
                    wasSet = true;
                }
            }
        }

        if (!wasSet) {
            isNetworkSecure.setValue(wifiNetworkManager.isNetworkSecure());
            isNetworkSecure.notifyObservers();
        }
        // refresh security alert on each network change
        if (context instanceof CockpitMainActivity) {
            CockpitMainActivity mainActivity = (CockpitMainActivity) context;
            mainActivity.refreshSecurityAlert();
        }
        Log.d(TAG, "network receive event finished");
    }

    private boolean isValidSsid(String ssid) {
        if (ssid == null || ssid.equals(WifiNetworkManager.UNKNOWN_SSID_KEY)) {
            return false;
        }
        return true;
    }
}
