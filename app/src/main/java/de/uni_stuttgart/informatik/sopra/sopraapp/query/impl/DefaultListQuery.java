package de.uni_stuttgart.informatik.sopra.sopraapp.query.impl;

import org.snmp4j.smi.OID;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.AbstractQueryRequest;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.AbstractSnmpListQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.SimpleSnmpListRequest;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;

/**
 * Every OID-Query of this app can be displayed in a list
 */
public class DefaultListQuery extends AbstractSnmpListQuery {

    /**
     * snmp usage information
     */
    public static class SnmpUsageQueryRequest extends AbstractQueryRequest<DefaultListQuery> {

        public SnmpUsageQueryRequest(DeviceConfiguration deviceConfiguration) {
            super(deviceConfiguration);
        }

        @Override
        public boolean isSingleRequest() {
            return false;
        }

        @Override
        public OID getOidQuery() {
            return new OID(new int[]{1, 3, 6, 1, 2, 1, 11});
        }

        @Override
        public Class<DefaultListQuery> getQueryClass() {
            return DefaultListQuery.class;
        }
    }

    /**
     * ip section
     */
    public static class IpSectionQueryRequest extends AbstractQueryRequest<DefaultListQuery> {

        public IpSectionQueryRequest(DeviceConfiguration deviceConfiguration) {
            super(deviceConfiguration);
        }

        @Override
        public boolean isSingleRequest() {
            return false;
        }

        @Override
        public OID getOidQuery() {
            return new OID(new int[]{1, 3, 6, 1, 2, 1, 4});
        }

        @Override
        public Class<DefaultListQuery> getQueryClass() {
            return DefaultListQuery.class;
        }
    }

    /**
     * mibs
     */
    public static class MrTableQueryRequest extends AbstractQueryRequest {

        public MrTableQueryRequest(DeviceConfiguration deviceConfiguration) {
            super(deviceConfiguration);
        }

        @Override
        public boolean isSingleRequest() {
            return false;
        }

        @Override
        public OID getOidQuery() {
            return new OID(new int[]{1, 3, 6, 1, 4, 1, 2021, 102});
        }

        @Override
        public Class getQueryClass() {
            return DefaultListQuery.class;
        }
    }
}
