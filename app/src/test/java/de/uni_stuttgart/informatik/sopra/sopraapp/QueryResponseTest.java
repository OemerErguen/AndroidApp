package de.uni_stuttgart.informatik.sopra.sopraapp;

import junit.framework.Assert;

import org.junit.Test;
import org.snmp4j.PDU;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.VariableBinding;

import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

public class QueryResponseTest {

    @Test
    public void testQueryResponseModel() {
        QueryResponse response = new QueryResponse(new PDU(), SnmpConstants.sysLocation.toDottedString(), new VariableBinding());
        Assert.assertNotNull(response);
        Assert.assertEquals(SnmpConstants.sysLocation.toDottedString(), response.getOid());
        Assert.assertTrue(response.getVariableBinding() != null);
    }
}
