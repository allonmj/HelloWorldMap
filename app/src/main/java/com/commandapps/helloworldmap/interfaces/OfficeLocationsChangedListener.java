package com.commandapps.helloworldmap.interfaces;

import com.commandapps.helloworldmap.model.OfficeLocation;

import java.util.List;

/**
 * Created by Michael on 10/9/2014.
 */
public interface OfficeLocationsChangedListener {
    public void onOfficeLocationsChanged(List<OfficeLocation> officeLocations);
}
