package com.commandapps.helloworldmap.fragments;

import android.app.Activity;

import com.commandapps.helloworldmap.R;
import com.commandapps.helloworldmap.interfaces.OfficeLocationChangedListener;
import com.commandapps.helloworldmap.interfaces.OfficeLocationProvider;
import com.commandapps.helloworldmap.model.OfficeLocation;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Michael on 10/11/2014.
 */
public class StaticLocationMapFragment extends MapFragment implements OfficeLocationChangedListener {
    private OfficeLocationProvider officeLocationProvider;

    @Override
    public void onOfficeLocationChanged(OfficeLocation officeLocation) {
        GoogleMap map = getMap();
        Marker marker = map.addMarker(createmarkerOptions(officeLocation));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(marker.getPosition()).zoom(14.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.moveCamera(cameraUpdate);
        UiSettings settings = map.getUiSettings();
        settings.setAllGesturesEnabled(false);
        settings.setMyLocationButtonEnabled(false);
        settings.setZoomControlsEnabled(false);
    }

    private MarkerOptions createmarkerOptions(OfficeLocation officeLocation) {
        double lat = Double.parseDouble(officeLocation.getLatitude());
        double lng = Double.parseDouble(officeLocation.getLongitude());
        LatLng markerPosition = new LatLng(lat, lng);
        return new MarkerOptions().title(officeLocation.getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)).snippet(officeLocation.getAddress()).position(markerPosition);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            officeLocationProvider = (OfficeLocationProvider) activity;
            officeLocationProvider.addOfficeLocationChangedListener(this);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
}
