package de.uni_stuttgart.informatik.sopra.sopraapp;


import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.ProgressBar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class CockpitMainActivityTest {

    @Rule
    public ActivityTestRule<CockpitMainActivity> rule = new ActivityTestRule<>(CockpitMainActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        rule.launchActivity(new Intent());
    }

    @Test
    public void testStartup() {
        CockpitMainActivity activity = rule.getActivity();
        assertEquals(activity.getResources().getString(R.string.app_name), activity.getTitle());
        ConstraintLayout cl = activity.findViewById(R.id.fragment_container);
        // test our fragment container is not empty
        assertTrue(cl.getChildCount() > 0);

        ProgressBar pb = activity.findViewById(R.id.app_progress_bar);
        assertNotNull(pb);
        // assert support toolbar is available
        assertNull(activity.getActionBar());
        assertNotNull(activity.findViewById(R.id.toolbar));
        assertNotNull(activity.getSupportActionBar());
    }
}
