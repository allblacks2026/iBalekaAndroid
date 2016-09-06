package Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import allblacks.com.iBaleka.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportExplorerFragment extends Fragment implements View.OnClickListener {

    private TextView toolbarTextView;
    private Button caloriesBurntButton;
    private Button distanceRanButton;
    private Button eventPersonalRunButton;
    private Button recordedTimesButton;
    private Button clubButton;

    public ReportExplorerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View currentView = inflater.inflate(R.layout.fragment_report_explorer, container, false);
        initializeComponents(currentView);
        return currentView;
    }

    private void initializeComponents(View currentView) {
        toolbarTextView = (TextView) getActivity().findViewById(R.id.MainActivityTextView);
        toolbarTextView.setText("Report Explorer");
        caloriesBurntButton = (Button) currentView.findViewById(R.id.ReportExplorerCaloriesBurntButton);
        distanceRanButton = (Button) currentView.findViewById(R.id.ReportExplorerDistanceButton);
        eventPersonalRunButton = (Button) currentView.findViewById(R.id.ReportExplorerEventAndPersonalRunButton);
        recordedTimesButton = (Button) currentView.findViewById(R.id.ReportExplorerTimesOverRangeButton);
        clubButton = (Button) currentView.findViewById(R.id.ReportExplorerClubYouButton);

        caloriesBurntButton.setOnClickListener(this);
        distanceRanButton.setOnClickListener(this);
        eventPersonalRunButton.setOnClickListener(this);
        recordedTimesButton.setOnClickListener(this);
        clubButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ReportExplorerCaloriesBurntButton:
                CaloriesBurntFragment caloriesBurntFragment = new CaloriesBurntFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.MainActivityContentArea, caloriesBurntFragment, "CaloriesBurntFragment")
                        .addToBackStack("CaloriesBurntFragment")
                        .commit();
                break;
            case R.id.ReportExplorerDistanceButton:
                DistanceRanFragment distanceRanFragment = new DistanceRanFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.MainActivityContentArea, distanceRanFragment, "DistanceRanFragment")
                        .addToBackStack("DistanceRanFragment")
                        .commit();
                break;
            case R.id.ReportExplorerEventAndPersonalRunButton:
                EventsPersonalRunFragment eventsPersonalRunFragment = new EventsPersonalRunFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.MainActivityContentArea, eventsPersonalRunFragment, "EventPersonalRunFragment")
                        .addToBackStack("EventPersonalRunFragment")
                        .commit();
                break;
            case R.id.ReportExplorerTimesOverRangeButton:
                RecordedTimesFragment recordedTimesFragment = new RecordedTimesFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.MainActivityContentArea, recordedTimesFragment, "RecordedTimesFragment")
                        .addToBackStack("RecordedTimesFragment")
                        .commit();
                break;

            case R.id.ReportExplorerClubYouButton:

                break;


        }

    }
}
