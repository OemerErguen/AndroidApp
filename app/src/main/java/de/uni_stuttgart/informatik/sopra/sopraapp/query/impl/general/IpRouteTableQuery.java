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
public class IpRouteTableQuery extends AbstractSnmpTableQuery {

    public static final String TAG = IpRouteTableQuery.class.getName();

    @Override
    public Map<String, OID> getColumnDefinition() {
        Map<String, OID> columnDefinition = new HashMap<>();
        columnDefinition.put("ipRouteDest", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 21, 1, 1}));
        columnDefinition.put("ipRouteIfIndex", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 21, 1, 2}));
        columnDefinition.put("ipRouteMetric1", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 21, 1, 3}));
        columnDefinition.put("ipRouteMetric2", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 21, 1, 4}));
        columnDefinition.put("ipRouteMetric3", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 21, 1, 5}));
        columnDefinition.put("ipRouteMetric4", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 21, 1, 6}));
        columnDefinition.put("ipRouteNextHop", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 21, 1, 7}));
        columnDefinition.put("ipRouteType", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 21, 1, 8}));
        columnDefinition.put("ipRouteProto", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 21, 1, 9}));
        columnDefinition.put("ipRouteAge", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 21, 1, 10}));
        columnDefinition.put("ipRouteMask", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 21, 1, 11}));
        columnDefinition.put("ipRouteMetric5", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 21, 1, 12}));
        columnDefinition.put("ipRouteInfo", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 21, 1, 13}));
        return columnDefinition;
    }

    @Override
    public String getRowTitle(Map<String, QueryResponse> singleRow, int rowIndex) {
        QueryResponse ipRouteInfoTitle = singleRow.get("ipRouteInfo");
        if (ipRouteInfoTitle != null) {
            String ipRouteInfo = ipRouteInfoTitle.getValue();
            if (!ipRouteInfo.isEmpty()) {
                return rowIndex + " - " + ipRouteInfo;
            }
        }
        return String.valueOf(rowIndex);
    }

    /**
     * request class
     */
    public static class IpRouteTableRequest extends AbstractTableQueryRequest<IpRouteTableQuery> {
        public IpRouteTableRequest(DeviceConfiguration deviceConfiguration) {
            super(deviceConfiguration);
        }

        @Override
        public OID getOidQuery() {
            return new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 21});
        }

        @Override
        public Class<IpRouteTableQuery> getQueryClass() {
            return IpRouteTableQuery.class;
        }
    }
}
