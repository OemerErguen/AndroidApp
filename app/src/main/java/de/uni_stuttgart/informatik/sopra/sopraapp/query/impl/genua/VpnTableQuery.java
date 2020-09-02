package de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.genua;

import org.snmp4j.smi.OID;

import java.util.HashMap;
import java.util.Map;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.AbstractSnmpTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

public class VpnTableQuery extends AbstractSnmpTableQuery {
    @Override
    public Map<String, OID> getColumnDefinition() {
        Map<String, OID> columnDefinition = new HashMap<>();
        columnDefinition.put("vpnIndex", new OID(new int[]{1, 3, 6, 1, 4, 1, 3717, 2, 1, 3, 1, 1}));
        columnDefinition.put("vpnPeer", new OID(new int[]{1, 3, 6, 1, 4, 1, 3717, 2, 1, 3, 1, 2}));
        columnDefinition.put("vpnPeerip", new OID(new int[]{1, 3, 6, 1, 4, 1, 3717, 2, 1, 3, 1, 3}));
        columnDefinition.put("vpnLocal", new OID(new int[]{1, 3, 6, 1, 4, 1, 3717, 2, 1, 3, 1, 4}));
        columnDefinition.put("vpnRemote", new OID(new int[]{1, 3, 6, 1, 4, 1, 3717, 2, 1, 3, 1, 5}));
        columnDefinition.put("vpnStatus", new OID(new int[]{1, 3, 6, 1, 4, 1, 3717, 2, 1, 3, 1, 6}));
        return columnDefinition;
    }

    @Override
    public String getRowTitle(Map<String, QueryResponse> singleRow, int index) {
        return concatenateIfPossible(singleRow, new String[]{
                "vpnIndex",
                "vpnPeer",
                "vpnPeerip",
        }, "-");
    }

    public static class VpnTableQueryRequest extends AbstractTableQueryRequest<VpnTableQuery> {

        public VpnTableQueryRequest(DeviceConfiguration deviceConfiguration) {
            super(deviceConfiguration);
        }

        @Override
        public OID getOidQuery() {
            return new OID(new int[]{1, 3, 6, 1, 4, 1, 3717, 2, 1, 3});
        }

        @Override
        public Class<VpnTableQuery> getQueryClass() {
            return VpnTableQuery.class;
        }
    }
}
