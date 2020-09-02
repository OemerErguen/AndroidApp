package de.uni_stuttgart.informatik.sopra.sopraapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.uni_stuttgart.informatik.sopra.sopraapp.CockpitMainActivity;
import de.uni_stuttgart.informatik.sopra.sopraapp.CockpitPreferenceManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.CockpitStateManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.network.WifiNetworkManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.ManagedDevice;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.SnmpManager;

/**
 * this class encapsulates alert dialogs and their functionality in this app
 */
public class AlertHelper {
    public static final int SETTINGS_ACTIVITY_REQUEST_CODE = 167;

    private Context context;
    private final CockpitPreferenceManager cockpitPreferenceManager;
    private ArrayList<AlertDialog> alertDialogs = new ArrayList<>();
    private ArrayList<AlertDialog> timeoutDialogs = new ArrayList<>();
    private ArrayList<AlertDialog> sessionTimeoutDialogs = new ArrayList<>();
    private static final String TAG = AlertHelper.class.getName();

    /**
     * constructor
     *
     * @param context
     */
    public AlertHelper(Context context) {
        this.context = context;
        this.cockpitPreferenceManager = new CockpitPreferenceManager(this.context);
    }

    /**
     * this dialog displays the flashlight hint dialog and - on dismiss - it starts the wifi qr code scanner
     * given in input params
     *
     * @param qrScannerActivityHelper called on dismiss
     */
    public void showFlashlightHintDialog(QrScannerActivityHelper qrScannerActivityHelper, boolean isWifi) {
        // show alert and start scanner on dismiss event
        new AlertDialog.Builder(context)
                .setCancelable(true)
                .setMessage(R.string.flashlight_hint)
                .setPositiveButton(R.string.btn_ok, (dialog, which) -> dialog.dismiss())
                .setNegativeButton(R.string.btn_no_longer_show, (dialog, which) -> cockpitPreferenceManager.disableFlashLightHint())
                .setOnDismissListener(dialog -> {
                    if (isWifi) {
                        qrScannerActivityHelper.startWifiScanner();
                    } else {
                        qrScannerActivityHelper.startDeviceScanner();
                    }
                }).show();
    }

    /**
     * wifi not accessible
     *
     * @param wifiSSid
     */
    public void showUnsuccessfulWifiConnectionAlert(String wifiSSid) {
        new AlertDialog.Builder(context)
                .setCancelable(true)
                .setMessage(String.format(context.getString(R.string.error_connecting_to_network), wifiSSid)).show();
    }

    private void cleanupDialogList(@NonNull List<AlertDialog> dialogList) {
        Iterator<AlertDialog> iterator = dialogList.iterator();
        while(iterator.hasNext()) {
            AlertDialog dialog = iterator.next();
            if (!dialog.isShowing()) {
                iterator.remove();
            }
        }
    }

    /**
     * builds the network security blocking overlay dialog
     * NOTE: only 3 buttons possible
     */
    public void showNotSecureAlert() {
        cleanupDialogList(alertDialogs);
        if (!alertDialogs.isEmpty()) {
            Log.w(TAG, "security dialog is already shown");
            return;
        }

        WifiNetworkManager cockpitWifiNetworkManager = WifiNetworkManager.getInstance(null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(R.string.no_secure_environment)
                .setMessage(context.getString(R.string.no_secure_environment_label)
                        + cockpitWifiNetworkManager.getCurrentModeLabel())
                .setPositiveButton(R.string.menu_settings_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialogs.clear();
                        CockpitMainActivity mainActivity = (CockpitMainActivity) context;
                        ((CockpitMainActivity) context)
                                .startActivityForResult(new Intent(context, BlockedSettingsActivity.class), SETTINGS_ACTIVITY_REQUEST_CODE);
                        mainActivity.checkState();
                    }
                })
                .setNeutralButton(R.string.menu_action_qr_code_label,
                        (dialog, which) -> new QrScannerActivityHelper((CockpitMainActivity) context).startWifiScanner());

        // if wifi is disabled - we show the button "enable wifi"
        WifiManager androidWifiManager = cockpitWifiNetworkManager.getAndroidWifiManager();
        if (androidWifiManager != null && !androidWifiManager.isWifiEnabled()) {
            builder.setNegativeButton(R.string.menu_action_wifi_activate, (dialog, which) -> {
                if (!androidWifiManager.isWifiEnabled()) {
                    Log.i(TAG, "Enable wifi with app");
                    androidWifiManager.setWifiEnabled(true);
                }
            });
        } else {
            if (cockpitWifiNetworkManager.isTargetNetworkReachable()) {
                // show "connect" button for target fixed ssid
                builder.setNegativeButton("Erneut verbinden", (dialog, which) -> cockpitWifiNetworkManager.connectToTargetNetwork());
            } else {
                // .. or else: offer close app btn - there is nothing we can do
                builder.setNegativeButton(R.string.cancel_app_btn_label, (dialog, which) -> Process.killProcess(Process.myPid()));
            }
        }
        builder.setIcon(R.drawable.ic_warning_black_48_dp);

        if (context instanceof Activity && !((Activity) context).isFinishing()) {
            ((Activity) context).runOnUiThread(() -> {
                AlertDialog dialog = builder.create();
                alertDialogs.add(dialog);
                dialog.show();
            });
        } else {
            Log.e(TAG, "no valid context!");
        }
    }

    /**
     * helper to close all security alert dialogs at once
     */
    public void closeAllSecurityAlerts() {
        Log.d(TAG, "close all security alerts");
        for (AlertDialog dialog : alertDialogs) {
            dialog.cancel();
        }
        alertDialogs.clear();
    }

    /**
     * method to close all timeout alerts
     */
    public void closeAllTimeoutAlerts() {
        Log.d(TAG, "close all timeout alerts");
        for (AlertDialog dialog : timeoutDialogs) {
            dialog.cancel();
        }
        timeoutDialogs.clear();
        for (AlertDialog dialog : sessionTimeoutDialogs) {
            dialog.cancel();
        }
        sessionTimeoutDialogs.clear();
    }

    /**
     * shows the connection broken dialog
     */
    public synchronized void showDeviceTimeoutDialog() {
        cleanupDialogList(timeoutDialogs);
        if (!timeoutDialogs.isEmpty()) {
            Log.d(TAG, "dialog is already shown");
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final ManagedDevice[] devicesInTimeout = SnmpManager.getInstance().getDevicesInTimeout();
        if (devicesInTimeout.length == 0) {
            Log.e(TAG, "no devices in timeout, but dialog was called!");
            return;
        }
        StringBuilder sb = new StringBuilder();
        boolean isSingular = devicesInTimeout.length == 1;
        String positiveButtonLabel = context.getString(R.string.alert_connection_timeout_multi_remove);
        if (isSingular) {
            sb.append(context.getString(R.string.alert_connection_timeout_single_message)).append("\n");
            sb.append(devicesInTimeout[0].getDeviceLabel());
            positiveButtonLabel = context.getString(R.string.alert_connection_timeout_single_remove);
        } else {
            sb.append(context.getString(R.string.alert_connection_timeout_multiple_devices)).append("\n");
            for (ManagedDevice dc : devicesInTimeout) {
                sb.append("\t• ").append(dc.getDeviceLabel()).append("\n");
            }
        }
        builder.setIcon(R.drawable.ic_warning_black_48_dp);

        builder.setMessage(sb.toString())
                .setCancelable(false)
                .setTitle(R.string.alert_connection_timeout_title)
                .setNeutralButton(R.string.alert_connection_timeout_retry, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "action: retry connections");
                        for (ManagedDevice md : devicesInTimeout) {
                            SnmpManager.getInstance().resetTimeout(md.getDeviceConfiguration());
                        }
                        if (context instanceof ProtectedActivity) {
                            ((ProtectedActivity) context).restartQueryCall();
                        }
                        dialog.cancel();
                    }
                })
                .setPositiveButton(positiveButtonLabel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "action: remove connections");
                        for (ManagedDevice md : devicesInTimeout) {
                            // implicit "timeout reset"
                            DeviceManager.getInstance().removeItem(md.getDeviceConfiguration().getUniqueDeviceId());
                        }
                        // only close
                        if (context instanceof TabbedDeviceActivity
                                || context instanceof SingleQueryResultActivity) {
                            ((Activity) context).finish();
                        } else if (context instanceof Activity) {
                            ((Activity) context).recreate();
                        }
                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.cancel_app_btn_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "action: quit app");
                        Process.killProcess(Process.myPid());
                    }
                });

        if (context instanceof Activity && !((Activity) context).isFinishing()) {
            ((Activity) context).runOnUiThread(() -> {
                AlertDialog dialog = builder.create();
                timeoutDialogs.add(dialog);
                dialog.show();
            });
        }
    }

    /**
     * session timeout dialog
     */
    public void showSessionTimeoutDialog() {
        cleanupDialogList(sessionTimeoutDialogs);
        if (!sessionTimeoutDialogs.isEmpty()) {
            Log.d(TAG, "session timeout dialog is already shown");
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.alert_session_timeout_title)
                .setMessage(R.string.alert_session_timeout_message)
                .setCancelable(false)
                .setPositiveButton(R.string.btn_ok, (dialog, which) -> {
                    dialog.cancel();
                    if (context instanceof CockpitMainActivity) {
                        ((CockpitMainActivity) context).restartView();
                    } else {
                        if (context instanceof Activity) {
                            ((Activity) context).finish();
                        }
                    }
                    if (context instanceof ProtectedActivity) {
                        ((ProtectedActivity) context).restartQueryCall();
                    }
                })
                .setOnDismissListener(dialog -> {
                    // "clear all" and unset session timeout
                    CockpitStateManager.getInstance().getIsInSessionTimeoutObservable().setValueAndTriggerObservers(false);
                    CockpitStateManager.getInstance().getIsInTimeoutsObservable().setValueAndTriggerObservers(false);
                });
        builder.setIcon(R.drawable.ic_warning_black_48_dp);

        if (context instanceof Activity && !((Activity) context).isFinishing()) {
            ((Activity) context).runOnUiThread(() -> {
                AlertDialog alertDialog = builder.create();
                sessionTimeoutDialogs.add(alertDialog);
                alertDialog.show();
            });
        }
    }

    /**
     * show dialog to start an oid query
     *
     * @param oidValue
     */
    public void showQueryTargetDialog(String oidValue) {
        Map<String, String> items = DeviceManager.getInstance().getDisplayableDeviceList();

        if (items.isEmpty()) {
            Log.d(TAG, "do not show dialog - no devices listed");

            new AlertDialog.Builder(context)
                    .setMessage(R.string.alert_please_add_device_first)
                    .setCancelable(true)
                    .create().show();
            return;
        }

        String[] deviceIds = new String[items.size()];
        String[] deviceLabels = new String[items.size()];
        int i = 0;
        for (String deviceIdKey : items.keySet()) {
            deviceIds[i] = deviceIdKey;
            deviceLabels[i] = items.get(deviceIdKey);
            i++;
        }
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.alert_select_device_dialog_title) + " '" + oidValue + "'")
                .setCancelable(true)
                .setSingleChoiceItems(deviceLabels, 0, null)
                .setNeutralButton(R.string.alert_open_query_in_new_device_tab, (dialog, which) -> {
                    int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                    Log.d(TAG, "device selected: " + selectedPosition);
                    ManagedDevice md = DeviceManager.getInstance().getDevice(deviceIds[selectedPosition]);
                    dialog.dismiss();
                    if (md != null) {
                        showNewQuery(true, md.getDeviceConfiguration(), oidValue);
                    }
                })
                .setPositiveButton(R.string.alert_open_query_in_new_activity, (dialog, which) -> {
                    int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                    Log.d(TAG, "device selected: " + selectedPosition);
                    ManagedDevice md = DeviceManager.getInstance().getDevice(deviceIds[selectedPosition]);
                    dialog.dismiss();
                    if (md != null) {
                        showNewQuery(false, md.getDeviceConfiguration(), oidValue);
                    }
                })
                .setNegativeButton(R.string.close, null)
                .create().show();
    }

    /**
     * method to display a new tab in a single device activity or a new simple query activity
     *
     * @param isNewTab
     * @param deviceConfiguration
     * @param oidQuery
     */
    private void showNewQuery(boolean isNewTab, DeviceConfiguration deviceConfiguration, String oidQuery) {
        Log.d(TAG, "show new query " + oidQuery);
        if (!isNewTab) {
            // show new activity
            Intent deviceDetailIntent = new Intent(context, SingleQueryResultActivity.class);
            deviceDetailIntent.putExtra(SingleQueryResultActivity.EXTRA_DEVICE_ID, deviceConfiguration.getUniqueDeviceId());
            deviceDetailIntent.putExtra(SingleQueryResultActivity.EXTRA_OID_QUERY, oidQuery);
            context.startActivity(deviceDetailIntent);
        } else {
            // show default device detail activity
            DeviceManager.getInstance().addNewDeviceTab(deviceConfiguration.getUniqueDeviceId(), oidQuery);
            Intent deviceDetailIntent = new Intent(context, TabbedDeviceActivity.class);
            deviceDetailIntent.putExtra(TabbedDeviceActivity.EXTRA_DEVICE_ID, deviceConfiguration.getUniqueDeviceId());
            deviceDetailIntent.putExtra(TabbedDeviceActivity.EXTRA_OPEN_TAB_OID, oidQuery);
            context.startActivity(deviceDetailIntent);
        }
    }

    /**
     * confirmation dialog before connection test is closed
     */
    public void showCancelConnectionConfirmationDialog() {
        new AlertDialog.Builder(context).setTitle(R.string.dialog_cancel_connection_dialog_title)
                .setMessage(R.string.dialog_cancel_connection_test_message)
                .setPositiveButton(R.string.btn_ok, (dialog, which) -> {
                    Log.d(TAG, "cancel connection attempt");
                    if (context instanceof CockpitMainActivity) {
                        ((CockpitMainActivity) context).cancelConnectionTestTask();
                        ((CockpitMainActivity) context).getProgressRow().setVisibility(View.GONE);
                    }
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    Log.d(TAG, "NOT canceling connection attempt");
                    dialog.dismiss();
                })
                .create().show();
    }
}
