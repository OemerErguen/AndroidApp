package de.uni_stuttgart.informatik.sopra.sopraapp;

import org.junit.Assert;
import org.junit.Test;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.AuthSHA;
import org.snmp4j.security.PrivAES128;

import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.SnmpConfigurationFactory;

public class DeviceConfigurationTest {

    @Test
    public void testDeviceConfiguration() {
        DeviceConfiguration dc = new DeviceConfiguration();
        Assert.assertEquals("", dc.getAuthPassphrase());
        Assert.assertEquals("", dc.getPrivacyPassphrase());
        Assert.assertEquals("", dc.getUsername());
        Assert.assertEquals(AuthSHA.ID, dc.getAuthProtocol());
        Assert.assertEquals(PrivAES128.ID, dc.getPrivProtocol());
        Assert.assertEquals("udp", dc.getNetworkProtocol());
        Assert.assertNull(dc.getSnmpVersionEnum());
        Assert.assertEquals(161, dc.getTargetPort());

        dc.setTargetPort(162);
        Assert.assertEquals(162, dc.getTargetPort());
    }

    @Test
    public void testDeviceConfigurationFactoryV1() {
        SnmpConfigurationFactory configurationFactory = new SnmpConfigurationFactory();
        DeviceConfiguration configV1 = configurationFactory.createSnmpV1Config("127.0.0.1", "public");
        Assert.assertEquals("127.0.0.1", configV1.getTargetIp());
        Assert.assertEquals("udp", configV1.getNetworkProtocol());
        Assert.assertEquals("public", configV1.getUsername());
        Assert.assertEquals(SnmpConstants.version1, configV1.getSnmpVersion());
    }

    @Test
    public void testDeviceConfigurationFactoryV3() {
        SnmpConfigurationFactory configurationFactory = new SnmpConfigurationFactory();
        DeviceConfiguration configV3 = configurationFactory.createSnmpV3Config("127.0.0.1", "testuser", "testpassword", "privKey");
        Assert.assertEquals("127.0.0.1", configV3.getTargetIp());
        Assert.assertEquals("udp", configV3.getNetworkProtocol());
        Assert.assertEquals("testuser", configV3.getUsername());
        Assert.assertEquals("testpassword", configV3.getAuthPassphrase());
        Assert.assertEquals("privKey", configV3.getPrivacyPassphrase());
        Assert.assertEquals(161, configV3.getTargetPort());
    }
}
