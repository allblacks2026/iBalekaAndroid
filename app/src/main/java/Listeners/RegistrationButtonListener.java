package Listeners;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import DataAccessLayer.iBalekaClient;
import Fragments.CreateAccountStepTwoFragment;
import RetroFitModels.Athlete;
import RetroFitModels.ResponseBody;
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
 * Created by Okuhle on 6/26/2016.
 */
public class RegistrationButtonListener implements View.OnClickListener {

    private Activity currentActivity;
    private SharedPreferences appSharedPreferences;
    private SharedPreferences globalPreferences;
    private SharedPreferences.Editor editor;
    private int selectedDay = 0, selectedMonth = 0, selectedYear = 0;
    private TextView selectedDOB;
    private String [] countryList;
    private TextView toolbarTextView;
    private ProgressDialog progressDialog;
    private SharedPreferences.Editor globalEditor;
    private Retrofit retrofitClient;
    private boolean isWorking = false;

    public RegistrationButtonListener(Activity currentActivity) {
        this.currentActivity = currentActivity;
        appSharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        editor = appSharedPreferences.edit();
        countryList = currentActivity.getResources().getStringArray(R.array.countries_list);
        globalPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        toolbarTextView = (TextView) currentActivity.findViewById(R.id.LoginActivityToolbarTextView);
        progressDialog = new ProgressDialog(this.currentActivity);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.SelectDateOfBirthButton:
                selectedDOB = (TextView) currentActivity.findViewById(R.id.SelectedDateOfBirthLabel);
                DatePickerDialog dateDialog = new DatePickerDialog(currentActivity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        selectedDay = dayOfMonth;
                        selectedMonth = monthOfYear + 1;
                        selectedYear = year;
                        selectedDOB.setText(selectedYear + "/" + selectedMonth + "/" + selectedDay);

                        editor.putString("dateOfBirth", selectedYear + "/" + selectedMonth + "/" + selectedDay);
                    }
                }, 2015, 10, 10);
                dateDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                dateDialog.show();
                break;
            case R.id.NextStepButton:
                toolbarTextView.setText("Create Account: Step 2 of 2");
                TextView enteredName = (TextView) currentActivity.findViewById(R.id.NameEditText);
                TextView enteredSurname = (TextView) currentActivity.findViewById(R.id.SurnameEditText);
                TextView enteredEmail = (TextView) currentActivity.findViewById(R.id.EmailEditText);
                MaterialSpinner selectedCountry = (MaterialSpinner) currentActivity.findViewById(R.id.CountrySpinner);
                MaterialSpinner selectedGender = (MaterialSpinner) currentActivity.findViewById(R.id.RegistrationGenderSpinner);
                int selectedGenderIndex = selectedGender.getSelectedIndex();
                int selectedCountryIndex = selectedCountry.getSelectedIndex();
                String country = countryList[selectedCountryIndex];
                String selectedDate = globalPreferences.getString("dateOfBirth", "");
                editor.putInt("gender", selectedGenderIndex);
                editor.commit();
                if (enteredName.getText().toString().length() != 0 && enteredSurname.getText().toString().trim() != null && enteredEmail.getText().toString().trim() != null) {

                    Random randNum = new Random(5000);
                    String generatedAthleteID = Integer.toString(enteredName.length() + enteredSurname.length() + enteredEmail.length() + randNum.nextInt());
                    int athleteID = new Integer(generatedAthleteID);
                    String name = TextSanitizer.sanitizeText(enteredName.getText().toString(), true);
                    String surname = TextSanitizer.sanitizeText(enteredSurname.getText().toString(), true);
                    String email = TextSanitizer.sanitizeText(enteredEmail.getText().toString(), true);

                    boolean isValidName = TextSanitizer.isValidText(name, 1, 100);
                    boolean isValidSurname = TextSanitizer.isValidText(surname, 1, 100);
                    boolean isValidEmail = TextSanitizer.isValidEmail(email, 1, 100);

                    if (!isValidName) {
                        displayMessage("Invalid Name", "The entered name must be between 1 and 100 characters");
                    } else if (!isValidSurname) {
                        displayMessage("Invalid Email", "The entered surname must be betweeen 1 and 100 characters");
                    } else if (!isValidEmail) {
                        displayMessage("Invalid EmailAddress", "Please ensure you enter a valid email address");
                    }
                    //If these three parameters have been correctly added, save current information, and move to the next fragment
                    if (isValidName && isValidSurname && isValidEmail) {

                        editor.putString("name", name);
                        editor.putString("surname", surname);
                        editor.putString("emailAddress", email);
                        editor.putString("country", country);
                        editor.commit();

                        if (selectedDate != "" || selectedDate != null) {

                            CreateAccountStepTwoFragment nextStepFrag = new CreateAccountStepTwoFragment();
                            FragmentManager mgr = currentActivity.getFragmentManager();
                            FragmentTransaction transaction = mgr.beginTransaction();
                            transaction.replace(R.id.LoginActivityContentArea, nextStepFrag, "NextStepFragment");
                            transaction.addToBackStack("NextStepFragment");
                            transaction.commit();
                        } else {
                            displayMessage("Select Date of Birth", "Please select a date of birth");
                        }
                    } else {
                        displayMessage("Invalid Registration", "In order to continue with registration, please ensure all fields have data");
                    }
                } else {
                    displayMessage("Please Enter Text", "Please note that all fields are required");
                }
                break;
            case R.id.CreateAccountButton:
                DeviceHardwareChecker checker = new DeviceHardwareChecker(currentActivity);
                checker.checkNetworkConnection();
                if (checker.isConnectedToInternet()) {
                    EditText usernameEditText = (EditText) currentActivity.findViewById(R.id.UsernameEditText);
                    EditText passwordEditText = (EditText) currentActivity.findViewById(R.id.PasswordEditText);
                    MaterialSpinner securityQuestionSpinner = (MaterialSpinner) currentActivity.findViewById(R.id.SecurityQuestionSpinner);
                    EditText securityAnswerEditText = (EditText) currentActivity.findViewById(R.id.SecurityAnswerEditText);
                    EditText heightEditText = (EditText) currentActivity.findViewById(R.id.CreateAccountHeightEditText);
                    EditText weightEditText = (EditText) currentActivity.findViewById(R.id.CreateAccountWeightEditText);

                    if (usernameEditText.getText().toString() != null && passwordEditText.getText().toString() != null && securityQuestionSpinner.getText().toString() != null && securityAnswerEditText.getText().toString() != null && selectedDay == 0 && selectedMonth == 0 && selectedYear == 0) {
                        String username = TextSanitizer.sanitizeText(usernameEditText.getText().toString(), false);
                        String password = TextSanitizer.sanitizeText(passwordEditText.getText().toString(), false);
                        String question = TextSanitizer.sanitizeText(securityQuestionSpinner.getText().toString(), false);
                        String secAnswer = TextSanitizer.sanitizeText(securityAnswerEditText.getText().toString(), false);

                        editor.putString("securityQuestion", question);
                        editor.putString("securityAnswer", secAnswer);
                        editor.putString("username", username);
                        editor.putString("password", password);
                        editor.commit();

                        float height = 0;
                        float weight = 0;
                        if (heightEditText.getText().toString() != "") {
                            height = Float.parseFloat(heightEditText.getText().toString());
                        }

                        if (weightEditText.getText().toString() != "") {
                            weight = Float.parseFloat(weightEditText.getText().toString());
                        }

                        editor.putFloat("height", height);
                        editor.putFloat("weight", weight);
                        editor.commit();

                        String answer = TextSanitizer.sanitizeText(securityAnswerEditText.getText().toString(), false);
                        boolean[] isValid = new boolean[4];
                        isValid[0] = TextSanitizer.isValidText(username, 1, 20);
                        isValid[1] = TextSanitizer.isValidText(password, 1, 20);
                        isValid[2] = TextSanitizer.isValidText(question, 1, 150);
                        isValid[3] = TextSanitizer.isValidText(answer, 1, 150);

                        if (isValid[0] && isValid[1] && isValid[2] && isValid[3]) {
                        try {

                            progressDialog.setTitle("Registering");
                            progressDialog.setMessage("Please wait while we register you...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();

                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

                            Date currentDate = new Date();
                            JSONObject athleteObject = new JSONObject();

                            //athleteObject.put("athleteId", Integer.parseInt(appSharedPreferences.getString("AthleteID", "")));
                            athleteObject.put("dateOfBirth", appSharedPreferences.getString("DateOfBirth", ""));
                            athleteObject.put("dateJoined", dateFormat.format(currentDate));
                            athleteObject.put("deleted", Boolean.parseBoolean("false"));
                            athleteObject.put("name", appSharedPreferences.getString("name", ""));
                            athleteObject.put("userName", appSharedPreferences.getString("username", ""));
                            athleteObject.put("gender", appSharedPreferences.getInt("gender", 0));
                            athleteObject.put("height", appSharedPreferences.getFloat("height", 0));
                            athleteObject.put("weight", appSharedPreferences.getFloat("weight", 0));
                            athleteObject.put("password", appSharedPreferences.getString("password", ""));
                            athleteObject.put("emailAddress", appSharedPreferences.getString("emailAddress", ""));
                            athleteObject.put("securityQuestion", appSharedPreferences.getString("securityQuestion", ""));
                            athleteObject.put("securityAnswer", appSharedPreferences.getString("securityAnswer", ""));
                            athleteObject.put("surname", appSharedPreferences.getString("surname", ""));
                            athleteObject.put("country", appSharedPreferences.getString("country", ""));
                            Athlete newAthlete = new Athlete(athleteObject);
                            registerAthlete(newAthlete, progressDialog);

                        } catch (Exception error) {
                            progressDialog.cancel();
                            displayMessage("Error With Registration", error.getMessage() +"\n" + Log.getStackTraceString(error));
                        }
                        } else {
                            displayMessage("Invalid Data Detected", "One or more text fields contain insufficient / invalid data. Please ensure data entered is between 1 and 100 characters");
                        }
                    } else {
                        displayMessage("All Fields Required", "One or more fields have missing data. Please note that all fields are required");
                    }

                } else {
                    displayMessage("Check Your Internet Connection", "You are not connected to the internet. Please check your internet connection");
                }
        }
    }

    public void displayMessage(String title, String message) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(currentActivity);
        messageBox.setTitle(title);
        messageBox.setMessage(message);
        messageBox.setPositiveButton("Got it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        messageBox.show();
    }

    public void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(currentActivity);
        messageBox.setTitle(title);
        messageBox.setMessage(message);
        messageBox.setPositiveButton("Got it", listener);
        messageBox.show();
    }


    private void registerAthlete(Athlete newAthlete, final ProgressDialog dialog) {
        Retrofit retrofitClient = new Retrofit.Builder()
                .baseUrl("https://ibalekaapi.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        iBalekaClient iBalekaClient = retrofitClient.create(DataAccessLayer.iBalekaClient.class);
        Call<ResponseBody> responseCall = iBalekaClient.registerAthlete(newAthlete);
        responseCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.cancel();
                ResponseBody body = response.body();
                switch (response.code()) {
                    case 200:
                        displayMessage("Registration Successful", "You have successfully registered for iBaleka! Welcome to iBaleka!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent mainScreen = new Intent(currentActivity, MainActivity.class);
                                currentActivity.startActivity(mainScreen);
                                currentActivity.finish();
                            }
                        });
                        break;
                    case 204:
                        displayMessage("Error Registering for iBaleka", "The server did not return any data");
                        break;
                    case 500:
                        displayMessage("Error Registering For iBaleka", "Internal Server Error");
                        break;
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.cancel();
                displayMessage("Error Registering Athlete", t.getMessage());
                System.out.println(Log.getStackTraceString(t));
            }
        });
    }

}
