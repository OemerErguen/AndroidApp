package de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general;

import org.snmp4j.smi.OID;

import java.util.HashMap;
import java.util.Map;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.AbstractSnmpTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * this class represents a query for ip address table 1.3.6.1.2.1.4.20
 */
public class IpAddressTableQuery extends AbstractSnmpTableQuery {
    @Override
    public Map<String, OID> getColumnDefinition() {
        Map<String, OID> columnDefinition = new HashMap<>();
        columnDefinition.put("ipAdEntAddr", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 20, 1, 1}));
        columnDefinition.put("ipAdEntIfIndex", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 20, 1, 2}));
        columnDefinition.put("ipAdEntNetMask", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 20, 1, 3}));
        columnDefinition.put("ipAdEntBcastAddr", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 20, 1, 4}));
        columnDefinition.put("ipAdEntReasmMaxSize", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 20, 1, 5}));
        return columnDefinition;
    }

    @Override
    public String getRowTitle(Map<String, QueryResponse> singleRow, int index) {
        return concatenateIfPossible(singleRow, new String[]{
                "ipAdEntIfIndex",
                "ipAdEntAddr",
        }, "-");
    }

    /**
     * request class
     */
    public static class IpAddrTableRequest extends AbstractTableQueryRequest<IpAddressTableQuery> {
        public IpAddrTableRequest(DeviceConfiguration deviceConfiguration) {
            super(deviceConfiguration);
        }

        @Override
        public OID getOidQuery() {
            return new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 20});
        }

        @Override
        public Class<IpAddressTableQuery> getQueryClass() {
            return IpAddressTableQuery.class;
        }
    }
}
