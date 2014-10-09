package com.commandapps.helloworldmap.interfaces;

import com.commandapps.helloworldmap.model.OfficeLocation;

/**
 * Created by Michael on 10/9/2014.
 */
public interface OfficeLocationsProvider {
    public void addOfficeLocationsChangedListener(OfficeLocationsChangedListener listener);
    public void removeOfficeLocationsChangedListener(OfficeLocationsChangedListener listener);
    public void onOfficeLocationSelected(OfficeLocation officeLocation);
}
