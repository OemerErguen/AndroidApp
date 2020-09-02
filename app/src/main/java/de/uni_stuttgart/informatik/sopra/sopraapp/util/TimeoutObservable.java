package de.uni_stuttgart.informatik.sopra.sopraapp.util;

import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;

/**
 * class to observe timeout events of single devices
 */
public class TimeoutObservable extends BooleanObservable {
    private DeviceConfiguration deviceConfiguration;

    /**
     * constructor
     *
     * @param initialState
     */
    public TimeoutObservable(boolean initialState, DeviceConfiguration deviceConfiguration) {
        super(initialState);
        this.deviceConfiguration = deviceConfiguration;
    }

    public DeviceConfiguration getDeviceConfiguration() {
        return deviceConfiguration;
    }
}
