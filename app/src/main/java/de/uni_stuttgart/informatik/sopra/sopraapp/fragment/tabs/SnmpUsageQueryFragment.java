package de.uni_stuttgart.informatik.sopra.sopraapp.fragment.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.DefaultListQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general.SysORTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.view.CockpitQueryView;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.ManagedDevice;

/**
 * fragment for snmp usage
 */
public class SnmpUsageQueryFragment extends DeviceFragment {

    private String snmpUsageTitle = null;
    private String snmpMibsTitle = null;
    private String mibsOrTitle = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_device_usage_queries, container, false);
        TextView hintTextView = (TextView) rootView.findViewById(R.id.device_detail_usage_hint);
        hintTextView.setText(R.string.please_wait_label);

        ManagedDevice md = getManagedDevice();
        if (md == null) {
            throw new IllegalStateException("no managed device found!");
        }

        initQueryView(rootView.findViewById(R.id.default_cockpit_query_view_usage));

        hintTextView.setText(R.string.device_detail_usage_tab_hint);

        getQueryView().render(true);
        snmpUsageTitle = getString(R.string.snmp_usage_tab_content_title);
        snmpMibsTitle = getString(R.string.snmp_usage_tab_content_mibs);
        mibsOrTitle = getString(R.string.snmp_usage_tab_content_mibs);

        return rootView;
    }

    @Override
    public void reloadData() {
        refresh();
    }

    public void refresh() {
        CockpitQueryView queryView = getQueryView();
        if (queryView != null) {
            queryView.clear();
            DeviceConfiguration deviceConfiguration = getManagedDevice().getDeviceConfiguration();
            queryView.addTableQuery(mibsOrTitle + " | sysORTable", new SysORTableQuery.SysORTableQueryRequest(deviceConfiguration), true);

            queryView.addListQuery(snmpMibsTitle, new DefaultListQuery.MrTableQueryRequest(deviceConfiguration));

            queryView.addListQuery(snmpUsageTitle,
                    new DefaultListQuery.SnmpUsageQueryRequest(deviceConfiguration));

            queryView.render(true);
        }
    }
}