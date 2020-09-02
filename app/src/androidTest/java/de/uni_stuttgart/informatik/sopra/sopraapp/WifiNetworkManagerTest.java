package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.uni_stuttgart.informatik.sopra.sopraapp.network.WifiNetworkManager;

@RunWith(AndroidJUnit4.class)
public class WifiNetworkManagerTest {

    @Rule
    public ActivityTestRule<CockpitMainActivity> rule = new ActivityTestRule<>(CockpitMainActivity.class);

    @Test
    public void testWifiModesAndPreferenceInteraction() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(rule.getActivity());
        SharedPreferences.Editor edit = sharedPreferences.edit();

        edit.putBoolean(CockpitPreferenceManager.KEY_IS_WIFI_SSID_LOCKED, false);
        edit.putBoolean(CockpitPreferenceManager.KEY_IS_WPA2_ONLY, false);
        edit.putBoolean(CockpitPreferenceManager.KEY_DEBUG_ALLOW_ALL_NETWORKS, false);
        edit.apply();

        WifiNetworkManager wifiNetworkManager = WifiNetworkManager.getInstance(rule.getActivity());
        wifiNetworkManager.updateMode();
        Assert.assertEquals("AndroidWifi", wifiNetworkManager.getCurrentSsid());
        Assert.assertEquals(4, wifiNetworkManager.getCurrentMode());

        edit.putBoolean(CockpitPreferenceManager.KEY_IS_WPA2_ONLY, true);
        edit.apply();
        wifiNetworkManager.updateMode();
        Assert.assertEquals(1, wifiNetworkManager.getCurrentMode());

        edit.putBoolean(CockpitPreferenceManager.KEY_IS_WIFI_SSID_LOCKED, true);
        edit.apply();
        wifiNetworkManager.updateMode();
        Assert.assertEquals(3, wifiNetworkManager.getCurrentMode());

        edit.putBoolean(CockpitPreferenceManager.KEY_IS_WPA2_ONLY, false);
        edit.apply();
        wifiNetworkManager.updateMode();
        Assert.assertEquals(2, wifiNetworkManager.getCurrentMode());
    }
}
