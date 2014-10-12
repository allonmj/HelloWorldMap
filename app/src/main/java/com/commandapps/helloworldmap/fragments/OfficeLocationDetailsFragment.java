package com.commandapps.helloworldmap.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.commandapps.helloworldmap.DistanceUtils;
import com.commandapps.helloworldmap.R;
import com.commandapps.helloworldmap.interfaces.OfficeLocationChangedListener;
import com.commandapps.helloworldmap.interfaces.OfficeLocationProvider;
import com.commandapps.helloworldmap.interfaces.UserLocationListener;
import com.commandapps.helloworldmap.interfaces.UserLocationProvider;
import com.commandapps.helloworldmap.model.OfficeLocation;
import com.commandapps.helloworldmap.views.ActionButtonView;
import com.commandapps.helloworldmap.views.LocationRowView;
import com.squareup.picasso.Picasso;

public class OfficeLocationDetailsFragment extends Fragment implements OfficeLocationChangedListener, UserLocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OfficeLocationProvider officeLocationProvider;
    private UserLocationProvider userLocationProvider;
    private OfficeLocation officeLocation;
    private Location userLocation;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OfficeLocationDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OfficeLocationDetailsFragment newInstance(String param1, String param2) {
        OfficeLocationDetailsFragment fragment = new OfficeLocationDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public OfficeLocationDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_office_location_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
