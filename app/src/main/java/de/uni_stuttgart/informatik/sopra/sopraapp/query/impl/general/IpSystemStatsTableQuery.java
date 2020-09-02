package de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general;

import org.snmp4j.smi.OID;

import java.util.HashMap;
import java.util.Map;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.AbstractSnmpTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * this class represents a query for ip route table 1.3.6.1.2.1.4.21
 */
public class IpSystemStatsTableQuery extends AbstractSnmpTableQuery {

    public static final String TAG = IpSystemStatsTableQuery.class.getName();

    @Override
    public Map<String, OID> getColumnDefinition() {
        Map<String, OID> columnDefinition = new HashMap<>();
        columnDefinition.put("ipSystemStatsIPVersion", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 1}));
        columnDefinition.put("ipSystemStatsInReceives", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 3}));
        columnDefinition.put("ipSystemStatsHCInReceives", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 4}));
        columnDefinition.put("ipSystemStatsInOctets", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 5}));
        columnDefinition.put("ipSystemStatsHCInOctets", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 6}));
        columnDefinition.put("ipSystemStatsInHdrErrors", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 7}));
        columnDefinition.put("ipSystemStatsInNoRoutes", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 8}));
        columnDefinition.put("ipSystemStatsInAddrErrors", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 9}));
        columnDefinition.put("ipSystemStatsInUnknownProtos", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 10}));
        columnDefinition.put("ipSystemStatsInTruncatedPkts", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 11}));
        columnDefinition.put("ipSystemStatsInForwDatagrams", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 12}));
        columnDefinition.put("ipSystemStatsHCInForwDatagrams", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 13}));
        columnDefinition.put("ipSystemStatsReasmReqds", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 14}));
        columnDefinition.put("ipSystemStatsReasmOKs", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 15}));
        columnDefinition.put("ipSystemStatsReasmFails", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 16}));
        columnDefinition.put("ipSystemStatsInDiscards", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 17}));
        columnDefinition.put("ipSystemStatsInDelivers", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 18}));
        columnDefinition.put("ipSystemStatsHCInDelivers", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 19}));
        columnDefinition.put("ipSystemStatsOutRequests", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 20}));
        columnDefinition.put("ipSystemStatsHCOutRequests", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 21}));
        columnDefinition.put("ipSystemStatsOutNoRoutes", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 22}));
        columnDefinition.put("ipSystemStatsOutForwDatagrams", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 23}));
        columnDefinition.put("ipSystemStatsHCOutForwDatagrams", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 24}));
        columnDefinition.put("ipSystemStatsOutDiscards", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 25}));
        columnDefinition.put("ipSystemStatsOutFragReqds", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 26}));
        columnDefinition.put("ipSystemStatsOutFragOKs", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 27}));
        columnDefinition.put("ipSystemStatsOutFragFails", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 28}));
        columnDefinition.put("ipSystemStatsOutFragCreates", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 29}));
        columnDefinition.put("ipSystemStatsOutTransmits", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 30}));
        columnDefinition.put("ipSystemStatsHCOutTransmits", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 31}));
        columnDefinition.put("ipSystemStatsOutOctets", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 32}));
        columnDefinition.put("ipSystemStatsHCOutOctets", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 33}));
        columnDefinition.put("ipSystemStatsInMcastPkts", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 34}));
        columnDefinition.put("ipSystemStatsHCInMcastPkts", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 35}));
        columnDefinition.put("ipSystemStatsInMcastOctets", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 36}));
        columnDefinition.put("ipSystemStatsHCInMcastOctets", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 37}));
        columnDefinition.put("ipSystemStatsOutMcastPkts", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 38}));
        columnDefinition.put("ipSystemStatsHCOutMcastPkts", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 39}));
        columnDefinition.put("ipSystemStatsOutMcastOctets", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 40}));
        columnDefinition.put("ipSystemStatsHCOutMcastOctets", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 41}));
        columnDefinition.put("ipSystemStatsInBcastPkts", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 42}));
        columnDefinition.put("ipSystemStatsHCInBcastPkts", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 43}));
        columnDefinition.put("ipSystemStatsOutBcastPkts", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 44}));
        columnDefinition.put("ipSystemStatsHCOutBcastPkts", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 45}));
        columnDefinition.put("ipSystemStatsDiscontinuityTime", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 46}));
        columnDefinition.put("ipSystemStatsRefreshRate", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1, 1, 47}));
        return columnDefinition;
    }

    @Override
    public String getRowTitle(Map<String, QueryResponse> singleRow, int rowIndex) {
        return String.valueOf(rowIndex) + concatenateIfPossible(singleRow, new String[] {
                "ipSystemStatsIPVersion"
        }, null);
    }

    /**
     * request class
     */
    public static class IpSystemStatsTableRequest extends AbstractTableQueryRequest<IpSystemStatsTableQuery> {
        public IpSystemStatsTableRequest(DeviceConfiguration deviceConfiguration) {
            super(deviceConfiguration);
        }

        @Override
        public OID getOidQuery() {
            return new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 1});
        }

        @Override
        public Class<IpSystemStatsTableQuery> getQueryClass() {
            return IpSystemStatsTableQuery.class;
        }
    }
}
