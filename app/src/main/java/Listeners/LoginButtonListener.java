package Listeners;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import BackgroundTasks.ForgotPasswordBackgroundTask;
import BackgroundTasks.LoginBackgroundTask;
import DataAccessLayer.iBalekaClient;
import Fragments.CreateAccountStepOneFragment;
import Fragments.ForgotPasswordFragment;
import RetroFitModels.Athlete;
import RetroFitModels.AthleteArray;
import Utilities.DeviceHardwareChecker;
import Utilities.TextSanitizer;
import allblacks.com.iBaleka.MainActivity;
import allblacks.com.iBaleka.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Okuhle on 5/26/2016.
 */
public class LoginButtonListener implements View.OnClickListener {

    private Activity currentContext;
    private TextView toolbarTextView;
    private FragmentManager fragmentManager;
    private LoginBackgroundTask userGatewayTask;
    private ForgotPasswordBackgroundTask forgotPasswordBackgroundTask;
    private SharedPreferences applicationPreferences;
    private SharedPreferences.Editor editor;

    private EditText forgotPasswordEmailEditText;

    public LoginButtonListener(Activity currentContext) {
        this.currentContext = currentContext;
        toolbarTextView = (TextView) currentContext.findViewById(R.id.LoginActivityToolbarTextView);
        fragmentManager = currentContext.getFragmentManager();
        applicationPreferences = PreferenceManager.getDefaultSharedPreferences(currentContext);
        editor = applicationPreferences.edit();
        forgotPasswordBackgroundTask = new ForgotPasswordBackgroundTask(currentContext);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton:
                DeviceHardwareChecker checker = new DeviceHardwareChecker(currentContext);
                View currentView = currentContext.getCurrentFocus();
                if (currentView != null) {
                    InputMethodManager mgr = (InputMethodManager) currentContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(currentView.getWindowToken(), 0);
                }
                checker.checkNetworkConnection();
                if (checker.isConnectedToInternet()) {

                    TextView usernameEditText = (TextView) currentContext.findViewById(R.id.usernameEditText);
                    TextView passwordEditText = (TextView) currentContext.findViewById(R.id.passwordEditText);

                    if (usernameEditText.getText().toString() != null && passwordEditText.getText().toString() != null) {
                        String userName = TextSanitizer.sanitizeText(usernameEditText.getText().toString().trim(), false);
                        String password = TextSanitizer.sanitizeText(passwordEditText.getText().toString().trim(), false);

                        boolean isValidUsername = TextSanitizer.isValidText(userName, 1, 100);
                        boolean isValidPassword = TextSanitizer.isValidText(password, 1, 100);

                        if (isValidUsername && isValidPassword) {
                            processAthleteLogin(userName, password);
                        } else {
                            displayMessage("Login Error", "Please ensure your username and password is between 1 and 100 characters");
                        }
                    } else {
                        displayMessage("Login Error", "Please Enter a valid Username and Password");
                    }
                } else {
                    displayMessage("Check Internet Connection", "You are not connected to the internet. Please check your internet connection");
                }

                break;
            case R.id.registerAccountbtn:
                CreateAccountStepOneFragment registerAccountFragment = new
                        CreateAccountStepOneFragment();
                FragmentTransaction createAccountTransaction = fragmentManager.beginTransaction();
                createAccountTransaction.replace(R.id.LoginActivityContentArea,
                        registerAccountFragment, "RegisterFragmentStepOne");
                createAccountTransaction.addToBackStack("RegisterFragmentStepOne");
                createAccountTransaction.commit();
                toolbarTextView.setText("Register Account - Step 1 of 2");
                break;
            case R.id.forgotPasswordButton:
                ForgotPasswordFragment forgotPasswordFragment = new ForgotPasswordFragment();
                FragmentTransaction forgotPasswordTransaction = fragmentManager.beginTransaction();
                forgotPasswordTransaction.replace(R.id.LoginActivityContentArea,
                        forgotPasswordFragment, "ForgotPasswordFragment");
                toolbarTextView.setText("Reset Your Password");
                forgotPasswordTransaction.addToBackStack("ForgotPasswordFragment");
                forgotPasswordTransaction.commit();
                break;
            case R.id.ForgotPasswordNextStepButton:
                DeviceHardwareChecker checkInternet = new DeviceHardwareChecker(currentContext);
                checkInternet.checkNetworkConnection();
                if (checkInternet.isConnectedToInternet()) {
                    forgotPasswordEmailEditText = (EditText) currentContext.findViewById(R.id
                            .ForgotPasswordEmailEditText);
                    String enteredEmail = TextSanitizer.sanitizeText(forgotPasswordEmailEditText
                            .getText().toString(), true);
                    boolean isValidText = TextSanitizer.isValidText(enteredEmail, 10, 100);
                    if (isValidText) {
                        forgotPasswordBackgroundTask.execute(enteredEmail);
                    } else {
                        displayMessage("Invalid Email Entered", "Please enter an email address that " +
                                "is between 10 and 100 characters");
                    }
                } else {
                    displayMessage("Check Your Internet Connection", "You are not connected to the internet. Please check your internet connection");
                }
                break;
        }

    }

    public void displayMessage(String title, String message) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(currentContext);
        messageBox.setTitle(title);
        messageBox.setMessage(message);
        messageBox.setPositiveButton("Got it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        messageBox.show();
    }

    private void processAthleteLogin(String username, String password) {
        final ProgressDialog progressDialog = new ProgressDialog(currentContext);
        progressDialog.setTitle("Logging You In");
        progressDialog.setMessage("Please wait while we log you into the system. This may take a few seconds to complete, depending on your internet connection speed... ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ibalekaapi.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        iBalekaClient client = retrofit.create(iBalekaClient.class);
        final Call<AthleteArray> loginCall = client.loginAthlete(username, password);
        loginCall.enqueue(new Callback<AthleteArray>() {
            @Override
            public void onResponse(Call<AthleteArray> call, Response<AthleteArray> response) {
                progressDialog.cancel();
                switch (response.code()) {
                    case 200:
                        AthleteArray array = response.body();
                        Athlete loggedInAthlete = array.getModel();

                        editor.putInt("athleteId", loggedInAthlete.getAthleteID());
                        editor.putString("dateOfBirth", loggedInAthlete.getDateOfBirth());
                        editor.putString("dateJoined", loggedInAthlete.getDateJoined());
                        editor.putBoolean("deleted", loggedInAthlete.isDeleted());
                        editor.putString("name", loggedInAthlete.getName());
                        editor.putInt("gender", loggedInAthlete.getGender());
                        editor.putFloat("height", Float.parseFloat(Double.toString(loggedInAthlete.getHeight())));
                        editor.putFloat("weight", Float.parseFloat(Double.toString(loggedInAthlete.getWeight())));
                        editor.putString("password", loggedInAthlete.getPassword());
                        editor.putString("emailAddress", loggedInAthlete.getEmailAddress());
                        editor.putString("securityQuestion", loggedInAthlete.getSecurityQuestion());
                        editor.putString("securityAnswer", loggedInAthlete.getSecurityAnswer());
                        editor.putString("surname", loggedInAthlete.getSurname());
                        editor.putString("country", loggedInAthlete.getCountry());
                        editor.putString("userName", loggedInAthlete.getUsername());
                        editor.commit();

                        Intent mainActivity = new Intent(currentContext, MainActivity.class);
                        mainActivity.putExtra("LoggedInAthlete", loggedInAthlete);
                        currentContext.startActivity(mainActivity);
                        currentContext.finish();
                        break;
                    case 500:
                        displayMessage("Login Error", "An error occurred while logging in\n"+response.message());
                        break;
                    case 204:
                        displayMessage("Login Error", "The entered username and/or password does not match records on our system. Please try again");
                        break;
                }
            }

            @Override
            public void onFailure(Call<AthleteArray> call, Throwable t) {
                progressDialog.cancel();
                displayMessage("Error Logging In Athlete", t.getMessage());
            }
        });


    }
}
