package de.uni_stuttgart.informatik.sopra.sopraapp;

import org.snmp4j.mp.MPv3;
import org.snmp4j.smi.OctetString;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.QueryCache;
import de.uni_stuttgart.informatik.sopra.sopraapp.tasks.SNMPConnectivityAddDeviceTask;
import de.uni_stuttgart.informatik.sopra.sopraapp.util.BooleanObservable;

/**
 * activities are coming and going - this class should last to hold the (network security) state of the app
 */
public class CockpitStateManager {
    private static CockpitStateManager instance;
    // observables
    private BooleanObservable networkSecurityBooleanObservable = new BooleanObservable(false);
    private BooleanObservable isInTimeoutsObservable = new BooleanObservable(false);
    private BooleanObservable isInSessionTimeoutObservable = new BooleanObservable(false);

    private boolean isConnecting = false;
    private boolean isInRemoval = false;
    private SNMPConnectivityAddDeviceTask connectionTask = null;
    private OctetString localEngineId = null;
    private QueryCache queryCache;

    private CockpitStateManager() {
        queryCache = new QueryCache();
    }

    /**
     * singleton access method
     *
     * @return
     */
    public static synchronized CockpitStateManager getInstance() {
        if (instance == null) {
            instance = new CockpitStateManager();
        }
        return instance;
    }

    public BooleanObservable getNetworkSecurityObservable() {
        return networkSecurityBooleanObservable;
    }

    public BooleanObservable getIsInTimeoutsObservable() {
        return isInTimeoutsObservable;
    }

    public BooleanObservable getIsInSessionTimeoutObservable() {
        return isInSessionTimeoutObservable;
    }

    public void setConnecting(boolean connecting) {
        isConnecting = connecting;
    }

    public boolean isConnecting() {
        return isConnecting;
    }

    public SNMPConnectivityAddDeviceTask getConnectionTask() {
        return connectionTask;
    }

    public void setConnectionTask(SNMPConnectivityAddDeviceTask connectionTask) {
        this.connectionTask = connectionTask;
    }

    /**
     * get app wide unique local engine id
     *
     * @return
     */
    public OctetString getLocalEngineId() {
        if (localEngineId == null) {
            localEngineId = new OctetString(MPv3.createLocalEngineID());
        }
        return localEngineId;
    }

    public QueryCache getQueryCache() {
        return queryCache;
    }

    /**
     * helper method to detect timeout state
     * 
     * @return
     */
    public boolean isInTimeouts() {
        if (getIsInTimeoutsObservable().getValue()) {
            return true;
        }
        if (getIsInSessionTimeoutObservable().getValue()) {
            return true;
        }
        return false;
    }

    public void setRemovalOngoing(boolean isInRemoval) {
        this.isInRemoval = isInRemoval;
    }

    public boolean isInRemoval() {
        return isInRemoval;
    }
}
