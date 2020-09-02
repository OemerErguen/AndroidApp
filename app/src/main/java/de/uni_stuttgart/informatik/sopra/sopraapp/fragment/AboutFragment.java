package de.uni_stuttgart.informatik.sopra.sopraapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;

/**
 * main fragment for about section
 */
public class AboutFragment extends Fragment {


    public static final String TAG = AboutFragment.class.getName();

    public AboutFragment() {
        // android needs an empty fragment constructor
    }

    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        String[] arrays = getResources().getStringArray(R.array.about_screen);
        ListView listView = view.findViewById(R.id.lv_aboutItems);

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arrays);
        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), AboutHelperFragment.class);
                switch (position) {
                    case 0: //License
                        intent.putExtra("File", "file:///android_asset/license.html");
                        intent.putExtra("Message", "license");
                        break;
                    case 1: //Credits
                        intent.putExtra("File", "file:///android_asset/credits.html");
                        intent.putExtra("Message", "credits");
                        break;
                    case 2: //Wifi QR-Code Schema
                        intent.putExtra("File", "file:///android_asset/wifiregexp.html");
                        intent.putExtra("Message", "wifiQR");
                        break;
                    case 3: //Geräte QR-Code Schema
                        intent.putExtra("File", "file:///android_asset/deviceregexp.html");
                        intent.putExtra("Message", "deviceQR");
                        break;
                    case 4: //Versions
                        intent.putExtra("File", "file:///android_asset/version.html");
                        intent.putExtra("Message", "version");
                        break;
                    default:
                        Log.w(TAG, "unexpected index: " + position);
                }

                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
