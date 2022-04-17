package cysrides.cysrides;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import domain.UserInfo;
import service.Callback;
import service.UserIntentService;
import service.UserIntentServiceImpl;
import service.UserRatingService;
import service.UserRatingServiceImpl;
import volley.UserRatingVolleyImpl;
import volley.UserVolleyImpl;

public class RateRider extends AppCompatActivity {

    private String riderName;

    private UserIntentService userIntentService = new UserIntentServiceImpl();
    private UserRatingService userRatingService = new UserRatingServiceImpl();

    private EditText resultView;

    private float rating;
    private UserInfo user;
    private int numRatings;
    private float userRating;
    private String username1;

    /**
     * Initializes page
     * @param savedInstanceState - app data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_rider);

        riderName = (this.getIntent().getExtras().getString("Rider name"));

        resultView = (EditText) findViewById(R.id.ratingResultTextView);

        Button goButton;
        TextView rateRiderTextView;

        goButton = (Button) findViewById(R.id.goButton);
        rateRiderTextView = (TextView) findViewById(R.id.rateRiderTextView);

        rateRiderTextView.append(riderName);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userRating = Float.parseFloat(resultView.getText().toString());

                getUser(riderName);

                user = userIntentService.getUserFromIntent(getIntent());

                Intent i = userIntentService.createIntent(RateRider.this, LoginActivity.class, user);
                startActivity(i);
            }
        });
    }

    /**
     * Returns user data
     * @param username - user's username
     */
    @SuppressWarnings("unchecked")
    private void getUser(final String username) {
        username1 = username;
        UserVolleyImpl volley = new UserVolleyImpl(new Callback() {
            ArrayList<UserInfo> users;

            public void call(ArrayList<?> result) {
                try {
                    if(result.get(0) instanceof UserInfo) {
                        users = (ArrayList<UserInfo>) result;
                    }
                } catch(Exception e) {
                    users = new ArrayList<>();
                }
                for(int i = 0; i < users.size(); i++){
                    if(users.get(i).getNetID().equals(username1)){
                        user = users.get(i);
                    }
                }

                getRatings();
            }
        });
        volley.execute();
    }

    /**
     * gets user's ratings from the database
     */
    @SuppressWarnings("unchecked")
    private void getRatings() {
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
                    if(ratings.get(i).equals(username1)) {
                        if (ratings.get(i + 1) != null && ratings.get(i + 2) != null){
                            user.setUserRating(Float.valueOf(ratings.get(i + 1)));
                            rating = Float.valueOf(ratings.get(i + 1));
                            numRatings = Integer.valueOf(ratings.get(i + 2));

                        }
                    }

                }
                userRatingService.updateRating(RateRider.this, rating, userRating, numRatings, user);


            }
        });
        volley.execute();
    }

}
