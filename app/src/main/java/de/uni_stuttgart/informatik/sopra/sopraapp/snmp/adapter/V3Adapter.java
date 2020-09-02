package de.uni_stuttgart.informatik.sopra.sopraapp.snmp.adapter;

import android.support.annotation.Nullable;
import android.util.Log;

import org.snmp4j.AbstractTarget;
import org.snmp4j.PDU;
import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.UserTarget;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TreeUtils;

import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.AbstractSnmpAdapter;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.NoSnmpResponseException;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * this class handles snmp4j version specific stuff for snmpv3 connections
 */
public class V3Adapter extends AbstractSnmpAdapter {

    public static final String TAG = V3Adapter.class.getName();

    /**
     * constructor
     *
     * @param snmp
     * @param udpAddressTransportMapping
     */
    public V3Adapter(Snmp snmp, TransportMapping<UdpAddress> udpAddressTransportMapping) {
        super(snmp, udpAddressTransportMapping);
    }

    @Override
    public AbstractTarget getTarget() {
        if (target != null) {
            return target;
        }
        UserTarget userTarget = new UserTarget();
        int secLevel = SecurityLevel.AUTH_PRIV;
        if (deviceConfiguration.getSecurityLevel() != null) {
            secLevel = deviceConfiguration.getSecurityLevel().getSnmpValue();
        }
        Log.d(TAG, "using security level: " + SecurityLevel.values()[secLevel]);
        userTarget.setSecurityLevel(secLevel);
        OctetString securityName = new OctetString(deviceConfiguration.getUsername());
        userTarget.setSecurityName(securityName);

        final String udpIpAddress = getAddress();

        userTarget.setAddress(GenericAddress.parse(udpIpAddress));
        userTarget.setVersion(deviceConfiguration.getSnmpVersion());
        userTarget.setRetries(deviceConfiguration.getRetries());
        userTarget.setTimeout(deviceConfiguration.getTimeout());
        target = userTarget;
        return userTarget;
    }

    /**
     * specific snmpv3 method for usm user entry handling
     *
     * @return
     */
    @Nullable
    public UsmUser getUser() {
        OctetString authPassphrase = new OctetString(deviceConfiguration.getAuthPassphrase());
        OctetString privPassphrase = new OctetString(deviceConfiguration.getPrivacyPassphrase());
        OctetString securityName = new OctetString(deviceConfiguration.getUsername());
        SecurityLevel securityLevel = deviceConfiguration.getSecurityLevel();
        Log.d(TAG, "security level: " + securityLevel);
        switch (securityLevel) {
            case authPriv:
                if (authPassphrase.length() < 8) {
                    return null;
                }
                if (privPassphrase.length() < 8) {
                    return null;
                }
                return new UsmUser(
                        securityName,
                        deviceConfiguration.getAuthProtocol(),
                        authPassphrase,
                        deviceConfiguration.getPrivProtocol(),
                        privPassphrase);
            case authNoPriv:
                if (authPassphrase.length() < 8) {
                    return null;
                }
                return new UsmUser(securityName, deviceConfiguration.getAuthProtocol(), authPassphrase,
                                    null, null);
            case undefined:
            case noAuthNoPriv:
                return new UsmUser(securityName, null, null, null, null);
        }
        Log.e(TAG, "invalid security level configuration for seclevel:" + securityLevel);
        return null;
    }

    @Override
    public List<QueryResponse> querySingle(String oid) {
        Log.d(TAG, "query single: " + oid);
        ScopedPDU pdu = new ScopedPDU();
        pdu.add(new VariableBinding(new OID(oid)));
        if (deviceConfiguration.getContext() != null) {
            pdu.setContextName(new OctetString(deviceConfiguration.getContext()));
        }
        pdu.setType(PDU.GET);
        return basicSingleGet(pdu);
    }

    @Override
    public List<QueryResponse> queryWalk(String oid) {
        TreeUtils treeUtils = new TreeUtils(snmp, new DefaultPDUFactory(PDU.GETBULK));
        try {
            return basicWalk(treeUtils, oid);
        } catch (NoSnmpResponseException e) {
            return null;
        }
    }
}
