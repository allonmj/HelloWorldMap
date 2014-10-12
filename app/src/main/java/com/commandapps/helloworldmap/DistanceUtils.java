package com.commandapps.helloworldmap;

import android.location.Location;

import com.commandapps.helloworldmap.model.OfficeLocation;

import java.text.DecimalFormat;

/**
 * Created by Michael on 10/10/2014.
 */
public class DistanceUtils {

    private static double MILES_PER_METER = 0.000621371;

    public static double metersToMiles(float meters){
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        double miles = meters * MILES_PER_METER;
        return Double.valueOf(twoDForm.format(miles));
    }

    /**
     * Calculates the straight line distance in meeters between an office location
     * and a user location
     * @param officeLocation
     * @param userLocation
     * @return
     */
    public static float calculateDistanceMeters(OfficeLocation officeLocation, Location userLocation){
        Location targetLocation = new Location("");
        double lat = Double.parseDouble(officeLocation.getLatitude());
        double lng = Double.parseDouble(officeLocation.getLongitude());
        targetLocation.setLatitude(lat);
        targetLocation.setLongitude(lng);
        return userLocation.distanceTo(targetLocation);
    }
}
