package de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general;

import org.snmp4j.smi.OID;

import java.util.HashMap;
import java.util.Map;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.AbstractSnmpTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * this class represents a query for at table
 */
public class AtTableQuery extends AbstractSnmpTableQuery {

    public static final String TAG = AtTableQuery.class.getName();

    @Override
    public Map<String, OID> getColumnDefinition() {
        Map<String, OID> columnDefinition = new HashMap<>();
        columnDefinition.put("atIfIndex", new OID(new int[]{1, 3, 6, 1, 2, 1, 3, 1, 1, 1}));
        columnDefinition.put("atIfPhysAddress", new OID(new int[]{1, 3, 6, 1, 2, 1, 3, 1, 1, 2}));
        columnDefinition.put("atNetAddress", new OID(new int[]{1, 3, 6, 1, 2, 1, 3, 1, 1, 3}));
        return columnDefinition;
    }

    @Override
    public String getRowTitle(Map<String, QueryResponse> singleRow, int rowIndex) {
        return concatenateIfPossible(singleRow, new String[]{
                "atIfIndex",
                "atIfPhysAddress"
        }, "-");
    }

    /**
     * request class
     */
    public static class AtTableRequest extends AbstractTableQueryRequest<AtTableQuery> {
        public AtTableRequest(DeviceConfiguration deviceConfiguration) {
            super(deviceConfiguration);
        }

        @Override
        public OID getOidQuery() {
            return new OID(new int[]{1, 3, 6, 1, 2, 1, 3, 1});
        }

        @Override
        public Class<AtTableQuery> getQueryClass() {
            return AtTableQuery.class;
        }
    }
}
