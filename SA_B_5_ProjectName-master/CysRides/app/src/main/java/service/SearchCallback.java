package service;

import com.google.android.gms.location.places.Place;

public interface SearchCallback {

    /**
     * Returns user's selected location to caller
     * @param location that user selected
     */
    void call(Place location);
}
