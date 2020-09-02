package de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general;

import org.snmp4j.smi.OID;

import java.util.HashMap;
import java.util.Map;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.AbstractSnmpTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * this class represents a query for 1.3.6.1.2.1.25.3.6
 */
public class HrPartitionTableQuery extends AbstractSnmpTableQuery {

    public static final String TAG = HrPartitionTableQuery.class.getName();

    @Override
    public Map<String, OID> getColumnDefinition() {
        Map<String, OID> columnDefinition = new HashMap<>();
        columnDefinition.put("hrPartitionIndex", new OID(new int[]{1, 3, 6, 1, 2, 1, 25, 3, 7, 1, 1}));
        columnDefinition.put("hrPartitionLabel", new OID(new int[]{1, 3, 6, 1, 2, 1, 25, 3, 7, 1, 2}));
        columnDefinition.put("hrPartitionID", new OID(new int[]{1, 3, 6, 1, 2, 1, 25, 3, 7, 1, 3}));
        columnDefinition.put("hrPartitionSize", new OID(new int[]{1, 3, 6, 1, 2, 1, 25, 3, 7, 1, 4}));
        columnDefinition.put("hrPartitionFSIndex", new OID(new int[]{1, 3, 6, 1, 2, 1, 25, 3, 7, 1, 5}));
        return columnDefinition;
    }

    @Override
    public String getRowTitle(Map<String, QueryResponse> singleRow, int rowIndex) {
        return concatenateIfPossible(singleRow, new String[]{
                "hrPartitionIndex",
                "hrPartitionID",
                "hrPartitionLabel"
        }, "-");
    }

    /**
     * request class
     */
    public static class HrPartitionTableRequest extends AbstractTableQueryRequest<HrPartitionTableQuery> {
        public HrPartitionTableRequest(DeviceConfiguration deviceConfiguration) {
            super(deviceConfiguration);
        }

        @Override
        public OID getOidQuery() {
            return new OID(new int[]{1, 3, 6, 1, 2, 1, 25, 3, 7});
        }

        @Override
        public Class<HrPartitionTableQuery> getQueryClass() {
            return HrPartitionTableQuery.class;
        }
    }
}
