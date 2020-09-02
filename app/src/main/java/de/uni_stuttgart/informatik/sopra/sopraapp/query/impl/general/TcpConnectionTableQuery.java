package de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general;

import org.snmp4j.smi.OID;

import java.util.HashMap;
import java.util.Map;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.AbstractSnmpTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * this class represents a query for ip net to media table 1.3.6.1.2.1.6.13
 */
public class TcpConnectionTableQuery extends AbstractSnmpTableQuery {

    public static final String TAG = TcpConnectionTableQuery.class.getName();

    @Override
    public Map<String, OID> getColumnDefinition() {
        Map<String, OID> columnDefinition = new HashMap<>();
        columnDefinition.put("tcpConnState", new OID(new int[]{1, 3, 6, 1, 2, 1, 6, 13, 1, 1}));
        columnDefinition.put("tcpConnLocalAddress", new OID(new int[]{1, 3, 6, 1, 2, 1, 6, 13, 1, 2}));
        columnDefinition.put("tcpConnLocalPort", new OID(new int[]{1, 3, 6, 1, 2, 1, 6, 13, 1, 3}));
        columnDefinition.put("tcpConnRemAddress", new OID(new int[]{1, 3, 6, 1, 2, 1, 6, 13, 1, 4}));
        columnDefinition.put("tcpConnRemPort", new OID(new int[]{1, 3, 6, 1, 2, 1, 6, 13, 1, 5}));
        return columnDefinition;
    }

    @Override
    public String getRowTitle(Map<String, QueryResponse> singleRow, int rowIndex) {
        String localAddr = getFieldOrEmpty(singleRow, "tcpConnLocalAddress");
        String localPort = getFieldOrEmpty(singleRow, "tcpConnLocalPort");
        String remoteAddr = getFieldOrEmpty(singleRow, "tcpConnRemAddress");
        String remotePort = getFieldOrEmpty(singleRow, "tcpConnRemPort");

        if (!localAddr.isEmpty() && !localPort.isEmpty() && !remoteAddr.isEmpty() && !remotePort.isEmpty()) {
            return rowIndex + " - " + localAddr + ":" + localPort + " -> " + remoteAddr + remotePort;
        }
        if (!localAddr.isEmpty() && !remoteAddr.isEmpty()) {
            return rowIndex + " - " + localAddr + " -> " + remoteAddr;
        }

        return String.valueOf(rowIndex);
    }

    /**
     * request class
     */
    public static class TcpConnectionTableQueryRequest extends AbstractTableQueryRequest<TcpConnectionTableQuery> {
        public TcpConnectionTableQueryRequest(DeviceConfiguration deviceConfiguration) {
            super(deviceConfiguration);
        }

        @Override
        public OID getOidQuery() {
            return new OID(new int[]{1, 3, 6, 1, 2, 1, 6, 13});
        }

        @Override
        public Class<TcpConnectionTableQuery> getQueryClass() {
            return TcpConnectionTableQuery.class;
        }
    }
}
