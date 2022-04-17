package cysrides.cysrides;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import domain.Request;
import service.ActivityService;
import service.ActivityServiceImpl;
import service.NavigationService;
import service.NavigationServiceImpl;
import service.RequestService;
import service.RequestServiceImpl;
import service.UserIntentService;
import service.UserIntentServiceImpl;

public class CreateRequest extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private UserIntentService userIntentService = new UserIntentServiceImpl();
    private ActivityService activityService = new ActivityServiceImpl();

    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timeSetListener;
    private Place destination;
    private Place start;
    private int year, month, day, hour, minute, seconds;
    private boolean dateChanged = false;
    private String description;
    private int numBags;
    private Intent i;
    private RequestService requestService = new RequestServiceImpl();
    private NavigationService navigationService = new NavigationServiceImpl();
    private boolean retValue;
    private final static int RQS_1 = 1;

    /**
     * Initializes page
     * @param savedInstanceState - app data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.create_request_activity);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        navigationService.hideMenuItems(menu, userIntentService.getUserFromIntent(this.getIntent()));

        PlaceAutocompleteFragment destinationAutoComplete = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.request_destination_autocomplete);
        destinationAutoComplete.setHint("Where are you going?");
        destinationAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                destination = place;
                Log.d("Maps", "Place selected: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                Log.d("Maps", "An error occurred: " + status);
            }
        });

        PlaceAutocompleteFragment startAutoComplete = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.request_start_autocomplete);
        startAutoComplete.setHint("Where are you leaving from?");
        startAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                start = place;
                Log.d("Maps", "Place selected: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                Log.d("Maps", "An error occurred: " + status);
            }
        });

        EditText displayDate = (EditText) findViewById(R.id.LeaveDate);
        displayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateRequest.this,
                        android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth,
                        dateSetListener, year, month, day);

                datePickerDialog.setCancelable(false);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                            if(!dateChanged) {
                                year = 0;
                                month = 0;
                                day = 0;
                            }
                        }
                    }
                });
                if(null != datePickerDialog.getWindow()) {
                    datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                datePickerDialog.show();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                month = m + 1;
                day = d;
                year = y;
                Log.d("CreateOffer", "onDateSet date: " + month + "/" + day + "/" + year);
                String date = month + "/" + day + "/" + year;
                EditText editText = (EditText)findViewById(R.id.LeaveDate);
                editText.setText(date);
                dateChanged = true;
            }
        };

        EditText displayTime = (EditText) findViewById(R.id.LeaveTime);
        displayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();

                hour = cal.get(Calendar.HOUR_OF_DAY);
                minute = cal.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateRequest.this, android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth,
                        timeSetListener, hour, minute, false);


                timePickerDialog.setCancelable(false);

                timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                            if(!dateChanged) {
                                minute = 0;
                                hour = 0;
                            }
                        }
                    }
                });
                if(null != timePickerDialog.getWindow()) {
                    timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                timePickerDialog.show();
            }
        });

        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int h, int m) {
                hour = h;
                minute = m;
                seconds = 0;
                Log.d("CreateOffer", "onTimeSet date: " + String.format("%02d", hour) + ":" + String.format("%02d", minute) + ":" + String.format("%02d", seconds));
                String time = String.format(Locale.US, "%02d", hour) + ":" + String.format(Locale.US, "%02d", minute) + ":" + String.format(Locale.US, "%02d", seconds);
                EditText editText = (EditText)findViewById(R.id.LeaveTime);
                editText.setText(time);
            }
        };

        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkInput()) {

                    GregorianCalendar gc = new GregorianCalendar();
                    gc.set(year, (month - 1), day, hour, minute, seconds);

                    Request r = new Request(numBags, userIntentService.getUserFromIntent(
                            getIntent()).getNetID(), (String) destination.getName(),
                            destination.getLatLng(), (String) start.getName(), start.getLatLng(),
                            description, gc.getTime());
                    requestService.createRequest(CreateRequest.this, r);

                    setAlarm(gc);

                    gc.set(year, (month - 1), day, (hour - 1), minute, seconds);

                    Calendar now = Calendar.getInstance();

                    if(gc.getTime().after(now.getTime())){
                        setAlarm(gc);
                    }

                    finish();
                    startActivity(getIntent());
                    Log.d("Date", Long.toString(r.getDate().getTime()));
                }
            }
        });

        if(navigationService.checkInternetConnection(CreateRequest.this)) {
            connectionPopUp();
        }
    }

    /**
     * set user's alarm
     * @param targetCal - user's calendar
     */
    private void setAlarm(Calendar targetCal){

        Intent intent = new Intent(CreateRequest.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);

    }

    /**
     * Checks user input to determine if valid for submission
     * @return true if input is valid
     */
    private boolean checkInput() {
        EditText data = (EditText) findViewById(R.id.numBags);
        boolean noDestination = null == destination;
        boolean noStart = null == start;
        boolean noBags = null == data.getText();
        boolean noDate = 0 == year;
        boolean allValid = false;

        if (noDestination || noStart|| noBags || noDate) {
            Snackbar.make(findViewById(R.id.submit), "All data fields required", Snackbar.LENGTH_LONG).show();
        }
        else {
            allValid = true;

                    /* check that number of bags is valid */
            try {
                numBags = Integer.parseInt(data.getText().toString());
            } catch (Exception e) {
                numBags = 0;
                allValid = false;
                Snackbar.make(findViewById(R.id.submit), "Number of bags must be a number", Snackbar.LENGTH_LONG).show();
            }
                    /* check that start and end locations are valid */
            try {
                Geocoder gcd = new Geocoder(CreateRequest.this, Locale.getDefault());
                List<Address> destAddress = gcd.getFromLocation(destination.getLatLng().latitude, destination.getLatLng().longitude, 1);
                List<Address> startAddress = gcd.getFromLocation(start.getLatLng().latitude, start.getLatLng().longitude, 1);

                /* start or end locations must be in Ames, IA */
                if(!destAddress.get(0).getLocality().equals("Ames") && !startAddress.get(0).getLocality().equals("Ames") &&
                        !destAddress.get(0).getAdminArea().equals("Iowa") && !startAddress.get(0).getAdminArea().equals("United States")) {
                    allValid = false;
                    Snackbar.make(findViewById(R.id.submit), "Ride must start or end in Ames, IA", Snackbar.LENGTH_LONG).show();
                }

                /* start and end locations must be in United States */
                if(!destAddress.get(0).getCountryName().equals("United States") || !startAddress.get(0).getCountryName().equals("United States")) {
                    allValid = false;
                    Snackbar.make(findViewById(R.id.submit), "Ride must start and end in United States", Snackbar.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            data = (EditText) findViewById(R.id.Description);
            description = data.getText().toString();
        }
        return allValid;
    }

    /**
     * Method that handles back button press
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.create_request_activity);
        i = userIntentService.createIntent(CreateRequest.this, MainActivity.class, userIntentService.getUserFromIntent(this.getIntent()));
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Discard Request");
            alert.setMessage("This will discard your current request. Continue anyway?");
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
        i = this.getIntent();
        //noinspection SimplifiableIfStatement
        if (id == R.id.my_profile) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Discard Request");
            alert.setMessage("This will discard your current request. Continue anyway?");
            alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    finish();
                    i = userIntentService.createIntent(CreateRequest.this, ViewProfile.class, userIntentService.getUserFromIntent(i));
                    i.putExtra("caller", "Create Request");
                    startActivity(i);
                }});
            alert.setNegativeButton(android.R.string.no, null);
            alert.show();
        } else if(id == R.id.admin_actions) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Discard Request");
            alert.setMessage("This will discard your current request. Continue anyway?");
            alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    i = userIntentService.createIntent(CreateRequest.this, AdminActions.class, userIntentService.getUserFromIntent(i));
                    startActivity(i);
                }});
            alert.setNegativeButton(android.R.string.no, null);
            alert.show();
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
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        i = navigationService.getNavigationIntent(item, CreateRequest.this, this.getIntent());
        AlertDialog.Builder alert;

        if (R.id.logout == item.getItemId()) {
            alert = navigationService.logOutButton(CreateRequest.this);
            alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    SaveSharedPreference.clearUsernamePassword(CreateRequest.this);
                    startActivity(i);
                }
            });
            alert.show();
            retValue = true;
        }
        else {
            alert = new AlertDialog.Builder(this);
            alert.setTitle("Discard Offer");
            alert.setMessage("This will discard your current offer. Continue anyway?");
            alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Handle navigation view item clicks here.
                    i = navigationService.getNavigationIntent(item, CreateRequest.this, i);

                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.create_request_activity);
                    drawer.closeDrawer(GravityCompat.START);

                    if (navigationService.checkInternetConnection(CreateRequest.this)) {
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
        }
        return retValue;
    }

    /**
     * insert option to connect to wifi
     */
    public void connectionPopUp() {
        Snackbar snackbar = activityService.setupConnection(CreateRequest.this, findViewById(R.id.contacts_activity));
        snackbar.show();
    }
}