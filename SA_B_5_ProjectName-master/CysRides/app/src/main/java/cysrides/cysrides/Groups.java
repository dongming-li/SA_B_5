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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import domain.GOR;
import domain.UserInfo;
import service.ActivityService;
import service.ActivityServiceImpl;
import service.Callback;
import service.NavigationService;
import service.NavigationServiceImpl;
import service.UserIntentService;
import service.UserIntentServiceImpl;
import volley.GroupVolleyImpl;

public class Groups extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationService navigationService = new NavigationServiceImpl();
    private UserIntentService userIntentService = new UserIntentServiceImpl();
    private ActivityService activityService = new ActivityServiceImpl();

    private List<String> display = new ArrayList<>();
    private List<GOR> groups = new ArrayList<>();
    private List<GOR> gors = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private Intent i;

    /**
     * Initializes page to be displayed
     * @param savedInstanceState - Page info
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* set up page elements */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.groups_activity);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        navigationService.hideMenuItems(menu, userIntentService.getUserFromIntent(this.getIntent()));

        ListView listView;
        listView = (ListView)findViewById(R.id.groups_list);

        adapter = new ArrayAdapter<>(Groups.this, android.R.layout.simple_list_item_1, display);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /* go to group messaging page */
                i = navigationService.getNavigationIntent(navigationView.getMenu().getItem(navigationView.getMenu().size() - 1),
                        Groups.this, Groups.this.getIntent());
                i.putExtra("groupID", groups.get(position).getGroup().getId());
                startActivity(i);
            }
        });

        getGroupsList();

        if(navigationService.checkInternetConnection(Groups.this)) {
            connectionPopUp();
        }
    }

    /**
     * Method which notifies ride offer volley to pull ride offer data from database
     */
    @SuppressWarnings("unchecked")
    public void getGroupsList() {
        /* notify group volley to pull data */
        GroupVolleyImpl volley = new GroupVolleyImpl(Groups.this, new Callback() {
            public void call(ArrayList<?> result) {
                try {
                    if (result.get(0) instanceof GOR) {
                        gors = (ArrayList<GOR>) result;
                    }
                } catch (Exception e) {
                    gors = new ArrayList<>();
                    e.printStackTrace();
                }

                /* display data to user */
                UserInfo user = userIntentService.getUserFromIntent(Groups.this.getIntent());
                adapter.clear();
                display.clear();
                for (int i = 0; i < gors.size(); i++) {
                    if(gors.get(i).getGroup().inGroup(user.getNetID())) {
                        try {
                            display.add(gors.get(i).getOffer().getDestination() + " " + gors.get(i).getOffer().getDate());
                            groups.add(gors.get(i));
                        }
                        catch(Exception e) {
                            display.add(gors.get(i).getRequest().getDestination()  + " " + gors.get(i).getRequest().getDate());
                            groups.add(gors.get(i));
                        }
                    }
                }

                adapter.notifyDataSetChanged();
            }
        });
        volley.execute();
    }

    /**
     * Method that handles back button press
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.groups_activity);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
            i = userIntentService.createIntent(Groups.this, MainActivity.class, userIntentService.getUserFromIntent(this.getIntent()));
            startActivity(i);
        }
    }

    /**
     * Initializes options menu
     * @param menu to be built
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
     * Method to handle user's menu item selection
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
            i = userIntentService.createIntent(Groups.this, ViewProfile.class, userIntentService.getUserFromIntent(this.getIntent()));
            i.putExtra("caller", "Groups");
            startActivity(i);
        } else if(id == R.id.admin_actions) {
            i = userIntentService.createIntent(Groups.this, AdminActions.class, userIntentService.getUserFromIntent(this.getIntent()));
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
        i = navigationService.getNavigationIntent(item, Groups.this, this.getIntent());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.groups_activity);
        drawer.closeDrawer(GravityCompat.START);
        if(R.id.logout == id) {
            AlertDialog.Builder alert = navigationService.logOutButton(Groups.this);
            alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    SaveSharedPreference.clearUsernamePassword(Groups.this);
                    startActivity(i);
                }});
            alert.show();

            return true;
        }
        else if(navigationService.checkInternetConnection(Groups.this)) {
            connectionPopUp();
            return false;
        }
        else {
            startActivity(i);
            return true;
        }
    }

    /**
     * insert option to connect to wifi
     */
    public void connectionPopUp() {
        Snackbar snackbar = activityService.setupConnection(Groups.this, findViewById(R.id.groups_activity));
        snackbar.show();
    }
}
