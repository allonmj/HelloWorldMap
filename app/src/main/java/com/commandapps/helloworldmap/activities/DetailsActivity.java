package com.commandapps.helloworldmap.activities;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.commandapps.helloworldmap.R;
import com.commandapps.helloworldmap.interfaces.OfficeLocationChangedListener;
import com.commandapps.helloworldmap.interfaces.OfficeLocationProvider;
import com.commandapps.helloworldmap.interfaces.UserLocationListener;
import com.commandapps.helloworldmap.interfaces.UserLocationProvider;
import com.commandapps.helloworldmap.model.OfficeLocation;

import java.util.ArrayList;

public class DetailsActivity extends Activity implements OfficeLocationProvider, UserLocationProvider {

    private OfficeLocation officeLocation;
    private Location userLocation;
    private ArrayList<OfficeLocationChangedListener> officeLocationChangedListeners;
    private ArrayList<UserLocationListener> userLocationListeners;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        if (intent != null) {
            if(intent.hasExtra(OfficeLocation.TAG)) {
                officeLocation = intent.getParcelableExtra(OfficeLocation.TAG);
                notifyOfficeLocationsChanged();
            }
            if (intent.hasExtra("USER_LOCATION")){
                userLocation = intent.getParcelableExtra(("USER_LOCATION"));
                notifyUserLocationChanged();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details, menu);
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

    private void notifyOfficeLocationsChanged() {
        for (OfficeLocationChangedListener listener : officeLocationChangedListeners) {
            listener.onOfficeLocationChanged(officeLocation);
        }
    }

    private void notifyUserLocationChanged(){
        for (UserLocationListener listener : userLocationListeners){
            listener.onUserLocationChanged(userLocation);
        }
    }

    @Override
    public void addOfficeLocationChangedListener(OfficeLocationChangedListener listener) {
        if (null == officeLocationChangedListeners) {
            officeLocationChangedListeners = new ArrayList<OfficeLocationChangedListener>();
        }
        officeLocationChangedListeners.add(listener);
        if (officeLocation != null) {
            // immediately return the office locations if available
            listener.onOfficeLocationChanged(officeLocation);
        }
    }

    @Override
    public void removeOfficeLocationChangedListener(OfficeLocationChangedListener listener) {
        if (null != listener) {
            officeLocationChangedListeners.remove(listener);
        }
    }

    @Override
    public void addUserLocationListener(UserLocationListener listener) {
        if (null == userLocationListeners) {
            userLocationListeners = new ArrayList<UserLocationListener>();
        }
        userLocationListeners.add(listener);
        if (userLocation != null) {
            listener.onUserLocationChanged(userLocation);
        }
    }

    @Override
    public void removeUserLocationListener(UserLocationListener listener) {
        if (null != listener) {
            userLocationListeners.remove(listener);
        }
    }
}
