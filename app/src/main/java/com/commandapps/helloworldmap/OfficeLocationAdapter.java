package com.commandapps.helloworldmap;

import android.content.Context;
import android.location.Location;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.commandapps.helloworldmap.model.OfficeLocation;
import com.commandapps.helloworldmap.views.LocationRowView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Michael on 10/8/2014.
 */
public class OfficeLocationAdapter extends ArrayAdapter<OfficeLocation> {
    private final Context context;

    public OfficeLocationAdapter(Context context, int resource, List<OfficeLocation> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null){
            convertView = new LocationRowView(context);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
            holder.tvDistance = (TextView) convertView.findViewById(R.id.tvDistance);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        OfficeLocation officeLocation = getItem(position);
        holder.tvAddress.setText(officeLocation.getAddress() + "\n" + officeLocation.getAddress2());
        holder.tvName.setText(officeLocation.getName());
        float lastKnownLocation = officeLocation.getLastKnownDistance();
        if (lastKnownLocation > 0) {
            double miles = DistanceUtils.metersToMiles(lastKnownLocation);
            holder.tvDistance.setText(miles + " mi");
        }


        return convertView;
    }

    private class ViewHolder{
        TextView tvName, tvAddress, tvDistance;
    }
}