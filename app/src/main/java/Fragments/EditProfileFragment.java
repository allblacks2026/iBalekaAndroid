package Fragments;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import BackgroundTasks.UpdateProfileBackgroundTask;
import DataAccessLayer.iBalekaClient;
import Listeners.MainActivityListener;
import RetroFitModels.Athlete;
import RetroFitModels.ResponseBody;
import Utilities.TextSanitizer;
import allblacks.com.iBaleka.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment implements View.OnClickListener {

    private EditText nameEditText;
    private EditText surnameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText weightEditText;
    private EditText heightEditText;
    private EditText securityQuestionEditText;
    private EditText securityAnswerEditText;
    private MaterialSpinner genderSpinner, countrySpinner;
    private Button updateButton;
    private List<String> genders = new ArrayList<String>();
    private UpdateProfileBackgroundTask updateProfileBackgroundTask;
    private SharedPreferences appSharedPreferences;
    private MainActivityListener buttonListener;
    private TextView toolbarTextView;
    String [] countryArray;
    private ProgressDialog dialog;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView =  inflater.inflate(R.layout.fragment_edit_profile, container, false);
        appSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        initializeComponents(currentView);
        setupData();
        return currentView;
    }

    private void initializeComponents(View currentView) {
        genders.add("Male");
        genders.add("Female");
        toolbarTextView = (TextView) getActivity().findViewById(R.id.MainActivityTextView);
        toolbarTextView.setText("Edit Your Profile");
        nameEditText = (EditText) currentView.findViewById(R.id.EditProfileNameEditText);
        surnameEditText = (EditText) currentView.findViewById(R.id.EditProfileSurnameEditText);
        emailEditText = (EditText) currentView.findViewById(R.id.EditProfileEmailEditText);
        emailEditText.setKeyListener(null);
        securityQuestionEditText = (EditText) currentView.findViewById(R.id.EditProfileSecurityQuestionEditText);
        securityAnswerEditText = (EditText) currentView.findViewById(R.id.EditProfileSecurityAnswerEditText);
        passwordEditText = (EditText) currentView.findViewById(R.id.EditProfilePasswordEditText);
        weightEditText = (EditText) currentView.findViewById(R.id.WeightEditText);
        heightEditText = (EditText) currentView.findViewById(R.id.HeightEditText);
        genderSpinner = (MaterialSpinner) currentView.findViewById(R.id.GenderSpinner);
        genderSpinner.setItems(genders);
        buttonListener = new MainActivityListener(getActivity());
        updateButton = (Button) currentView.findViewById(R.id.UpdateProfileButton);
        updateButton.setOnClickListener(this);
        countrySpinner = (MaterialSpinner) currentView.findViewById(R.id.SelectedCountrySpinner);
        countryArray = getResources().getStringArray(R.array.account_array);
        countrySpinner.setItems(countryArray);
        dialog = new ProgressDialog(getActivity());

    }

    @Override
    public void onResume() {
        super.onResume();
        toolbarTextView.setText("Edit Your Profile");
        setupData();
    }

    private void setupData()
    {
        nameEditText.setText(appSharedPreferences.getString("name", ""));
        surnameEditText.setText(appSharedPreferences.getString("surname", ""));
        emailEditText.setText(appSharedPreferences.getString("emailAddress", ""));
        securityQuestionEditText.setText(appSharedPreferences.getString("securityQuestion", ""));
        securityAnswerEditText.setText(appSharedPreferences.getString("securityAnswer", ""));
        weightEditText.setText(Float.toString(appSharedPreferences.getFloat("weight", 0)));
        heightEditText.setText(Float.toString(appSharedPreferences.getFloat("height", 0)));
        passwordEditText.setText(appSharedPreferences.getString("password", ""));
        genderSpinner.setSelectedIndex(appSharedPreferences.getInt("gender", 0));
        int index = 0;
        for (int a=0; a < countryArray.length - 1; a++) {
            if (countryArray[a].equalsIgnoreCase(appSharedPreferences.getString("country", ""))){
                index = a;
            }
        }
        countrySpinner.setSelectedIndex(index);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.UpdateProfileButton:
                processUpdateAthleteProfile();
                break;
        }
    }

    private void processUpdateAthleteProfile()
    {
        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Updating Your Profile");
        dialog.setMessage("Please wait while we update your profile. This may take a few seconds to ccomplete, depending on your internet connection... ");
        dialog.setCancelable(false);
        dialog.show();

        try {
            final String enteredName = TextSanitizer.sanitizeText(nameEditText.getText().toString(), true);
            final String enteredSurname = TextSanitizer.sanitizeText(surnameEditText.getText().toString(), true);
            final String securityQuestion = TextSanitizer.sanitizeText(securityQuestionEditText.getText().toString(), true);
            final String securityAnswer = TextSanitizer.sanitizeText(securityAnswerEditText.getText().toString(), true);
            final Double weight = Double.parseDouble(weightEditText.getText().toString());
            final Double height = Double.parseDouble(heightEditText.getText().toString());
            final String password = TextSanitizer.sanitizeText(passwordEditText.getText().toString(), false);
            final int gender = genderSpinner.getSelectedIndex();

            JSONObject athleteObject = new JSONObject();
            athleteObject.put("athleteId", appSharedPreferences.getInt("athleteId", 0));
            athleteObject.put("dateOfBirth", appSharedPreferences.getString("dateOfBirth", ""));
            athleteObject.put("dateJoined", appSharedPreferences.getString("dateJoined", ""));
            athleteObject.put("deleted", appSharedPreferences.getBoolean("deleted", false));
            athleteObject.put("name", enteredName);
            athleteObject.put("userName", appSharedPreferences.getString("userName", ""));
            athleteObject.put("gender", gender);
            athleteObject.put("height", height);
            athleteObject.put("weight", weight);
            athleteObject.put("surname", enteredSurname);
            athleteObject.put("password", password);
            athleteObject.put("securityQuestion", securityQuestion);
            athleteObject.put("securityAnswer", securityAnswer);
            athleteObject.put("country", countryArray[countrySpinner.getSelectedIndex()]);

            Athlete updateAthlete = new Athlete(athleteObject);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://ibalekaapi.azurewebsites.net/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            iBalekaClient client = retrofit.create(iBalekaClient.class);
            Call<ResponseBody> responseBodyCall = client.updateAthlete(updateAthlete);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dialog.cancel();
                    switch (response.code()) {
                        case 200:
                          displayMessage("Update Successful", "You have successfully updated your profile", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                  SharedPreferences.Editor editor = appSharedPreferences.edit();
                                  editor.putInt("gender", gender);
                                  editor.putFloat("height", Float.parseFloat(height.toString()));
                                  editor.putFloat("weight", Float.parseFloat(weight.toString()));
                                  editor.putString("name", enteredName);
                                  editor.putString("surname", enteredSurname);
                                  editor.putString("securityQuestion", securityQuestion);
                                  editor.putString("securityAnswer", securityAnswer);
                                  editor.putString("password", password);
                                  editor.putString("country", countryArray[countrySpinner.getSelectedIndex()]);
                                  editor.commit();
                              }
                          });
                            break;
                        case 204:
                            displayMessage("Error Updating Profile", "An error occurred while updating your profile\n" + response.message(), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            break;
                        case 500:
                            displayMessage("Error Updating Profile", "A critical error occurred while updating your profile. Please try again", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            break;
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.cancel();
                    displayMessage("Error Processing Profile Update", t.getMessage(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                }
            });


        } catch (Exception error) {

        }

    }

    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(getActivity());
        messageBox.setTitle(title).setMessage(message).setCancelable(false).setPositiveButton("Got It", listener).show();
    }
}
