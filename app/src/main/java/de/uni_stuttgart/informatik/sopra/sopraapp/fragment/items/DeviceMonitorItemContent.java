package de.uni_stuttgart.informatik.sopra.sopraapp.fragment.items;

import java.util.List;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general.SystemQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceManager;

/**
 * device monitor item content class
 */
public class DeviceMonitorItemContent {

    /**
     * array of sample items.
     */
    private static final List<DeviceMonitorItem> ITEMS = DeviceManager.getInstance().getDeviceList();

    public static List<DeviceMonitorItem> getItems() {
        return ITEMS;
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DeviceMonitorItem {
        public final String id;
        public final String host;
        public final String port;
        public final DeviceConfiguration deviceConfiguration;
        public final SystemQuery systemQuery;

        /**
         * constructor
         *
         * @param id
         * @param host
         * @param port
         * @param deviceConfiguration
         * @param systemQuery
         */
        public DeviceMonitorItem(String id, String host, String port, DeviceConfiguration deviceConfiguration, SystemQuery systemQuery) {
            this.id = id;
            this.host = host;
            this.port = port;
            this.deviceConfiguration = deviceConfiguration;
            this.systemQuery = systemQuery;
        }

        public DeviceConfiguration getDeviceConfiguration() {
            return deviceConfiguration;
        }

        @Override
        public String toString() {
            return "DeviceMonitorItem{" +
                    "id='" + id + '\'' +
                    ", host='" + host + '\'' +
                    ", port='" + port + '\'' +
                    ", deviceConfiguration=" + deviceConfiguration +
                    '}';
        }
    }
}
