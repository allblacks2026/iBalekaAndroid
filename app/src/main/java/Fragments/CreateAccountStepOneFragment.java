package Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;

import Listeners.RegistrationButtonListener;
import allblacks.com.iBaleka.R;

public class CreateAccountStepOneFragment extends Fragment {

    private EditText nameEditText, surnameEditText, emailEditText;
    private Spinner athleteTypeSpinner;
    private MaterialSpinner selectedCountrySpinner, genderSpinner;
    private ArrayAdapter spinnerAdapter;
    private Button nextStepButton;
    private RegistrationButtonListener buttonListener;
    private Button dateOfBirthButton;
    private TextView toolbarTextView;
    private String [] genderArray = new String[] {"Male", "Female"};

    public CreateAccountStepOneFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_create_account_step_one, container, false);
        initializeComponents(myView);
        return myView;
    }

    private void initializeComponents(View myView) {
        toolbarTextView = (TextView) getActivity().findViewById(R.id.LoginActivityToolbarTextView);
        toolbarTextView.setText("Create Account - Step 1 of 2");
        selectedCountrySpinner = (MaterialSpinner) myView.findViewById(R.id.CountrySpinner);
        selectedCountrySpinner.setDropdownMaxHeight(750);

        genderSpinner = (MaterialSpinner) myView.findViewById(R.id.RegistrationGenderSpinner);
        genderSpinner.setItems(genderArray);
        String [] countries = this.getResources().getStringArray(R.array.countries_list);
        selectedCountrySpinner.setItems(countries);
        int southAfrica = 0;
        for (int a = 0; a < countries.length; a++) {
            String selected = countries[a];
            if (selected.equalsIgnoreCase("South Africa")) {
                southAfrica = a;
            }
        }
        selectedCountrySpinner.setSelectedIndex(southAfrica);
        nameEditText = (EditText) myView.findViewById(R.id.NameEditText);
        surnameEditText = (EditText) myView.findViewById(R.id.SurnameEditText);
        emailEditText = (EditText) myView.findViewById(R.id.EmailEditText);
        nextStepButton = (Button) myView.findViewById(R.id.NextStepButton);
        dateOfBirthButton = (Button) myView.findViewById(R.id.SelectDateOfBirthButton);
        buttonListener = new RegistrationButtonListener(this.getActivity());
        nextStepButton.setOnClickListener(buttonListener);
        dateOfBirthButton.setOnClickListener(buttonListener);
    }

    @Override
    public void onPause() {
        setRetainInstance(true);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        toolbarTextView.setText("Create Account - Step 1 of 2");

    }
}
