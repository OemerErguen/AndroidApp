package de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general;

import org.snmp4j.smi.OID;

import java.util.HashMap;
import java.util.Map;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.AbstractSnmpTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * this class represents a query for ip net to media table 1.3.6.1.2.1.7.5
 */
public class UdpTableQuery extends AbstractSnmpTableQuery {

    public static final String TAG = UdpTableQuery.class.getName();

    @Override
    public Map<String, OID> getColumnDefinition() {
        Map<String, OID> columnDefinition = new HashMap<>();
        columnDefinition.put("udpLocalAddress", new OID(new int[]{1, 3, 6, 1, 2, 1, 7, 5, 1, 1}));
        columnDefinition.put("udpLocalPort", new OID(new int[]{1, 3, 6, 1, 2, 1, 7, 5, 1, 2}));
        return columnDefinition;
    }

    @Override
    public String getRowTitle(Map<String, QueryResponse> singleRow, int rowIndex) {
        return concatenateIfPossible(singleRow, new String[]{
                "udpLocalAddress",
                "udpLocalPort",
        }, "|");
    }

    /**
     * request class
     */
    public static class UdpTableQueryRequest extends AbstractTableQueryRequest<UdpTableQuery> {
        public UdpTableQueryRequest(DeviceConfiguration deviceConfiguration) {
            super(deviceConfiguration);
        }

        @Override
        public OID getOidQuery() {
            return new OID(new int[]{1, 3, 6, 1, 2, 1, 7, 5});
        }

        @Override
        public Class<UdpTableQuery> getQueryClass() {
            return UdpTableQuery.class;
        }
    }
}
