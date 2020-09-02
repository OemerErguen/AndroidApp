package de.uni_stuttgart.informatik.sopra.sopraapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.fragment.DeviceMonitorViewFragment.OnListFragmentInteractionListener;
import de.uni_stuttgart.informatik.sopra.sopraapp.fragment.items.DeviceMonitorItemContent.DeviceMonitorItem;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DeviceMonitorItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class DeviceMonitorViewRecyclerViewAdapter extends RecyclerView.Adapter<DeviceMonitorViewRecyclerViewAdapter.DeviceItemViewHolder> {

    public static final String TAG = DeviceMonitorViewRecyclerViewAdapter.class.getName();
    private final List<DeviceMonitorItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public DeviceMonitorViewRecyclerViewAdapter(List<DeviceMonitorItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public DeviceItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_device_card_item, parent, false);
        return new DeviceItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DeviceItemViewHolder holder, int position) {
        if (mValues.isEmpty()) {
            Log.w(TAG, "empty device item view holder");
            return;
        }
        DeviceMonitorItem deviceMonitorItem = mValues.get(position);
        holder.setDeviceMonitorItem(deviceMonitorItem);
        if (deviceMonitorItem.systemQuery == null) {
            Log.e(TAG, "null system query detected!");
            return;
        }
        String sysName = deviceMonitorItem.systemQuery.getSysName();
        holder.mIdView.setText(deviceMonitorItem.id
                + " " + deviceMonitorItem.deviceConfiguration.getListLabel(sysName));
        Log.i(TAG, deviceMonitorItem.deviceConfiguration.getSnmpVersionEnum().toString());
        holder.sysIpTextView.setText(deviceMonitorItem.deviceConfiguration.getTargetIp());
        holder.snmpVersionTextView
                .setText(deviceMonitorItem.deviceConfiguration.getSnmpVersionEnum().toString());
        holder.sysDescrTextView.setText(deviceMonitorItem.systemQuery.getSysDescr());
        holder.sysLocationTextView.setText(deviceMonitorItem.systemQuery.getSysLocation());
        holder.sysNameTextView.setText(sysName);
        holder.sysContactTextView.setText(deviceMonitorItem.systemQuery.getSysContact());
        holder.sysObjectIdTextView.setText(deviceMonitorItem.systemQuery.getSysObjectId());
        holder.sysUpTimeTextView.setText(deviceMonitorItem.systemQuery.getSysUpTime());
        holder.sysServicesTextView.setText(deviceMonitorItem.systemQuery.getSysServices());

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(holder.getDeviceMonitorItem());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     * viewholder class for recycler view
     */
    public class DeviceItemViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        private final TextView sysDescrTextView;
        private final TextView sysLocationTextView;
        private final TextView sysNameTextView;
        private final TextView sysContactTextView;
        private final TextView sysObjectIdTextView;
        private final TextView sysUpTimeTextView;
        private final TextView sysServicesTextView;
        private final TextView sysIpTextView;
        private DeviceMonitorItem deviceMonitorItem;
        public final TextView snmpVersionTextView;

        public DeviceItemViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.item_number);
            snmpVersionTextView = view.findViewById(R.id.device_monitor_text_snmp_version);
            sysDescrTextView = view.findViewById(R.id.device_monitor_sys_description);
            sysLocationTextView = view.findViewById(R.id.device_monitor_sys_location);
            sysNameTextView = view.findViewById(R.id.device_monitor_sys_name);
            sysIpTextView = view.findViewById(R.id.device_monitor_sys_ip);
            sysContactTextView = view.findViewById(R.id.device_monitor_sys_contact);
            sysObjectIdTextView = view.findViewById(R.id.device_monitor_object_id);
            sysUpTimeTextView = view.findViewById(R.id.device_monitor_sys_uptime);
            sysServicesTextView = view.findViewById(R.id.device_monitor_sys_services);
        }

        @Override
        public String toString() {
            return "QueryAdapterViewHolder{" +
                    "mView=" + mView +
                    ", mIdView=" + mIdView +
                    ", sysDescrTextView=" + sysDescrTextView +
                    ", sysLocationTextView=" + sysLocationTextView +
                    ", sysNameTextView=" + sysNameTextView +
                    ", sysContactTextView=" + sysContactTextView +
                    ", sysObjectIdTextView=" + sysObjectIdTextView +
                    ", sysUpTimeTextView=" + sysUpTimeTextView +
                    ", sysServicesTextView=" + sysServicesTextView +
                    ", snmpVersionTextView=" + snmpVersionTextView +
                    '}';
        }

        public DeviceMonitorItem getDeviceMonitorItem() {
            return deviceMonitorItem;
        }

        public void setDeviceMonitorItem(DeviceMonitorItem deviceMonitorItem) {
            this.deviceMonitorItem = deviceMonitorItem;
        }
    }
}
