package cysrides.cysrides;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import domain.UserInfo;
import service.ActivityService;
import service.ActivityServiceImpl;
import service.Callback;
import service.NavigationService;
import service.NavigationServiceImpl;
import service.UserIntentService;
import service.UserIntentServiceImpl;
import volley.UserRatingVolleyImpl;

public class ViewProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationService navigationService = new NavigationServiceImpl();
    private UserIntentService userIntentService = new UserIntentServiceImpl();
    private ActivityService activityService = new ActivityServiceImpl();

    private Intent i;
    UserInfo user;

    TextView testView;
    RatingBar userRatingBar;

    /**
     * When this activity is created, it initializes all the UI components to the values of the UserInfo
     * object.
     * @param savedInstanceState - app data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView netIDView = (TextView) findViewById(R.id.netIDView);
        TextView firstNameView = (TextView) findViewById(R.id.firstNameView);
        TextView lastNameView = (TextView) findViewById(R.id.lastNameView);
        TextView descriptionView = (TextView) findViewById(R.id.descriptionView);
        TextView venmoView = (TextView) findViewById(R.id.venmoView);
        TextView dateJoinedView = (TextView) findViewById(R.id.dateJoinedView);
        userRatingBar = (RatingBar) findViewById(R.id.userRatingBar);

        testView = (TextView) findViewById(R.id.testView);

        user = userIntentService.getUserFromIntent(this.getIntent());

        netIDView.setText(user.getNetID().split("@iastate.edu")[0]);
        firstNameView.setText(user.getFirstName());
        lastNameView.append(user.getLastName());
        descriptionView.setText(user.getProfileDescription());
        venmoView.append(user.getVenmoName());
        dateJoinedView.append(user.getDateJoined());

        getRatings(user.getNetID());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.my_profile);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        navigationService.hideMenuItems(menu, userIntentService.getUserFromIntent(this.getIntent()));

    }

    /**
     * handles back button presses
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.my_profile);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
            i = userIntentService.createIntent(ViewProfile.this, MainActivity.class, userIntentService.getUserFromIntent(this.getIntent()));
            startActivity(i);
        }
    }

    /**
     * Creates options menu
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
     * handles options menu selections
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
            i = userIntentService.createIntent(ViewProfile.this, ViewProfile.class, userIntentService.getUserFromIntent(this.getIntent()));
            i.putExtra("caller", "View Profile");
            startActivity(i);
        } else if(id == R.id.admin_actions) {
            i = userIntentService.createIntent(ViewProfile.this, AdminActions.class, userIntentService.getUserFromIntent(this.getIntent()));
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handles navigation item selections
     * @param item selected
     * @return true on success
     */
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        i = navigationService.getNavigationIntent(item, ViewProfile.this, this.getIntent());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.my_profile);
        drawer.closeDrawer(GravityCompat.START);
        if(R.id.logout == id) {
            AlertDialog.Builder alert = navigationService.logOutButton(ViewProfile.this);
            alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    SaveSharedPreference.clearUsernamePassword(ViewProfile.this);
                    startActivity(i);
                }});
            alert.show();

            return true;
        } else if (navigationService.checkInternetConnection(getApplicationContext())) {
            connectionPopUp();
            return false;
        } else {
            startActivity(i);
            return true;
        }
    }

    /**
     * opens snackbar if wifi is not connected
     */
    public void connectionPopUp() {
        Snackbar snackbar = activityService.setupConnection(this.getApplicationContext(), findViewById(R.id.my_profile));
        snackbar.show();
    }

    /**
     * returns user's rating from the database
     * @param netID - user's netID
     */
    @SuppressWarnings("unchecked")
    private void getRatings(final String netID) {
        UserRatingVolleyImpl volley = new UserRatingVolleyImpl(new Callback() {
            ArrayList<String> ratings;

            public void call(ArrayList<?> result) {
                try {
                    if(result.get(0) instanceof String) {
                        ratings = (ArrayList<String>) result;
                    }
                } catch(Exception e) {
                    ratings = new ArrayList<>();
                }

                for(int i = 0; i < ratings.size(); i++){
                    if(ratings.get(i).equals(netID)) {
                        if (ratings.get(i + 1) != null && ratings.get(i + 2) != null){
                            testView.append(ratings.get(i) + " " + ratings.get(i + 1) + " " + ratings.get(i + 2));
                            user.setUserRating(Float.valueOf(ratings.get(i + 1)));
                            userRatingBar.setRating(user.getUserRating());

                        }
                    }

                }

            }
        });
        volley.execute();
    }

}
