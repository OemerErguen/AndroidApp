package de.uni_stuttgart.informatik.sopra.sopraapp.tasks;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import de.uni_stuttgart.informatik.sopra.sopraapp.CockpitPreferenceManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.CockpitStateManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.QueryCache;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.QueryRequest;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.SnmpQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.SnmpConnection;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.SnmpManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * class to query with a special class in background
 *
 * @param <T>
 */
public class QueryTask<T extends SnmpQuery> extends AsyncTask<QueryRequest<? extends SnmpQuery>, Void, T> {

    public static final String TAG = QueryTask.class.getName();
    private volatile DeviceConfiguration deviceConfiguration = null;
    private long startTime;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        startTime = System.currentTimeMillis();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected T doInBackground(QueryRequest<? extends SnmpQuery>... queryRequests) {
        if (queryRequests.length == 0 || queryRequests[0] == null) {
            Log.w(TAG, "invalid query request given");
            return null;
        }

        Log.d(TAG, "query request received");
        QueryRequest queryRequest = queryRequests[0];
        deviceConfiguration = queryRequest.getDeviceConfiguration();
        if (deviceConfiguration == null) {
            throw new IllegalStateException("null device config!");
        }
        SnmpConnection connector = SnmpManager.getInstance().getConnection(deviceConfiguration);

        if (queryRequest.isCacheable()) {
            Log.d(TAG, "query has cache id: " + queryRequest.getCacheId());
            QueryCache queryCache = CockpitStateManager.getInstance().getQueryCache();
            T cachedQuery = (T) queryCache.get(queryRequest.getCacheId());
            if (cachedQuery != null) {
                Log.d(TAG, "return query from cache");
                return cachedQuery;
            }
        }
        if (connector == null) {
            Log.d(TAG, "no connection available");
            return null;
        }
        if (!connector.canPing(deviceConfiguration)
                && !CockpitStateManager.getInstance().isInTimeouts()) {
            Log.d(TAG, "reset due to inactivity");
            if (deviceConfiguration.getSnmpVersion() < 3) {
                SnmpManager.getInstance().resetV1Connection();
            } else {
                SnmpManager.getInstance().resetV3Connection();
            }
        }
        connector = SnmpManager.getInstance().getConnection(deviceConfiguration);

        if (connector == null) {
            Log.w(TAG, "no connection available");
            return null;
        }
        try {
            connector.isUp();
        } catch (IOException e) {
            Log.e(TAG, "problem with connection socket: " + e.getMessage());
            return null;
        }

        List<QueryResponse> queryResponses;
        Log.i(TAG, "querying " + queryRequest.getOidQuery().toDottedString());
        if (queryRequest.isSingleRequest()) {
            queryResponses = connector.querySingle(deviceConfiguration,
                    queryRequest.getOidQuery());
        } else {
            queryResponses = connector.queryWalk(deviceConfiguration,
                    queryRequest.getOidQuery());
        }

        if (queryResponses == null) {
            return null;
        }
        SnmpQuery queryResponseClass = null;
        try {
            queryResponseClass = (SnmpQuery) queryRequest.getQueryClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            Log.e(TAG, "Error instantiate SnmpQuery class: " + e.getMessage());
            return null;
        }
        if (queryResponseClass == null) {
            return null;
        }
        queryResponseClass.processResult(queryResponses);
        if (queryRequest.isCacheable()) {
            QueryCache queryCache = CockpitStateManager.getInstance().getQueryCache();
            queryCache.put(queryRequest.getCacheId(), queryResponseClass);
        }
        return (T) queryResponseClass;
    }

    @Override
    protected void onPostExecute(T t) {
        super.onPostExecute(t);
        Log.d(TAG, "QueryTask: consumed time: " + (System.currentTimeMillis() - startTime) + " ms");
    }

    @Nullable
    public DeviceConfiguration getDeviceConfiguration() {
        Log.d(TAG, "wait for device config set..");
        while(deviceConfiguration == null) {}
        return deviceConfiguration;
    }

    /**
     * helper method to get query object or null
     *
     * @return
     */
    public T getQuery() {
        try {
            // wait for reference or illegal state exception is thrown
            int offset = getDeviceConfiguration().getVersionSpecificTimeoutOffset();
            T query = get((long) CockpitPreferenceManager.TIMEOUT_WAIT_ASYNC_MILLISECONDS + offset, TimeUnit.MILLISECONDS);
            if (query != null) {
                SnmpManager.getInstance().resetTimeout(deviceConfiguration);
                return query;
            }
        } catch (InterruptedException e) {
            Log.e(TAG, "interrupted");
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            Log.w(TAG, "execution exception: " + e.getMessage());
        } catch (TimeoutException e) {
            Log.w(TAG, "timeout reached for query task");
            if (deviceConfiguration != null) {
                SnmpManager.getInstance().registerTimeout(deviceConfiguration);
            }
        }
        return null;
    }
}
