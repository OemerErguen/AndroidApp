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
public class HrDiskStorageTableQuery extends AbstractSnmpTableQuery {

    public static final String TAG = HrDiskStorageTableQuery.class.getName();

    @Override
    public Map<String, OID> getColumnDefinition() {
        Map<String, OID> columnDefinition = new HashMap<>();
        columnDefinition.put("hrDiskStorageAccess", new OID(new int[]{1, 3, 6, 1, 2, 1, 25, 3, 6, 1, 1}));
        columnDefinition.put("hrDiskStorageMedia", new OID(new int[]{1, 3, 6, 1, 2, 1, 25, 3, 6, 1, 2}));
        columnDefinition.put("hrDiskStorageRemoveble", new OID(new int[]{1, 3, 6, 1, 2, 1, 25, 3, 6, 1, 3}));
        columnDefinition.put("hrDiskStorageCapacity", new OID(new int[]{1, 3, 6, 1, 2, 1, 25, 3, 6, 1, 4}));
        return columnDefinition;
    }

    @Override
    public String getRowTitle(Map<String, QueryResponse> singleRow, int rowIndex) {
        return concatenateIfPossible(singleRow, new String[]{
                "hrDiskStorageMedia",
        }, "-");
    }

    /**
     * request class
     */
    public static class HrDiskStorageTableRequest extends AbstractTableQueryRequest<HrDiskStorageTableQuery> {
        public HrDiskStorageTableRequest(DeviceConfiguration deviceConfiguration) {
            super(deviceConfiguration);
        }

        @Override
        public OID getOidQuery() {
            return new OID(new int[]{1, 3, 6, 1, 2, 1, 25, 3, 6});
        }

        @Override
        public Class<HrDiskStorageTableQuery> getQueryClass() {
            return HrDiskStorageTableQuery.class;
        }
    }
}
