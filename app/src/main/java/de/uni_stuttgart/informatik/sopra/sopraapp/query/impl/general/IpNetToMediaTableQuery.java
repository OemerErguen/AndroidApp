package de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general;

import org.snmp4j.smi.OID;

import java.util.HashMap;
import java.util.Map;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.AbstractSnmpTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * this class represents a query for ip net to media table 1.3.6.1.2.1.4.22
 */
public class IpNetToMediaTableQuery extends AbstractSnmpTableQuery {

    public static final String TAG = IpNetToMediaTableQuery.class.getName();

    @Override
    public Map<String, OID> getColumnDefinition() {
        Map<String, OID> columnDefinition = new HashMap<>();
        columnDefinition.put("ipNetToMediaIfIndex", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 22, 1, 1}));
        columnDefinition.put("ipNetToMediaPhysAddress", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 22, 1, 2}));
        columnDefinition.put("ipNetToMediaNetAddress", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 22, 1, 3}));
        columnDefinition.put("ipNetToMediaType", new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 22, 1, 4}));
        return columnDefinition;
    }

    @Override
    public String getRowTitle(Map<String, QueryResponse> singleRow, int rowIndex) {
        return concatenateIfPossible(singleRow, new String[]{
                "ipNetToMediaNetAddress",
                "ipNetToMediaPhysAddress",
        }, "-");
    }

    /**
     * request class
     */
    public static class IpNetToMediaTableRequest extends AbstractTableQueryRequest<IpNetToMediaTableQuery> {
        public IpNetToMediaTableRequest(DeviceConfiguration deviceConfiguration) {
            super(deviceConfiguration);
        }

        @Override
        public OID getOidQuery() {
            return new OID(new int[]{1, 3, 6, 1, 2, 1, 4, 22});
        }

        @Override
        public Class<IpNetToMediaTableQuery> getQueryClass() {
            return IpNetToMediaTableQuery.class;
        }
    }
}
