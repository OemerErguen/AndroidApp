package de.uni_stuttgart.informatik.sopra.sopraapp;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.uni_stuttgart.informatik.sopra.sopraapp.activity.SNMPLoginActivity;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class SNMPLoginActivityTest {

    @Rule
    public ActivityTestRule<SNMPLoginActivity> rule = new ActivityTestRule<>(SNMPLoginActivity.class);

    @Test
    public void testStartup() {
        SNMPLoginActivity activity = rule.getActivity();
        assertEquals(activity.getString(R.string.title_activity_snmplogin), activity.getTitle());
        // TODO
    }
}
