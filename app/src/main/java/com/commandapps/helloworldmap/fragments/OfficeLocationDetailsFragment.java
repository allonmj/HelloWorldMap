package com.commandapps.helloworldmap.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.commandapps.helloworldmap.DistanceUtils;
import com.commandapps.helloworldmap.R;
import com.commandapps.helloworldmap.interfaces.OfficeLocationChangedListener;
import com.commandapps.helloworldmap.interfaces.OfficeLocationProvider;
import com.commandapps.helloworldmap.interfaces.UserLocationListener;
import com.commandapps.helloworldmap.interfaces.UserLocationProvider;
import com.commandapps.helloworldmap.model.OfficeLocation;
import com.commandapps.helloworldmap.views.LocationRowView;
import com.squareup.picasso.Picasso;

public class OfficeLocationDetailsFragment extends Fragment implements OfficeLocationChangedListener, UserLocationListener {

    private OfficeLocationProvider officeLocationProvider;
    private UserLocationProvider userLocationProvider;
    private OfficeLocation officeLocation;
    private Location userLocation;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_office_location_details, container, false);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            officeLocationProvider = (OfficeLocationProvider) activity;
            officeLocationProvider.addOfficeLocationChangedListener(this);
            userLocationProvider = (UserLocationProvider) activity;
            userLocationProvider.addUserLocationListener(this);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void updateView(final OfficeLocation officeLocation) {
        View view = getView();
        if (view != null) {
            ImageView civ_office = (ImageView) view.findViewById(R.id.civ_office);
            Picasso.with(getActivity()).load(officeLocation.getOfficeImageUrl()).into(civ_office);
            LocationRowView locationRowView = (LocationRowView) view.findViewById(R.id.cv_location);
            locationRowView.setName(officeLocation.getName());
            locationRowView.setAddress(officeLocation.getAddress() + "\n" + officeLocation.getAddress2());
            locationRowView.setDistanceLabel(getString(R.string.distance));
            if (userLocation!=null) {
                float metersDistance = DistanceUtils.calculateDistanceMeters(officeLocation, userLocation);
                locationRowView.setDistance(DistanceUtils.metersToMiles(metersDistance) + " mi");
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        officeLocationProvider.removeOfficeLocationChangedListener(this);
        userLocationProvider.removeUserLocationListener(this);
        userLocationProvider = null;
        officeLocationProvider = null;
    }

    @Override
    public void onOfficeLocationChanged(OfficeLocation officeLocation) {
        this.officeLocation = officeLocation;
        updateView(officeLocation);
    }

    @Override
    public void onUserLocationChanged(Location userLocation) {
        this.userLocation = userLocation;
        if (getView()!=null && officeLocation!=null) {
            float metersDistance = DistanceUtils.calculateDistanceMeters(officeLocation, userLocation);
            LocationRowView locationRowView = (LocationRowView) getView().findViewById(R.id.cv_location);
            locationRowView.setDistance(DistanceUtils.metersToMiles(metersDistance) + " mi");
        }

    }
}
