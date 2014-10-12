package com.commandapps.helloworldmap.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.commandapps.helloworldmap.R;
import com.commandapps.helloworldmap.interfaces.OfficeLocationChangedListener;
import com.commandapps.helloworldmap.interfaces.OfficeLocationProvider;
import com.commandapps.helloworldmap.interfaces.UserLocationListener;
import com.commandapps.helloworldmap.interfaces.UserLocationProvider;
import com.commandapps.helloworldmap.model.OfficeLocation;
import com.commandapps.helloworldmap.views.ActionButtonView;

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
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if (intent != null) {
            if(intent.hasExtra(OfficeLocation.TAG)) {
                officeLocation = intent.getParcelableExtra(OfficeLocation.TAG);
                initActionButtons(officeLocation);
                actionBar.setTitle(officeLocation.getName());
                notifyOfficeLocationsChanged();
            }
            if (intent.hasExtra("USER_LOCATION")){
                userLocation = intent.getParcelableExtra(("USER_LOCATION"));
                notifyUserLocationChanged();
            }
        }
    }

    private void initActionButtons(final OfficeLocation location) {

        ActionButtonView directions = (ActionButtonView) findViewById(R.id.buttonDirections);
        ActionButtonView phoneCall = (ActionButtonView) findViewById(R.id.buttonCall);
        phoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchDialer(location.getPhone());
            }
        });
        directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchDirections(location);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details, menu);
        return true;
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

    private void launchDialer(String phoneNumber){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    private void launchDirections(OfficeLocation location){
        String city = getCityStringHack(location);
        String addr = location.getAddress() + ", " + location.getAddress2();
        if (city.length()>0){
            addr += ", " + city;
        }
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr="+addr));
        startActivity(intent);
    }

    /**
     * This is a hack for now to get the city where the address is, since that's not provided.
     * The location's have names which contain their city. We check for those names and return it.
     * We could use latitude and longitude, but those numbers turned out to be inaccurate and showed
     * a false address.
     * @param location
     * @return
     */
    private String getCityStringHack(OfficeLocation location) {
        String name = location.getName();
        if (name.toUpperCase().contains("NEW YORK")){
            return "New York";
        }else if (name.toUpperCase().contains("CHICAGO")){
            return "Chicago";
        }else if (name.toUpperCase().contains("NASHVILLE")){
            return "Nashville";
        }else if (name.toUpperCase().contains("DETROIT")){
            return "Detroit";
        }else if (name.toUpperCase().contains("PHOENIX")){
            return "Phoenix";
        }else if (name.toUpperCase().contains("SEATTLE")){
            return "Seatle";
        }else if (name.toUpperCase().contains("LOS ANGELES")){
            return "Los Angeles";
        }
        // if we can't find the name of the city, return empty string.  It still might work fine
        // on google maps.
        return "";
    }
}
