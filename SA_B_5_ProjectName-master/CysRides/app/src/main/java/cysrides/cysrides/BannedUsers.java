package cysrides.cysrides;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

import domain.Ban;
import service.ActivityService;
import service.ActivityServiceImpl;
import service.Callback;
import service.DrawerLock;
import service.NavigationService;
import service.NavigationServiceImpl;
import service.RefreshService;
import service.RefreshServiceImpl;
import service.UserIntentService;
import service.UserIntentServiceImpl;
import volley.BanVolleyImpl;

public class BannedUsers extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DrawerLock {

    private UserIntentService userIntentService = new UserIntentServiceImpl();
    private NavigationService navigationService = new NavigationServiceImpl();
    private RefreshService refreshService = new RefreshServiceImpl();
    private ActivityService activityService = new ActivityServiceImpl();

    private Intent i;
    private DrawerLayout drawer;
    private SwipeRefreshLayout refresh;
    private ArrayAdapter<String> adapter;
    private List<Ban> bans = new ArrayList<>();
    private ArrayList<String> bansString;
    private FragmentManager fragmentManager = this.getSupportFragmentManager();

    /**
     * Constructs page
     * @param savedInstanceState - app data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banned_users);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.banned_users_activity);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        navigationService.hideMenuItems(menu, userIntentService.getUserFromIntent(this.getIntent()));

        refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        refresh.setColorSchemeColors(ContextCompat.getColor(BannedUsers.this,
                R.color.colorGold), ContextCompat.getColor(BannedUsers.this, R.color.colorCardinal));
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBansList();
            }
        });
        getBansList();

        ListView listView = (ListView)findViewById(R.id.banned_users_list);
        adapter = new ArrayAdapter<>(BannedUsers.this, android.R.layout.simple_list_item_1, bansString);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                ViewBannedUser viewBannedUser = new ViewBannedUser();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                viewBannedUser.setData(bans.get(position));
                fragmentTransaction.replace(R.id.banned_users_activity, viewBannedUser);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        if(navigationService.checkInternetConnection(BannedUsers.this)) {
            connectionPopUp();
        }
    }

    /**
     * Gets banned users from database
     */
    @SuppressWarnings("unchecked")
    public void getBansList() {
        bansString = new ArrayList<>();
        BanVolleyImpl volley = new BanVolleyImpl(new Callback() {
            public void call(ArrayList<?> result) {
                try {
                    if (result.get(0) instanceof Ban) {
                        bans = (ArrayList<Ban>) result;
                    }
                } catch(Exception e) {
                    bans = new ArrayList<>();
                }

                adapter.clear();
                bansString.clear();
                for(int i = 0; i < bans.size(); i++) {
                    bansString.add(bans.get(i).getEmail() + " " + bans.get(i).getReason());
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
     * Handles back button press
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.banned_users_activity);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        else {
            finish();
            i = userIntentService.createIntent(BannedUsers.this, MainActivity.class, userIntentService.getUserFromIntent(this.getIntent()));
            startActivity(i);
        }
    }

    /**
     * Locks the drawer
     * @param enabled - true when drawer is locked
     */
    @Override
    public void lockDrawer(boolean enabled) {
        int lockMode = enabled ? DrawerLayout.LOCK_MODE_LOCKED_CLOSED : DrawerLayout.LOCK_MODE_UNLOCKED;
        drawer.setDrawerLockMode(lockMode);
    }

    /**
     * Creates the options menu
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
            i = userIntentService.createIntent(BannedUsers.this, ViewProfile.class, userIntentService.getUserFromIntent(this.getIntent()));
            i.putExtra("caller", "Ride Offers");
            startActivity(i);
        } else if(id == R.id.admin_actions) {
            i = userIntentService.createIntent(BannedUsers.this, AdminActions.class, userIntentService.getUserFromIntent(this.getIntent()));
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handles navigation item selections
     * @param item selected
     * @return true on success
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        i = navigationService.getNavigationIntent(item, BannedUsers.this, this.getIntent());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.banned_users_activity);
        drawer.closeDrawer(GravityCompat.START);
        if(R.id.logout == id) {
            AlertDialog.Builder alert = navigationService.logOutButton(BannedUsers.this);
            alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    startActivity(i);
                }});
            alert.show();

            return true;
        }
        else if(navigationService.checkInternetConnection(BannedUsers.this)) {
            connectionPopUp();
            return false;
        }
        else {
            startActivity(i);
            return true;
        }
    }

    /**
     * opens snackbar when no wifi connection
     */
    public void connectionPopUp() {
        Snackbar snackbar = activityService.setupConnection(BannedUsers.this, findViewById(R.id.contacts_activity));
        snackbar.show();
    }

}
