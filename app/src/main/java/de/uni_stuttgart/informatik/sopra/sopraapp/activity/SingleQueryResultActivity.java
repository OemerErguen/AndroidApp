package de.uni_stuttgart.informatik.sopra.sopraapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.fragment.SingleQueryResultActivityFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.util.PeriodicTask;

/**
 * Activity to display a single oid query
 */
public class SingleQueryResultActivity extends AppCompatActivity implements ProtectedActivity {
    public static final String EXTRA_DEVICE_ID = "device_id";
    public static final String EXTRA_OID_QUERY = "oid_query";
    public static final String TAG = SingleQueryResultActivity.class.getName();
    private SingleQueryResultActivityFragment queryFragment;
    private PeriodicTask periodicTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_query_result);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initObservables(this, new AlertHelper(this), null);

        String deviceId = getIntent().getStringExtra(EXTRA_DEVICE_ID);
        String oidQuery = getIntent().getStringExtra(EXTRA_OID_QUERY);

        if (deviceId == null || oidQuery == null) {
            Log.e(TAG, "empty deviceId or oidQuery intent extra");
            return;
        }
        setTitle(getString(R.string.activty_title_oid_single_query) + " | " +  oidQuery);

        queryFragment = (SingleQueryResultActivityFragment)
                getSupportFragmentManager().findFragmentById(R.id.single_query_fragment);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // TODO implement safely with options menu item to disable
//        if (new CockpitPreferenceManager(this).isPeriodicUpdateEnabled()) {
//            periodicTask = new PeriodicTask(new Runnable() {
//                @Override
//                public void run() {
//                    restartQueryCall();
//                }
//            }, 5000);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_query_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            // update view
            if (queryFragment != null) {
                queryFragment.reloadData();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void restartQueryCall() {
        if (queryFragment != null) {
            queryFragment.reloadData();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startProtection(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        restartTrigger(this);
    }
}
