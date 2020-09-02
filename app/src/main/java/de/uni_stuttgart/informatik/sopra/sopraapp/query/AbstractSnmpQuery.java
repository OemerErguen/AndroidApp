package de.uni_stuttgart.informatik.sopra.sopraapp.query;

import org.snmp4j.smi.OID;

import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * every snmp query class should derive from this superclass
 */
public abstract class AbstractSnmpQuery implements SnmpQuery {
    @Override
    public abstract void processResult(List<QueryResponse> results);

    /**
     * internal helper method to retrieve an oid out of the result list or return null
     *
     * @param results
     * @param oid
     * @return
     */
    protected String getOIDValue(List<QueryResponse> results, OID oid) {
        for (QueryResponse queryResponse : results) {
            if (queryResponse.getVariableBinding().getOid().equals(oid)) {
                return queryResponse.getVariableBinding().getVariable().toString();
            }
        }
        return null;
    }

}
