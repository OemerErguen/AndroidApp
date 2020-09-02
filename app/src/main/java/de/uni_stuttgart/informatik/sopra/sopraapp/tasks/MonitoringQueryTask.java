package de.uni_stuttgart.informatik.sopra.sopraapp.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicReference;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general.IcmpStatsTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general.IpIfStatsTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general.IpSystemStatsTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.genua.PingTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.ucdavis.LaTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.ucdavis.PrTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.view.CockpitQueryView;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.SnmpManager;

/**
 * this class is called when detail info of a device is requested
 */
public class MonitoringQueryTask extends AsyncTask<Void, Void, Void> implements TabTaskHelper {

    public static final String TAG = MonitoringQueryTask.class.getName();

    private final AtomicReference<CockpitQueryView> queryView = new AtomicReference<>();
    private DeviceConfiguration deviceConfiguration;
    private QueryTask<LaTableQuery> laTableQueryTask;
    private QueryTask<PrTableQuery> prTableQueryTask;
    private QueryTask<IpSystemStatsTableQuery> ipSystemStatsQueryTask;
    private QueryTask<IpIfStatsTableQuery> ipIfStatsQueryTask;
    private QueryTask<IcmpStatsTableQuery> icmpStatsQueryTask;
    private QueryTask<PingTableQuery> pingQueryTask;

    /**
     * constructor
     *
     * @param queryView
     * @param deviceConfiguration
     */
    public MonitoringQueryTask(CockpitQueryView queryView, DeviceConfiguration deviceConfiguration) {
        this.queryView.set(queryView);
        this.deviceConfiguration = deviceConfiguration;
    }

    @Override
    protected void onPreExecute() {
        Log.d(TAG, "entering monitoring query task");

        super.onPreExecute();

        ThreadPoolExecutor tpe = SnmpManager.getInstance().getThreadPoolExecutor();
        if (tpe.isTerminating() || tpe.isShutdown() || tpe.isTerminated()) {
            Log.d(TAG, "invalid thread pool given");
            return;
        }
        // start and send queries
        laTableQueryTask = new QueryTask<>();
        laTableQueryTask.executeOnExecutor(tpe, new LaTableQuery.LaTableQueryRequest(deviceConfiguration));
        prTableQueryTask = new QueryTask<>();
        prTableQueryTask.executeOnExecutor(tpe, new PrTableQuery.PrTableQueryRequest(deviceConfiguration));
        ipSystemStatsQueryTask = new QueryTask<>();
        ipSystemStatsQueryTask.executeOnExecutor(tpe, new IpSystemStatsTableQuery.IpSystemStatsTableRequest(deviceConfiguration));
        ipIfStatsQueryTask = new QueryTask<>();
        ipIfStatsQueryTask.executeOnExecutor(tpe, new IpIfStatsTableQuery.IpIfStatsTableRequest(deviceConfiguration));
        icmpStatsQueryTask = new QueryTask<>();
        icmpStatsQueryTask.executeOnExecutor(tpe, new IcmpStatsTableQuery.IcmpStatsTableRequest(deviceConfiguration));
        pingQueryTask = new QueryTask<>();
        pingQueryTask.executeOnExecutor(tpe, new PingTableQuery.PingTableQueryRequest(deviceConfiguration));
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (deviceConfiguration == null) {
            throw new IllegalArgumentException("null DeviceConfiguration given");
        }

        addTableSection("Load | laTable", laTableQueryTask, queryView);
        addTableSection("prTable", prTableQueryTask, queryView);
        addTableSection("ipSystemStatsTable", ipSystemStatsQueryTask, queryView);
        addTableSection("ipIfStatsTable", ipIfStatsQueryTask, queryView);
        addTableSection("icmpStatsTable", icmpStatsQueryTask, queryView);
        addTableSection("pingTable", pingQueryTask, queryView);
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
        laTableQueryTask.cancel(true);
        prTableQueryTask.cancel(true);
        ipSystemStatsQueryTask.cancel(true);
        ipIfStatsQueryTask.cancel(true);
        icmpStatsQueryTask.cancel(true);
        pingQueryTask.cancel(true);
    }
}