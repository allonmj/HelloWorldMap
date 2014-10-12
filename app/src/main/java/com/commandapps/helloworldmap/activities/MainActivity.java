package com.commandapps.helloworldmap.activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends Activity implements OfficeLocationsProvider, UserLocationProvider, LoaderManager.LoaderCallbacks<List<OfficeLocation>>, GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener, SlidingUpPanelLayout.PanelSlideListener {

    private ArrayList<OfficeLocationsChangedListener> officeLocationsChangedListeners;
    private ArrayList<UserLocationListener> userLocationListeners;
    private List<OfficeLocation> officeLocations;
    private OfficeLocationsLoader loader;

    private LocationClient locationClient;
    private Location userLocation;
    private MenuItem refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        locationClient = new LocationClient(this, this, this);
        getLoaderManager().initLoader(0, null, this).forceLoad();
        SlidingUpPanelLayout slidingPaneLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        slidingPaneLayout.setPanelSlideListener(this);
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
        refreshButton = menu.findItem(R.id.action_refresh);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_refresh:
                if (loader != null) {
                    setProgressBarIndeterminateVisibility(Boolean.TRUE);
                    loader.forceLoad();
                }
                break;
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
        setProgressBarIndeterminateVisibility(Boolean.TRUE);
        loader = new OfficeLocationsLoader(this);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<OfficeLocation>> listLoader, List<OfficeLocation> officeLocations) {
        setProgressBarIndeterminateVisibility(Boolean.FALSE);
        if (officeLocations.isEmpty()) {
            // check if we have office location data in shared prefs.
            String officeLocationsStr = StorageUtil.getStringFromPreferences(this, StorageUtil.OFFICE_LOCATION_JSON_TAG);
            Gson gson = new Gson();
            OfficeLocations locs = gson.fromJson(officeLocationsStr, OfficeLocations.class);
            officeLocations = Arrays.asList(locs.getLocations());
        }
        this.officeLocations = officeLocations;
        notifyOfficeLocationsChanged();
        // cheating here a little.  By now all fragments have probably loaded, so we can get their views.
        setDragView();
    }

    /**
     * Sets the drag view for the sliding up panel.  This will allow list view scrolling without accidentally closing
     * the panel.
     */
    private void setDragView() {
        SlidingUpPanelLayout slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        View dragView = findViewById(R.id.rl_bottom_panel);
        if (null != slidingUpPanelLayout && null != dragView) {
            slidingUpPanelLayout.setDragView(dragView);
        }
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
        Toast.makeText(this, getString(R.string.google_play_services_error), Toast.LENGTH_LONG).show();
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

    @Override
    public void onPanelSlide(View view, float v) {

    }

    @Override
    public void onPanelCollapsed(View view) {
        TextView tvPanelLabel = (TextView) view.findViewById(R.id.tvPanelLabel);
        tvPanelLabel.setText(getString(R.string.locations));
        ImageView ivPanel = (ImageView) view.findViewById(R.id.ivPanel);
        ivPanel.setImageResource(R.drawable.ic_action_navigation_collapse);
    }

    @Override
    public void onPanelExpanded(View view) {
        TextView tvPanelLabel = (TextView) view.findViewById(R.id.tvPanelLabel);
        tvPanelLabel.setText(getString(R.string.map));
        ImageView ivPanel = (ImageView) view.findViewById(R.id.ivPanel);
        ivPanel.setImageResource(R.drawable.ic_action_navigation_expand);
    }

    @Override
    public void onPanelAnchored(View view) {

    }

    @Override
    public void onPanelHidden(View view) {

    }

    @Override
    public void onBackPressed() {
        SlidingUpPanelLayout slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        if (slidingUpPanelLayout.isPanelExpanded()) {
            slidingUpPanelLayout.collapsePanel();
        } else {
            super.onBackPressed();
        }
    }
}
