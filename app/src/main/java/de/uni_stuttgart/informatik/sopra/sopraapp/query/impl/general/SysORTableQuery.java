package de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general;

import org.snmp4j.smi.OID;

import java.util.HashMap;
import java.util.Map;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.AbstractQueryRequest;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.AbstractSnmpTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * table query for mibs 1.3.6.1.2.1.1.9 - sysORTable
 */
public class SysORTableQuery extends AbstractSnmpTableQuery {
    @Override
    public Map<String, OID> getColumnDefinition() {
        HashMap<String, OID> columns = new HashMap<>();
        columns.put("sysORIndex", new OID(new int[]{1, 3, 6, 1, 2, 1, 1, 9, 1, 1}));
        columns.put("sysORID", new OID(new int[]{1, 3, 6, 1, 2, 1, 1, 9, 1, 2}));
        columns.put("sysORDescr", new OID(new int[]{1, 3, 6, 1, 2, 1, 1, 9, 1, 3}));
        columns.put("sysORUpTime", new OID(new int[]{1, 3, 6, 1, 2, 1, 1, 9, 1, 4}));
        return columns;
    }

    @Override
    public String getRowTitle(Map<String, QueryResponse> singleRow, int index) {
        return concatenateIfPossible(singleRow, new String[]{"sysORDescr"}, "-");
    }

    public static class SysORTableQueryRequest extends AbstractQueryRequest<SysORTableQuery> {

        public SysORTableQueryRequest(DeviceConfiguration deviceConfiguration) {
            super(deviceConfiguration);
        }

        @Override
        public boolean isSingleRequest() {
            return false;
        }

        @Override
        public OID getOidQuery() {
            return new OID(new int[]{1, 3, 6, 1, 2, 1, 1, 9});
        }

        @Override
        public Class<SysORTableQuery> getQueryClass() {
            return SysORTableQuery.class;
        }
    }
}
