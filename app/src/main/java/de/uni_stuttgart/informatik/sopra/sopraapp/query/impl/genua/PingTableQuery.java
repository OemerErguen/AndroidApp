package de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.genua;

import org.snmp4j.smi.OID;

import java.util.HashMap;
import java.util.Map;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.AbstractSnmpTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * 1.3.6.1.4.1.3717.2.1.4
 */
public class PingTableQuery extends AbstractSnmpTableQuery {
    @Override
    public Map<String, OID> getColumnDefinition() {
        Map<String, OID> columnDefinition = new HashMap<>();
        columnDefinition.put("pingIndex", new OID(new int[]{1, 3, 6, 1, 4, 1, 3717, 2, 1, 4, 1, 1}));
        columnDefinition.put("pingName", new OID(new int[]{1, 3, 6, 1, 4, 1, 3717, 2, 1, 4, 1, 2}));
        columnDefinition.put("pingIp", new OID(new int[]{1, 3, 6, 1, 4, 1, 3717, 2, 1, 4, 1, 3}));
        columnDefinition.put("pingStatus", new OID(new int[]{1, 3, 6, 1, 4, 1, 3717, 2, 1, 4, 1, 4}));
        return columnDefinition;
    }

    @Override
    public String getRowTitle(Map<String, QueryResponse> singleRow, int index) {
        return concatenateIfPossible(singleRow, new String[]{
                "pingIndex",
                "pingName",
                "pingIp"
        }, "-");
    }

    public static class PingTableQueryRequest extends AbstractTableQueryRequest<PingTableQuery> {

        public PingTableQueryRequest(DeviceConfiguration deviceConfiguration) {
            super(deviceConfiguration);
        }

        @Override
        public OID getOidQuery() {
            return new OID(new int[]{1, 3, 6, 1, 4, 1, 3717, 2, 1, 4});
        }

        @Override
        public Class<PingTableQuery> getQueryClass() {
            return PingTableQuery.class;
        }
    }
}
