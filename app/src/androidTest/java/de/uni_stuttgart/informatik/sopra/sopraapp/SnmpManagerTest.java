package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.snmp4j.smi.OID;

import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.SnmpManager;


@RunWith(AndroidJUnit4.class)
public class SnmpManagerTest {

    @Rule
    public ActivityTestRule<CockpitMainActivity> rule = new ActivityTestRule<>(CockpitMainActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        rule.launchActivity(new Intent());
    }

    @Test
    public void testDeviceConfigLabelsAreImplemented() {
        DeviceConfiguration deviceConfiguration = new DeviceConfiguration();
        for (OID authProtocol : SnmpManager.getInstance().getAuthProtocols()) {
            deviceConfiguration.setAuthProtocol(authProtocol);
            Assert.assertNotNull(deviceConfiguration.getAuthProtocolLabel());
        }
        for (OID privProtocol : SnmpManager.getInstance().getPrivProtocols()) {
            deviceConfiguration.setPrivProtocol(privProtocol);
            Assert.assertNotNull(deviceConfiguration.getPrivProtocolLabel());
        }
    }
}
