package cysrides.cysrides;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import domain.Offer;
import domain.Request;
import domain.UserInfo;
import domain.UserType;
import service.Callback;
import service.EmailSenderService;
import service.EmailSenderServiceImpl;
import service.UserIntentService;
import service.UserIntentServiceImpl;
import volley.UserVolley;
import volley.UserVolleyImpl;

public class CreateProfile extends AppCompatActivity {

    //UI references fields
    private EditText fNameView;
    private EditText lNameView;
    private EditText venmoView;
    private EditText profileDescriptionView;
    private RadioButton driverRadioButton;
    private RadioButton passengerRadioButton;

    private static final int RB1_ID = 1000; //first radio button id
    private static final int RB2_ID = 1001; //second radio button id

    //User object fields
    private String netID;
    private String password;
    private String firstName;
    private String lastName;
    private String venmo;
    private String profileDescription;
    private String confirmationCode;
    private UserType userType;

    private Callback call;
    private UserIntentService userIntentService = new UserIntentServiceImpl();
    private EmailSenderService emailSenderService = new EmailSenderServiceImpl();
    private UserVolley userVolley = new UserVolleyImpl(call);
    private Intent i;

    /**
     * Initializes all UI components in the class to the ones in the xml file.
     * Grabs username and password from the previous intent and adds then to the text fields.
     * Listeners for radio group and create profile button.
     * @param savedInstanceState - App info
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView netIDView = (TextView) findViewById(R.id.NetID);
        TextView passwordView = (TextView) findViewById(R.id.Password);
        fNameView = (EditText) findViewById(R.id.First_Name);
        lNameView = (EditText) findViewById(R.id.Last_Name);
        venmoView = (EditText) findViewById(R.id.Venmo);
        profileDescriptionView = (EditText) findViewById(R.id.Description);
        Button createProfileButton = (Button) findViewById(R.id.createProfileButton);
        driverRadioButton = (RadioButton) findViewById(R.id.driverRadioButton);
        passengerRadioButton = (RadioButton) findViewById(R.id.passengerRadioButton);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        driverRadioButton.setId(RB1_ID);
        passengerRadioButton.setId(RB2_ID);

        netID = this.getIntent().getExtras().getString("netID");
        password = this.getIntent().getExtras().getString("password");

        netIDView.append(netID);
        passwordView.append(password);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId == RB1_ID) {
                            userType = UserType.DRIVER;
                        } else if (checkedId == RB2_ID) {
                            userType = UserType.PASSENGER;
                        }
                    }
                });

        final Context context = this.getApplicationContext();

        /*
        When the profile button is selected, all inputs are verified. If inputs are invalid, a toast appears saying so.
        If all user inputs are valid, the user is created. An email is sent to the net-id the user entered.
        User is taken to the confirmation code page.
         */
        createProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firstName = fNameView.getText().toString();
                lastName = lNameView.getText().toString();
                if (!isNameValid(firstName, lastName)) {
                    Toast.makeText(CreateProfile.this, "You did not enter a valid name", Toast.LENGTH_LONG).show();
                }

                venmo = venmoView.getText().toString();
                if(!isVenmoValid(venmo)){
                    Toast.makeText(CreateProfile.this, "You did not enter a valid venmo", Toast.LENGTH_LONG).show();
                }

                profileDescription = profileDescriptionView.getText().toString();
                if(!isDescriptionValid(profileDescription)){
                    Toast.makeText(CreateProfile.this, "You did not enter a long enough profile description", Toast.LENGTH_LONG).show();
                }

                Random rand = new Random();
                confirmationCode = String.format(Locale.US, "%04d", rand.nextInt(10000));

                if (!isTypeSelected()) {
                    Toast.makeText(CreateProfile.this, "You did not select a user type", Toast.LENGTH_LONG).show();
                }

                List<Offer> offers = new ArrayList<>();
                List<Request> requests = new ArrayList<>();

                if (inputsValid()) {
                    UserInfo user = new UserInfo(netID, password, confirmationCode, firstName, lastName, venmo, profileDescription,
                            userType, 0, offers, requests);

                    userVolley.createUser(CreateProfile.this, user);
                    finish();

                    i = userIntentService.createIntent(context, DialogConfirmationCode.class, user);

                    emailSenderService.sendEmail(user, context);

                    startActivity(i);
                }
            }
        });

    }

    /**
     * An email is valid if it contains "@iastate.edu"
     * @param email of user
     * @return true if valid
     */
    private boolean isEmailValid(String email) {
        return email.contains("@iastate.edu");
    }

    /**
     *  A password is valid if it contains a digit and is at least eight characters long
     *  @param password - user password
     */
    private boolean isPasswordValid(String password) {
        if (password.length() > 8) {
            for (int i = 0; i < password.length(); i++) {
                if (Character.isDigit(password.charAt(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * A description is valid if it is longer than 10 characters
     * @param profileDescription - user's profile description
     * @return true if valid
     */
    private boolean isDescriptionValid(String profileDescription) {
        return profileDescription.length() > 10;
    }

    /**
     *  A name is valid if the first and last name are greater than one character and do not contain a digit
     *  @param firstName - user's first name
     *  @param lastName - user's last name
     *  @return true if valid
     */
    private boolean isNameValid(String firstName, String lastName) {
        if (firstName.length() > 1 && lastName.length() > 1) {
            for (int i = 0; i < firstName.length(); i++) {
                if (Character.isDigit(firstName.charAt(i))) {
                    return false;
                }
            }
            for (int i = 0; i < lastName.length(); i++) {
                if (Character.isDigit(lastName.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * A venmo is valid if it is longer than 2 characters
     * @param venmo - user's venmo
     * @return true if valid
     */
    private boolean isVenmoValid(String venmo){
        return venmo.length() > 2;
    }

    /* A user type is valid if the driver or the passenger button is selected*/
    private boolean isTypeSelected(){
        return (driverRadioButton.isChecked() || passengerRadioButton.isChecked());
    }

    /**
     * All inputs are valid if the aforementioned methods are true
     * @return true if valid
     */
    private boolean inputsValid(){
        return (isEmailValid(netID) && isPasswordValid(password) && isDescriptionValid(profileDescription) &&
                isNameValid(firstName, lastName) && isVenmoValid(venmo) && isTypeSelected());
    }
}