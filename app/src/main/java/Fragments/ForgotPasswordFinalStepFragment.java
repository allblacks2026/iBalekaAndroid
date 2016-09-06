package Fragments;


import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import allblacks.com.iBaleka.R;

public class ForgotPasswordFinalStepFragment extends Fragment {

    private SharedPreferences appSharedPreferences;
    private TextView toolbarTextView;
    public ForgotPasswordFinalStepFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password_final_step, container, false);
    }

}
