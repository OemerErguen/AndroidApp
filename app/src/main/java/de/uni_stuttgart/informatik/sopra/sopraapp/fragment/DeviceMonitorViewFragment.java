package de.uni_stuttgart.informatik.sopra.sopraapp.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;


import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.adapter.DeviceMonitorViewRecyclerViewAdapter;
import de.uni_stuttgart.informatik.sopra.sopraapp.fragment.items.DeviceMonitorItemContent.DeviceMonitorItem;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.tasks.RefreshListTask;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DeviceMonitorViewFragment extends Fragment {

    public static final String TAG = DeviceMonitorViewFragment.class.getName();
    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private DeviceMonitorViewRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DeviceMonitorViewFragment() {
        // android needs an empty constructor
    }

    @SuppressWarnings("unused")
    public static DeviceMonitorViewFragment newInstance() {
        DeviceMonitorViewFragment fragment = new DeviceMonitorViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_devicemonitorview_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            LinearLayoutManager layout;
            // layout manager depending on orientation
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                layout = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            } else {
                layout = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            }
            recyclerView.setLayoutManager(layout);
            adapter = new DeviceMonitorViewRecyclerViewAdapter(DeviceManager.getInstance().getDeviceList(), mListener);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    /**
     * refresh device list + system queries of it
     */
    public void refreshListAsync() {
        Log.d(TAG, "refresh device list");
        RefreshListTask refreshListTask = new RefreshListTask(recyclerView);
        // NOTE: we do not use the default thread pool executor here. it would cause crashes
        refreshListTask.execute();
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main_options_menu, menu);
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(DeviceMonitorItem item);
    }
}
