package cysrides.cysrides;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import domain.UserInfo;
import service.UserIntentService;
import service.UserIntentServiceImpl;

public class DialogConfirmationCode extends AppCompatActivity {

    private UserIntentService userIntentService = new UserIntentServiceImpl();
    private EditText resultView;
    private UserInfo user;

    /**
     * This method primarily listens for the "goButton" to be clicked. When clicked, it references the
     * code the user inputs to the code in the database for that user. If the user inputs the wrong code,
     * a toast appears saying so.
     * @param savedInstanceState - app data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_confirmation_code);
        resultView = (EditText) findViewById(R.id.resultText);
        Button goButton = (Button) findViewById(R.id.goButton);
        user = userIntentService.getUserFromIntent(this.getIntent());

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resultView.getText().toString().equals(user.getConfirmationCode())){
                    Toast toast = Toast.makeText(getApplicationContext(), "Valid confirmation code", Toast.LENGTH_LONG);
                    toast.show();

                    Intent i = userIntentService.createIntent(DialogConfirmationCode.this, LoginActivity.class, user);
                    startActivity(i);
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Not valid credentials, " /*+ user.getConfirmationCode()*/, Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }


}
