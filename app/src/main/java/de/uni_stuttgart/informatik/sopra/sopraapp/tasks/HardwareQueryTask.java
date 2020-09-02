package de.uni_stuttgart.informatik.sopra.sopraapp.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicReference;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.bsd.SensorTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general.HrDeviceTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general.HrDiskStorageTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general.HrPartitionTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.genua.FanTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.ucdavis.DskTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.view.CockpitQueryView;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.SnmpManager;

/**
 * this class is called when detail info of a device is requested
 */
public class HardwareQueryTask extends AsyncTask<Void, Void, Void> implements TabTaskHelper {

    public static final String TAG = HardwareQueryTask.class.getName();

    private final AtomicReference<CockpitQueryView> queryView = new AtomicReference<>();
    private DeviceConfiguration deviceConfiguration;
    // query tasks
    private QueryTask<SensorTableQuery> sensorTableQueryTask;
    private QueryTask<DskTableQuery> dskQueryTask;
    private QueryTask<FanTableQuery> fanQueryTask;
    private QueryTask<HrDeviceTableQuery> hrDeviceQueryTask;
    private QueryTask<HrDiskStorageTableQuery> hrDiskStorageQueryTask;
    private QueryTask<HrPartitionTableQuery> hrPartitionTableQueryTask;

    /**
     * constructor
     *
     * @param queryView
     * @param deviceConfiguration
     */
    public HardwareQueryTask(CockpitQueryView queryView, DeviceConfiguration deviceConfiguration) {
        this.queryView.set(queryView);
        this.deviceConfiguration = deviceConfiguration;
    }

    @Override
    protected void onPreExecute() {
        Log.d(TAG, "entering hardware query task");
        super.onPreExecute();

        ThreadPoolExecutor tpe = SnmpManager.getInstance().getThreadPoolExecutor();

        if (tpe.isTerminating() || tpe.isShutdown() || tpe.isTerminated()) {
            Log.d(TAG, "invalid thread pool given");
            return;
        }
        // start and send queries
        sensorTableQueryTask = new QueryTask<>();
        sensorTableQueryTask.executeOnExecutor(tpe, new SensorTableQuery.SensorTableQueryRequest(deviceConfiguration));
        dskQueryTask = new QueryTask<>();
        dskQueryTask.executeOnExecutor(tpe, new DskTableQuery.DskTableQueryRequest(deviceConfiguration));
        fanQueryTask = new QueryTask<>();
        fanQueryTask.executeOnExecutor(tpe, new FanTableQuery.FanTableQueryRequest(deviceConfiguration));
        hrDeviceQueryTask = new QueryTask<>();
        hrDeviceQueryTask.executeOnExecutor(tpe, new HrDeviceTableQuery.HrDeviceTableRequest(deviceConfiguration));
        hrDiskStorageQueryTask = new QueryTask<>();
        hrDiskStorageQueryTask.executeOnExecutor(tpe, new HrDiskStorageTableQuery.HrDiskStorageTableRequest(deviceConfiguration));
        hrPartitionTableQueryTask = new QueryTask<>();
        hrPartitionTableQueryTask.executeOnExecutor(tpe, new HrPartitionTableQuery.HrPartitionTableRequest(deviceConfiguration));
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (deviceConfiguration == null) {
            throw new IllegalArgumentException("null DeviceConfiguration given");
        }

        addTableSection("Sensoren | sensorTable", sensorTableQueryTask, queryView);
        addTableSection("Lüfter | fanTable", fanQueryTask, queryView);
        addTableSection("Festplatten 1 | dskTable", dskQueryTask, queryView);
        addTableSection("Festplatten 2 | hrDeviceTable", hrDeviceQueryTask, queryView);
        addTableSection("Storage | hrDiskStorageTable", hrDiskStorageQueryTask, queryView);
        addTableSection("Partitionstabelle | hrPartitionTable", hrPartitionTableQueryTask, queryView);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.d(TAG, "start rendering");
        // here we are on the ui thread
        queryView.get().render(true);
    }

    @Override
    public void cancelTasks() {
        sensorTableQueryTask.cancel(true);
        dskQueryTask.cancel(true);
        fanQueryTask.cancel(true);
        hrDeviceQueryTask.cancel(true);
        hrDiskStorageQueryTask.cancel(true);
        hrPartitionTableQueryTask.cancel(true);
    }
}