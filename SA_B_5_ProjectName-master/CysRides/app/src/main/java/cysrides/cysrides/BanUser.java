package cysrides.cysrides;

import android.app.AlertDialog;
import android.content.Context;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import domain.Ban;
import service.ActivityService;
import service.ActivityServiceImpl;
import service.NavigationService;
import service.NavigationServiceImpl;
import service.UserIntentService;
import service.UserIntentServiceImpl;
import volley.BanVolleyImpl;

public class BanUser extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private UserIntentService userIntentService = new UserIntentServiceImpl();
    private ActivityService activityService = new ActivityServiceImpl();

    private String email, reason;
    private Intent i;
    private BanVolleyImpl banVolley = new BanVolleyImpl();
    private NavigationService navigationService = new NavigationServiceImpl();
    boolean retValue;

    /**
     * Creates page features
     * @param savedInstanceState - app info
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ban_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.ban_user_activity);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        navigationService.hideMenuItems(menu, userIntentService.getUserFromIntent(this.getIntent()));

        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText data = (EditText) findViewById(R.id.Email);
                email = data.getText().toString();
                data = (EditText) findViewById(R.id.Reason);
                reason = data.getText().toString();

                if (email == null) {
                    Snackbar.make(findViewById(R.id.submit), "All data fields required", Snackbar.LENGTH_LONG).show();
                } else {
                    Ban ban = new Ban(email, reason);
                    banVolley.createBan(BanUser.this, ban);

                    /* Refresh the page */
                    finish();
                    startActivity(getIntent());
                }
            }
        });

        if(navigationService.checkInternetConnection(BanUser.this)) {
            connectionPopUp();
        }
    }

    /**
     * Handlse back button presses
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.ban_user_activity);
        i = userIntentService.createIntent(BanUser.this, MainActivity.class, userIntentService.getUserFromIntent(this.getIntent()));
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Discard Request");
            alert.setMessage("This will discard your current offer. Continue anyway?");
            alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    finish();
                    startActivity(i);
                }});
            alert.setNegativeButton(android.R.string.no, null);
            alert.show();
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
        return true;
    }

    /**
     * Handles menu selections
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
            i = userIntentService.createIntent(BanUser.this, ViewProfile.class, userIntentService.getUserFromIntent(this.getIntent()));
            i.putExtra("caller", "Ride Offers");
            startActivity(i);
        } else if(id == R.id.admin_actions) {
            i = userIntentService.createIntent(BanUser.this, AdminActions.class, userIntentService.getUserFromIntent(this.getIntent()));
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handles navigation item selection
     * @param item selected
     * @return true on success
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        i = this.getIntent();
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Discard Offer");
        alert.setMessage("This will discard your current offer. Continue anyway?");
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                i = navigationService.getNavigationIntent(item, BanUser.this, i);

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.ban_user_activity);
                drawer.closeDrawer(GravityCompat.START);
                if (R.id.logout == id) {
                    AlertDialog.Builder alert = navigationService.logOutButton(BanUser.this);
                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            startActivity(i);
                        }
                    });
                    alert.show();

                    retValue = true;
                } else if (navigationService.checkInternetConnection(BanUser.this)) {
                    connectionPopUp();
                    retValue = false;
                } else {
                    startActivity(i);
                    retValue = true;
                }
            }
        });
        alert.setNegativeButton(android.R.string.no, null);
        alert.show();

        return retValue;
    }

    /**
     * opens snackbar when no wifi connection
     */
    public void connectionPopUp() {
        Snackbar snackbar = activityService.setupConnection(BanUser.this, findViewById(R.id.contacts_activity));
        snackbar.show();
    }
}
