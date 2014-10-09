package com.commandapps.helloworldmap.fragments;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import com.commandapps.helloworldmap.interfaces.OfficeLocationsChangedListener;
import com.commandapps.helloworldmap.interfaces.OfficeLocationsProvider;
import com.commandapps.helloworldmap.model.OfficeLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 10/8/2014.
 */
public class LocationMapFragment extends MapFragment implements OfficeLocationsChangedListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener{

    private LocationClient locationClient;
    private Location currentLocation;
    private OfficeLocationsProvider mListener;
    private int markerPadding = 100;



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationClient = new LocationClient(getActivity(), this, this);
        locationClient.connect();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OfficeLocationsProvider) activity;
            mListener.addOfficeLocationsChangedListener(this);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.removeOfficeLocationsChangedListener(this);
        mListener = null;
    }

    @Override
    public void onStop() {
        locationClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        currentLocation = locationClient.getLastLocation();

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
 /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
//            try {
//                // Start an Activity that tries to resolve the error
////                connectionResult.startResolutionForResult(
////                        this,
////                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
//                /*
//                 * Thrown if Google Play services canceled the original
//                 * PendingIntent
//                 */
//            } catch (IntentSender.SendIntentException e) {
//                // Log the error
//                e.printStackTrace();
//            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
//            showErrorDialog(connectionResult.getErrorCode());
        }
    }

    @Override
    public void onOfficeLocationsChanged(List<OfficeLocation> officeLocations) {
        GoogleMap map = getMap();
        double latitude = currentLocation.getLatitude();
        double longitude = currentLocation.getLongitude();
        LatLng userLatLng = new LatLng(latitude, longitude);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 13));

        map.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(userLatLng));

        List<Marker> markers = new ArrayList<Marker>();
        for (OfficeLocation officeLocation : officeLocations){
            Marker marker = map.addMarker(createmarkerOptions(officeLocation));
            markers.add(marker);
        }
        LatLngBounds latLngBounds = calculateLatLngBounds(markers);
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, markerPadding));

    }

    private MarkerOptions createmarkerOptions(OfficeLocation officeLocation){
        double lat = Double.parseDouble(officeLocation.getLatitude());
        double lng = Double.parseDouble(officeLocation.getLongitude());
        LatLng markerPosition = new LatLng(lat, lng);
        return new MarkerOptions().title(officeLocation.getName()).snippet(officeLocation.getAddress()).position(markerPosition);
    }

    private LatLngBounds calculateLatLngBounds(List<Marker> markers){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers){
            builder.include(marker.getPosition());
        }
        return builder.build();

    }
}
