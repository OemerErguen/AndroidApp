package de.uni_stuttgart.informatik.sopra.sopraapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.uni_stuttgart.informatik.sopra.sopraapp.network.WifiNetworkManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.util.BooleanObservable;

import static java.lang.Integer.parseInt;

/**
 * contains {@link GeneralPreferenceFragment} which handles app preferences
 */
public class CockpitPreferenceManager {
    public static final String KEY_IS_WIFI_SSID_LOCKED = "is_wifi_ssid_locked";
    public static final String KEY_SECURE_WIFI_SSID = "secure_wifi_ssid";
    public static final String KEY_IS_SSID_MANUAL = "is_ssid_manual";
    public static final String KEY_IS_WPA2_ONLY = "is_wpa2_only";
    public static final String KEY_SHOW_FLASHLIGHT_HINT = "show_flash_light_hint";
    public static final String KEY_DEBUG_ALLOW_ALL_NETWORKS = "is_all_networks_allowed_debug";
    // connection detail user preferences
    public static final String KEY_CONNECTION_ATTEMPT_RETRIES = "connection_test_retries";
    public static final String KEY_CONNECTION_ATTEMPT_TIMEOUT = "connection_test_timeout";
    public static final String KEY_CONNECTION_RETRIES = "connection_retries";
    public static final String KEY_CONNECTION_TIMEOUT = "connection_timeout";
    public static final String KEY_SESSION_TIMEOUT = "session_timeout";
    public static final String KEY_IS_V1_INSTEAD_OF_V2C = "use_v1_instead_of_v2c";
    public static final String KEY_PERIODIC_UI_UPDATE_ENABLED = "periodic_ui_update_enabled";
    public static final String KEY_PERIODIC_UI_UPDATE_SECONDS = "ui_update_interval_seconds";
    public static final String KEY_REQUEST_COUNTER = "request_action_counter";
    public static final String KEY_BUILD_TIMESTAMP = "build_timestamp";
    public static final String KEY_VERSION = "version";
    // internal keys
    public static final String KEY_INTERNAL_LAST_ACTIVITY_MS = "last_activity_in_ms";
    public static final String KEY_PREF_VERSION = "pref_version_code";


    // static prefs
    // TODO customize = snmp request timeout + 2000
    public static final int TIMEOUT_WAIT_ASYNC_MILLISECONDS_SHORT = 3000;
    public static final int TIMEOUT_WAIT_ASYNC_MILLISECONDS = 7500;

    private static final String[] PREFERENCE_KEYS = new String[]{
            KEY_IS_WIFI_SSID_LOCKED,
            KEY_SECURE_WIFI_SSID,
            KEY_IS_SSID_MANUAL,
            KEY_IS_WPA2_ONLY,
            KEY_DEBUG_ALLOW_ALL_NETWORKS,
            KEY_SHOW_FLASHLIGHT_HINT
    };
    public static final String BAD_USER_INPUT_DETECTED = "Bad user input detected.";
    private final SharedPreferences sharedPreferences;
    private static final String TAG = CockpitPreferenceManager.class.getName();

    /**
     * constructor
     *
     * @param context
     */
    public CockpitPreferenceManager(Context context) {
        sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);

        if (BuildConfig.VERSION_CODE > getPrefVersion()) {
            Log.d(TAG, "update pref version. reset request counter.");
            resetRequestCounter();
            setPrefVersion(BuildConfig.VERSION_CODE);
        }
    }

    /**
     * helper method to reset request counter
     */
    public void resetRequestCounter() {
        Log.d(TAG, "reset request counter");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_REQUEST_COUNTER, String.valueOf(0));
        editor.apply();
    }

    /**
     * increment request counter
     */
    public synchronized void incrementRequestCounter() {
        long oldValue;
        try {
            oldValue = parseInt(sharedPreferences.getString(KEY_REQUEST_COUNTER, "0"));
        } catch (NumberFormatException | ClassCastException ignore) {
            sharedPreferences.edit().putString(KEY_REQUEST_COUNTER, "0").apply();
            oldValue = 0;
        }
        Log.d(TAG, "increment request counter to: " + (oldValue + 1));
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_REQUEST_COUNTER, String.valueOf(++oldValue));
        editor.apply();
    }

    /**
     * ui update interval
     *
     * @return
     */
    public int getUiUpdateSeconds() {
        int value;
        try {
            value = parseInt(sharedPreferences.getString(KEY_PERIODIC_UI_UPDATE_SECONDS, "0"));
        } catch (NumberFormatException ignore) {
            value = 5;
        }
        // 3 is minimum
        if (value < 3) {
            return 3;
        }
        // 3000 secs max
        if (value > 3000) {
            return 3000;
        }
        return value;
    }

    /**
     * helper method to set the version code pref
     * internal
     *
     * @param versionCode
     */
    private void setPrefVersion(int versionCode) {
        Log.d(TAG, "update pref version to: " + versionCode);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_PREF_VERSION, versionCode);
        editor.apply();
    }

    private int getPrefVersion() {
        return sharedPreferences.getInt(KEY_PREF_VERSION, 0);
    }

    /**
     * check session timeout
     */
    public void checkSessionTimeout() {
        if (DeviceManager.getInstance().getDeviceList().isEmpty()) {
            Log.d(TAG, "session timeout measurement is active only if devices are available");
            setLastActivity();
            return;
        }
        BooleanObservable isInSessionTimeoutObservable =
                CockpitStateManager.getInstance().getIsInSessionTimeoutObservable();
        // check for session timeout here
        long lastActivity = getLastActivity();
        if (lastActivity == 0) {
            // first time
            setLastActivity();
        } else {
            // calculate difference in secs
            long difference = ((System.currentTimeMillis() - lastActivity) / 1000);
            if (difference <= 10) {
                // this value is performance relevant!
                return;
            }
            Log.d(TAG, "activity difference (seconds): " + difference);
            if ((getSessionTimeoutInMin() * 60) < difference) {
                // session timeout detected!
                isInSessionTimeoutObservable.setValueAndTriggerObservers(true);
            } else {
                isInSessionTimeoutObservable.setValueAndTriggerObservers(false);
            }
            // update
            setLastActivity();
        }
    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener
            = (preference, newValue) -> {
        String stringValue = newValue.toString();

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list.
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringValue);

            // Set the summary to reflect the new value.
            preference.setSummary(
                    index >= 0
                            ? listPreference.getEntries()[index]
                            : null);
        } else {
            // For all other preferences, set the summary to the value's
            // simple string representation.
            switch (preference.getKey()) {
                case KEY_CONNECTION_ATTEMPT_RETRIES:
                    preference.setSummary(preference.getContext()
                            .getString(R.string.pref_connection_test_retries_summary)
                            + ": " + stringValue);
                    break;
                case KEY_CONNECTION_ATTEMPT_TIMEOUT:
                    preference.setSummary(preference.getContext()
                            .getString(R.string.pref_connection_test_timeout_summary)
                            + ": " + stringValue);
                    break;
                case KEY_CONNECTION_RETRIES:
                    preference.setSummary(preference.getContext().getString(R.string.pref_general_connection_retries_summary)
                            + ": " + stringValue);
                    break;
                case KEY_CONNECTION_TIMEOUT:
                    preference.setSummary(preference.getContext().getString(R.string.pref_general_connection_timeout_summary)
                            + ": " + stringValue);
                    break;
                default:
                    preference.setSummary(stringValue);
                    break;
            }
        }
        return true;
    };

    /**
     * get preference value
     *
     * @return
     */
    public boolean isWifiSSidLocked() {
        return sharedPreferences.getBoolean(KEY_IS_WIFI_SSID_LOCKED, true);
    }

    /**
     * get preference value
     *
     * @return
     */
    public String getFixedWifiSSID() {
        return sharedPreferences.getString(KEY_SECURE_WIFI_SSID, null);
    }

    /**
     * get preference value
     *
     * @return
     */
    public boolean isPeriodicUpdateEnabled() {
        return sharedPreferences.getBoolean(KEY_PERIODIC_UI_UPDATE_ENABLED, false);
    }

    /**
     * get preference value
     *
     * @return
     */
    public boolean isSSIDManuallyDefined() {
        return sharedPreferences.getBoolean(KEY_IS_SSID_MANUAL, false);
    }

    /**
     * method to retrieve connection test timeout safely
     *
     * @return
     */
    public int getConnectionTestTimeout() {
        int prefValue = 0;
        try {
            prefValue = parseInt(sharedPreferences.getString(KEY_CONNECTION_ATTEMPT_TIMEOUT, "1000"));
        } catch (NumberFormatException ignore) {
            Log.w(TAG, "not an integer given!");
        }
        if (prefValue == 0) {
            Log.w(TAG, "Bad user input detected. Connection test timeout too small. Using 1000 ms.");
            return 1000;
        }
        if (prefValue <= 100) {
            Log.w(TAG, "Bad user input detected. Connection test timeout too small. Using 100 ms.");
            // less than 100 ms is not a good idea..
            return 100;
        }
        if (prefValue >= 30000) {
            Log.w(TAG, "Bad user input detected. Connection test timeout too big. Using 30 s.");
            // limit to 30 s
            return 30000;
        }
        return prefValue;
    }

    /**
     * method to get a safe value for connection test reties
     *
     * @return
     */
    public int getConnectionTestRetries() {
        int prefValue = 0;
        try {
            prefValue = parseInt(sharedPreferences.getString(KEY_CONNECTION_ATTEMPT_RETRIES, "2"));
        } catch (NumberFormatException ignore) {
            Log.w(TAG, "not an integer given!");
        }
        if (prefValue <= 0) {
            Log.w(TAG, "Bad user input detected. Connection test retries too small. Using 1.");
            return 1;
        }
        if (prefValue > 25) {
            Log.w(TAG, "Bad user input detected. Connection test retries too big. Using 25.");
            return 25;
        }
        return prefValue;
    }

    /**
     * get connection retries
     *
     * @return
     */
    public int getConnectionRetries() {
        int prefValue = 3;
        try {
            prefValue = parseInt(sharedPreferences.getString(KEY_CONNECTION_RETRIES, "3"));
        } catch (NumberFormatException ignore) {
            Log.w(TAG, "not an integer given!");
        }

        if (prefValue < 1) {
            Log.w(TAG, BAD_USER_INPUT_DETECTED);
            prefValue = 1;
        }
        if (prefValue > 10) {
            Log.w(TAG, BAD_USER_INPUT_DETECTED);
            prefValue = 10;
        }
        return prefValue;
    }

    /**
     * method to get connection timeout in ms
     *
     * @return
     */
    public int getConnectionTimeout() {
        int prefValue = 5000;
        try {
            prefValue = parseInt(sharedPreferences.getString(KEY_CONNECTION_TIMEOUT, "5000"));
        } catch (NumberFormatException ignore) {
            Log.w(TAG, "not an integer given!");
        }
        if (prefValue < 1000) {
            Log.w(TAG, BAD_USER_INPUT_DETECTED);
            prefValue = 1000;
        }
        if (prefValue > 20000) {
            Log.w(TAG, BAD_USER_INPUT_DETECTED);
            prefValue = 20000;
        }
        return prefValue;
    }

    public boolean isWpa2Only() {
        return sharedPreferences.getBoolean(KEY_IS_WPA2_ONLY, false);
    }

    public boolean isAllNetworksAllowed() {
        return sharedPreferences.getBoolean(KEY_DEBUG_ALLOW_ALL_NETWORKS, false);
    }

    /**
     * update ssid method
     *
     * @param ssid
     */
    public void updateFixedSSID(String ssid) {
        if (ssid == null) {
            throw new IllegalArgumentException("null ssid not allowed");
        }
        Log.i(TAG, "Update fixed ssid to: " + ssid);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(KEY_SECURE_WIFI_SSID, ssid);
        edit.apply();
    }

    public static String[] getAllPreferenceKeys() {
        return PREFERENCE_KEYS;
    }

    /**
     * indicator whether to show the flashlisght hint or not
     *
     * @return
     */
    public boolean showFlashlightHint() {
        return sharedPreferences.getBoolean(CockpitPreferenceManager.KEY_SHOW_FLASHLIGHT_HINT, true);
    }

    /**
     * helper method to disable flashlight hint dialogs app-wide
     */
    public void disableFlashLightHint() {
        Log.d(TAG, "Flashlight hint is disabled");
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(KEY_SHOW_FLASHLIGHT_HINT, false);
        edit.apply();
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    /**
     * last session activity
     *
     * @return
     */
    public long getLastActivity() {
        return sharedPreferences.getLong(CockpitPreferenceManager.KEY_INTERNAL_LAST_ACTIVITY_MS, 0);
    }

    /**
     * sets current moment as last activity
     */
    public void setLastActivity() {
        final long currentTime = System.currentTimeMillis();
        Log.d(TAG, "set last activity to: " + currentTime);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putLong(CockpitPreferenceManager.KEY_INTERNAL_LAST_ACTIVITY_MS, currentTime);
        edit.apply();
    }

    /**
     * session timeout in min
     *
     * @return
     */
    public int getSessionTimeoutInMin() {
        try {
            return parseInt(sharedPreferences.getString(CockpitPreferenceManager.KEY_SESSION_TIMEOUT, "60"));
        } catch (NumberFormatException ignore) {
        }
        // return default
        return 60;
    }

    /**
     * user pref
     *
     * @return
     */
    public boolean isV1InsteadOfV3() {
        return sharedPreferences.getBoolean(KEY_IS_V1_INSTEAD_OF_V2C, false);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    public static class GeneralPreferenceFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.pref_general);

            PackageInfo packageInfo = null;
            try {
                packageInfo = getContext().getPackageManager().
                        getPackageInfo(getContext().getPackageName(), 0);

                String version = packageInfo.versionName;
                int versionCode = packageInfo.versionCode;
                findPreference(KEY_VERSION).setSummary(getContext().getString(R.string.preference_version, version, versionCode));

                String timestamp = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss",
                        getCurrentLocale(getContext())).format(new Date(BuildConfig.BUILD_TIMESTAMP));
                findPreference(KEY_BUILD_TIMESTAMP).setSummary(getString(R.string.timestamp, timestamp));
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "preference build timestamp name not found: " + e.getMessage());
            }

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            EditTextPreference secureWifiSSID = (EditTextPreference)
                    getPreferenceScreen().findPreference(KEY_SECURE_WIFI_SSID);
            bindPreferenceSummaryToValue(secureWifiSSID);

            EditTextPreference connectionTestRetries =
                    (EditTextPreference) getPreferenceScreen().findPreference(KEY_CONNECTION_ATTEMPT_RETRIES);
            bindPreferenceSummaryToValue(connectionTestRetries);

            EditTextPreference connectionTestTimeout =
                    (EditTextPreference) getPreferenceScreen().findPreference(KEY_CONNECTION_ATTEMPT_TIMEOUT);
            bindPreferenceSummaryToValue(connectionTestTimeout);

            Preference requestCounter = getPreferenceScreen().findPreference(KEY_REQUEST_COUNTER);
            bindPreferenceSummaryToValue(requestCounter);

            EditTextPreference connectionTimeout = (EditTextPreference) getPreferenceScreen()
                    .findPreference(KEY_CONNECTION_TIMEOUT);
            bindPreferenceSummaryToValue(connectionTimeout);

            EditTextPreference connectionRetries = (EditTextPreference) getPreferenceScreen()
                    .findPreference(KEY_CONNECTION_RETRIES);
            bindPreferenceSummaryToValue(connectionRetries);

            findPreference(KEY_REQUEST_COUNTER)
                    .setSummary(
                            String.valueOf(getPreferenceManager()
                                    .getSharedPreferences().getString(KEY_REQUEST_COUNTER, "0"))
                    );

            PreferenceManager.getDefaultSharedPreferences(getContext())
                    .registerOnSharedPreferenceChangeListener((sharedPreferences, key) -> {
                        if (key.equals(KEY_IS_WIFI_SSID_LOCKED)
                                || key.equals(KEY_IS_SSID_MANUAL)
                                || key.equals(KEY_SECURE_WIFI_SSID)
                                || key.equals(KEY_DEBUG_ALLOW_ALL_NETWORKS)
                                || key.equals(KEY_IS_WPA2_ONLY)) {
                            WifiNetworkManager networkManager = WifiNetworkManager.getInstance(getContext());
                            networkManager.updateMode();
                        }

                        if (key.equals(KEY_PERIODIC_UI_UPDATE_ENABLED)) {
                            if (getContext() instanceof CockpitMainActivity) {
                                ((CockpitMainActivity) getContext()).initTasks();
                            }
                        }
                    });
        }

        /**
         * Binds a preference's summary to its value. More specifically, when the
         * preference's value is changed, its summary (line of text below the
         * preference title) is updated to reflect the value. The summary is also
         * immediately updated upon calling this method. The exact display format is
         * dependent on the type of preference.
         *
         * @see #sBindPreferenceSummaryToValueListener
         */
        private static void bindPreferenceSummaryToValue(android.support.v7.preference.Preference preference) {
            Log.d(TAG, "registered " + preference.getKey() + " preference");
            // Set the listener to watch for value changes.
            preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
            // Trigger the listener immediately with the preference's
            // current value.
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), ""));
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
    }

}
