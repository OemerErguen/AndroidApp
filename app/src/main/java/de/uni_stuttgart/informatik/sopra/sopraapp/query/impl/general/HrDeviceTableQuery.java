package de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general;

import org.snmp4j.smi.OID;

import java.util.HashMap;
import java.util.Map;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.AbstractSnmpTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * this class represents a query for 1.3.6.1.2.1.25.3.2
 */
public class HrDeviceTableQuery extends AbstractSnmpTableQuery {

    public static final String TAG = HrDeviceTableQuery.class.getName();

    @Override
    public Map<String, OID> getColumnDefinition() {
        Map<String, OID> columnDefinition = new HashMap<>();
        columnDefinition.put("hrDeviceIndex", new OID(new int[]{1, 3, 6, 1, 2, 1, 25, 3, 2, 1, 1}));
        columnDefinition.put("hrDeviceType", new OID(new int[]{1, 3, 6, 1, 2, 1, 25, 3, 2, 1, 2}));
        columnDefinition.put("hrDeviceDescr", new OID(new int[]{1, 3, 6, 1, 2, 1, 25, 3, 2, 1, 3}));
        columnDefinition.put("hrDeviceID", new OID(new int[]{1, 3, 6, 1, 2, 1, 25, 3, 2, 1, 4}));
        columnDefinition.put("hrDeviceStatus", new OID(new int[]{1, 3, 6, 1, 2, 1, 25, 3, 2, 1, 5}));
        columnDefinition.put("hrDeviceErrors", new OID(new int[]{1, 3, 6, 1, 2, 1, 25, 3, 2, 1, 6}));
        return columnDefinition;
    }

    @Override
    public String getRowTitle(Map<String, QueryResponse> singleRow, int rowIndex) {
        return concatenateIfPossible(singleRow, new String[]{
                "hrDeviceIndex",
                "hrDeviceID"
        }, "-");
    }

    /**
     * request class
     */
    public static class HrDeviceTableRequest extends AbstractTableQueryRequest<HrDeviceTableQuery> {
        public HrDeviceTableRequest(DeviceConfiguration deviceConfiguration) {
            super(deviceConfiguration);
        }

        @Override
        public OID getOidQuery() {
            return new OID(new int[]{1, 3, 6, 1, 2, 1, 25, 3, 2});
        }

        @Override
        public Class<HrDeviceTableQuery> getQueryClass() {
            return HrDeviceTableQuery.class;
        }
    }
}
