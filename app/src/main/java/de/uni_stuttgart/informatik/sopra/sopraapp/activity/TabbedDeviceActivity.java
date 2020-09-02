package de.uni_stuttgart.informatik.sopra.sopraapp.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.CockpitPreferenceManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.CockpitStateManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.adapter.DeviceSpinnerAdapter;
import de.uni_stuttgart.informatik.sopra.sopraapp.adapter.ViewPagerAdapter;
import de.uni_stuttgart.informatik.sopra.sopraapp.fragment.tabs.DeviceCustomQueryFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.fragment.tabs.DeviceDetailFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.fragment.tabs.DeviceFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.fragment.tabs.HardwareQueryFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.fragment.tabs.MonitorQueryFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.fragment.tabs.SnmpUsageQueryFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.persistence.CockpitDbHelper;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general.SystemQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.ManagedDevice;
import de.uni_stuttgart.informatik.sopra.sopraapp.util.PeriodicTask;

/**
 * Activity for single device view
 */
public class TabbedDeviceActivity extends AppCompatActivity implements ProtectedActivity {

    public static final String EXTRA_DEVICE_ID = "device_id";
    public static final String EXTRA_IS_COLLAPSED = "is_collapsed";
    public static final String EXTRA_OPEN_TAB_OID = "open_tab_oid";
    public static final String TAG = TabbedDeviceActivity.class.getName();
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private ManagedDevice managedDevice;
    private boolean isCollapsed;
    private DeviceDetailFragment deviceDetailFragment;
    private ProgressBar deviceProgressBar;
    private AlertHelper alertHelper;
    private PeriodicTask periodicTask = new PeriodicTask(this::checkSecurity, 2500);
    private CockpitDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_device);

        String deviceId = getIntent().getStringExtra(EXTRA_DEVICE_ID);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        alertHelper = new AlertHelper(this);
        initObservables(this, alertHelper, null);
        dbHelper = new CockpitDbHelper(this);

        List<ManagedDevice> managedDevices = DeviceManager.getInstance().getManagedDevices();
        // build device array
        String[] deviceList = new String[managedDevices.size()];
        int targetPosition = 0;
        for (int i = 0; i < managedDevices.size(); i++) {
            if (managedDevices.get(i).getDeviceConfiguration().getUniqueDeviceId().equals(deviceId)) {
                targetPosition = i;
            }
            deviceList[i] = managedDevices.get(i).getShortDeviceLabel();
        }
        if (!managedDevices.isEmpty()) {
            managedDevice = managedDevices.get(targetPosition);
        }
        if (managedDevice == null) {
            Log.e(TAG, "null device!");
            return;
        }

        Log.d(TAG, "init: " + managedDevice.getDeviceConfiguration().getUniqueDeviceId());
        // init view pager
        viewPager = findViewById(R.id.device_info_container_viewpager);

        if (deviceId != null) {
            initTabs(deviceId);
        }

        updateDeviceInformation();

        CockpitPreferenceManager cockpitPreferenceManager = new CockpitPreferenceManager(this);
        if (cockpitPreferenceManager.isPeriodicUpdateEnabled()) {
            periodicTask = new PeriodicTask(
                    () -> refreshView(true), cockpitPreferenceManager.getUiUpdateSeconds() * 1000);
        } else {
            periodicTask = new PeriodicTask(this::checkSecurity, 2500);
        }

        // Setup spinner
        Spinner spinner = findViewById(R.id.device_spinner);
        spinner.setAdapter(new DeviceSpinnerAdapter(
                toolbar.getContext(),
                deviceList));

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
                if (managedDevices.get(position) != null) {
                    managedDevice = managedDevices.get(position);
                }
                Log.d(TAG, "choose " + managedDevice.getDeviceConfiguration().getUniqueDeviceId());
                // updating system query information of managed device
                DeviceManager.getInstance().updateSystemQueryAsync(managedDevice);
                updateDeviceInformation();
                initTabs(managedDevice.getDeviceConfiguration().getUniqueDeviceId());
                if (deviceDetailFragment.getDeviceId().equals(
                        managedDevice.getDeviceConfiguration().getUniqueDeviceId()
                )) {
                    Log.d(TAG, "skip refresh. selected same device.");
                    return;
                }
                reloadTabData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });

        initHeadTable();

        if (targetPosition != spinner.getSelectedItemPosition()) {
            spinner.setSelection(targetPosition);
        }

        deviceProgressBar = findViewById(R.id.device_progress_bar);
        deviceProgressBar.setVisibility(View.GONE);
    }

    /**
     * alertHelper method which handles head table folding + icon changes
     */
    private void initHeadTable() {
        TableRow tableRow1 = findViewById(R.id.device_detail_row_1);
        TableRow tableRow2 = findViewById(R.id.device_detail_row_2);
        TableRow tableRow3 = findViewById(R.id.device_detail_row_3);
        TableRow tableRow4 = findViewById(R.id.device_detail_row_4);
        TableRow tableRow5 = findViewById(R.id.device_detail_row_5);
        TableRow tableRow6 = findViewById(R.id.device_detail_row_6);
        TableRow tableRow7 = findViewById(R.id.device_detail_row_7);

        TextView deviceDetailLabel = findViewById(R.id.device_detail_device_label);

        tableRow1.setOnClickListener(v -> {
            boolean isShowing = tableRow2.getVisibility() == View.VISIBLE;
            if (isShowing) {
                isCollapsed = true;
                deviceDetailLabel.setCompoundDrawablesRelativeWithIntrinsicBounds(getDrawable(R.drawable.ic_keyboard_arrow_right_black),
                        null, null, null);
                tableRow2.setVisibility(View.GONE);
                tableRow3.setVisibility(View.GONE);
                tableRow4.setVisibility(View.GONE);
                tableRow5.setVisibility(View.GONE);
                tableRow6.setVisibility(View.GONE);
                tableRow7.setVisibility(View.GONE);
            } else {
                isCollapsed = false;
                deviceDetailLabel.setCompoundDrawablesRelativeWithIntrinsicBounds(getDrawable(R.drawable.ic_keyboard_arrow_down_black),
                        null, null, null);
                tableRow2.setVisibility(View.VISIBLE);
                tableRow3.setVisibility(View.VISIBLE);
                tableRow4.setVisibility(View.VISIBLE);
                tableRow5.setVisibility(View.VISIBLE);
                tableRow6.setVisibility(View.VISIBLE);
                tableRow7.setVisibility(View.VISIBLE);
            }
        });

        // initial collapse
        tableRow1.callOnClick();
        TabLayout tl = findViewById(R.id.query_tabs);
        tl.setupWithViewPager(viewPager, false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startProtection(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        restartTrigger(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (periodicTask != null) {
            periodicTask.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (periodicTask != null) {
            periodicTask.stop();
        }
    }

    /**
     * method to load main deviec detail data which is shown over tab bar
     */
    private void updateDeviceInformation() {
        Log.d(TAG, "update general device information");
        TextView deviceLabel = findViewById(R.id.device_detail_device_label);
        if (managedDevice == null) {
            return;
        }
        deviceLabel.setText(managedDevice.getDeviceLabel());

        TextView userOrCommunityLabel = findViewById(R.id.device_detail_user_label);
        if (managedDevice.getDeviceConfiguration().getSnmpVersionEnum() == DeviceConfiguration.SNMP_VERSION.v3) {
            userOrCommunityLabel.setText(R.string.user_label);
        } else {
            userOrCommunityLabel.setText(R.string.community_label);
        }

        TextView userLabel = findViewById(R.id.device_detail_sys_ip);
        userLabel.setText(managedDevice.getDeviceConfiguration().getUsername());

        TextView sysDescrLabel = findViewById(R.id.device_detail_sys_descr);
        SystemQuery initialSystemQuery = managedDevice.getLastSystemQuery();
        if (initialSystemQuery == null) {
            throw new IllegalStateException("no system query given!");
        }
        sysDescrLabel.setText(initialSystemQuery.getSysDescr());

        TextView sysLocationLabel = findViewById(R.id.device_detail_sys_location);
        sysLocationLabel.setText(initialSystemQuery.getSysLocation());

        TextView sysContactLabel = findViewById(R.id.device_detail_sys_contact);
        sysContactLabel.setText(initialSystemQuery.getSysContact());

        TextView sysUpTimeLabel = findViewById(R.id.device_detail_sys_uptime);
        sysUpTimeLabel.setText(initialSystemQuery.getSysUpTime());
        TextView sysObjectIdLabel = findViewById(R.id.device_detail_sys_object_id);
        sysObjectIdLabel.setText(initialSystemQuery.getSysObjectId());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putBoolean(EXTRA_IS_COLLAPSED, isCollapsed);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            isCollapsed = savedInstanceState.getBoolean(EXTRA_IS_COLLAPSED, false);
        }
        if (!isCollapsed) {
            findViewById(R.id.device_detail_row_1).callOnClick();
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * re-init tabs and show first one "general"
     *
     * @param deviceId
     */
    private void initTabs(String deviceId) {
        Log.d(TAG, "init tabs of device detail view");
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        deviceDetailFragment = new DeviceDetailFragment();
        viewPagerAdapter.addFragment(deviceDetailFragment, deviceId, getString(R.string.device_detail_fragment_tab_title));
        viewPagerAdapter.addFragment(new HardwareQueryFragment(), deviceId, getString(R.string.device_detail_tab_hardware_label));
        viewPagerAdapter.addFragment(new MonitorQueryFragment(), deviceId, getString(R.string.device_detail_snmp_usage_query_label));
        if (dbHelper.getQueryRowCount() > 0) {
            viewPagerAdapter.addFragment(new DeviceCustomQueryFragment(), deviceId, getString(R.string.device_custom_query_tab_title));
        }
        viewPagerAdapter.addFragment(new SnmpUsageQueryFragment(), deviceId, getString(R.string.snmp_usage_tab_content_title));

        final int staticTabCount = viewPagerAdapter.getCount();
        String tabOidQuery = getIntent().getStringExtra(EXTRA_OPEN_TAB_OID);

        List<String> oidQueryList = DeviceManager.getInstance().getTabs(deviceId);
        int openedTabId = 0;
        if (oidQueryList != null) {
            Log.d(TAG, "found: " + oidQueryList.size() + " user defined tabs");
            int i = 1;
            for (String oidQuery : oidQueryList) {
                if (tabOidQuery != null && tabOidQuery.equals(oidQuery)) {
                    openedTabId = i;
                }
                viewPagerAdapter.addUserTabFragment(deviceId, oidQuery,
                        i + " | " + oidQuery);
                i++;
            }
        }
        viewPagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(viewPagerAdapter);
        if (openedTabId != 0) {
            viewPager.setCurrentItem(staticTabCount + openedTabId, true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.single_device_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_device_disconnect) {
            if (managedDevice != null) {
                DeviceManager.getInstance()
                        .removeItem(managedDevice.getDeviceConfiguration().getUniqueDeviceId());
                // refresh ui on list change!
                Log.d(TAG, "new device list size: " + DeviceManager.getInstance().getDeviceList().size());
                Toast.makeText(this,
                        String.format("Das Gerät %s wurde entfernt",
                                managedDevice.getShortDeviceLabel()),
                        Toast.LENGTH_SHORT).show();
            }
            finish();
            return true;
        }
        if (id == R.id.action_device_refresh) {
            Log.d(TAG, "refreshing cockpit query view");
            // avoid mass double-click
            item.setEnabled(false);
            refreshView(false);
            item.setEnabled(true);
            return true;
        }
        if (id == R.id.action_device_connection_info) {
            Log.d(TAG, "show connection info");

            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_info)
                    .setTitle(R.string.connection_information)
                    .setMessage(managedDevice.getDeviceConfiguration().getConnectionDetailsText())
                    .setPositiveButton(R.string.close, (dialog, which) -> dialog.dismiss())
                    .create().show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void refreshView(boolean onlyHead) {
        ProgressBar progressBar = findViewById(R.id.device_progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        if (managedDevice == null || managedDevice.getDeviceConfiguration() == null) {
            Log.w(TAG, "missing device configuration! this should never happen!");
            return;
        }

        if (!onlyHead) {
            CockpitStateManager.getInstance()
                    .getQueryCache().evictDeviceEntries(managedDevice.getDeviceConfiguration().getUniqueDeviceId());
            // update default tabs first
            reloadTabData();
            DeviceDetailFragment ddFragment = (DeviceDetailFragment) viewPagerAdapter.getItem(0);
            Toast.makeText(this, getString(R.string.device_refresh_toast_message)
                    + ddFragment.getManagedDevice().getLastSystemQuery().getSysName(), Toast.LENGTH_SHORT).show();
        } else {
            CockpitStateManager.getInstance()
                    .getQueryCache().evictSystemQueries();
            progressBar.setVisibility(View.GONE);
        }

        // updating system query information of managed device
        if (!managedDevice.isDummy()) {
            DeviceManager.getInstance().updateSystemQueryAsync(managedDevice);
        }
        updateDeviceInformation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(alertHelper != null) {
            // avoid window leaks
            alertHelper.closeAllTimeoutAlerts();
        }
        if (periodicTask != null) {
            periodicTask.stop();
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    /**
     * reload all tabs
     * add progress bar finished listener
     */
    private void reloadTabData() {
        Log.d(TAG, "reload tab data");
        // refresh all device fragments with interface method, except first tab
        for (int k = 1; k < viewPagerAdapter.getCount(); k++) {
            DeviceFragment fragment = (DeviceFragment) viewPagerAdapter.getItem(k);
            if (fragment != null && !fragment.isDetached()) {
                if (fragment instanceof DeviceDetailFragment ||
                        fragment instanceof HardwareQueryFragment) {
                    fragment.setOnRenderingFinishedListener(() -> {
                        deviceProgressBar.setVisibility(View.GONE);
                    });
                }
                fragment.reloadData();
            }
        }
    }

    @Override
    public void restartQueryCall() {
        reloadTabData();
    }

    public void checkSecurity() {
        Log.d(TAG, "periodic security check");
        restartTrigger(this);
    }
}
