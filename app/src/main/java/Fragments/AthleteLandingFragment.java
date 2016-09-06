package Fragments;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import BackgroundTasks.AthleteDataBackgroundTask;
import allblacks.com.iBaleka.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AthleteLandingFragment extends Fragment implements View.OnClickListener {

    private SharedPreferences globalPreferences;
    private SharedPreferences.Editor editor;
    private TextView toolbarTextView;
    private Button startRunButton;
    private TextView totalDistanceLabel;
    private TextView totalCaloriesLabel;
    private TextView totalPersonalRunsLabel;
    private TextView totalEventRunsLabel;

    public AthleteLandingFragment() {
        // Required empty public constructor
    }

    private void initializeComponents(View currentView) {
        globalPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = globalPreferences.edit();
        toolbarTextView = (TextView) getActivity().findViewById(R.id.MainActivityTextView);
        String welcomeString = "Hi, " + globalPreferences.getString("name", "") + "!";
        toolbarTextView.setText(welcomeString);
        totalDistanceLabel = (TextView) currentView.findViewById(R.id.AthleteLandingTotalDistanceTextView);
        totalCaloriesLabel = (TextView) currentView.findViewById(R.id.AthleteLandingTotalCaloriesTextView);
        totalPersonalRunsLabel = (TextView) currentView.findViewById(R.id.AthleteLandingTotalPersonalRunsTextView);
        totalEventRunsLabel = (TextView) currentView.findViewById(R.id.AthleteLandingTotalEventRunsTextView);
        startRunButton = (Button) currentView.findViewById(R.id.AthleteLandingStartRunButton);
        startRunButton.setOnClickListener(this);

    }

    private void populateData()
    {
        totalDistanceLabel.setText(Float.toString(globalPreferences.getFloat("totalDistance", 0)));
        totalCaloriesLabel.setText(Float.toString(globalPreferences.getFloat("totalCalories", 0)));
        totalPersonalRunsLabel.setText(Float.toString(globalPreferences.getFloat("totalPersonalRuns", 0)));
        totalEventRunsLabel.setText(Float.toString(globalPreferences.getFloat("totalEventRuns", 0)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View currentView = inflater.inflate(R.layout.fragment_athlete_landing, container, false);
        initializeComponents(currentView);
        return currentView;
    }
    @Override
    public void onPause() {
        setRetainInstance(true);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

   private void runBackgroundTask(Bundle savedInstanceState)
   {
       if (savedInstanceState == null) {
           try {
               AthleteDataBackgroundTask backgroundTask = new AthleteDataBackgroundTask(getActivity());
               backgroundTask.execute();
           } catch (Exception error) {
               displayMessage("Error Getting Feed Data", error.getMessage());
           }
       }
   }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        runBackgroundTask(savedInstanceState);
        populateData();
    }

    private void displayMessage(String title, String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(title).setMessage(message).setPositiveButton("Got It", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    private void loadRunning()
    {
        RunningFragment fragment = new RunningFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.MainActivityContentArea, fragment, "RunningFragment");
        transaction.addToBackStack("RunningFragment");
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        loadRunning();
    }
}
