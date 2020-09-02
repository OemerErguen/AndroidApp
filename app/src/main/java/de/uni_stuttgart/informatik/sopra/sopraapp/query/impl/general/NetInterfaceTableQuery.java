package de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general;

import org.snmp4j.smi.OID;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.AbstractSnmpTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * this class represents a query for network 1.3.6.1.2.1.2.2.1.*
 */
public class NetInterfaceTableQuery extends AbstractSnmpTableQuery {

    public static final String TAG = NetInterfaceTableQuery.class.getName();

    @Override
    public Map<String, OID> getColumnDefinition() {
        Map<String, OID> columnDefinition = new HashMap<>();
        columnDefinition.put("ifIndex", new OID(new int[]{1, 3, 6, 1, 2, 1, 2, 2, 1, 1}));
        columnDefinition.put("ifDescr", new OID(new int[]{1, 3, 6, 1, 2, 1, 2, 2, 1, 2}));
        columnDefinition.put("ifType", new OID(new int[]{1, 3, 6, 1, 2, 1, 2, 2, 1, 3}));
        columnDefinition.put("ifMtu", new OID(new int[]{1, 3, 6, 1, 2, 1, 2, 2, 1, 4}));
        columnDefinition.put("ifSpeed", new OID(new int[]{1, 3, 6, 1, 2, 1, 2, 2, 1, 5}));
        columnDefinition.put("ifPhysAddress", new OID(new int[]{1, 3, 6, 1, 2, 1, 2, 2, 1, 6}));
        columnDefinition.put("ifAdminStatus", new OID(new int[]{1, 3, 6, 1, 2, 1, 2, 2, 1, 7}));
        columnDefinition.put("ifOperStatus", new OID(new int[]{1, 3, 6, 1, 2, 1, 2, 2, 1, 8}));
        columnDefinition.put("ifLastChange", new OID(new int[]{1, 3, 6, 1, 2, 1, 2, 2, 1, 9}));
        columnDefinition.put("ifInOctets", new OID(new int[]{1, 3, 6, 1, 2, 1, 2, 2, 1, 10}));
        columnDefinition.put("ifInUcastPkts", new OID(new int[]{1, 3, 6, 1, 2, 1, 2, 2, 1, 11}));
        columnDefinition.put("ifInNUcastPkts", new OID(new int[]{1, 3, 6, 1, 2, 1, 2, 2, 1, 12}));
        columnDefinition.put("ifInDiscards", new OID(new int[]{1, 3, 6, 1, 2, 1, 2, 2, 1, 13}));
        columnDefinition.put("ifInErrors", new OID(new int[]{1, 3, 6, 1, 2, 1, 2, 2, 1, 14}));
        columnDefinition.put("ifInUnknownProtos", new OID(new int[]{1, 3, 6, 1, 2, 1, 2, 2, 1, 15}));
        columnDefinition.put("ifOutOctets", new OID(new int[]{1, 3, 6, 1, 2, 1, 2, 2, 1, 16}));
        columnDefinition.put("ifOutUcastPkts", new OID(new int[]{1, 3, 6, 1, 2, 1, 2, 2, 1, 17}));
        columnDefinition.put("ifOutNUcastPkts", new OID(new int[]{1, 3, 6, 1, 2, 1, 2, 2, 1, 18}));
        columnDefinition.put("ifOutDiscards", new OID(new int[]{1, 3, 6, 1, 2, 1, 2, 2, 1, 19}));
        columnDefinition.put("ifOutErrors", new OID(new int[]{1, 3, 6, 1, 2, 1, 2, 2, 1, 20}));
        columnDefinition.put("ifOutQLen", new OID(new int[]{1, 3, 6, 1, 2, 1, 2, 2, 1, 21}));
        columnDefinition.put("ifSpecific", new OID(new int[]{1, 3, 6, 1, 2, 1, 2, 2, 1, 22}));
        return columnDefinition;
    }

    @Override
    public String[] getHeaderColumns() {
        Set<String> strings = getColumnDefinition().keySet();
        return strings.toArray(new String[]{});
    }

    @Override
    public String getRowTitle(Map<String, QueryResponse> singleRow, int rowIndex) {
        return concatenateIfPossible(singleRow, new String[]{
                "ifDescr",
                "ifIndex",
                "ifPhysAddress",
        }, "-");
    }

    /**
     * request class
     */
    public static class NetInterfaceTableRequest extends AbstractTableQueryRequest<NetInterfaceTableQuery> {
        public NetInterfaceTableRequest(DeviceConfiguration deviceConfiguration) {
            super(deviceConfiguration);
        }

        @Override
        public OID getOidQuery() {
            return new OID(new int[]{1, 3, 6, 1, 2, 1, 2, 2});
        }

        @Override
        public Class<NetInterfaceTableQuery> getQueryClass() {
            return NetInterfaceTableQuery.class;
        }
    }
}
