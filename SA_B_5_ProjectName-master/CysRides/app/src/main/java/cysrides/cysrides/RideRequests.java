package cysrides.cysrides;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import domain.Request;
import service.ActivityService;
import service.ActivityServiceImpl;
import service.Callback;
import service.DrawerLock;
import service.NavigationService;
import service.NavigationServiceImpl;
import service.RefreshService;
import service.RefreshServiceImpl;
import service.SearchCallback;
import service.UserIntentService;
import service.UserIntentServiceImpl;
import volley.RequestVolleyImpl;

public class RideRequests extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DrawerLock {

    private UserIntentService userIntentService = new UserIntentServiceImpl();
    private NavigationService navigationService = new NavigationServiceImpl();
    private RefreshService refreshService = new RefreshServiceImpl();
    private ActivityService activityService = new ActivityServiceImpl();

    private Intent i;
    private DrawerLayout drawer;
    private SwipeRefreshLayout refresh;
    private ArrayAdapter<String> adapter;
    private List<Request> requests = new ArrayList<>();
    private List<String> destinations = new ArrayList<>();
    private TextView searchResult;
    private FragmentManager fragmentManager = this.getSupportFragmentManager();

    /**
     * Initializes page to be displayed
     * @param savedInstanceState page info
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_requests);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* initialize page input/output items */
        drawer = (DrawerLayout) findViewById(R.id.ride_requests_activity);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        navigationService.hideMenuItems(menu, userIntentService.getUserFromIntent(this.getIntent()));

        searchResult = (TextView) findViewById(R.id.search_result);

        /* initialize page refreshing to take input from user */
        refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        refresh.setColorSchemeColors(ContextCompat.getColor(RideRequests.this, R.color.colorGold),
                ContextCompat.getColor(RideRequests.this, R.color.colorCardinal));
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRequestsList();
            }
        });

        /* notify requests volley to pull from database */
        getRequestsList();

        /* display list of ride requests on screen */
        i = this.getIntent();
        ListView listView = (ListView)findViewById(R.id.ride_requests_list);
        adapter = new ArrayAdapter<>(RideRequests.this, android.R.layout.simple_list_item_1, destinations);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                /* notify fragment handler to display fragment to user */
                RideFragment viewRequest = new ViewRequest();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                viewRequest.setData(requests.get(position));
                viewRequest.setContext(RideRequests.this);
                viewRequest.setUserInfo(userIntentService.getUserFromIntent(i));

                fragmentTransaction.replace(R.id.ride_requests_activity, viewRequest);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        /* check for internet connection */
        if(navigationService.checkInternetConnection(RideRequests.this)) {
            connectionPopUp();
        }
    }

    /**
     * Method that notifies request volley to pull all ride request data from database
     */
    @SuppressWarnings("unchecked")
    public void getRequestsList() {
        RequestVolleyImpl volley = new RequestVolleyImpl(this, new Callback() {
            public void call(ArrayList<?> result) {
                try {
                    if (result.get(0) instanceof Request) {
                        requests = (ArrayList<Request>) result;
                    }
                } catch(Exception e) {
                    requests = new ArrayList<>();
                }

                /* display all data to user */
                adapter.clear();
                destinations.clear();
                for(int i = 0; i < requests.size(); i++) {
                    destinations.add(requests.get(i).getDestination());
                }

                /* stop refreshing page */
                if(refresh.isRefreshing()) {
                    refreshService.stopRefreshing(refresh, adapter);
                }
                else {
                    adapter.notifyDataSetChanged();
                }
            }
        });
        volley.execute();
    }

    /**
     * Method that handles back press
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.ride_requests_activity);

        /* close drawer */
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        /* close all fragments */
        else if(fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        /* close search results */
        else if (View.VISIBLE == searchResult.getVisibility()) {
            searchResult.setVisibility(View.GONE);
            getRequestsList();
        }
        else {
            /* return to main activity */
            finish();
            i = userIntentService.createIntent(RideRequests.this, MainActivity.class, userIntentService.getUserFromIntent(this.getIntent()));
            startActivity(i);
        }
    }

    /**
     * Method that locks side drawer when fragment open
     * @param enabled - true if drawer is locked
     */
    @Override
    public void lockDrawer(boolean enabled) {
        int lockMode = enabled ? DrawerLayout.LOCK_MODE_LOCKED_CLOSED : DrawerLayout.LOCK_MODE_UNLOCKED;
        drawer.setDrawerLockMode(lockMode);
    }

    /*
     * Method to handle user's menu item selection
     *
     * Param: selected item
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_and_search, menu);
        navigationService.hideAdminButton(menu, userIntentService.getUserFromIntent(this.getIntent()));
        return true;
    }

    /**
     * Handles selected item
     * @param item selected
     * @return true on success
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (R.id.my_profile == id) {
            i = userIntentService.createIntent(RideRequests.this, ViewProfile.class, userIntentService.getUserFromIntent(this.getIntent()));
            i.putExtra("caller", "Ride Requests");
            startActivity(i);
        } else if (R.id.search == id) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            RideSearch rideSearch = new RideSearch();
            rideSearch.setData(new SearchCallback() {
                @Override
                public void call(Place place) {
                    onBackPressed();
                    String display = "Rides near\n" + place.getName().toString();

                    onBackPressed();

                    if(filterResults(place)) {
                        searchResult.setText(display);
                        searchResult.setVisibility(View.VISIBLE);
                    }
                }
            });

            fragmentTransaction.replace(R.id.ride_requests_activity, rideSearch);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if(id == R.id.admin_actions) {
            i = userIntentService.createIntent(RideRequests.this, AdminActions.class, userIntentService.getUserFromIntent(this.getIntent()));
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * method to handle user's page navigation selection
     * @param item selected
     * @return true on success
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        i = navigationService.getNavigationIntent(item, RideRequests.this, this.getIntent());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.ride_requests_activity);
        drawer.closeDrawer(GravityCompat.START);
        if(R.id.logout == id) {
            AlertDialog.Builder alert = navigationService.logOutButton(RideRequests.this);
            alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    SaveSharedPreference.clearUsernamePassword(RideRequests.this);
                    startActivity(i);
                }});
            alert.show();

            return true;
        }
        else if(navigationService.checkInternetConnection(RideRequests.this)) {
            /* check for wifi connection */
            connectionPopUp();
            /* close drawer */
            return false;
        }
        else {
            /* close drawer and move to next activity */
            startActivity(i);
            return true;
        }
    }

    /**
     * insert option to connect to wifi
     */
    public void connectionPopUp() {
        Snackbar snackbar = activityService.setupConnection(RideRequests.this, findViewById(R.id.contacts_activity));
        snackbar.show();
    }

    /**
     * Filters ride offers based on search information
     * @param place - user's search info
     * @return true on success
     */
    public boolean filterResults(Place place) {
        List<Request> filtered = new ArrayList<>();
        List<String> destinations = new ArrayList<>();
        LatLng compare = place.getLatLng();

        for(int i = 0; i < requests.size(); i++) {
            float distance[] = new float[1];
            LatLng current = requests.get(i).getDestCoordinates();

            Location.distanceBetween(compare.latitude, compare.longitude,
                    current.latitude, current.longitude, distance);

            /* if distance is less than 15 miles away, add it to filtered destinations list */
            if(distance[0] <= 1600 * 15) {
                destinations.add(requests.get(i).getDestination());
                filtered.add(requests.get(i));
            }
        }

        /* if no destinations were found, return false */
        if(destinations.size() == 0) {
            Snackbar.make(findViewById(R.id.ride_requests_activity),
                    "No rides available for this location. You can try making a new offer.",
                    Snackbar.LENGTH_SHORT).show();
            return false;
        }

        /* display the filtered results */
        adapter.clear();
        for(int i = 0; i < destinations.size(); i++) {
            this.destinations.add(destinations.get(i));
        }
        this.requests = filtered;
        adapter.notifyDataSetChanged();

        return true;
    }

}