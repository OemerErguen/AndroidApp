package de.uni_stuttgart.informatik.sopra.sopraapp.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicReference;

import de.uni_stuttgart.informatik.sopra.sopraapp.persistence.CockpitDbHelper;
import de.uni_stuttgart.informatik.sopra.sopraapp.persistence.model.CustomQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.SimpleSnmpListRequest;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.SnmpQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.DefaultListQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.view.CockpitQueryView;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.SnmpManager;

/**
 * this task class handles display of custom queries
 */
public class CustomQueryTask extends AsyncTask<Void, Void, Void> implements TabTaskHelper {

    public static final String TAG = CustomQueryTask.class.getName();

    private final AtomicReference<CockpitQueryView> queryView = new AtomicReference<>();
    private DeviceConfiguration deviceConfiguration = null;
    private CockpitDbHelper cockpitDbHelper = null;

    private List<TaskWrapper> taskList = Collections.synchronizedList(new ArrayList<>());

    /**
     * constructor
     *
     * @param queryView
     * @param deviceConfiguration
     * @param dbHelper
     */
    public CustomQueryTask(CockpitQueryView queryView, DeviceConfiguration deviceConfiguration, CockpitDbHelper dbHelper) {
        this.queryView.set(queryView);
        this.deviceConfiguration = deviceConfiguration;
        this.cockpitDbHelper = dbHelper;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        ThreadPoolExecutor tpe = SnmpManager.getInstance().getThreadPoolExecutor();
        if (tpe.isTerminating() || tpe.isShutdown() || tpe.isTerminated()) {
            Log.d(TAG, "invalid thread pool given");
            return;
        }

        int rowCount = cockpitDbHelper.getQueryRowCount();
        for (int j = 0; j < rowCount; j++) {
            CustomQuery customQuery = cockpitDbHelper.getCustomQueryByListOffset(j);
            if (customQuery != null) {
                String oidToQuery = customQuery.getOid();

                Log.d(TAG, "start query task for oid:" + oidToQuery);
                QueryTask<DefaultListQuery> queryTask = new QueryTask<>();
                SimpleSnmpListRequest request = new SimpleSnmpListRequest(deviceConfiguration, oidToQuery);
                queryTask.executeOnExecutor(tpe, request);
                taskList.add(new TaskWrapper(queryTask, customQuery));
            }
        }

        if (rowCount == 0) {
            // finish loading if user has no queries
            this.queryView.get().render(true);
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (deviceConfiguration == null) {
            throw new IllegalArgumentException("null DeviceConfiguration given");
        }

        for (TaskWrapper taskWrapper : taskList) {
            CustomQuery customQuery = taskWrapper.getCustomQuery();
            addListQuery(customQuery.getName() + " | " + customQuery.getOid(), taskWrapper.getQueryTask(), queryView);
        }

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
        for (TaskWrapper taskWrapper: taskList) {
            taskWrapper.getQueryTask().cancel(true);
        }
    }

    /**
     * simple wrapper for two objects
     */
    class TaskWrapper {
        private final QueryTask<? extends SnmpQuery> queryTask;
        private final CustomQuery customQuery;

        TaskWrapper(QueryTask<? extends SnmpQuery> queryTask, CustomQuery customQuery) {
            this.queryTask = queryTask;
            this.customQuery = customQuery;
        }

        QueryTask<? extends SnmpQuery> getQueryTask() {
            return queryTask;
        }

        CustomQuery getCustomQuery() {
            return customQuery;
        }
    }
}
