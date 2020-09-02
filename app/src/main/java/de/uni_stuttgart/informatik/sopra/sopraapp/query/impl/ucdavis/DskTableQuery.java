package de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.ucdavis;

import org.snmp4j.smi.OID;

import java.util.HashMap;
import java.util.Map;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.AbstractSnmpTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * 1.3.6.1.4.1.30155.2.1.2
 */
public class DskTableQuery extends AbstractSnmpTableQuery {
    @Override
    public Map<String, OID> getColumnDefinition() {
        Map<String, OID> columnDefinition = new HashMap<>();
        columnDefinition.put("dskIndex", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 9, 1, 1}));
        columnDefinition.put("dskPath", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 9, 1, 2}));
        columnDefinition.put("dskDevice", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 9, 1, 3}));
        columnDefinition.put("dskMinimum", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 9, 1, 4}));
        columnDefinition.put("dskMinPercent", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 9, 1, 5}));
        columnDefinition.put("dskTotal", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 9, 1, 6}));
        columnDefinition.put("dskAvail", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 9, 1, 7}));
        columnDefinition.put("dskUsed", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 9, 1, 8}));
        columnDefinition.put("dskPercent", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 9, 1, 9}));
        columnDefinition.put("dskPercentNode", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 9, 1, 10}));
        columnDefinition.put("dskTotalLow", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 9, 1, 11}));
        columnDefinition.put("dskTotalHigh", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 9, 1, 12}));
        columnDefinition.put("dskAvailLow", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 9, 1, 13}));
        columnDefinition.put("dskAvailHigh", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 9, 1, 14}));
        columnDefinition.put("dskUsedLow", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 9, 1, 15}));
        columnDefinition.put("dskUsedHigh", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 9, 1, 16}));
        columnDefinition.put("dskErrorFlag", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 9, 1, 17}));
        columnDefinition.put("dskErrorMsg", new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 9, 1, 18}));
        return columnDefinition;
    }

    @Override
    public String getRowTitle(Map<String, QueryResponse> singleRow, int index) {
        return concatenateIfPossible(singleRow, new String[]{
                "dskIndex",
                "dskDevice",
        }, "-");
    }

    public static class DskTableQueryRequest extends AbstractTableQueryRequest<DskTableQuery> {

        public DskTableQueryRequest(DeviceConfiguration deviceConfiguration) {
            super(deviceConfiguration);
        }

        @Override
        public OID getOidQuery() {
            return new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 9});
        }

        @Override
        public Class<DskTableQuery> getQueryClass() {
            return DskTableQuery.class;
        }
    }
}
