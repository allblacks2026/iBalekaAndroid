package Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import Listeners.LoginButtonListener;
import allblacks.com.iBaleka.R;

public class ForgotPasswordFragment extends Fragment {

    private EditText emailAddressEditText;
    private EditText usernameEditText;
    private Button nextStepButton;
    private LoginButtonListener buttonListener;
    private TextView toolbarTextView;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View currentView = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        initializeComponents(currentView);
        return currentView;
    }

    private void initializeComponents(View currentView) {
        emailAddressEditText = (EditText) currentView.findViewById(R.id
                .ForgotPasswordEmailEditText);
        nextStepButton = (Button) currentView.findViewById(R.id.ForgotPasswordNextStepButton);
        buttonListener = new LoginButtonListener(this.getActivity());
        nextStepButton.setOnClickListener(buttonListener);
        toolbarTextView = (TextView) getActivity().findViewById(R.id.LoginActivityToolbarTextView);
        toolbarTextView.setText("Change Your Password");
    }

    @Override
    public void onResume() {
        super.onResume();
        toolbarTextView.setText("Change Your Password");
    }
}
