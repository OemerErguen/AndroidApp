package de.uni_stuttgart.informatik.sopra.sopraapp.snmp;

import android.util.Log;

import org.snmp4j.AbstractTarget;
import org.snmp4j.MessageDispatcher;
import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.SNMP4JSettings;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.Priv3DES;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.uni_stuttgart.informatik.sopra.sopraapp.CockpitStateManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.adapter.V1Adapter;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.adapter.V3Adapter;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * this class represents a real connection to an snmp daemon
 */
public class SnmpConnection {

    public static final String TAG = SnmpConnection.class.getName();
    private final DeviceConfiguration[] deviceConfigurations;
    private Snmp snmp;
    private DefaultUdpTransportMapping transport;
    private MessageDispatcher dispatcher;
    // device specific:
    private Map<String, AbstractSnmpAdapter> adapterList = new ConcurrentHashMap<>();
    private Map<String, AbstractTarget> targetList = new ConcurrentHashMap<>();
    private static OctetString localEngineId;
    private static boolean isInited = false;
    private static USM usm;
    private static int engineBoots = -1;

    /**
     * constructor
     *
     * @param deviceConfig
     */
    public SnmpConnection(final DeviceConfiguration[] deviceConfig) {
        deviceConfigurations = deviceConfig;
        init();
    }

    /**
     * for single connections
     *
     * @param deviceConfiguration
     */
    public SnmpConnection(final DeviceConfiguration deviceConfiguration) {
        deviceConfigurations = new DeviceConfiguration[]{deviceConfiguration};
        init();
    }

    /**
     * is called in constructor and sets up a snmp connection
     */
    private void init() {
        // set local engine id
        if (localEngineId == null) {
            localEngineId = new OctetString(MPv3.createLocalEngineID());
            Log.d(TAG, "set local engine id: " + localEngineId);
        }

        if (!isInited) {
            oneTimeInit();
            isInited = true;
        }

        // load generic adapter stuff
        loadAdapters();

        Log.d(TAG, "using local engine id: " + localEngineId);

        if (transport == null || snmp == null) {
            Log.e(TAG, "no valid transport object");
            return;
        }
        try {
            isUp();
        } catch (IOException e) {
            Log.w(SnmpConnection.class.getName(), e.getMessage());
        } finally {
            Log.d(TAG, "is listening: " + transport.isListening());
        }
    }

    /**
     * this is executed only once per app lifetime
     */
    private void oneTimeInit() {
        Log.d(TAG, "snmp4j is initially configured");
        SNMP4JSettings.setAllowSNMPv2InV1(true);
        SNMP4JSettings.setSnmp4jStatistics(SNMP4JSettings.Snmp4jStatistics.basic);
        SNMP4JSettings.setCheckUsmUserPassphraseLength(true);
        SNMP4JSettings.setForwardRuntimeExceptions(false);
        SNMP4JSettings.setExtensibilityEnabled(false);
        SecurityProtocols.getInstance().addDefaultProtocols();
        SecurityProtocols.getInstance().addPrivacyProtocol(new Priv3DES());
    }

    /**
     * the transport instance
     *
     * @return
     */
    public TransportMapping<UdpAddress> getTransport() {
        return transport;
    }

    /**
     * direct access should be avoided
     *
     * @return
     */
    protected Snmp getSnmp() {
        return snmp;
    }

    /**
     * checks if a device configuration is registered in this connection
     *
     * @param deviceConfiguration
     * @return
     */
    private boolean isRegistered(DeviceConfiguration deviceConfiguration) {
        if (adapterList.containsKey(deviceConfiguration.getUniqueConnectionId())
                && targetList.containsKey(deviceConfiguration.getUniqueConnectionId())) {
            return true;
        }
        return false;
    }

    /**
     * method to query single oids.
     * NOTE: sometimes you should append ".0" to avoid noSuchName Err
     *
     * @param deviceConfiguration
     * @param oid
     * @return
     */
    public List<QueryResponse> querySingle(DeviceConfiguration deviceConfiguration, String oid) {
        return querySingle(deviceConfiguration, new OID(oid));
    }

    /**
     * method to retrieve device specific version adapters
     *
     * @param deviceConfiguration
     * @return
     */
    private AbstractSnmpAdapter getAdapter(DeviceConfiguration deviceConfiguration) {
        if (adapterList.containsKey(deviceConfiguration.getUniqueConnectionId())) {
            return adapterList.get(deviceConfiguration.getUniqueConnectionId());
        }
        throw new IllegalStateException("invalid call for adapter");
    }

    /**
     * query a single oid
     *
     * @param deviceConfiguration
     * @param oid
     * @return
     */
    public List<QueryResponse> querySingle(DeviceConfiguration deviceConfiguration, OID oid) {
        if (!isRegistered(deviceConfiguration)) {
            Log.e(TAG, "device configuration not registered");
            return null;
        }
        if (!isSnmpAllowed()) {
            Log.w(TAG, "no snmp connection is allowed because of unsafe network");
            return null;
        }
        AbstractSnmpAdapter adapter = getAdapter(deviceConfiguration);
        return adapter.querySingle(oid.toDottedString());
    }

    /**
     * method to walk an oid subtree
     *
     * @param deviceConfiguration
     * @param oid
     * @return
     */
    public List<QueryResponse> queryWalk(DeviceConfiguration deviceConfiguration, String oid) {
        return queryWalk(deviceConfiguration, new OID(oid));
    }

    /**
     * query walk
     *
     * @param deviceConfiguration
     * @param oid
     * @return
     */
    public List<QueryResponse> queryWalk(DeviceConfiguration deviceConfiguration, OID oid) {
        if (!isRegistered(deviceConfiguration)) {
            Log.e(TAG, "device configuration not registered");
            return null;
        }
        if (!isSnmpAllowed()) {
            Log.w(TAG, "no snmp connection is allowed because of unsafe network");
            return null;
        }
        AbstractSnmpAdapter adapter = getAdapter(deviceConfiguration);
        return adapter.queryWalk(oid.toDottedString());
    }

    public DeviceConfiguration[] getDeviceConfigurations() {
        return deviceConfigurations;
    }

    /**
     * simple debug method
     *
     * @param oid
     */
    public void printWalk(DeviceConfiguration deviceConfiguration, String oid) {
        List<QueryResponse> queryResponses = queryWalk(deviceConfiguration, oid);
        for (QueryResponse qr : queryResponses) {
            Log.d(TAG, qr.toString());
        }
    }

    /**
     * helper method to check if snmp connections are allowed
     *
     * @return
     */
    private boolean isSnmpAllowed() {
        boolean isNetworkSecure = CockpitStateManager.getInstance().getNetworkSecurityObservable().getValue();
        boolean isInTimeouts = CockpitStateManager.getInstance().getIsInTimeoutsObservable().getValue();
        boolean isInSessionTimeout = CockpitStateManager.getInstance().getIsInSessionTimeoutObservable().getValue();
        Log.d(TAG, "network security: " + isNetworkSecure);
        Log.d(TAG, "timeout state: " + isInTimeouts);
        Log.d(TAG, "session timeout state: " + isInTimeouts);

        return isNetworkSecure && !isInTimeouts && !isInSessionTimeout;
    }

    /**
     * closes transport + snmp
     */
    public void close() {
        Log.d(TAG, "stop listening on: " + transport.getListenAddress());
        try {
            if (snmp != null) {
                snmp.close();
            }
            transport.close();
            dispatcher = null;
            snmp = null;
        } catch (IOException | RuntimeException e) {
            Log.e(SnmpConnection.class.getName(), e.getMessage());
        } finally {
            Log.d(TAG, "closing snmp connection finished");
        }
    }

    /**
     * load correct adapter depending on version
     */
    private final void loadAdapters() {
        startupSnmp();

        // fill adapter and target list
        adapterList.clear();
        targetList.clear();
        boolean isV3 = false;
        List<UsmUser> userList = new ArrayList<>();
        for (DeviceConfiguration singleDeviceConfig : deviceConfigurations) {
            AbstractSnmpAdapter snmpAdapter = null;
            if (singleDeviceConfig.isV3()) {
                // we use one single v3 adapter
                snmpAdapter = new V3Adapter(snmp, transport);
            }
            if (singleDeviceConfig.isV1()) {
                snmpAdapter = new V1Adapter(snmp, transport, false);
            }
            if (singleDeviceConfig.isV2c()) {
                snmpAdapter = new V1Adapter(snmp, transport, true);
            }
            if (snmpAdapter == null) {
                throw new IllegalStateException("adapter loading failed: " + singleDeviceConfig.getSnmpVersion());
            }
            if (singleDeviceConfig.getSnmpVersion() == 3) {
                isV3 = true;
            }
            snmpAdapter.setDeviceConfiguration(singleDeviceConfig);
            adapterList.put(singleDeviceConfig.getUniqueConnectionId(), snmpAdapter);
            targetList.put(singleDeviceConfig.getUniqueConnectionId(), snmpAdapter.getTarget());
            if (snmpAdapter instanceof V3Adapter) {
                UsmUser user = ((V3Adapter) snmpAdapter).getUser();
                if (user != null) {
                    userList.add(user);
                } else {
                    Log.e(TAG, "invalud user configuration detected");
                }
            }
        }

        // setup usm
        if (isV3) {
            resetUsm(userList);
        }
    }

    /**
     * init transport + snmp class of this connection
     */
    @SuppressWarnings("squid:S1313")
    private void startupSnmp() {
        if (transport == null) {
            try {
                transport = new DefaultUdpTransportMapping(new UdpAddress("0.0.0.0/0"), true);
                transport.setAsyncMsgProcessingSupported(false);
                transport.setSocketTimeout(5000);
            } catch (IOException e) {
                Log.e(TAG, "exception during transport startup: " + e.getMessage());
            } catch (RuntimeException re) {
                Log.e(TAG, "runtime exception during snmp startup: " + re.getMessage());
            }
        }
        if (dispatcher == null) {
            dispatcher = getMessageDispatcher();
        }
        snmp = new Snmp(dispatcher, transport);
    }

    /**
     * configure snmp4j message dispatcher
     *
     * @return
     */
    public MessageDispatcher getMessageDispatcher() {
        if (dispatcher == null) {
            dispatcher = new MessageDispatcherImpl();
            dispatcher.addMessageProcessingModel(new MPv1());
            dispatcher.addMessageProcessingModel(new MPv2c());
            dispatcher.addMessageProcessingModel(new MPv3());
        }
        return dispatcher;
    }

    /**
     * simulates a "ping"
     *
     * @return
     */
    public boolean canPing(DeviceConfiguration deviceConfiguration) {
        // use sysName
        List<QueryResponse> responseList = querySingle(deviceConfiguration, "1.3.6.1.2.1.1.5.0");
        if (responseList == null) {
            Log.d(TAG, "ping: false");
            return false;
        }
        if (!checkResponseList(responseList)) {
            Log.w(TAG, "ping: false - snmp connection error");
            return false;
        }

        Log.d(TAG, "ping responseList (should be sysName):" + responseList.toString());
        boolean canPing = !responseList.isEmpty();
        Log.d(TAG, "ping: " + canPing);
        return canPing;
    }

    /**
     * check for wrong user
     *
     * @param responseList
     * @return
     */
    private boolean checkResponseList(List<QueryResponse> responseList) {
        for (QueryResponse singleResponse : responseList) {
            if (singleResponse.getVariableBinding().getOid().equals(SnmpConstants.usmStatsUnknownUserNames)) {
                Log.d(TAG, "wrong username detected in ping");
                // unknown user detected!
                return false;
            }
        }
        return true;
    }

    /**
     * checks transport is listening
     *
     * @throws IOException
     */
    public void isUp() throws IOException {
        if (!transport.isListening()) {
            snmp.listen();
            Log.d(TAG, "transport was closed. start listening on new udp socket: " + transport.getListenAddress().toString());
        } else {
            Log.d(TAG, "transport already listening");
        }
    }

    /**
     * helper method to control usm
     * @param userList
     */
    public void resetUsm(List<UsmUser> userList) {
        Log.d(TAG, "usm handling enabled - v3 connection exists");
        if (usm != null) {
            Log.d(TAG, "unregistering old security model");
            SecurityModels.getInstance().removeSecurityModel(new Integer32(usm.getID()));
        }
        usm = new USM(SecurityProtocols.getInstance(), localEngineId, engineBoots++);
        Log.d(TAG, "engine boots: " + engineBoots);
        for (UsmUser singleUser : userList) {
            usm.addUser(singleUser);
        }
        Log.d(TAG, "usm: added " + userList.size() + " users");
        SecurityModels.getInstance().addSecurityModel(usm);
    }
}
