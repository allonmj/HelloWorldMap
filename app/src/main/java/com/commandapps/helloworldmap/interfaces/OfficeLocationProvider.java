package com.commandapps.helloworldmap.interfaces;

/**
 * Created by Michael on 10/10/2014.
 */
public interface OfficeLocationProvider {
    public void addOfficeLocationChangedListener(OfficeLocationChangedListener listener);
    public void removeOfficeLocationChangedListener(OfficeLocationChangedListener listener);
}
