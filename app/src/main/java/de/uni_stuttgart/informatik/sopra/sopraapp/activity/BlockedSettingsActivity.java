package de.uni_stuttgart.informatik.sopra.sopraapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;

/**
 * this class is a wrapper around the settings fragment defined here:
 * {@link de.uni_stuttgart.informatik.sopra.sopraapp.CockpitPreferenceManager.GeneralPreferenceFragment}
 *
 * this activity is displayed when app has no secure network
 */
public class BlockedSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_settings);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
