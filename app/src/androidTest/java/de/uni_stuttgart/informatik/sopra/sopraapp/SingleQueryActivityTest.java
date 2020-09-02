package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.uni_stuttgart.informatik.sopra.sopraapp.activity.SingleQueryResultActivity;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.SnmpConfigurationFactory;

import static android.support.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;

@RunWith(AndroidJUnit4.class)
public class SingleQueryActivityTest {

    @Rule
    public ActivityTestRule<SingleQueryResultActivity> rule = new ActivityTestRule<>(SingleQueryResultActivity.class, true, false);

    @Before
    public void setUp() throws Exception {
        DeviceConfiguration dummyConfig = new SnmpConfigurationFactory().createDummyV1Config("192.165.213.123", "public");
        DeviceManager.getInstance().add(dummyConfig, true);
        Assert.assertNotNull(dummyConfig);
        Assert.assertNotNull(dummyConfig.getUniqueConnectionId());
        Intent startIntent = new Intent();
        startIntent.putExtra(SingleQueryResultActivity.EXTRA_DEVICE_ID, dummyConfig.getUniqueDeviceId());
        startIntent.putExtra(SingleQueryResultActivity.EXTRA_OID_QUERY, "1.3.5");
        rule.launchActivity(startIntent);
    }

    @Test
    public void testRefreshWorksOnDummy() throws Throwable {
        runOnUiThread(() -> {
            rule.getActivity().restartQueryCall();
            Assert.assertTrue(true);
        });
    }
}
