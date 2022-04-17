package cysrides.cysrides;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;

import service.FragmentImpl;
import service.SearchCallback;

public class RideSearch extends FragmentImpl {

    private SearchCallback callback;
    private AutocompleteFilter autocompleteFilter;

    /**
     * Required empty public constructor
     */
    public RideSearch() {
        // Required empty public constructor
    }

    /**
     * initializes data to be displayed
     * @param inflater inflates the fragment
     * @param container fragment view
     * @param savedInstanceState app info
     * @return fragment view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ride_search, container, false);
        setAdminTextInfo(v);
        return v;
    }

    /**
     * Sets callback to return user's search information
     * @param callback - determines where to send data back to
     * @param <T> SearchCallback
     */
    @Override
    public <T> void setData(T callback) {
        this.callback = (SearchCallback) callback;
    }

    /**
     * set text to be displayed for non-admins
     * @param v view to be set
     */
    @Override
    protected void setNonAdminTextInfo(View v) {
        SupportPlaceAutocompleteFragment placeAutoComplete = new SupportPlaceAutocompleteFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_ride_search, placeAutoComplete);
        fragmentTransaction.commit();

        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_COUNTRY)
                .setCountry("US")
                .build();

        placeAutoComplete.setFilter(autocompleteFilter);
        placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                callback.call(place);
            }

            @Override
            public void onError(Status status) {
                Log.d("Maps", "An error occurred: " + status);
            }
        });
    }

    /**
     * set text to be displayedf for admins
     * @param v view to be set
     */
    @Override
    protected void setAdminTextInfo(View v) {
        setNonAdminTextInfo(v);
    }
}
