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

import com.commandapps.helloworldmap.DistanceUtils;
import com.commandapps.helloworldmap.OfficeLocationAdapter;
import com.commandapps.helloworldmap.R;
import com.commandapps.helloworldmap.interfaces.OfficeLocationsChangedListener;
import com.commandapps.helloworldmap.interfaces.OfficeLocationsProvider;
import com.commandapps.helloworldmap.interfaces.UserLocationListener;
import com.commandapps.helloworldmap.interfaces.UserLocationProvider;
import com.commandapps.helloworldmap.model.OfficeLocation;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class OfficeLocationListFragment extends ListFragment implements OfficeLocationsChangedListener, UserLocationListener {

    private OfficeLocationAdapter adapter;

    private OfficeLocationsProvider officeLocationsProvider;
    private UserLocationProvider userLocationProvider;
    private List<OfficeLocation> officeLocations;
    private Location userLocation;

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

        adapter = new OfficeLocationAdapter(getActivity(), 0, new ArrayList<OfficeLocation>());
        if (null != officeLocations) {
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
        if (userLocation!=null){
            updateOfficeLocations(userLocation);
        }
        if (adapter != null) {
            adapter.clear();
            adapter.addAll(officeLocations);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onUserLocationChanged(Location userLocation) {
        if (officeLocations!=null) {
            updateOfficeLocations(userLocation);
        }
        this.userLocation = userLocation;
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void updateOfficeLocations(Location userLocation){
        for (OfficeLocation officeLocation : officeLocations) {
            float metersDistance = DistanceUtils.calculateDistanceMeters(officeLocation, userLocation);
            officeLocation.setLastKnownDistance(metersDistance);
        }
        Collections.sort(officeLocations);
    }
}
