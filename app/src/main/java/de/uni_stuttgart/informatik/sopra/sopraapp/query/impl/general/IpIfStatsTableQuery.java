package de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general;

import org.snmp4j.smi.OID;

import java.util.HashMap;
import java.util.Map;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.AbstractSnmpTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.OIDCatalog;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * this class represents a query for ip route table 1.3.6.1.2.1.4.31.3
 */
public class IpIfStatsTableQuery extends AbstractSnmpTableQuery {

    public static final String TAG = IpIfStatsTableQuery.class.getName();

    @Override
    public Map<String, OID> getColumnDefinition() {

        String[] columns = new String[] {
                "ipIfStatsIPVersion",
                "ipIfStatsIfIndex",
                "ipIfStatsInReceives",
                "ipIfStatsHCInReceives",
                "ipIfStatsInOctets",
                "ipIfStatsHCInOctets",
                "ipIfStatsInHdrErrors",
                "ipIfStatsInNoRoutes",
                "ipIfStatsInAddrErrors",
                "ipIfStatsInUnknownProtos",
                "ipIfStatsInTruncatedPkts",
                "ipIfStatsInForwDatagrams",
                "ipIfStatsHCInForwDatagrams",
                "ipIfStatsReasmReqds",
                "ipIfStatsReasmOKs",
                "ipIfStatsReasmFails",
                "ipIfStatsInDiscards",
                "ipIfStatsInDelivers",
                "ipIfStatsInDelivers",
                "ipIfStatsHCInDelivers",
                "ipIfStatsOutRequests",
                "ipIfStatsHCOutRequests",
                "ipIfStatsOutForwDatagrams",
                "ipIfStatsHCOutForwDatagrams",
                "ipIfStatsOutDiscards",
                "ipIfStatsOutFragReqds",
                "ipIfStatsOutFragOKs",
                "ipIfStatsOutFragFails",
                "ipIfStatsOutFragCreates",
                "ipIfStatsOutTransmits",
                "ipIfStatsHCOutTransmits",
                "ipIfStatsOutOctets",
                "ipIfStatsHCOutOctets",
                "ipIfStatsInMcastPkts",
                "ipIfStatsHCInMcastPkts",
                "ipIfStatsInMcastOctets",
                "ipIfStatsHCInMcastOctets",
                "ipIfStatsOutMcastPkts",
                "ipIfStatsHCOutMcastPkts",
                "ipIfStatsOutMcastOctets",
                "ipIfStatsHCOutMcastOctets",
                "ipIfStatsInBcastPkts",
                "ipIfStatsHCInBcastPkts",
                "ipIfStatsOutBcastPkts",
                "ipIfStatsHCOutBcastPkts",
                "ipIfStatsDiscontinuityTime",
                "ipIfStatsRefreshRate",
        };

        Map<String, OID> columnDefinition = new HashMap<>();

        OIDCatalog oidCatalog = OIDCatalog.getInstance(null);
        for (String asnName : columns) {
            columnDefinition.put(asnName, new OID(oidCatalog.getOidByAsn(asnName)));
        }

        return columnDefinition;
    }

    @Override
    public String getRowTitle(Map<String, QueryResponse> singleRow, int rowIndex) {
        return String.valueOf(rowIndex) + concatenateIfPossible(singleRow, new String[] {
                "ipIfStatsIfIndex",
                "ipIfStatsIPVersion"
        }, null);
    }

    /**
     * request class
     */
    public static class IpIfStatsTableRequest extends AbstractTableQueryRequest<IpIfStatsTableQuery> {
        public IpIfStatsTableRequest(DeviceConfiguration deviceConfiguration) {
            super(deviceConfiguration);
        }

        @Override
        public OID getOidQuery() {
            return new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 31, 3});
        }

        @Override
        public Class<IpIfStatsTableQuery> getQueryClass() {
            return IpIfStatsTableQuery.class;
        }
    }
}
