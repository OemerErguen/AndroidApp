package de.uni_stuttgart.informatik.sopra.sopraapp.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.SnmpConnection;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.SnmpManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * class to query with a special class in background
 */
public class QueryResponseTask extends AsyncTask<String, Void, List<QueryResponse>> {

    public static final String TAG = QueryResponseTask.class.getName();
    private final DeviceConfiguration deviceConfiguration;
    private SnmpConnection connector;

    public QueryResponseTask(DeviceConfiguration deviceConfiguration) {
        this.deviceConfiguration = deviceConfiguration;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        connector = SnmpManager.getInstance().getConnection(deviceConfiguration);
    }

    @Override
    protected List<QueryResponse> doInBackground(String... strings) {
        if (strings.length == 0 || strings[0] == null) {
            Log.w(TAG, "invalid query request given");
            return null;
        }
        String oidReq = strings[0];
        if (connector == null) {
            return null;
        }
        return connector.queryWalk(deviceConfiguration, oidReq);
    }
}
