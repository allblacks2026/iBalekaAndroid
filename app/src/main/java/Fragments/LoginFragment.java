package Fragments;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.wallet.NotifyTransactionStatusRequest;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import Listeners.LoginButtonListener;
import Utilities.DataContainer;
import allblacks.com.iBaleka.R;

public class LoginFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private static final int SIGN_IN_REQUEST = 400;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button createAccountButton;
    private Button forgotPasswordButton;
    private LoginButtonListener buttonListener;
    private List<String> signInList;
    private TextView toolbarTextView;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions googleSignInOptions;
    private SignInButton googleSignInButton;


    public LoginFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View currentView = inflater.inflate(R.layout.fragment_login, container, false);
        buildGoogleSignIn();
        buildGoogleApi();
        initializeComponents(currentView);
        return currentView;
    }

    private void initializeComponents(View currentView) {
        signInList = new ArrayList<>();
        signInList.add("Sign-up with Facebook");
        signInList.add("Sign-up with Google");
        toolbarTextView = (TextView) getActivity().findViewById(R.id.LoginActivityToolbarTextView);
        toolbarTextView.setText("Login to Continue");
        usernameEditText = (EditText) currentView.findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) currentView.findViewById(R.id.passwordEditText);
        loginButton = (Button) currentView.findViewById(R.id.loginButton);
        forgotPasswordButton = (Button) currentView.findViewById(R.id.forgotPasswordButton);
        createAccountButton = (Button) currentView.findViewById(R.id.registerAccountbtn);
        buttonListener = new LoginButtonListener(this.getActivity());
        loginButton.setOnClickListener(buttonListener);
        forgotPasswordButton.setOnClickListener(buttonListener);
        createAccountButton.setOnClickListener(buttonListener);
        googleSignInButton = (SignInButton) currentView.findViewById(R.id.GoogleSignInButton);
        googleSignInButton.setSize(SignInButton.SIZE_WIDE);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(signInIntent, SIGN_IN_REQUEST);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            final GoogleSignInAccount account = result.getSignInAccount(); //Information of account is loaded here
            Plus.PeopleApi.load(googleApiClient, account.getId()).setResultCallback(new ResultCallback<People.LoadPeopleResult>() {
                @Override
                public void onResult(@NonNull People.LoadPeopleResult loadPeopleResult) {
                    Person googlePerson = loadPeopleResult.getPersonBuffer().get(0);
                    displayPerson(account, googlePerson);
                }
            });
        }
    }

    public void displayPerson(GoogleSignInAccount account, Person p) {

        DataContainer.getDataContainerInstance().setGoogleSignInAccount(account);
        DataContainer.getDataContainerInstance().setGooglePerson(p);

        if (account != null && p != null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.LoginActivityContentArea, new GoogleSignUpFragment(), "GoogleSignUpFragment")
                    .addToBackStack("GoogleSignUpFragment")
                    .commit();
        } else {
            Toast.makeText(getActivity(), "Error Getting Google Profile", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        toolbarTextView.setText("Login to Continue");
        buildGoogleApi();
    }

    private void buildGoogleApi() {
        googleApiClient = new GoogleApiClient.Builder(getActivity(), this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_PROFILE).build();
        googleApiClient.connect(GoogleApiClient.SIGN_IN_MODE_OPTIONAL);
    }

    private void buildGoogleSignIn()
    {
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(Plus.SCOPE_PLUS_LOGIN, Plus.SCOPE_PLUS_PROFILE, new Scope("https://www.googleapis.com/auth/plus.profile.emails.read"))
                .requestEmail()
                .requestProfile()
                .requestId()
                .build();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
