package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.uni_stuttgart.informatik.sopra.sopraapp.activity.TabbedDeviceActivity;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.SnmpConfigurationFactory;

import static android.support.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;

@RunWith(AndroidJUnit4.class)
public class TabbedDeviceActivityTest {

    @Rule
    public ActivityTestRule<TabbedDeviceActivity> rule = new ActivityTestRule<>(TabbedDeviceActivity.class, true, false);

    @Before
    public void setUp() throws Exception {
        DeviceConfiguration dummyConfig = new SnmpConfigurationFactory().createDummyV1Config("192.165.213.123", "public");
        DeviceManager.getInstance().add(dummyConfig, true);
        Assert.assertNotNull(dummyConfig);
        Assert.assertNotNull(dummyConfig.getUniqueConnectionId());
        Intent startIntent = new Intent();
        startIntent.putExtra(TabbedDeviceActivity.EXTRA_DEVICE_ID, dummyConfig.getUniqueDeviceId());
        rule.launchActivity(startIntent);
    }

    @Test
    public void testRefreshWorksOnDummy() throws Throwable {
        runOnUiThread(() -> {
            rule.getActivity().refreshView(true);
            rule.getActivity().restartQueryCall();
            Assert.assertTrue(true);
        });
    }
}
