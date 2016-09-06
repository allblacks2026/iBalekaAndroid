package Fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import BackgroundTasks.GetAthleteProfileBackgroundTask;
import RetroFitModels.Run;
import allblacks.com.iBaleka.R;

/**
 * Created by Okuhle on 3/28/2016.
 */
public class UserProfileTabFragment extends Fragment {

    private SharedPreferences appSharedPreferences;
    private GetAthleteProfileBackgroundTask backgroundTask;
    private TextView dateRegistered;
    private TextView toolbarTextView;
    private TextView totalPersonalRunsTextView;
    private TextView totalEventRunsTextView;
    private TextView weightTextView;
    private TextView heightTextView;
    private TextView nameSurnameTextView;
    private TextView userTypeTextView;
    private TextView userCountryTextView;
    private List<Run> runsList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.user_profile_tab_layout, container, false);
        initializeComponents(myView);
        return myView;
    }

    private void initializeComponents(View currentView) {
        dateRegistered = (TextView) currentView.findViewById(R.id.dateRegisteredTextView);
        toolbarTextView = (TextView) getActivity().findViewById(R.id.MainActivityTextView);
        toolbarTextView.setText("View Your Profile");
        totalPersonalRunsTextView = (TextView) currentView.findViewById(R.id.totalPersonalRunsTextView);
        totalEventRunsTextView = (TextView) currentView.findViewById(R.id.totalEventRunsTextView);
        weightTextView = (TextView) currentView.findViewById(R.id.weightText);
        heightTextView = (TextView) currentView.findViewById(R.id.heightText);
        nameSurnameTextView = (TextView) currentView.findViewById(R.id.profileNameSurnameTextView);
        userTypeTextView = (TextView) currentView.findViewById(R.id.userTypeTextView);
        userCountryTextView = (TextView) currentView.findViewById(R.id.countryTextView);
        appSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    private void setupData() {
        weightTextView.setText(Float.toString(appSharedPreferences.getFloat("weight", 0)));
        heightTextView.setText(Float.toString(appSharedPreferences.getFloat("height", 0)));
        nameSurnameTextView.setText(appSharedPreferences.getString("name", "") + " "+appSharedPreferences.getString("surname", ""));
        dateRegistered.setText(appSharedPreferences.getString("dateJoined", ""));
        totalPersonalRunsTextView.setText(Float.toString(appSharedPreferences.getFloat("totalPersonalRuns", 0)));
        totalEventRunsTextView.setText(Float.toString(appSharedPreferences.getFloat("totalEventRuns", 0)));
        userTypeTextView.setText("Athlete");
        userCountryTextView.setText(appSharedPreferences.getString("country", ""));
    }

    @Override
    public void onResume() {
        super.onResume();
        toolbarTextView.setText("View Your Profile");
        setupData();
    }


}
