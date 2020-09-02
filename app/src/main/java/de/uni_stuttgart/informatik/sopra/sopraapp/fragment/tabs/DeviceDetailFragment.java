package de.uni_stuttgart.informatik.sopra.sopraapp.fragment.tabs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.view.CockpitQueryView;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.ManagedDevice;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.SnmpManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.tasks.DetailInfoQueryTask;

/**
 * A placeholder fragment containing a simple view.
 */
public class DeviceDetailFragment extends DeviceFragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ManagedDevice md = getManagedDevice();
        if (md == null) {
            throw new IllegalStateException("no managed device found!");
        }

        View rootView = inflater.inflate(R.layout.fragment_device_detail_main, container, false);
        TextView hintView = rootView.findViewById(R.id.device_detail_main_hint);
        hintView.setText(R.string.please_wait_label);

        initQueryView(rootView.findViewById(R.id.default_cockpit_query_view));
        hintView.setText(R.string.device_detail_main_hint);

        return rootView;
    }

    /**
     * method to update cockpit query view
     */
    private void updateCockpitQueryView() {
        CockpitQueryView queryView = getQueryView();
        if (queryView == null) {
            Log.w(TAG, "null query view");
            return;
        }
        queryView.clear();

        ManagedDevice md = getManagedDevice();
        if (!md.isDummy()) {
            DetailInfoQueryTask backgroundTask = new DetailInfoQueryTask(queryView, md.getDeviceConfiguration());
            backgroundTask.executeOnExecutor(SnmpManager.getInstance().getThreadPoolExecutor());
            // we wait async for the result to use the android task timeout feature

            waitForTaskResultAsync(backgroundTask, md.getDeviceConfiguration());
        }
    }

    @Override
    public void reloadData() {
        updateCockpitQueryView();
    }
}