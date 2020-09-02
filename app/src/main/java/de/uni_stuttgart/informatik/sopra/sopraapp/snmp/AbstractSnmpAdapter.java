package de.uni_stuttgart.informatik.sopra.sopraapp.snmp;

import android.util.Log;

import org.snmp4j.AbstractTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.TreeEvent;
import org.snmp4j.util.TreeUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.CockpitStateManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * this adapter abstracts the (partially) version specific device/message handling
 * <p>
 */
public abstract class AbstractSnmpAdapter {

    public static final String TAG = AbstractSnmpAdapter.class.getName();
    protected Snmp snmp;
    protected TransportMapping<UdpAddress> udpAddressTransportMapping;
    protected AbstractTarget target = null;
    // can be set in subclasses
    protected DeviceConfiguration deviceConfiguration = null;

    /**
     * constructor
     *
     * @param snmp
     * @param udpAddressTransportMapping
     */
    public AbstractSnmpAdapter(Snmp snmp, TransportMapping<UdpAddress> udpAddressTransportMapping) {
        this.snmp = snmp;
        this.udpAddressTransportMapping = udpAddressTransportMapping;
    }

    // abstract methods

    /**
     * define version specific target in this class
     *
     * @return
     */
    public abstract AbstractTarget getTarget();

    /**
     * method to query a single oid
     *
     * @param oid
     * @return
     */
    public abstract List<QueryResponse> querySingle(String oid);

    /**
     * method to query a walk of a single oid
     *
     * @param oid
     * @return
     */
    public abstract List<QueryResponse> queryWalk(String oid);

    /**
     * common used methods
     *
     * @param treeUtils
     * @param oid
     * @return
     * @throws NoSnmpResponseException
     */
    public synchronized List<QueryResponse> basicWalk(TreeUtils treeUtils, String oid) throws NoSnmpResponseException {
        Log.d(TAG, "query walk");
        if (checkIfAllowed()) {
            // FIXME naming
            return null;
        }
        final List<TreeEvent> events = Collections.synchronizedList(treeUtils.getSubtree(getTarget(), new OID(oid)));
        if (events.isEmpty()) {
            Log.w(TAG, "no response event returned");
            throw new NoSnmpResponseException();
        }

        synchronized (events) {
            List<QueryResponse> queryResponses = Collections.synchronizedList(new ArrayList<>());
            // this code is critical for ConcurrentModificationExceptions
            for (TreeEvent event : events) {
                if (event == null) {
                    continue;
                }
                if (event.isError()) {
                    Log.e(TAG, "Error: table OID [" + oid + "] " + event.getErrorMessage());
                    continue;
                }

                VariableBinding[] varBindings = event.getVariableBindings();
                if (varBindings == null || varBindings.length == 0) {
                    Log.d(TAG, "no var bindings");
                    continue;
                }
                for (VariableBinding varBinding : varBindings) {
                    if (varBinding == null) {
                        continue;
                    }
                    queryResponses.add(new QueryResponse(null, varBinding.getOid().toDottedString(), varBinding));
                }
            }
            SnmpManager.getInstance().incrementRequestCounter();
            return queryResponses;
        }
    }

    public DeviceConfiguration getDeviceConfiguration() {
        return deviceConfiguration;
    }

    public void setDeviceConfiguration(DeviceConfiguration deviceConfiguration) {
        this.deviceConfiguration = deviceConfiguration;
    }

    /**
     * synchronous get
     *
     * @param pdu
     * @return
     */
    protected List<QueryResponse> basicSingleGet(PDU pdu) {
        List<QueryResponse> responseList = new ArrayList<>();
        if (checkIfAllowed()) return null;
        try {
            ResponseEvent re = snmp.get(pdu, getTarget());
            boolean isResponseValid = checkResponseEvent(re);
            if (isResponseValid) {
                PDU response = re.getResponse();
                for (VariableBinding varBind : response.getVariableBindings()) {
                    responseList.add(new QueryResponse(pdu, varBind.getOid().toDottedString(), varBind));
                }
                SnmpManager.getInstance().incrementRequestCounter();
                return responseList;
            }
        } catch (IOException | NoSnmpResponseException e) {
            Log.w(SnmpConnection.class.getName(), "" + e.getMessage());
            return null;
        }
        return responseList;
    }

    /**
     * helper method to check if we are allowed to fire a snmp request
     *
     * TODO invert name
     * @return
     */
    private boolean checkIfAllowed() {
        if (!CockpitStateManager.getInstance().getNetworkSecurityObservable().getValue()) {
            Log.w(TAG, "request not allowed. network not secure!");
            return true;
        }
        return false;
    }

    /**
     * generic method to check if response event is valid
     *
     * @param re ResponseEvent
     * @return result
     * @throws NoSnmpResponseException
     */
    protected boolean checkResponseEvent(ResponseEvent re) throws NoSnmpResponseException {
        if (re != null) {
            PDU pdu = re.getResponse();
            if (pdu == null) {
                throw new NoSnmpResponseException();
            }
            Log.d(TAG, "pdu status: " + pdu.getErrorStatus() + " " + pdu.getErrorStatusText());
            if (pdu.getErrorStatus() == PDU.noError) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * formats the address string for a connection
     *
     * @return
     */
    protected String getAddress() {
        return String.format(
                "%s:%s/%s",
                deviceConfiguration.getNetworkProtocol(),
                deviceConfiguration.getTargetIp(),
                deviceConfiguration.getTargetPort()
        );
    }
}
