package Fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.util.ArrayList;
import java.util.List;

import Listeners.LoginButtonListener;
import Utilities.DataContainer;
import allblacks.com.iBaleka.R;

public class LoginFragment extends Fragment {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button createAccountButton;
    private Button forgotPasswordButton;
    private LoginButtonListener buttonListener;
    private List<String> signInList;
    private TextView toolbarTextView;
    private LoginButton facebookLoginButton;
    private CallbackManager callbackManager;


    public LoginFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View currentView = inflater.inflate(R.layout.fragment_login, container, false);
        initializeComponents(currentView);
        FacebookSdk.sdkInitialize(getActivity());
        AppEventsLogger.activateApp(getActivity());
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
        facebookLoginButton = (LoginButton) currentView.findViewById(R.id.FacebookLoginButton);
        facebookLoginButton.setFragment(this);
        callbackManager = CallbackManager.Factory.create();
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }



    @Override
    public void onResume() {
        super.onResume();
        toolbarTextView.setText("Login to Continue");
    }


}
