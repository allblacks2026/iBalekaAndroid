package allblacks.com.iBaleka;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import BackgroundTasks.LoginBackgroundTask;
import DataAccessLayer.iBalekaClient;
import Fragments.AthleteLandingFragment;
import RetroFitModels.Athlete;
import RetroFitModels.AthleteArray;
import Utilities.DeviceHardwareChecker;
import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppSplashScreen extends AppCompatActivity {

    private DeviceHardwareChecker hardwareChecker;
    private boolean connectionResult = false;
    private SharedPreferences globalPreferences;
    private SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_splash_screen);
        globalPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = globalPreferences.edit();
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startLoginActivity();
            }
        };
        hardwareChecker = new DeviceHardwareChecker(this.getApplicationContext());
        hardwareChecker.checkNetworkConnection();
        connectionResult = hardwareChecker.isConnectedToInternet();
        if (connectionResult) {
            checkUserLogin();
        } else {
            displayMessage("No Internet Detected", "We could not detect an internet connection on" +
                    " your smartphone. Please check your network settings", listener);

        }
    }


    public void displayMessage(String title, String message) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(this);
        messageBox.setTitle(title);
        messageBox.setMessage(message);
        messageBox.setPositiveButton("Got It", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        messageBox.show();
    }

    public void displayMessage(String title, String message, DialogInterface.OnClickListener
            listener) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(this);
        messageBox.setTitle(title);
        messageBox.setMessage(message);
        messageBox.setPositiveButton("Got It", listener);
        messageBox.show();
    }

    public void startLoginActivity()
    {
        Intent newIntent = new Intent(this, LoginActivity.class);
        startActivity(newIntent);
        finish();
    }

    private void checkUserLogin()
    {
        if (globalPreferences.getBoolean("remember_credentials", false)) {
            String username = globalPreferences.getString("userName", "");
            String password = globalPreferences.getString("password", "");
            if (username != "" && password != "") {
                processLogin(username, password);
            } else {
                startLoginActivity();
            }
        } else {
            startLoginActivity();
        }
    }
    private void processLogin(String userName, String password) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ibalekaapi.azurewebsites.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        iBalekaClient client = retrofit.create(iBalekaClient.class);
        final Call<AthleteArray> athleteArrayCall = client.loginAthlete(userName, password);
        athleteArrayCall.enqueue(new Callback<AthleteArray>() {
            @Override
            public void onResponse(Call<AthleteArray> call, Response<AthleteArray> response) {
                if (response.code() == 200) {
                    AthleteArray array = response.body();
                    Athlete athleteObject = array.getModel();

                    SharedPreferences appPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = appPref.edit();
                    editor.putInt("athleteId", athleteObject.getAthleteID());
                    editor.putString("name", athleteObject.getName());
                    editor.putString("surname", athleteObject.getSurname());
                    editor.putString("dateOfBirth", athleteObject.getDateOfBirth());
                    editor.putString("dateJoined", athleteObject.getDateJoined());
                    editor.putString("emailAddress", athleteObject.getEmailAddress());
                    editor.putString("userName", athleteObject.getUsername());
                    editor.putString("password", athleteObject.getPassword());
                    editor.putString("securityQuestion", athleteObject.getSecurityQuestion());
                    editor.putString("securityAnswer", athleteObject.getSecurityAnswer());
                    editor.putInt("gender", athleteObject.getGender());
                    editor.putFloat("weight", (float)athleteObject.getWeight());
                    editor.putFloat("height", (float) athleteObject.getHeight());
                    editor.putBoolean("deleted", athleteObject.isDeleted());
                    editor.putString("country", athleteObject.getCountry());
                    editor.commit();
                    //Start the app
                    startApp();

                } else if (response.code() == 500) {
                    displayMessage("Error Logging In", "The system could not log you in with the provided username and password. These details may have been changed on another device. Please continue to our login screen", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startLoginActivity();
                        }
                    });
                } else  {
                    startLoginActivity();
                }
            }

            @Override
            public void onFailure(Call<AthleteArray> call, Throwable t) {

            }
        });
    }

    private void startApp()
    {
        Intent main = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(main);
        finish();
    }
}
