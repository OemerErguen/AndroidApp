package de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general;

import org.snmp4j.smi.OID;

import java.util.HashMap;
import java.util.Map;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.AbstractSnmpTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * this class represents a query for ip route table 1.3.6.1.2.1.4.31.3
 */
public class IcmpStatsTableQuery extends AbstractSnmpTableQuery {

    public static final String TAG = IcmpStatsTableQuery.class.getName();

    @Override
    public Map<String, OID> getColumnDefinition() {
        Map<String, OID> columnDefinition = new HashMap<>();

        columnDefinition.put("icmpStatsIPVersion", new OID(new int[]{1, 3, 6, 1, 2, 1, 5, 29, 1, 1}));
        columnDefinition.put("icmpStatsInMsgs", new OID(new int[]{1, 3, 6, 1, 2, 1, 5, 29, 1, 2}));
        columnDefinition.put("icmpStatsInErrors", new OID(new int[]{1, 3, 6, 1, 2, 1, 5, 29, 1, 3}));
        columnDefinition.put("icmpStatsOutMsgs", new OID(new int[]{1, 3, 6, 1, 2, 1, 5, 29, 1, 4}));
        columnDefinition.put("icmpStatsOutErrors", new OID(new int[]{1, 3, 6, 1, 2, 1, 5, 29, 1, 5}));

        return columnDefinition;
    }

    @Override
    public String getRowTitle(Map<String, QueryResponse> singleRow, int rowIndex) {
        return String.valueOf(rowIndex) + concatenateIfPossible(singleRow, new String[]{
                "icmpStatsIPVersion",
        }, null);
    }

    /**
     * request class
     */
    public static class IcmpStatsTableRequest extends AbstractTableQueryRequest<IcmpStatsTableQuery> {
        public IcmpStatsTableRequest(DeviceConfiguration deviceConfiguration) {
            super(deviceConfiguration);
        }

        @Override
        public OID getOidQuery() {
            return new OID(new int[]{1, 3, 6, 1, 2, 1, 5, 29});
        }

        @Override
        public Class<IcmpStatsTableQuery> getQueryClass() {
            return IcmpStatsTableQuery.class;
        }
    }
}
