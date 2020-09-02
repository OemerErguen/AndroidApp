package de.uni_stuttgart.informatik.sopra.sopraapp.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.activity.TabbedDeviceActivity;
import de.uni_stuttgart.informatik.sopra.sopraapp.fragment.SingleQueryResultActivityFragment;

/**
 * view pager for tabs of single device view
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public static final String TAG = ViewPagerAdapter.class.getName();

    private final List<Fragment> mFragmentList = Collections.synchronizedList(new ArrayList<>());
    private final List<String> mFragmentTitleList = Collections.synchronizedList(new ArrayList<>());

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    /**
     * helper method
     *
     * @param fragment
     * @param deviceId
     * @param title
     */
    public void addFragment(Fragment fragment, String deviceId, String title) {
        Bundle bundleArgs = new Bundle();
        bundleArgs.putString(TabbedDeviceActivity.EXTRA_DEVICE_ID, deviceId);

        fragment.setArguments(bundleArgs);

        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    public void addUserTabFragment(String deviceId, String oidQuery, String title) {
        SingleQueryResultActivityFragment singleQueryResultActivityFragment =
                new SingleQueryResultActivityFragment();
        Bundle bundleArgs = new Bundle();
        bundleArgs.putString(TabbedDeviceActivity.EXTRA_DEVICE_ID, deviceId);
        bundleArgs.putString(SingleQueryResultActivityFragment.OID_QUERY, oidQuery);
        bundleArgs.putBoolean(SingleQueryResultActivityFragment.TAB_MODE, true);

        singleQueryResultActivityFragment.setArguments(bundleArgs);

        mFragmentList.add(singleQueryResultActivityFragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    public void clear() {
        mFragmentList.clear();
        mFragmentTitleList.clear();
        notifyDataSetChanged();
    }
}