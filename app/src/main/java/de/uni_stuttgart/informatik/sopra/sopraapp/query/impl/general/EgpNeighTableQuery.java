package de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general;

import org.snmp4j.smi.OID;

import java.util.HashMap;
import java.util.Map;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.AbstractSnmpTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * this class represents a query for ip net to media table 1.3.6.1.2.1.8.5
 */
public class EgpNeighTableQuery extends AbstractSnmpTableQuery {

    public static final String TAG = EgpNeighTableQuery.class.getName();

    @Override
    public Map<String, OID> getColumnDefinition() {
        Map<String, OID> columnDefinition = new HashMap<>();
        columnDefinition.put("egpNeighState", new OID(new int[]{1, 3, 6, 1, 2, 1, 8, 5, 1, 1}));
        columnDefinition.put("egpNeighAddr", new OID(new int[]{1, 3, 6, 1, 2, 1, 8, 5, 1, 2}));
        columnDefinition.put("egpNeighAs", new OID(new int[]{1, 3, 6, 1, 2, 1, 8, 5, 1, 3}));
        columnDefinition.put("egpNeighInMsgs", new OID(new int[]{1, 3, 6, 1, 2, 1, 8, 5, 1, 4}));
        columnDefinition.put("egpNeighInErrs", new OID(new int[]{1, 3, 6, 1, 2, 1, 8, 5, 1, 5}));
        columnDefinition.put("egpNeighOutMsgs", new OID(new int[]{1, 3, 6, 1, 2, 1, 8, 5, 1, 6}));
        columnDefinition.put("egpNeighOutErrs", new OID(new int[]{1, 3, 6, 1, 2, 1, 8, 5, 1, 7}));
        columnDefinition.put("egpNeighInErrMsgs", new OID(new int[]{1, 3, 6, 1, 2, 1, 8, 5, 1, 8}));
        columnDefinition.put("egpNeighOutErrMsgs", new OID(new int[]{1, 3, 6, 1, 2, 1, 8, 5, 1, 9}));
        columnDefinition.put("egpNeighStateUps", new OID(new int[]{1, 3, 6, 1, 2, 1, 8, 5, 1, 10}));
        columnDefinition.put("egpNeighStateDowns", new OID(new int[]{1, 3, 6, 1, 2, 1, 8, 5, 1, 11}));
        columnDefinition.put("egpNeighIntervalHello", new OID(new int[]{1, 3, 6, 1, 2, 1, 8, 5, 1, 12}));
        columnDefinition.put("egpNeighIntervalPoll", new OID(new int[]{1, 3, 6, 1, 2, 1, 8, 5, 1, 13}));
        columnDefinition.put("egpNeighMode", new OID(new int[]{1, 3, 6, 1, 2, 1, 8, 5, 1, 14}));
        columnDefinition.put("egpNeighEventTrigger", new OID(new int[]{1, 3, 6, 1, 2, 1, 8, 5, 1, 15}));
        return columnDefinition;
    }

    @Override
    public String getRowTitle(Map<String, QueryResponse> singleRow, int rowIndex) {
        return concatenateIfPossible(singleRow, new String[]{
                "egpNeighAddr"
        }, "-");
    }

    /**
     * request class
     */
    public static class EgpNeighTableQueryRequest extends AbstractTableQueryRequest<EgpNeighTableQuery> {
        public EgpNeighTableQueryRequest(DeviceConfiguration deviceConfiguration) {
            super(deviceConfiguration);
        }

        @Override
        public OID getOidQuery() {
            return new OID(new int[]{1, 3, 6, 1, 2, 1, 8, 5});
        }

        @Override
        public Class<EgpNeighTableQuery> getQueryClass() {
            return EgpNeighTableQuery.class;
        }
    }
}
