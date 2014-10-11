package com.commandapps.helloworldmap.fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.commandapps.helloworldmap.OfficeLocationAdapter;
import com.commandapps.helloworldmap.R;
import com.commandapps.helloworldmap.interfaces.OfficeLocationsChangedListener;
import com.commandapps.helloworldmap.interfaces.OfficeLocationsProvider;
import com.commandapps.helloworldmap.interfaces.UserLocationListener;
import com.commandapps.helloworldmap.interfaces.UserLocationProvider;
import com.commandapps.helloworldmap.model.OfficeLocation;

import java.util.ArrayList;
import java.util.List;


public class OfficeLocationListFragment extends ListFragment implements OfficeLocationsChangedListener, UserLocationListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OfficeLocationAdapter adapter;

    private OfficeLocationsProvider officeLocationsProvider;
    private UserLocationProvider userLocationProvider;
    private List<OfficeLocation> officeLocations;
    private Location userLocation;

    // TODO: Rename and change types of parameters
    public static OfficeLocationListFragment newInstance(String param1, String param2) {
        OfficeLocationListFragment fragment = new OfficeLocationListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflater.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_office_locations, container);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OfficeLocationListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // TODO: Change Adapter to display your content
        adapter = new OfficeLocationAdapter(getActivity(), 0, new ArrayList<OfficeLocation>());
        if (null != userLocation){
            adapter.setUserLocation(userLocation);
        }
        if (null != officeLocations){
            adapter.addAll(officeLocations);
        }
        setListAdapter(adapter);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            officeLocationsProvider = (OfficeLocationsProvider) activity;
            officeLocationsProvider.addOfficeLocationsChangedListener(this);
            userLocationProvider = (UserLocationProvider) activity;
            userLocationProvider.addUserLocationListener(this);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        officeLocationsProvider.removeOfficeLocationsChangedListener(this);
        userLocationProvider.removeUserLocationListener(this);
        userLocationProvider = null;
        officeLocationsProvider = null;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != officeLocationsProvider) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            officeLocationsProvider.onOfficeLocationSelected(adapter.getItem(position));
        }
    }

    @Override
    public void onOfficeLocationsChanged(List<OfficeLocation> officeLocations) {
        this.officeLocations = officeLocations;
        if (adapter!=null) {
            adapter.clear();
            adapter.addAll(officeLocations);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onUserLocationChanged(Location userLocation) {
        this.userLocation = userLocation;
        if (adapter!=null) {
            adapter.setUserLocation(userLocation);
            adapter.notifyDataSetChanged();
        }
    }
}
