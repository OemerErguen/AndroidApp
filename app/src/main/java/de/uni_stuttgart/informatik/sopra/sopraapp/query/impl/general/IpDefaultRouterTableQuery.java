package de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general;

import org.snmp4j.smi.OID;

import java.util.HashMap;
import java.util.Map;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.AbstractSnmpTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * 1.3.6.1.2.1.4.37.1.1
 */
public class IpDefaultRouterTableQuery extends AbstractSnmpTableQuery {
    @Override
    public Map<String, OID> getColumnDefinition() {
        Map<String, OID> columnDefinition = new HashMap<>();
        columnDefinition.put("ipDefaultRouterAddressType", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 37, 1, 1}));
        columnDefinition.put("ipDefaultRouterAddress", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 37, 1, 2}));
        columnDefinition.put("ipDefaultRouterIfIndex", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 37, 1, 3}));
        columnDefinition.put("ipDefaultRouterLifetime", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 37, 1, 4}));
        columnDefinition.put("ipDefaultRouterPreference", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 37, 1, 5}));
        return columnDefinition;
    }

    @Override
    public String getRowTitle(Map<String, QueryResponse> singleRow, int index) {
        return concatenateIfPossible(singleRow, new String[]{
                "ipDefaultRouterIfIndex",
                "ipDefaultRouterAddress"
        }, "-");
    }

    public static class IpDefaultRouterTableQueryRequest extends AbstractTableQueryRequest<IpDefaultRouterTableQuery> {

        public IpDefaultRouterTableQueryRequest(DeviceConfiguration deviceConfiguration) {
            super(deviceConfiguration);
        }

        @Override
        public OID getOidQuery() {
            return new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 37});
        }

        @Override
        public Class<IpDefaultRouterTableQuery> getQueryClass() {
            return IpDefaultRouterTableQuery.class;
        }
    }
}
