package de.uni_stuttgart.informatik.sopra.sopraapp.query;

import org.snmp4j.smi.OID;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.DefaultListQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;

/**
 * You can do any recursive snmp query with this reqest class.
 * Note: This query is not cacheable!
 *
 */
public class SimpleSnmpListRequest extends AbstractQueryRequest<DefaultListQuery> {
    private OID oid;

    public SimpleSnmpListRequest(DeviceConfiguration deviceConfiguration, String oid) {
        super(deviceConfiguration);
        this.oid = new OID(oid);
    }

    @Override
    public boolean isSingleRequest() {
        return false;
    }

    @Override
    public boolean isCacheable() {
        return false;
    }

    @Override
    public OID getOidQuery() {
        return oid;
    }

    @Override
    public Class<DefaultListQuery> getQueryClass() {
        return DefaultListQuery.class;
    }
}
