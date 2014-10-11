package com.commandapps.helloworldmap.activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.commandapps.helloworldmap.DistanceUtils;
import com.commandapps.helloworldmap.OfficeLocationsLoader;
import com.commandapps.helloworldmap.R;
import com.commandapps.helloworldmap.StorageUtil;
import com.commandapps.helloworldmap.interfaces.OfficeLocationsChangedListener;
import com.commandapps.helloworldmap.interfaces.OfficeLocationsProvider;
import com.commandapps.helloworldmap.interfaces.UserLocationListener;
import com.commandapps.helloworldmap.interfaces.UserLocationProvider;
import com.commandapps.helloworldmap.model.OfficeLocation;
import com.commandapps.helloworldmap.model.OfficeLocations;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends Activity implements OfficeLocationsProvider, UserLocationProvider, LoaderManager.LoaderCallbacks<List<OfficeLocation>>, GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    private ArrayList<OfficeLocationsChangedListener> officeLocationsChangedListeners;
    private ArrayList<UserLocationListener> userLocationListeners;
    private List<OfficeLocation> officeLocations;

    private LocationClient locationClient;
    private Location userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationClient = new LocationClient(this, this, this);
        getLoaderManager().initLoader(0, null, this).forceLoad();

    }

    @Override
    protected void onStart() {
        super.onStart();
        locationClient.connect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOfficeLocationSelected(OfficeLocation officeLocation) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(OfficeLocation.TAG, officeLocation);
        if (null != userLocation) {
            intent.putExtra("USER_LOCATION", userLocation);
        }
        startActivity(intent);
    }

    @Override
    public void addOfficeLocationsChangedListener(OfficeLocationsChangedListener listener) {
        if (null == officeLocationsChangedListeners) {
            officeLocationsChangedListeners = new ArrayList<OfficeLocationsChangedListener>();
        }
        officeLocationsChangedListeners.add(listener);
        if (officeLocations != null) {
            // immediately return the office locations if available
            listener.onOfficeLocationsChanged(officeLocations);
        }
    }

    @Override
    public void removeOfficeLocationsChangedListener(OfficeLocationsChangedListener listener) {
        if (null != officeLocationsChangedListeners) {
            officeLocationsChangedListeners.remove(listener);
        }
    }

    private void notifyOfficeLocationsChanged() {
        for (OfficeLocationsChangedListener listener : officeLocationsChangedListeners) {
            listener.onOfficeLocationsChanged(officeLocations);
        }
    }

    @Override
    public Loader<List<OfficeLocation>> onCreateLoader(int i, Bundle bundle) {
        return new OfficeLocationsLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<OfficeLocation>> listLoader, List<OfficeLocation> officeLocations) {
        if (officeLocations.isEmpty()){
            // check if we have office location data in shared prefs.
            String officeLocationsStr = StorageUtil.getStringFromPreferences(this, StorageUtil.OFFICE_LOCATION_JSON_TAG);
            Gson gson = new Gson();
            OfficeLocations locs = gson.fromJson(officeLocationsStr, OfficeLocations.class);
            officeLocations = Arrays.asList(locs.getLocations());
        }
        this.officeLocations = officeLocations;
        notifyOfficeLocationsChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<OfficeLocation>> listLoader) {

    }

    @Override
    public void onStop() {
        locationClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        userLocation = locationClient.getLastLocation();
        notifyUserLocationChanged();
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

    private void notifyUserLocationChanged() {
        for (UserLocationListener listener : userLocationListeners) {
            listener.onUserLocationChanged(userLocation);
        }
    }

    @Override
    public void addUserLocationListener(UserLocationListener listener) {
        if (null == userLocationListeners) {
            userLocationListeners = new ArrayList<UserLocationListener>();
        }
        userLocationListeners.add(listener);
        if (userLocationListeners != null) {
            listener.onUserLocationChanged(userLocation);
        }
    }

    @Override
    public void removeUserLocationListener(UserLocationListener listener) {
        if (null != userLocationListeners) {
            userLocationListeners.remove(listener);
        }
    }
}
