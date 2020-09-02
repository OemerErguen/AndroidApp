package de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.genua;

import org.snmp4j.smi.OID;

import java.util.HashMap;
import java.util.Map;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.AbstractSnmpTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * 1.3.6.1.4.1.3717.2.1.1.1
 */
public class FanTableQuery extends AbstractSnmpTableQuery {
    @Override
    public Map<String, OID> getColumnDefinition() {
        Map<String, OID> columnDefinition = new HashMap<>();
        columnDefinition.put("fanIndex", new OID(new int[]{1, 3, 6, 1, 4, 1, 3717, 2, 1, 1, 1, 1, 1}));
        columnDefinition.put("fanName", new OID(new int[]{1, 3, 6, 1, 4, 1, 3717, 2, 1, 1, 1, 1, 2}));
        columnDefinition.put("fanRpm", new OID(new int[]{1, 3, 6, 1, 4, 1, 3717, 2, 1, 1, 1, 1, 3}));
        columnDefinition.put("fanStatus", new OID(new int[]{1, 3, 6, 1, 4, 1, 3717, 2, 1, 1, 1, 1, 4}));
        return columnDefinition;
    }

    @Override
    public String getRowTitle(Map<String, QueryResponse> singleRow, int index) {
        return concatenateIfPossible(singleRow, new String[]{
                "fanIndex",
                "fanName",
        }, "-");
    }

    public static class FanTableQueryRequest extends AbstractTableQueryRequest<FanTableQuery> {

        public FanTableQueryRequest(DeviceConfiguration deviceConfiguration) {
            super(deviceConfiguration);
        }

        @Override
        public OID getOidQuery() {
            return new OID(new int[]{1, 3, 6, 1, 4, 1, 3717, 2, 1, 1, 1});
        }

        @Override
        public Class<FanTableQuery> getQueryClass() {
            return FanTableQuery.class;
        }
    }
}
