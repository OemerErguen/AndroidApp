package de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.genua;

import org.snmp4j.smi.OID;

import java.util.HashMap;
import java.util.Map;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.AbstractSnmpTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * 1.3.6.1.2.1.4.37.1.1
 */
public class FuserTableQuery extends AbstractSnmpTableQuery {
    @Override
    public Map<String, OID> getColumnDefinition() {
        Map<String, OID> columnDefinition = new HashMap<>();
        columnDefinition.put("fuserIndex", new OID(new int[]{1, 3, 6, 1, 4, 1, 3717, 64, 1, 1, 1}));
        columnDefinition.put("fuserId", new OID(new int[]{1, 3, 6, 1, 4, 1, 3717, 64, 1, 1, 2}));
        columnDefinition.put("fuserName", new OID(new int[]{1, 3, 6, 1, 4, 1, 3717, 64, 1, 1, 3}));
        columnDefinition.put("fuserRealname", new OID(new int[]{1, 3, 6, 1, 4, 1, 3717, 64, 1, 1, 4}));
        columnDefinition.put("fuserEmail", new OID(new int[]{1, 3, 6, 1, 4, 1, 3717, 64, 1, 1, 5}));
        columnDefinition.put("fuserPhone", new OID(new int[]{1, 3, 6, 1, 4, 1, 3717, 64, 1, 1, 6}));
        columnDefinition.put("fuserCompany", new OID(new int[]{1, 3, 6, 1, 4, 1, 3717, 64, 1, 1, 7}));
        columnDefinition.put("fuserDescription", new OID(new int[]{1, 3, 6, 1, 4, 1, 3717, 64, 1, 1, 8}));
        columnDefinition.put("fuserState", new OID(new int[]{1, 3, 6, 1, 4, 1, 3717, 64, 1, 1, 9}));
        return columnDefinition;
    }

    @Override
    public String getRowTitle(Map<String, QueryResponse> singleRow, int index) {
        return concatenateIfPossible(singleRow, new String[]{
                "fuserIndex",
                "fuserId",
                "fuserName",
        }, "-");
    }

    public static class FuserTableQueryRequest extends AbstractTableQueryRequest<FuserTableQuery> {

        public FuserTableQueryRequest(DeviceConfiguration deviceConfiguration) {
            super(deviceConfiguration);
        }

        @Override
        public OID getOidQuery() {
            return new OID(new int[]{1, 3, 6, 1, 4, 1, 3717, 64, 1});
        }

        @Override
        public Class<FuserTableQuery> getQueryClass() {
            return FuserTableQuery.class;
        }
    }
}
