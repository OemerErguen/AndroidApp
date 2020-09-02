package de.uni_stuttgart.informatik.sopra.sopraapp.snmp;

import de.uni_stuttgart.informatik.sopra.sopraapp.util.DeviceAuthEncodingParser;
import de.uni_stuttgart.informatik.sopra.sopraapp.util.EncodingStringPartial;

/**
 * factory class for a {@link DeviceConfiguration} class ready to be used with the {@link SnmpConnection} class
 */
public class SnmpConfigurationFactory {
    /**
     * v1 config
     *
     * @param targetIp
     * @param community
     * @return
     */
    public DeviceConfiguration createSnmpV1Config(String targetIp, String community) {
        DeviceConfiguration dc = new DeviceConfiguration();
        dc.setSnmpVersion(DeviceConfiguration.SNMP_VERSION.v1);
        dc.setTargetIp(targetIp);
        dc.setUsername(community);
        dc.setAuthProtocol(null);
        dc.setPrivProtocol(null);
        return dc;
    }

    /**
     * v3 config
     *
     * @param targetIp
     * @param userName
     * @param password
     * @param encPhrase
     * @return
     */
    public DeviceConfiguration createSnmpV3Config(String targetIp, String userName, String password, String encPhrase) {
        DeviceConfiguration dc = new DeviceConfiguration();
        dc.setSnmpVersion(DeviceConfiguration.SNMP_VERSION.v3);
        dc.setTargetIp(targetIp);
        dc.setUsername(userName);
        dc.setAuthPassphrase(password);

        EncodingStringPartial encodingStringPartial = DeviceAuthEncodingParser.decodeString(encPhrase);
        if (encodingStringPartial != null) {
            if (encodingStringPartial.getPassword() != null && !encodingStringPartial.getPassword().isEmpty()) {
                dc.setPrivacyPassphrase(encodingStringPartial.getPassword());
            }
            if (encodingStringPartial.getSecurityLevel() != null) {
                dc.setSecurityLevel(encodingStringPartial.getSecurityLevel().getSnmp4jSecLevel());
            }
            if (encodingStringPartial.getAuthProtocol() != null) {
                dc.setAuthProtocol(encodingStringPartial.getAuthProtocol().getAuthOID());
            }
            if (encodingStringPartial.getPrivProtocol() != null) {
                dc.setPrivProtocol(encodingStringPartial.getPrivProtocol().getPrivOID());
            }
            if (encodingStringPartial.getContext() != null && !encodingStringPartial.getContext().trim().isEmpty()) {
                dc.setContext(encodingStringPartial.getContext());
            }
            if (encodingStringPartial.getPrivProtocol() != null && encodingStringPartial.getAuthProtocol() != null) {
                dc.setConnectionTestNeeded(false);
            }
        }
        return dc;
    }

    /**
     * dummy v1 config
     *
     * @param targetIp
     * @param community
     * @return
     */
    public DeviceConfiguration createDummyV1Config(String targetIp, String community) {
        DeviceConfiguration dc = new DeviceConfiguration();
        dc.setSnmpVersion(DeviceConfiguration.SNMP_VERSION.v1);
        dc.setDummy(true);
        dc.setTargetIp(targetIp);
        dc.setUsername(community);
        return dc;
    }
}
