package cysrides.cysrides;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import domain.Offer;
import domain.Request;
import service.ActivityService;
import service.ActivityServiceImpl;
import service.Callback;
import service.NavigationService;
import service.NavigationServiceImpl;
import service.OfferService;
import service.OfferServiceImpl;
import service.RefreshService;
import service.RefreshServiceImpl;
import service.RequestService;
import service.RequestServiceImpl;
import service.UserIntentService;
import service.UserIntentServiceImpl;
import volley.OfferVolleyImpl;
import volley.RequestVolleyImpl;

public class RequestsOffers extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private UserIntentService userIntentService = new UserIntentServiceImpl();
    private NavigationService navigationService = new NavigationServiceImpl();
    private OfferService offerService = new OfferServiceImpl();
    private RequestService requestService = new RequestServiceImpl();
    private RefreshService refreshService = new RefreshServiceImpl();
    private ActivityService activityService = new ActivityServiceImpl();

    private Intent i;
    private SwipeRefreshLayout refresh;
    private ArrayAdapter<String> adapter;
    private List<Offer> offers = new ArrayList<>();
    private List<Request> requests = new ArrayList<>();
    private List<String> destinations = new ArrayList<>();
    private FragmentManager fragmentManager = this.getSupportFragmentManager();

    /**
     * Initializes app page
     * @param savedInstanceState - app data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_offers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.requests_offers_activity);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        navigationService.hideMenuItems(menu, userIntentService.getUserFromIntent(this.getIntent()));

        refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        refresh.setColorSchemeColors(ContextCompat.getColor(RequestsOffers.this,
                R.color.colorGold), ContextCompat.getColor(RequestsOffers.this, R.color.colorCardinal));
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOffersList();
                getRequestsList();
            }
        });
        getOffersList();
        getRequestsList();

        ListView listView = (ListView)findViewById(R.id.requests_offers_list);
        adapter = new ArrayAdapter<>(RequestsOffers.this, android.R.layout.simple_list_item_1, destinations);
        listView.setAdapter(adapter);

        if(navigationService.checkInternetConnection(RequestsOffers.this)) {
            connectionPopUp();
        }
    }

    /**
     * gets list of offers from the database
     */
    @SuppressWarnings("unchecked")
    public void getOffersList() {
        i = this.getIntent();
        OfferVolleyImpl volley = new OfferVolleyImpl(this, new Callback() {
            public void call(ArrayList<?> result) {
                try {
                    if (result.get(0) instanceof Offer) {
                        offers = (ArrayList<Offer>) result;
                    }
                } catch(Exception e) {
                    offers = new ArrayList<>();
                }

                ArrayList<Offer> o = new ArrayList<>();
                o.addAll(offers);
                offers = offerService.findOffersByEmail(o, userIntentService.getUserFromIntent(i));
                for(int i = 0; i < offers.size(); i++) {
                    destinations.add(offers.get(i).getDestination());
                }

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
     * gets list of requests from the database
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

                ArrayList<Request> r = new ArrayList<>();
                for(int i=0 ; i<requests.size() ; i++) {
                    r.add(requests.get(i));
                }
                requests = requestService.findRequestsByEmail(r, userIntentService.getUserFromIntent(i));

                for(int i = 0; i < requests.size(); i++) {
                    destinations.add(requests.get(i).getDestination());
                }

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
     * handles back button presses
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.requests_offers_activity);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        else {
            finish();
            i = new Intent(RequestsOffers.this, MainActivity.class);
            i = userIntentService.createIntent(RequestsOffers.this, MainActivity.class, userIntentService.getUserFromIntent(this.getIntent()));
            startActivity(i);
        }
    }

    /**
     * creates options menu
     * @param menu to be created
     * @return true on success
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_profile_button, menu);
        navigationService.hideAdminButton(menu, userIntentService.getUserFromIntent(this.getIntent()));
        return true;
    }

    /**
     * handles options menu selection
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
        if (id == R.id.my_profile) {
            i = userIntentService.createIntent(RequestsOffers.this, ViewProfile.class, userIntentService.getUserFromIntent(this.getIntent()));
            i.putExtra("caller", "Ride Offers");
            startActivity(i);
        } else if(id == R.id.admin_actions) {
            i = userIntentService.createIntent(RequestsOffers.this, AdminActions.class, userIntentService.getUserFromIntent(this.getIntent()));
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * handles navigation item selections
     * @param item selected
     * @return true on success
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        i = navigationService.getNavigationIntent(item, RequestsOffers.this, this.getIntent());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.requests_offers_activity);
        drawer.closeDrawer(GravityCompat.START);
        if(R.id.logout == id) {
            AlertDialog.Builder alert = navigationService.logOutButton(RequestsOffers.this);
            alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    SaveSharedPreference.clearUsernamePassword(RequestsOffers.this);
                    startActivity(i);
                }});
            alert.show();

            return true;
        }
        else if(navigationService.checkInternetConnection(RequestsOffers.this)) {
            connectionPopUp();
            return false;
        }
        else {
            startActivity(i);
            return true;
        }
    }

    /**
     * opens snackbar if wifi is not connected
     */
    public void connectionPopUp() {
        Snackbar snackbar = activityService.setupConnection(RequestsOffers.this, findViewById(R.id.contacts_activity));
        snackbar.show();
    }
}