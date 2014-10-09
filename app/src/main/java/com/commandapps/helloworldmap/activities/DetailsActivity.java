package com.commandapps.helloworldmap.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.commandapps.helloworldmap.R;
import com.commandapps.helloworldmap.fragments.OfficeLocationDetailsFragment;
import com.commandapps.helloworldmap.model.OfficeLocation;

public class DetailsActivity extends Activity implements OfficeLocationDetailsFragment.OfficeLocationDetailsProvider{

    private OfficeLocation officeLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        if (intent!=null && intent.hasExtra(OfficeLocation.TAG)) {
            officeLocation = intent.getParcelableExtra(OfficeLocation.TAG);
            OfficeLocationDetailsFragment officeLocationDetailsFragment = (OfficeLocationDetailsFragment) getFragmentManager().findFragmentById(R.id.fragment_office_location_details);
            officeLocationDetailsFragment.updateView(officeLocation);
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

    @Override
    public OfficeLocation getOfficeLocation() {
        return officeLocation;
    }
}
