package com.commandapps.helloworldmap.activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.commandapps.helloworldmap.OfficeLocationsLoader;
import com.commandapps.helloworldmap.R;
import com.commandapps.helloworldmap.interfaces.OfficeLocationsChangedListener;
import com.commandapps.helloworldmap.interfaces.OfficeLocationsProvider;
import com.commandapps.helloworldmap.model.OfficeLocation;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements OfficeLocationsProvider, LoaderManager.LoaderCallbacks<List<OfficeLocation>> {

    private ArrayList<OfficeLocationsChangedListener> officeLocationsChangedListeners;
    private List<OfficeLocation> officeLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLoaderManager().initLoader(0, null, this).forceLoad();

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
        startActivity(intent);
    }

    @Override
    public void addOfficeLocationsChangedListener(OfficeLocationsChangedListener listener){
        if (null == officeLocationsChangedListeners){
            officeLocationsChangedListeners = new ArrayList<OfficeLocationsChangedListener>();
        }
        officeLocationsChangedListeners.add(listener);
    }

    @Override
    public void removeOfficeLocationsChangedListener(OfficeLocationsChangedListener listener){
        if (null != officeLocationsChangedListeners){
            officeLocationsChangedListeners.remove(listener);
        }
    }

    private void notifyOfficeLocationsChanged(){
        for (OfficeLocationsChangedListener listener : officeLocationsChangedListeners){
            listener.onOfficeLocationsChanged(officeLocations);
        }
    }

    @Override
    public Loader<List<OfficeLocation>> onCreateLoader(int i, Bundle bundle) {
        return new OfficeLocationsLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<OfficeLocation>> listLoader, List<OfficeLocation> officeLocations) {
        this.officeLocations = officeLocations;
        notifyOfficeLocationsChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<OfficeLocation>> listLoader) {

    }

}
