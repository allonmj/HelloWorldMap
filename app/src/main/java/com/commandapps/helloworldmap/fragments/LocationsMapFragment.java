package com.commandapps.helloworldmap.fragments;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import com.commandapps.helloworldmap.R;
import com.commandapps.helloworldmap.ScreenUtil;
import com.commandapps.helloworldmap.interfaces.OfficeLocationsChangedListener;
import com.commandapps.helloworldmap.interfaces.OfficeLocationsProvider;
import com.commandapps.helloworldmap.interfaces.UserLocationListener;
import com.commandapps.helloworldmap.interfaces.UserLocationProvider;
import com.commandapps.helloworldmap.model.OfficeLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Michael on 10/8/2014.
 */
public class LocationsMapFragment extends MapFragment implements OfficeLocationsChangedListener, UserLocationListener {

    private OfficeLocationsProvider officeLocationsProvider;
    private UserLocationProvider userLocationProvider;
    private int markerPadding = 100;
    private Map<Marker, OfficeLocation> markers;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        markers = new HashMap<Marker, OfficeLocation>();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            officeLocationsProvider = (OfficeLocationsProvider) activity;
            officeLocationsProvider.addOfficeLocationsChangedListener(this);
            userLocationProvider = (UserLocationProvider) activity;
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
        officeLocationsProvider = null;
        officeLocationsProvider = null;
    }


    @Override
    public void onOfficeLocationsChanged(final List<OfficeLocation> officeLocations) {
        GoogleMap map = getMap();

        for (OfficeLocation officeLocation : officeLocations) {
            Marker marker = map.addMarker(createmarkerOptions(officeLocation));
            markers.put(marker, officeLocation);
        }

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                OfficeLocation location = markers.get(marker);
                officeLocationsProvider.onOfficeLocationSelected(location);
            }
        });

        if (markers.size() > 0) {
            LatLngBounds latLngBounds = calculateLatLngBounds(markers);
            int width = ScreenUtil.getScreenWidth(getActivity());
            int height = ScreenUtil.getScreenHeight(getActivity());
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, width, height, markerPadding));

        }

    }

    private MarkerOptions createmarkerOptions(OfficeLocation officeLocation) {
        double lat = Double.parseDouble(officeLocation.getLatitude());
        double lng = Double.parseDouble(officeLocation.getLongitude());
        LatLng markerPosition = new LatLng(lat, lng);
        return new MarkerOptions().title(officeLocation.getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.hw_logo)).snippet(officeLocation.getAddress()).position(markerPosition);
    }

    private LatLngBounds calculateLatLngBounds(Map<Marker, OfficeLocation> markers) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers.keySet()) {
            builder.include(marker.getPosition());
        }

        return builder.build();

    }

    @Override
    public void onUserLocationChanged(Location userLocation) {

    }
}
