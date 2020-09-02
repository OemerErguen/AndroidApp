package de.uni_stuttgart.informatik.sopra.sopraapp.snmp;

import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;

import org.snmp4j.security.AuthHMAC128SHA224;
import org.snmp4j.security.AuthHMAC192SHA256;
import org.snmp4j.security.AuthHMAC256SHA384;
import org.snmp4j.security.AuthHMAC384SHA512;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.AuthSHA;
import org.snmp4j.security.Priv3DES;
import org.snmp4j.security.PrivAES128;
import org.snmp4j.security.PrivAES192;
import org.snmp4j.security.PrivAES256;
import org.snmp4j.security.PrivDES;
import org.snmp4j.smi.OID;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import de.uni_stuttgart.informatik.sopra.sopraapp.CockpitPreferenceManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.CockpitStateManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.util.TimeoutObservable;


/**
 * singleton snmp manager class implementation
 * <p>
 * TODO add support for other algorithms(?!)
 * PrivAES192With3DESKeyExtension.ID,
 * PrivAES256With3DESKeyExtension.ID,
 */
public class SnmpManager {
    public static final String TAG = SnmpManager.class.getName();
    private ConcurrentHashMap<String, DeviceConfiguration> deviceConfigMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, TimeoutObservable> timeoutCounterMap = new ConcurrentHashMap<>();
    private ThreadPoolExecutor threadPoolExecutor = null;

    private static SnmpManager instance = null;
    private static SnmpConnection v1Connection = null;
    private static SnmpConnection v3Connection = null;

    // only one at the same time possible
    private int connectionTestCounter = 0;

    private OID[] privProtocols = new OID[]{
            PrivAES128.ID,
            PrivDES.ID,
            PrivAES192.ID,
            PrivAES256.ID,
            Priv3DES.ID,
    };
    private OID[] authProtocols = new OID[]{
            AuthSHA.ID,
            AuthMD5.ID,
            AuthHMAC128SHA224.ID,
            AuthHMAC192SHA256.ID,
            AuthHMAC256SHA384.ID,
            AuthHMAC384SHA512.ID
    };
    private CockpitPreferenceManager cockpitPreferenceManager;

    /**
     * private singleton constructor
     */
    private SnmpManager() {
        threadPoolExecutor = createNewThreadPool();
    }

    /**
     * singleton access method
     *
     * @return
     */
    public static synchronized SnmpManager getInstance() {
        Log.d(TAG, "snmp manager instance requested");
        if (instance == null) {
            instance = new SnmpManager();
        }
        return instance;
    }

    /**
     * get connection of stored connection pool
     * you should always use this method to get a valid snmp connection object
     * exception: during connection test
     *
     * @param deviceConfiguration
     * @return
     */
    public synchronized SnmpConnection getConnection(DeviceConfiguration deviceConfiguration) {
        if (CockpitStateManager.getInstance().getIsInTimeoutsObservable().getValue()) {
            Log.w(TAG, "no connection available. clear timeout state first.");
            return null;
        }
        if (CockpitStateManager.getInstance().getIsInSessionTimeoutObservable().getValue()) {
            Log.w(TAG, "no connection available. session timeout detected.");
            return null;
        }
        // check general app session timeout
        if (cockpitPreferenceManager != null) {
            cockpitPreferenceManager.checkSessionTimeout();
        }
        // handle v1 first
        if (deviceConfiguration.getSnmpVersion() < 3) {
            if (v1Connection != null) {
                if (deviceConfigMap.containsKey(deviceConfiguration.getUniqueDeviceId())) {
                    Log.d(TAG, "existing snmp v1Connection is used");
                    return v1Connection;
                } else {
                    deviceConfigMap.put(deviceConfiguration.getUniqueDeviceId(), deviceConfiguration);
                    resetV1Connection();
                }
            }
            if (v1Connection == null) {
                deviceConfigMap.put(deviceConfiguration.getUniqueDeviceId(), deviceConfiguration);
                resetV1Connection();
            }
            return v1Connection;
        }
        if (v3Connection != null) {
            if (deviceConfigMap.containsKey(deviceConfiguration.getUniqueDeviceId())) {
                Log.d(TAG, "existing snmp v3Connection is used");
                return v3Connection;
            } else {
                deviceConfigMap.put(deviceConfiguration.getUniqueDeviceId(), deviceConfiguration);
                resetV3Connection();
            }
        }
        if (v3Connection == null) {
            deviceConfigMap.put(deviceConfiguration.getUniqueDeviceId(), deviceConfiguration);
            resetV3Connection();
        }
        return v3Connection;
    }

    /**
     * setup a new v1Connection. very expensive
     */
    public synchronized void resetV1Connection() {
        Log.d(TAG, "SNMPv1Connection is set up / reset with " + deviceConfigMap.size() + " connections");
        // re-init v1Connection
        if (v1Connection != null) {
            v1Connection.close();
            v1Connection = null;
        }
        List<DeviceConfiguration> deviceConfigurationList = new ArrayList<>();
        for (DeviceConfiguration dv : deviceConfigMap.values()) {
            if (dv.getSnmpVersion() < 3) {
                if (timeoutCounterMap.containsKey(dv.getUniqueDeviceId())) {
                    timeoutCounterMap.get(dv.getUniqueDeviceId()).setValueAndTriggerObservers(false);
                }
                deviceConfigurationList.add(dv);
            }
        }
        if (deviceConfigurationList.isEmpty()) {
            Log.d(TAG, "no v1 connections for startup found");
            return;
        }
        v1Connection = new SnmpConnection(deviceConfigurationList.toArray(new DeviceConfiguration[]{}));
    }

    /**
     * setup a new v3Connection. very expensive
     */
    public synchronized void resetV3Connection() {
        Log.d(TAG, "SNMPv3Connection is set up / reset with " + deviceConfigMap.size() + " connections");
        // re-init v1Connection
        if (v3Connection != null) {
            v3Connection.close();
            v3Connection = null;
        }
        List<DeviceConfiguration> deviceConfigurationList = new ArrayList<>();
        for (DeviceConfiguration dv : deviceConfigMap.values()) {
            if (dv.getSnmpVersion() == 3) {
                if (timeoutCounterMap.containsKey(dv.getUniqueDeviceId())) {
                    timeoutCounterMap.get(dv.getUniqueDeviceId()).setValueAndTriggerObservers(false);
                }
                deviceConfigurationList.add(dv);
            }
        }
        if (deviceConfigurationList.isEmpty()) {
            Log.d(TAG, "no v3 connections for startup found");
            return;
        }
        v3Connection = new SnmpConnection(deviceConfigurationList.toArray(new DeviceConfiguration[]{}));
    }

    /**
     * app-wide thread pool executor for android async tasks
     *
     * @return
     */
    public synchronized ThreadPoolExecutor getThreadPoolExecutor() {
        if (threadPoolExecutor == null || threadPoolExecutor.isShutdown()
                || threadPoolExecutor.isTerminated() || threadPoolExecutor.isTerminating()) {
            threadPoolExecutor = createNewThreadPool();
        }
        return threadPoolExecutor;
    }

    /**
     * this method instantiates a new thread pool we use for async tasks
     *
     * @return
     */
    @NonNull
    private ThreadPoolExecutor createNewThreadPool() {
        Log.d(TAG, "init new ThreadPoolFactory");
        return new ThreadPoolExecutor(5, 250,
                600, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
    }

    /**
     * the total number of tested connections
     *
     * @return
     */
    public int getTotalConnectionTestCount() {
        return authProtocols.length * privProtocols.length;
    }

    public int getCurrentConnectionTestsDoneCount() {
        return connectionTestCounter;
    }

    /**
     * test all transport security options
     * atm ~ 25-30
     *
     * @param deviceConfiguration
     */
    public synchronized List<Pair<OID, OID>> testConnections(DeviceConfiguration deviceConfiguration, Runnable progressCallback,
                                                int connectionTestTimeout, int connectionTestRetries) {
        List<Pair<OID, OID>> combinationList = new ArrayList<>();
        Log.d(TAG, "using v1Connection test timeout: " + connectionTestTimeout
                + " ms with " + connectionTestRetries + " retries");

        if (!CockpitStateManager.getInstance().getNetworkSecurityObservable().getValue()) {
            Log.w(TAG, "connection test not allowed to run! network is not secure!");
            return combinationList;
        }

        // reset counter here
        connectionTestCounter = 0;
        boolean shallBreak = false;
        // ... this is expensive:
        for (int i = 0; i < authProtocols.length; i++) {
            for (int k = 0; k < privProtocols.length; k++) {
                connectionTestCounter++;
                progressCallback.run();
                // clone device config object
                DeviceConfiguration testConfig = new DeviceConfiguration(deviceConfiguration);

                testConfig.setAuthProtocol(authProtocols[i]);
                testConfig.setPrivProtocol(privProtocols[k]);
                testConfig.setTimeout(connectionTestTimeout);
                testConfig.setRetries(connectionTestRetries);
                SnmpConnection connection = new SnmpConnection(testConfig);

                boolean isWorking = connection.canPing(testConfig);
                if (isWorking) {
                    Log.d(TAG, "successful test with combination: "
                            + testConfig.getAuthProtocol() + "/" + testConfig.getPrivProtocol()
                            + testConfig.getAuthProtocolLabel() + "/"
                            + testConfig.getPrivProtocolLabel());

                    combinationList.add(new Pair<>(testConfig.getAuthProtocol(), testConfig.getPrivProtocol()));
                    shallBreak = true;
                    connection.close();
                    break;
                }
                connection.close();
                Log.d(TAG, "no cv3 onnection possible with combination: "
                        + testConfig.getAuthProtocol() + "/" + testConfig.getPrivProtocol());
            }
            if (shallBreak) {
                break;
            }
        }
        Log.d(TAG, "found " + combinationList.size() + " combinations found: " + combinationList);
        return combinationList;
    }

    /**
     * check if we know this v1Connection + if transport is listening
     *
     * @param deviceId
     * @return
     */
    public boolean doesConnectionExist(String deviceId) {
        Log.d(TAG, "existing: " + deviceConfigMap.toString());
        return deviceConfigMap.containsKey(deviceId);
    }

    public OID[] getPrivProtocols() {
        return privProtocols;
    }

    public void setPrivProtocols(OID[] privProtocols) {
        this.privProtocols = privProtocols;
    }

    public OID[] getAuthProtocols() {
        return authProtocols;
    }

    public void setAuthProtocols(OID[] authProtocols) {
        this.authProtocols = authProtocols;
    }

    /**
     * method to clear all snmp connections
     */
    public synchronized void clearConnections() {
        Log.d(TAG, "clear all snmp connections");
        deviceConfigMap.clear();
        if (v1Connection != null) {
            v1Connection.close();
            v1Connection = null;
        }
        if (v3Connection != null) {
            v3Connection.close();
            v3Connection = null;
        }
        timeoutCounterMap.clear();
    }

    /**
     * remove a v1Connection of the v1Connection pool
     * NOTE: never call this method on main thread!
     *
     * @param deviceConfiguration
     */
    public synchronized void removeConnection(DeviceConfiguration deviceConfiguration) {
        Log.d(TAG, "removing connection from pool: " + deviceConfiguration.getUniqueDeviceId());
        deviceConfigMap.remove(deviceConfiguration.getUniqueDeviceId());
        timeoutCounterMap.remove(deviceConfiguration.getUniqueDeviceId());
        Log.d(TAG, "after deletion: " + deviceConfigMap.toString());
        Log.d(TAG, "after deletion: " + timeoutCounterMap.toString());

        if (deviceConfiguration.isV3()) {
            resetV3Connection();
        } else {
            resetV1Connection();
        }
    }

    /**
     * method to register a timeout event
     *
     * @param deviceConfiguration
     */
    public synchronized void registerTimeout(DeviceConfiguration deviceConfiguration) {
        if (deviceConfiguration == null) {
            throw new IllegalArgumentException("null device config given");
        }
        if (CockpitStateManager.getInstance().isInRemoval()
                || CockpitStateManager.getInstance().isConnecting()) {
            Log.d(TAG, "timeout event not registered during device connection or removal event");
            return;
        }
        if (!timeoutCounterMap.containsKey(deviceConfiguration.getUniqueDeviceId())) {
            timeoutCounterMap.put(deviceConfiguration.getUniqueDeviceId(),
                    new TimeoutObservable(true, deviceConfiguration));
        }
        timeoutCounterMap.get(deviceConfiguration.getUniqueDeviceId())
                .setValueAndTriggerObservers(true);

        CockpitStateManager.getInstance().getIsInTimeoutsObservable().setValueAndTriggerObservers(true);

        SnmpManager.getInstance().getThreadPoolExecutor().shutdownNow();

        Log.d(TAG, "timeout detected of device: " + deviceConfiguration.getUniqueDeviceId());
    }

    /**
     * fire this event if a connection worked
     *
     * @param deviceConfiguration
     */
    public synchronized void resetTimeout(DeviceConfiguration deviceConfiguration) {
        if (deviceConfiguration == null) {
            throw new IllegalArgumentException("null device config given");
        }
        if (!timeoutCounterMap.containsKey(deviceConfiguration.getUniqueDeviceId())) {
            timeoutCounterMap.put(deviceConfiguration.getUniqueDeviceId(), new TimeoutObservable(false, deviceConfiguration));
        }
        timeoutCounterMap.get(deviceConfiguration.getUniqueDeviceId())
                .setValueAndTriggerObservers(false);

        CockpitStateManager.getInstance().getIsInTimeoutsObservable().setValueAndTriggerObservers(false);
        Log.d(TAG, "general timeout state value: " + CockpitStateManager.getInstance().getIsInTimeoutsObservable().getValue());

        Log.d(TAG, "timeout counter reset for device: " + deviceConfiguration.getUniqueDeviceId());
    }

    /**
     * retrieves current devices which are in timeout state
     *
     * @return
     */
    public ManagedDevice[] getDevicesInTimeout() {
        ArrayList<ManagedDevice> devices = new ArrayList<>();
        Enumeration<String> keys = timeoutCounterMap.keys();
        for (TimeoutObservable timeoutObservable : timeoutCounterMap.values()) {
            String deviceIdKey = keys.nextElement();
            if (timeoutObservable.getValue()) {
                if (!deviceConfigMap.containsKey(deviceIdKey)
                        || !DeviceManager.getInstance().hasDevice(deviceIdKey)) {
                    continue;
                }
                devices.add(DeviceManager.getInstance().getDevice(deviceIdKey));
            }
        }
        return devices.toArray(new ManagedDevice[]{});
    }

    /**
     * helper method to debug internal state in logs
     *
     * @return
     */
    public String printState() {
        StringBuilder sb = new StringBuilder();
        sb.append("v1: ");
        if (v1Connection != null) {
            sb.append("up");
        } else {
            sb.append("down");
        }
        sb.append(" v3: ");
        if (v3Connection == null) {
            sb.append("down");
        } else {
            sb.append("up");
        }
        return sb.toString();
    }

    /**
     * should be injected one time
     *
     * @param cockpitPreferenceManager
     */
    public void setPreferenceManager(CockpitPreferenceManager cockpitPreferenceManager) {
        this.cockpitPreferenceManager = cockpitPreferenceManager;
    }

    /**
     * face method to call request counter
     */
    public void incrementRequestCounter() {
        if (cockpitPreferenceManager != null) {
            cockpitPreferenceManager.incrementRequestCounter();
        } else {
            Log.w(TAG, "no preference manager instance existing!");
        }
    }
}