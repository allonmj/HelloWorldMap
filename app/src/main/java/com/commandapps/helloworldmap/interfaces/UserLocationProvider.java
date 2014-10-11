package com.commandapps.helloworldmap.interfaces;

/**
 * Created by Michael on 10/10/2014.
 */
public interface UserLocationProvider {
    public void addUserLocationListener(UserLocationListener listener);
    public void removeUserLocationListener(UserLocationListener listener);
}
