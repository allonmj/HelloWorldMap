package com.commandapps.helloworldmap;

import android.content.Context;
import android.location.Location;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.commandapps.helloworldmap.model.OfficeLocation;
import com.commandapps.helloworldmap.views.LocationRowView;

import java.util.List;

/**
 * Created by Michael on 10/8/2014.
 */
public class OfficeLocationAdapter extends ArrayAdapter<OfficeLocation> {
    private final Context context;
    private Location userLocation;

    public OfficeLocationAdapter(Context context, int resource, List<OfficeLocation> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    public void setUserLocation(Location userLocation){
        this.userLocation = userLocation;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = new LocationRowView(context);
        TextView tvAddress = (TextView) rowView.findViewById(R.id.tvAddress);
        TextView tvName = (TextView) rowView.findViewById(R.id.tvName);
        TextView tvDistance = (TextView) rowView.findViewById(R.id.tvDistance);
        OfficeLocation officeLocation = getItem(position);
        tvAddress.setText(officeLocation.getAddress());
        tvName.setText(officeLocation.getName());
        if (userLocation!=null){
            float metersDistance = DistanceUtils.calculateDistanceMeters(officeLocation, userLocation);
            tvDistance.setText(DistanceUtils.metersToMiles(metersDistance) + " mi");

        }

        return rowView;
    }
}