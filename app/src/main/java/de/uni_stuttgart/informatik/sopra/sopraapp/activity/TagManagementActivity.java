package de.uni_stuttgart.informatik.sopra.sopraapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.adapter.TagListAdapter;
import de.uni_stuttgart.informatik.sopra.sopraapp.persistence.CockpitDbHelper;
import de.uni_stuttgart.informatik.sopra.sopraapp.persistence.model.Tag;

/**
 * activity to manage tags of this app
 */
public class TagManagementActivity extends AppCompatActivity implements ProtectedActivity {

    private TagListAdapter adapter;
    private LinearLayoutManager layoutManager;

    private static final String CURRENT_TAG_INPUT_KEY = "current_tag_input";
    private static final String CURRENT_TAG_ID = "current_tag_id";
    private TagAlertHelper alertHelper;
    private CockpitDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_management);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.activity_title_tag_management);

        RecyclerView recyclerView = findViewById(R.id.tag_recyclerview);

        dbHelper = new CockpitDbHelper(this);
        alertHelper = new TagAlertHelper(this);
        adapter = new TagListAdapter(dbHelper, alertHelper, this);
        recyclerView.setAdapter(adapter);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);


        initObservables(this, new AlertHelper(this), null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tag_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_tag:
                if (alertHelper != null) {
                    alertHelper.showTagEditDialog(null, false);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null && alertHelper != null) {
            outState.putString(CURRENT_TAG_INPUT_KEY, alertHelper.getCurrentTagInput());
            outState.putLong(CURRENT_TAG_ID, alertHelper.getCurrentTagId());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            String currentInput = savedInstanceState.getString(CURRENT_TAG_INPUT_KEY);
            long currentId = savedInstanceState.getLong(CURRENT_TAG_ID);

            if (alertHelper == null) {
                alertHelper = new TagAlertHelper(this);
            }
            alertHelper.showTagEditDialog(new Tag(currentId, currentInput), currentId != 0);
        }
    }

    @Override
    public void restartQueryCall() {
        adapter.notifyDataSetChanged();
        layoutManager.scrollToPosition(0);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
