package de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model;

import org.snmp4j.PDU;
import org.snmp4j.smi.VariableBinding;

/**
 * representing a snmp response of {@link de.uni_stuttgart.informatik.sopra.sopraapp.snmp.SnmpConnection}
 */
public class QueryResponse {
    private String oid;
    private VariableBinding variableBinding;

    /**
     * note: we should not store the whole pdu object, extract what you need in constructor to new class vars
     *
     * @param pdu
     * @param oid
     * @param variableBinding
     */
    public QueryResponse(PDU pdu, String oid, VariableBinding variableBinding) {
        this.oid = oid;
        this.variableBinding = variableBinding;
    }

    public String getValue() {
        return this.variableBinding.getVariable().toString();
    }

    public String getOid() {
        return oid;
    }

    public VariableBinding getVariableBinding() {
        return variableBinding;
    }

    @Override
    public String toString() {
        return "QueryResponse{" +
                "oid='" + oid + '\'' +
                ", variableBinding=" + variableBinding +
                '}';
    }
}
