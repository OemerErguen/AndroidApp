package de.uni_stuttgart.informatik.sopra.sopraapp.tasks;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import de.uni_stuttgart.informatik.sopra.sopraapp.CockpitStateManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.fragment.items.DeviceMonitorItemContent;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general.SystemQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.SnmpManager;

/**
 * simple async task to refresh list async way
 * <p>
 */
public class RefreshListTask extends AsyncTask<Void, Void, Void> {

    public static final String TAG = RefreshListTask.class.getName();

    private ArrayList<QueryTask<SystemQuery>> queryTaskList = new ArrayList<>();
    private final AtomicReference<RecyclerView> recyclerViewAtomicReference = new AtomicReference<>();

    public RefreshListTask(RecyclerView recyclerView) {
        recyclerViewAtomicReference.set(recyclerView);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // iterate on copy
        final List<DeviceMonitorItemContent.DeviceMonitorItem> deviceList = new ArrayList<>(DeviceManager.getInstance().getDeviceList());
        for (DeviceMonitorItemContent.DeviceMonitorItem deviceItem : deviceList) {
            QueryTask<SystemQuery> qt = new QueryTask<>();
            qt.executeOnExecutor(SnmpManager.getInstance().getThreadPoolExecutor(),
                    new SystemQuery.SystemQueryRequest(deviceItem.deviceConfiguration));
            queryTaskList.add(qt);
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ArrayList<DeviceMonitorItemContent.DeviceMonitorItem> deviceList = new ArrayList<>();
        // copy data to be avoid concurrent modification exceptions
        List<DeviceMonitorItemContent.DeviceMonitorItem> tmpDeviceList = new ArrayList<>(DeviceManager.getInstance().getDeviceList());
        int counter = 1;
        for (DeviceMonitorItemContent.DeviceMonitorItem deviceItem : tmpDeviceList) {
            SystemQuery systemQuery = null;
            if (!deviceItem.deviceConfiguration.isDummy()) {
                for (QueryTask qt : queryTaskList) {
                    if (deviceItem.getDeviceConfiguration().getUniqueDeviceId().equals(qt.getDeviceConfiguration().getUniqueDeviceId())) {
                        systemQuery = (SystemQuery) qt.getQuery();
                    }
                }
            }
            if (systemQuery == null) {
                Log.w(TAG, "no system query retrievable!");
                // use old as fallback
                systemQuery = deviceItem.systemQuery;
                if (!CockpitStateManager.getInstance().isInTimeouts()) {
                    if (deviceItem.deviceConfiguration.getSnmpVersion() < 3) {
                        SnmpManager.getInstance().resetV1Connection();
                    } else {
                        SnmpManager.getInstance().resetV3Connection();
                    }
                }
            }
            deviceList.add(new DeviceMonitorItemContent.DeviceMonitorItem("#" + counter, deviceItem.host, deviceItem.port,
                    deviceItem.deviceConfiguration, systemQuery));
            counter++;
        }
        Log.d(TAG, "re-init device list: " + deviceList.toString());


        if (DeviceManager.getInstance().getDeviceList().size() == deviceList.size()) {
            DeviceManager.getInstance().getDeviceList().clear();
            DeviceManager.getInstance().getDeviceList().addAll(deviceList);
        } else {
            Log.w(TAG, "prevent refreshing device list");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        recyclerViewAtomicReference.get().getAdapter().notifyDataSetChanged();
    }
}