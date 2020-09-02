package de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.bsd;

import org.snmp4j.smi.OID;

import java.util.HashMap;
import java.util.Map;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.AbstractSnmpTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * 1.3.6.1.4.1.30155.2.1.2
 */
public class SensorTableQuery extends AbstractSnmpTableQuery {
    @Override
    public Map<String, OID> getColumnDefinition() {
        Map<String, OID> columnDefinition = new HashMap<>();
        columnDefinition.put("sensorIndex", new OID(new int[]{1, 3, 6, 1, 4, 1, 30155, 2, 1, 2, 1, 1}));
        columnDefinition.put("sensorDescr", new OID(new int[]{1, 3, 6, 1, 4, 1, 30155, 2, 1, 2, 1, 2}));
        columnDefinition.put("sensorType", new OID(new int[]{1, 3, 6, 1, 4, 1, 30155, 2, 1, 2, 1, 3}));
        columnDefinition.put("sensorDevice", new OID(new int[]{1, 3, 6, 1, 4, 1, 30155, 2, 1, 2, 1, 4}));
        columnDefinition.put("sensorValue", new OID(new int[]{1, 3, 6, 1, 4, 1, 30155, 2, 1, 2, 1, 5}));
        columnDefinition.put("sensorUnits", new OID(new int[]{1, 3, 6, 1, 4, 1, 30155, 2, 1, 2, 1, 6}));
        columnDefinition.put("sensorStatus", new OID(new int[]{1, 3, 6, 1, 4, 1, 30155, 2, 1, 2, 1, 7}));
        return columnDefinition;
    }

    @Override
    public String getRowTitle(Map<String, QueryResponse> singleRow, int index) {
        return concatenateIfPossible(singleRow, new String[]{
                "sensorIndex",
                "sensorType",
                "sensorDescr"
        }, "-");
    }

    public static class SensorTableQueryRequest extends AbstractTableQueryRequest<SensorTableQuery> {

        public SensorTableQueryRequest(DeviceConfiguration deviceConfiguration) {
            super(deviceConfiguration);
        }

        @Override
        public OID getOidQuery() {
            return new OID(new int[]{1, 3, 6, 1, 4, 1, 30155, 2, 1, 2});
        }

        @Override
        public Class<SensorTableQuery> getQueryClass() {
            return SensorTableQuery.class;
        }
    }
}
