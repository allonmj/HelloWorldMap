package com.commandapps.helloworldmap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.commandapps.helloworldmap.model.OfficeLocation;

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
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_office_location, parent, false);
        TextView tvAddress = (TextView) rowView.findViewById(R.id.tvAdress);
        TextView tvName = (TextView) rowView.findViewById(R.id.tvName);
        OfficeLocation officeLocation = getItem(position);
        tvAddress.setText(officeLocation.getAddress());
        tvName.setText(officeLocation.getName());

        return rowView;
    }
}