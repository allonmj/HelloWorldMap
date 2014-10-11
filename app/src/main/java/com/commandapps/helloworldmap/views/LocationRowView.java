package com.commandapps.helloworldmap.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.commandapps.helloworldmap.R;

/**
 * Created by Michael on 10/10/2014.
 */
public class LocationRowView extends RelativeLayout{

    private TextView tvName, tvAddress, tvDistanceLabel, tvDistance;
    private String name = "";
    private String address = "";
    private String distanceLabel = "";
    private String distance = "";

    public LocationRowView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.view_location_info, this);
    }

    public LocationRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public LocationRowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.LocationRowView, 0, 0);
        try {
            // get the text and colors specified using the names in attrs.xml
            name = a.getString(R.styleable.LocationRowView_name);
            address = a.getString(R.styleable.LocationRowView_address);
            distanceLabel = a.getString(R.styleable.LocationRowView_distanceLabel);
            distance = a.getString(R.styleable.LocationRowView_distance);


        } finally {
            a.recycle();
        }

        LayoutInflater.from(context).inflate(R.layout.view_location_info, this);

        tvName = (TextView) this.findViewById(R.id.tvName);
        tvName.setText(name);

        tvAddress = (TextView) this.findViewById(R.id.tvAddress);
        tvAddress.setText(address);

        tvDistanceLabel = (TextView) this.findViewById(R.id.tvDistanceLabel);
        tvDistanceLabel.setText(distanceLabel);

        tvDistance = (TextView) this.findViewById(R.id.tvDistance);
        tvDistance.setText(distance);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        if (tvName!=null){
            tvName.setText(name);
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        if (tvAddress!=null){
            tvAddress.setText(address);
        }
    }

    public String getDistanceLabel() {
        return distanceLabel;
    }

    public void setDistanceLabel(String distanceLabel) {
        this.distanceLabel = distanceLabel;
        if (tvDistanceLabel!=null){
            tvDistanceLabel.setText(distanceLabel);
        }
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
        if (tvDistance!=null){
            tvDistance.setText(distance);
        }
    }
}
