package Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.util.AsyncListUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.model.people.Person;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import Utilities.DataContainer;
import Utilities.DeviceHardwareChecker;
import allblacks.com.iBaleka.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoogleSignUpFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private ImageView userProfileImage;
    private EditText username, password, securityAnswer, height, weight;
    private Button signUpButton;
    private TextView name, email, toolbarTextView, dob, gender, location;
    private MaterialSpinner securityQuestion;
    private List<String> questionsList;

    public GoogleSignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View currentView = inflater.inflate(R.layout.fragment_google_sign_up, container, false);
        DeviceHardwareChecker checker = new DeviceHardwareChecker(getActivity());
        checker.checkNetworkConnection();
        if (checker.isConnectedToInternet()) {
            initializeComponents(currentView);
            setupPage();
        } else {
            getFragmentManager().popBackStack();
        }
        return currentView;
    }

    private void initializeComponents(View currentView){
        location = (TextView)currentView.findViewById(R.id.GoogleSignInLocationTextView);
        toolbarTextView = (TextView) getActivity().findViewById(R.id.LoginActivityToolbarTextView);
        gender = (TextView) currentView.findViewById(R.id.GoogleSignInGenderTextView);
        toolbarTextView.setText("Finalize Your Profile");
        dob = (TextView) currentView.findViewById(R.id.GoogleSignInDateOfBirthTextView);
        name = (TextView) currentView.findViewById(R.id.GoogleSignInNameTextView);
        email = (TextView) currentView.findViewById(R.id.GoogleSignInEmailTextView);
        username = (EditText) currentView.findViewById(R.id.GoogleSignInUsernameTextView);
        password = (EditText) currentView.findViewById(R.id.GoogleSignInPasswordTextView);
        securityAnswer = (EditText) currentView.findViewById(R.id.GoogleSignInSecurityAnswer);
        height = (EditText) currentView.findViewById(R.id.GoogleSignInHeightTextView);
        weight = (EditText) currentView.findViewById(R.id.GoogleSignInWeightTextView);
        signUpButton = (Button) currentView.findViewById(R.id.GoogleSignInFinalizeProfileButton);
        securityQuestion = (MaterialSpinner) currentView.findViewById(R.id.GoogleSignInSecurityQuestion);
        questionsList = new ArrayList<>();
        questionsList.add("What was the name of your first grade teacher?");
        questionsList.add("What was the name of your first pet?");
        questionsList.add("Who was your first high school crush?");
        questionsList.add("What is your maiden name?");
        questionsList.add("What was the first city you lived in?");
        questionsList.add("Who was your Grade 12 mathematics teacher?");
        questionsList.add("Where did you graduate from high school?");
        securityQuestion.setItems(questionsList);
    }

    private void setupPage()
    {
        GoogleSignInAccount account = DataContainer.getDataContainerInstance().getGoogleSignInAccount();
        Person googlePerson = DataContainer.getDataContainerInstance().getGooglePerson();
        email.setText(account.getEmail());
        name.setText(account.getDisplayName());
        dob.setText(googlePerson.getBirthday());
        gender.setText(Integer.toString(googlePerson.getGender()));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
