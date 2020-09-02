package de.uni_stuttgart.informatik.sopra.sopraapp.query;


import android.util.Log;

import java.util.concurrent.ConcurrentHashMap;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general.SystemQuery;

/**
 * simple cache implementation with invalidation timeout
 */
public class QueryCache {

    public static final int CACHE_TIMEOUT_MS = 120 * 1000;
    public static final String TAG = QueryCache.class.getName();

    private ConcurrentHashMap<String, SnmpQuery> queryConcurrentHashMap =
            new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Long> accessHashMap =
            new ConcurrentHashMap<>();

    public boolean has(String key) {
        return queryConcurrentHashMap.containsKey(key);
    }

    public SnmpQuery get(String key) {
        if (queryConcurrentHashMap.containsKey(key)) {
            Long lastAccess = accessHashMap.get(key);
            if (lastAccess == null
                    || System.currentTimeMillis() - lastAccess < CACHE_TIMEOUT_MS) {
                return queryConcurrentHashMap.get(key);
            }
            Log.d(TAG, "invalidate cache key: " + key);
            queryConcurrentHashMap.remove(key);
            accessHashMap.remove(key);
        }
        return null;
    }

    public void put(String key, SnmpQuery query) {
        queryConcurrentHashMap.remove(key);
        accessHashMap.put(key, System.currentTimeMillis());
        queryConcurrentHashMap.put(key, query);
    }

    public void evictDeviceEntries(String deviceId) {
        for (String singleKeyInCache : queryConcurrentHashMap.keySet()) {
            if (singleKeyInCache.endsWith(deviceId)) {
                Log.d(TAG, "clear device query :" + singleKeyInCache);
                queryConcurrentHashMap.remove(singleKeyInCache);
                accessHashMap.remove(singleKeyInCache);
            }
        }
    }

    public void evictSystemQueries() {
        for (String singleKeyInCache : queryConcurrentHashMap.keySet()) {
            if (singleKeyInCache.contains(SystemQuery.class.getSimpleName())) {
                Log.d(TAG, "clear system query :" + singleKeyInCache);
                queryConcurrentHashMap.remove(singleKeyInCache);
                accessHashMap.remove(singleKeyInCache);
            }
        }
    }
}
