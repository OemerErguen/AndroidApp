package de.uni_stuttgart.informatik.sopra.sopraapp.fragment.tabs;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import de.uni_stuttgart.informatik.sopra.sopraapp.CockpitPreferenceManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.activity.TabbedDeviceActivity;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.view.CockpitQueryView;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.ManagedDevice;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.NoDeviceException;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.SnmpManager;

/**
 * all tab fragments should extend this
 */
public abstract class DeviceFragment extends Fragment {
    public static final String TAG = DeviceFragment.class.getName();
    private ManagedDevice managedDevice = null;

    private CockpitQueryView cockpitQueryView;

    public DeviceFragment() {
    }

    /**
     * implement this method in subclasses
     * this event is triggered when tabs are reloaded. so reload query view there
     */
    public abstract void reloadData();

    public void setManagedDevice(ManagedDevice managedDevice) {
        this.managedDevice = managedDevice;
    }

    @Override
    public void onStart() {
        super.onStart();
        reloadData();
    }

    /**
     * init query view in this class
     *
     * @param cockpitQueryView
     */
    protected void initQueryView(CockpitQueryView cockpitQueryView) {
        this.cockpitQueryView = cockpitQueryView;
    }

    /**
     * this method retrieves the {@link ManagedDevice} object of this class out of fragment args
     *
     * @return
     */
    public ManagedDevice getManagedDevice() {
        if (managedDevice == null) {
            String deviceId;
            // this should work in embedded activity mode and in pure fragment mode
            if (getArguments() == null && getActivity() != null) {
                deviceId = getActivity().getIntent().getStringExtra(TabbedDeviceActivity.EXTRA_DEVICE_ID);
            } else {
                deviceId = getArguments().getString(TabbedDeviceActivity.EXTRA_DEVICE_ID, null);
            }
            if (deviceId == null || deviceId.isEmpty()) {
                throw new IllegalStateException("no fragment argument EXTRA_DEVICE_ID set");
            }
            Log.d(TAG, "looking for id:" + deviceId);
            ManagedDevice managedDeviceObject = DeviceManager.getInstance().getDevice(deviceId);
            if (managedDeviceObject != null) {
                this.managedDevice = managedDeviceObject;
            } else {
                throw new NoDeviceException("no managed device with id " + TabbedDeviceActivity.EXTRA_DEVICE_ID);
            }
        }
        return managedDevice;
    }

    /**
     * async waiter with timeout handling
     *
     * @param queryTask
     * @param deviceConfiguration
     */
    protected void waitForTaskResultAsync(AsyncTask<?,?,?> queryTask, DeviceConfiguration deviceConfiguration) {
        AsyncTask.execute(() -> {
            try {
                int offset = deviceConfiguration.getVersionSpecificTimeoutOffset();
                queryTask.get((long) CockpitPreferenceManager.TIMEOUT_WAIT_ASYNC_MILLISECONDS + offset, TimeUnit.MILLISECONDS);
                SnmpManager.getInstance().resetTimeout(deviceConfiguration);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Log.w(TAG, "interrupted: " + e.getMessage());
            } catch (ExecutionException e) {
                Log.e(TAG, "execution exception: " + e.getMessage());
            } catch (TimeoutException e) {
                Log.w(TAG, "timeout reached!");
                SnmpManager.getInstance().registerTimeout(deviceConfiguration);
            }
        });
    }

    /**
     * @param listener
     */
    public void setOnRenderingFinishedListener(CockpitQueryView.OnRenderingFinishedListener listener) {
        CockpitQueryView queryView = getQueryView();
        if (queryView == null) {
            Log.w(TAG, "can not set rendering finished listener on null query view");
            return;
        }
        queryView.setOnRenderingFinishedListener(listener);
    }

    public CockpitQueryView getQueryView() {
        return cockpitQueryView;
    }

    public String getDeviceId() {
        return getManagedDevice().getDeviceConfiguration().getUniqueDeviceId();
    }
}