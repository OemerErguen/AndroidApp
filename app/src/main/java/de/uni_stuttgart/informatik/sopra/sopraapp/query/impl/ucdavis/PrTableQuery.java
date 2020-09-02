package de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.ucdavis;

import org.snmp4j.smi.OID;

import java.util.HashMap;
import java.util.Map;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.AbstractSnmpTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * 1.3.6.1.4.1.2021.2
 */
public class PrTableQuery extends AbstractSnmpTableQuery {
    @Override
    public Map<String, OID> getColumnDefinition() {
        Map<String, OID> columnDefinition = new HashMap<>();
        columnDefinition.put("prIndex", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 2, 1, 1}));
        columnDefinition.put("prNames", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 2, 1, 2}));
        columnDefinition.put("prMin", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 2, 1, 3}));
        columnDefinition.put("prMax", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 2, 1, 4}));
        columnDefinition.put("prCount", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 2, 1, 5}));
        columnDefinition.put("prErrorFlag", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 2, 1, 100}));
        columnDefinition.put("prErrMessage", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 2, 1, 101}));
        columnDefinition.put("prErrFix", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 2, 1, 102}));
        columnDefinition.put("prErrFixCmd", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 2, 1, 103}));
        return columnDefinition;
    }

    @Override
    public String getRowTitle(Map<String, QueryResponse> singleRow, int index) {
        return concatenateIfPossible(singleRow, new String[]{
                "prIndex",
                "prNames",
        }, "-");
    }

    public static class PrTableQueryRequest extends AbstractTableQueryRequest<PrTableQuery> {

        public PrTableQueryRequest(DeviceConfiguration deviceConfiguration) {
            super(deviceConfiguration);
        }

        @Override
        public OID getOidQuery() {
            return new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 2});
        }

        @Override
        public Class<PrTableQuery> getQueryClass() {
            return PrTableQuery.class;
        }
    }
}
