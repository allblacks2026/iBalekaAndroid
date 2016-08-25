package Listeners;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import BackgroundTasks.RegistrationBackgroundTask;
import Fragments.CreateAccountStepTwoFragment;
import Models.UserCredential;
import RetroFit.iBalekaApi;
import RetroFitModels.Athlete;
import Utilities.DeviceHardwareChecker;
import Utilities.TextSanitizer;
import allblacks.com.iBaleka.MainActivity;
import allblacks.com.iBaleka.R;
import okhttp3.ResponseBody;
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
    private iBalekaApi apiService;
    private boolean isWorking = false;

    public RegistrationButtonListener(Activity currentActivity) {
        this.currentActivity = currentActivity;
        appSharedPreferences = currentActivity.getSharedPreferences("iBalekaRegistration", Context.MODE_PRIVATE);
        editor = appSharedPreferences.edit();
        countryList = currentActivity.getResources().getStringArray(R.array.countries_list);
        globalPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        toolbarTextView = (TextView) currentActivity.findViewById(R.id.LoginActivityToolbarTextView);
        progressDialog = new ProgressDialog(this.currentActivity);
        //Create the retrofit client and service
        retrofitClient = new Retrofit.Builder()
                .baseUrl(currentActivity.getResources().getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofitClient.create(iBalekaApi.class);
        globalEditor = globalPreferences.edit();
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
                        selectedDOB.setText(selectedYear + "-" + selectedMonth + "-" + selectedDay);

                        editor.putString("DateOfBirth", selectedYear + "/" + selectedMonth + "/" + selectedDay);
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
                String selectedDate = globalPreferences.getString("DateOfBirth", "");
                editor.putInt("Gender", selectedGenderIndex);
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
                        editor.putInt("AthleteID", athleteID);
                        editor.putString("Name", name);
                        editor.putString("Surname", surname);
                        editor.putString("EmailAddress", email);
                        editor.putString("Country", country);
                        editor.commit();

                        CreateAccountStepTwoFragment nextStepFrag = new CreateAccountStepTwoFragment();
                        FragmentManager mgr = currentActivity.getFragmentManager();
                        FragmentTransaction transaction = mgr.beginTransaction();
                        transaction.replace(R.id.LoginActivityContentArea, nextStepFrag, "NextStepFragment");
                        transaction.addToBackStack("NextStepFragment");
                        transaction.commit();
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

                        float height = 0;
                        float weight = 0;
                        if (heightEditText.getText().toString() != "") {
                            height = Float.parseFloat(heightEditText.getText().toString());
                        }

                        if (weightEditText.getText().toString() != "") {
                            weight = Float.parseFloat(weightEditText.getText().toString());
                        }

                        editor.putFloat("Height",height);
                        editor.putFloat("Weight", weight);
                        editor.commit();

                        String answer = TextSanitizer.sanitizeText(securityAnswerEditText.getText().toString(), false);
                        boolean[] isValid = new boolean[4];
                        isValid[0] = TextSanitizer.isValidText(username, 1, 20);
                        isValid[1] = TextSanitizer.isValidText(password, 1, 20);
                        isValid[2] = TextSanitizer.isValidText(question, 1, 150);
                        isValid[3] = TextSanitizer.isValidText(answer, 1, 150);

                        if (isValid[0] && isValid[1] && isValid[2] && isValid[3]) {
                        try {
                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                            Date dob = dateFormat.parse(appSharedPreferences.getString("DateOfBirth", ""));
                            final Athlete newAthlete = new Athlete(appSharedPreferences.getInt("AthleteID", 0), dob, false, appSharedPreferences.getString("Name", ""), appSharedPreferences.getString("Username", ""), appSharedPreferences.getInt("Gender", 0), appSharedPreferences.getFloat("Height", 0), appSharedPreferences.getFloat("Weight", 0), appSharedPreferences.getString("Password", ""), appSharedPreferences.getString("EmailAddress", ""), appSharedPreferences.getString("SecurityQuestion", ""), appSharedPreferences.getString("SecurityAnswer", ""), appSharedPreferences.getString("Surname", ""), appSharedPreferences.getString("Country", ""));
                            progressDialog.setTitle("Registering Athlete");
                            progressDialog.setMessage("Please wait while we register you as an athlete...");
                            progressDialog.show();

                            Call<Athlete> athleteCall = apiService.addAthlete(newAthlete);
                            athleteCall.enqueue(new Callback<Athlete>() {
                                @Override
                                public void onResponse(Call<Athlete> call, Response<Athlete> response) {
                                    int responseCode = response.code();
                                    boolean success = response.isSuccessful();
                                    String message = response.message();
                                    ResponseBody body = response.errorBody();
                                    if (success)
                                    {
                                        if (progressDialog.isShowing())
                                        {
                                            progressDialog.cancel();
                                        }
                                        DialogInterface.OnClickListener successListener = new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                globalEditor.putString("AthleteID", Integer.toString(newAthlete.getAthleteID()));
                                                globalEditor.putString("DateOfBirth", newAthlete.getDateOfBirth().toString());
                                                globalEditor.putString("isDeleted", Boolean.toString(newAthlete.isDeleted()));
                                                globalEditor.putString("Name", newAthlete.getName());
                                                globalEditor.putString("Gender", Integer.toString(newAthlete.getGender()));
                                                globalEditor.putString("Height", Double.toString(newAthlete.getHeight()));
                                                globalEditor.putString("Weight", Double.toString(newAthlete.getWeight()));
                                                globalEditor.putString("Password", newAthlete.getPassword());
                                                globalEditor.putString("EmailAddress", newAthlete.getEmailAddress());
                                                globalEditor.putString("SecurityQuestion", newAthlete.getSecurityQuestion());
                                                globalEditor.putString("SecurityAnswer", newAthlete.getSecurityAnswer());
                                                globalEditor.putString("Surname", newAthlete.getSurname());
                                                globalEditor.putString("Country", newAthlete.getCountry());

                                                Intent mainActivityIntent = new Intent(currentActivity, MainActivity.class);
                                                currentActivity.startActivity(mainActivityIntent);
                                                currentActivity.finish();
                                            }
                                        };
                                        displayMessage("Registration Successful", "You have successfully registered as an athlete", successListener);
                                    } else {
                                        if (progressDialog.isShowing()) {
                                            progressDialog.cancel();
                                        }

                                        displayMessage("Error With Registration", message);
                                    }

                                }

                                @Override
                                public void onFailure(Call<Athlete> call, Throwable t) {
                                    displayMessage("Registration Failed", t.getMessage());
                                }
                            });




                        } catch (Exception error) {
                            displayMessage("Error With Registration", "Error With Registration: \n" +error.getMessage());
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
}
