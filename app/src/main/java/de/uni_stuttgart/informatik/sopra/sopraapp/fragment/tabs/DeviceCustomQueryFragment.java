package de.uni_stuttgart.informatik.sopra.sopraapp.fragment.tabs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.persistence.CockpitDbHelper;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.view.CockpitQueryView;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.ManagedDevice;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.SnmpManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.tasks.CustomQueryTask;

/**
 * fragment for custom queries
 */
public class DeviceCustomQueryFragment extends DeviceFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_device_custom_queries, container, false);
        TextView oidDumpView = rootView.findViewById(R.id.device_detail_custom_hint);
        oidDumpView.setText(R.string.device_detail_custom_hint);

        initQueryView(rootView.findViewById(R.id.default_cockpit_query_view_custom));
        return rootView;
    }

    @Override
    public void reloadData() {
        CockpitQueryView queryView = getQueryView();
        if (queryView == null) {
            Log.w(TAG, "null query view");
            return;
        }
        queryView.clear();

        ManagedDevice md = getManagedDevice();
        if (!md.isDummy() && getContext() != null) {
            CockpitDbHelper dbHelper = new CockpitDbHelper(getContext());
            CustomQueryTask backgroundTask = new CustomQueryTask(queryView, md.getDeviceConfiguration(), dbHelper);
            backgroundTask.executeOnExecutor(SnmpManager.getInstance().getThreadPoolExecutor());
            waitForTaskResultAsync(backgroundTask, md.getDeviceConfiguration());
        }
    }
}
