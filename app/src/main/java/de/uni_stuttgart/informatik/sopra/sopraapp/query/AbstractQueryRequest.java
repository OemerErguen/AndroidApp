package de.uni_stuttgart.informatik.sopra.sopraapp.query;

import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;

/**
 * abstract class for each query request
 * the subclasses are more interesting
 *
 * @param <T>
 */
public abstract class AbstractQueryRequest<T extends SnmpQuery> implements QueryRequest<T> {
    private DeviceConfiguration deviceConfiguration;

    public AbstractQueryRequest(DeviceConfiguration deviceConfiguration) {
        this.deviceConfiguration = deviceConfiguration;
    }

    public DeviceConfiguration getDeviceConfiguration() {
        return deviceConfiguration;
    }
}
