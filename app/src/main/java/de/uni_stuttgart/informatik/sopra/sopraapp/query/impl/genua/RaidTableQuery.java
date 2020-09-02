package de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.genua;

import org.snmp4j.smi.OID;

import java.util.HashMap;
import java.util.Map;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.AbstractSnmpTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

public class RaidTableQuery extends AbstractSnmpTableQuery {
    @Override
    public Map<String, OID> getColumnDefinition() {
        Map<String, OID> columnDefinition = new HashMap<>();
        columnDefinition.put("raidIndex", new OID(new int[]{1, 3, 6, 1, 4, 1, 3717, 2, 1, 1, 2, 1, 1}));
        columnDefinition.put("raidName", new OID(new int[]{1, 3, 6, 1, 4, 1, 3717, 2, 1, 1, 2, 1, 2}));
        columnDefinition.put("raidStatus", new OID(new int[]{1, 3, 6, 1, 4, 1, 3717, 2, 1, 1, 2, 1, 3}));
        return columnDefinition;
    }

    @Override
    public String getRowTitle(Map<String, QueryResponse> singleRow, int index) {
        return concatenateIfPossible(singleRow, new String[]{
                "raidIndex",
                "raidName",
        }, "-");
    }

    public static class RaidTableQueryRequest extends AbstractTableQueryRequest<RaidTableQuery> {

        public RaidTableQueryRequest(DeviceConfiguration deviceConfiguration) {
            super(deviceConfiguration);
        }

        @Override
        public OID getOidQuery() {
            return new OID(new int[]{1, 3, 6, 1, 4, 1, 3717, 2, 1, 1, 2});
        }

        @Override
        public Class<RaidTableQuery> getQueryClass() {
            return RaidTableQuery.class;
        }
    }
}
