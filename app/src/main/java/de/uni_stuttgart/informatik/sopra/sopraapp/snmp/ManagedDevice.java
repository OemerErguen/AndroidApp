package de.uni_stuttgart.informatik.sopra.sopraapp.snmp;

import java.util.Objects;

import de.uni_stuttgart.informatik.sopra.sopraapp.fragment.items.DeviceMonitorItemContent;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general.SystemQuery;

/**
 * this class represents a currently managed device by the app
 */
public class ManagedDevice {
    private final String id;
    private final DeviceMonitorItemContent.DeviceMonitorItem fragmentListItem;
    private final DeviceConfiguration deviceConfiguration;
    private final SystemQuery initialSystemQuery;
    private final boolean isDummy;
    private SystemQuery lastSystemQuery = null;

    /**
     * constructor
     *
     * @param id
     * @param fragmentListItem
     * @param deviceConfiguration
     * @param initialSystemQuery
     */
    public ManagedDevice(String id, DeviceMonitorItemContent.DeviceMonitorItem fragmentListItem,
                         DeviceConfiguration deviceConfiguration, SystemQuery initialSystemQuery, boolean isDummy) {
        this.id = id;
        this.fragmentListItem = fragmentListItem;
        this.deviceConfiguration = deviceConfiguration;
        this.initialSystemQuery = initialSystemQuery;
        this.isDummy = isDummy;
    }

    public String getId() {
        return id;
    }

    public boolean isDummy() {
        return isDummy;
    }

    public DeviceConfiguration getDeviceConfiguration() {
        return deviceConfiguration;
    }

    /**
     * control label in views
     *
     * @return
     */
    public String getDeviceLabel() {
        return initialSystemQuery.getSysName() + " | " + deviceConfiguration.getTargetIp()
                + ":" + deviceConfiguration.getTargetPort();
    }

    /**
     * used in spinner
     *
     * @return
     */
    public String getShortDeviceLabel() {
        if (initialSystemQuery.getSysName() == null || initialSystemQuery.getSysName().length() > 32) {
            return deviceConfiguration.getTargetIp() + ":" + deviceConfiguration.getTargetPort();
        }
        return initialSystemQuery.getSysName();
    }

    public SystemQuery getInitialSystemQuery() {
        return initialSystemQuery;
    }

    /**
     * returns last system query or initial one which should be always present
     * @return
     */
    public SystemQuery getLastSystemQuery() {
        if (lastSystemQuery != null) {
            return lastSystemQuery;
        }
        return initialSystemQuery;
    }

    @Override
    public String toString() {
        return "ManagedDevice{" +
                "id='" + id + '\'' +
                ", fragmentListItem=" + fragmentListItem +
                ", deviceConfiguration=" + deviceConfiguration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ManagedDevice that = (ManagedDevice) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void updateSystemQuery(SystemQuery systemQuery) {
        this.lastSystemQuery = systemQuery;
    }
}
