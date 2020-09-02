package de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.ucdavis;

import org.snmp4j.smi.OID;

import java.util.HashMap;
import java.util.Map;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.AbstractSnmpTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * 1.3.6.1.4.1.2021.10
 */
public class LaTableQuery extends AbstractSnmpTableQuery {
    @Override
    public Map<String, OID> getColumnDefinition() {
        Map<String, OID> columnDefinition = new HashMap<>();
        columnDefinition.put("laIndex", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 10, 1, 1}));
        columnDefinition.put("laNames", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 10, 1, 2}));
        columnDefinition.put("laLoad", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 10, 1, 3}));
        columnDefinition.put("laConfig", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 10, 1, 4}));
        columnDefinition.put("laLoadInt", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 10, 1, 5}));
        columnDefinition.put("laLoadFloat", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 10, 1, 6}));
        columnDefinition.put("laErrorFlag", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 10, 1, 100}));
        columnDefinition.put("laErrMessage", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 10, 1, 101}));
        return columnDefinition;
    }

    @Override
    public String getRowTitle(Map<String, QueryResponse> singleRow, int index) {
        return concatenateIfPossible(singleRow, new String[]{
                "laIndex",
                "laNames",
        }, "-");
    }

    public static class LaTableQueryRequest extends AbstractTableQueryRequest<LaTableQuery> {

        public LaTableQueryRequest(DeviceConfiguration deviceConfiguration) {
            super(deviceConfiguration);
        }

        @Override
        public OID getOidQuery() {
            return new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 10});
        }

        @Override
        public Class<LaTableQuery> getQueryClass() {
            return LaTableQuery.class;
        }
    }
}
