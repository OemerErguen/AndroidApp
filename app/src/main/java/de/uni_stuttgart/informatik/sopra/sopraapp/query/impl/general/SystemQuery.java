package de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general;

import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OID;

import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.AbstractQueryRequest;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.AbstractSnmpQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.VendorCatalog;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * first implementation of a query class
 */
public class SystemQuery extends AbstractSnmpQuery {

    private String sysDescr = null;
    private String sysObjectId = null;
    private String sysUpTime = null;
    private String sysContact = null;
    private String sysName = null;
    private String sysLocation = null;
    private String sysServices = null;
    private String space = "\n";

    @Override
    public void processResult(List<QueryResponse> results) {
        sysDescr = getOIDValue(results, SnmpConstants.sysDescr);
        String oidValue = getOIDValue(results, SnmpConstants.sysObjectID);
        String vendorName = VendorCatalog.getInstance(null).getVendorByOID(oidValue);
        if (vendorName != null) {
            sysObjectId = oidValue +  space + vendorName;
        } else {
            sysObjectId = oidValue;
        }
        sysUpTime = getOIDValue(results, SnmpConstants.sysUpTime);
        sysContact = getOIDValue(results, SnmpConstants.sysContact);
        sysName = getOIDValue(results, SnmpConstants.sysName);
        sysLocation = getOIDValue(results, SnmpConstants.sysLocation);
        sysServices = getOIDValue(results, SnmpConstants.sysServices);
    }

    public String getSysDescr() {
        return sysDescr;
    }

    public String getSysObjectId() {
        return sysObjectId;
    }

    public String getSysUpTime() {
        return sysUpTime;
    }

    public String getSysContact() {
        return sysContact;
    }

    public String getSysName() {
        return sysName;
    }

    public String getSysLocation() {
        return sysLocation;
    }

    public String getSysServices() {
        return sysServices;
    }

    public static class SystemQueryRequest extends AbstractQueryRequest<SystemQuery> {

        public SystemQueryRequest(DeviceConfiguration deviceConfiguration) {
            super(deviceConfiguration);
        }

        @Override
        public boolean isSingleRequest() {
            return false;
        }

        @Override
        public OID getOidQuery() {
            return new OID(new int[]{1, 3, 6, 1, 2, 1, 1});
        }

        @Override
        public Class<SystemQuery> getQueryClass() {
            return SystemQuery.class;
        }
    }
}
