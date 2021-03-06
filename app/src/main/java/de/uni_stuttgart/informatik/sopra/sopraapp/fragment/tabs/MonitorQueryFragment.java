package de.uni_stuttgart.informatik.sopra.sopraapp.fragment.tabs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.ManagedDevice;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.SnmpManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.tasks.MonitoringQueryTask;

/**
 * fragment for hardware info
 */
public class MonitorQueryFragment extends DeviceFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_device_custom_queries, container, false);
        TextView oidDumpView = (TextView) rootView.findViewById(R.id.device_detail_custom_hint);
        oidDumpView.setText(R.string.fragment_title_monitoring_info);
        // we add query sections to cockpitQueryView
        initQueryView(rootView.findViewById(R.id.default_cockpit_query_view_custom));
        return rootView;
    }

    /**
     * method to update cockpit query view
     */
    private void updateCockpitQueryView() {
        if (getQueryView() == null) {
            Log.w(TAG, "null query view");
            return;
        }
        getQueryView().clear();

        ManagedDevice md = getManagedDevice();
        if (!md.isDummy()) {
            MonitoringQueryTask backgroundTask = new MonitoringQueryTask(getQueryView(), md.getDeviceConfiguration());
            backgroundTask.executeOnExecutor(SnmpManager.getInstance().getThreadPoolExecutor());

            waitForTaskResultAsync(backgroundTask, md.getDeviceConfiguration());
        }
    }

    @Override
    public void reloadData() {
        updateCockpitQueryView();
    }
}