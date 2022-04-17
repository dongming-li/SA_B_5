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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import domain.Message;
import domain.UserInfo;
import service.ActivityService;
import service.ActivityServiceImpl;
import service.Callback;
import service.NavigationService;
import service.NavigationServiceImpl;
import service.UserIntentService;
import service.UserIntentServiceImpl;
import volley.MessageVolleyImpl;

public class Messaging extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationService navigationService = new NavigationServiceImpl();
    private UserIntentService userIntentService = new UserIntentServiceImpl();
    private ActivityService activityService = new ActivityServiceImpl();

    private ArrayList<Message> messages = new ArrayList<>();
    private ArrayList<String> display = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private Intent i;
    private UserInfo user;
    private int groupID = 0;

    /**
     * Initializes page to be displayed
     * @param savedInstanceState page data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.messaging_activity);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        navigationService.hideMenuItems(menu, userIntentService.getUserFromIntent(this.getIntent()));

        user = userIntentService.getUserFromIntent(this.getIntent());

        groupID = this.getIntent().getIntExtra("groupID", 0);

        ListView listView;
        listView = (ListView)findViewById(R.id.messages_list);

        adapter = new ArrayAdapter<>(Messaging.this, android.R.layout.simple_list_item_1, display);
        listView.setAdapter(adapter);

        Button send = (Button)findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
                getMessages();
            }
        });

        getMessages();

        /* Make a timer to constantly check for new messages */
        Timer timer = new Timer ();
        TimerTask task = new TimerTask () {
            @Override
            public void run () {
                getMessages();
            }
        };

        /* schedule the task to run starting now and then every hour... */
        timer.schedule(task, 0, 5000);   // refresh every 5 seconds

        if(navigationService.checkInternetConnection(Messaging.this)) {
            connectionPopUp();
        }
    }

    /**
     * Sends message to database
     */
    public void sendMessage() {
        EditText message = (EditText)findViewById(R.id.message);
        String text = message.getText().toString() + "\nfrom " + user.getFirstName();

        /* Send the message */
        MessageVolleyImpl messageVolley = new MessageVolleyImpl();
        messageVolley.createMessage(Messaging.this, new Message(groupID, user.getNetID(), text));

        /* reset text window */
        message.setText("");
    }

    /**
     * Pulls messsages from database and updates display
     */
    @SuppressWarnings("unchecked")
    private void getMessages() {
        MessageVolleyImpl volley = new MessageVolleyImpl(new Callback() {
            @Override
            public void call(ArrayList<?> result) {
                try {
                    if (result.get(0) instanceof Message) {
                        messages = (ArrayList<Message>) result;
                    }
                }
                catch(Exception e) {
                    messages = new ArrayList<>();
                }

                /* display results to user */
                adapter.clear();
                display.clear();

                for(int i = 0; i < messages.size(); i++) {
                    display.add(messages.get(i).getMessage());
                }

                adapter.notifyDataSetChanged();
            }
        });
        volley.execute(groupID);
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
            i = userIntentService.createIntent(Messaging.this, ViewProfile.class, userIntentService.getUserFromIntent(this.getIntent()));
            i.putExtra("caller", "Messaging");
            startActivity(i);
        } else if(id == R.id.admin_actions) {
            i = userIntentService.createIntent(Messaging.this, AdminActions.class, userIntentService.getUserFromIntent(this.getIntent()));
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
        i = navigationService.getNavigationIntent(item, Messaging.this, this.getIntent());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.messaging_activity);
        drawer.closeDrawer(GravityCompat.START);
        if(R.id.logout == id) {
            AlertDialog.Builder alert = navigationService.logOutButton(Messaging.this);
            alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    SaveSharedPreference.clearUsernamePassword(Messaging.this);
                    startActivity(i);
                }});
            alert.show();

            return true;
        }
        else if(navigationService.checkInternetConnection(Messaging.this)) {
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
        Snackbar snackbar = activityService.setupConnection(Messaging.this, findViewById(R.id.messaging_activity));
        snackbar.show();
    }
}
