package Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;

import allblacks.com.iBaleka.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AthleteLandingFragment extends Fragment {

    private SharedPreferences globalPreferences;
    private TextView toolbarTextView;
    private Button startRunButton;
    private TextView totalDistanceLabel;
    private TextView totalCaloriesLabel;
    private TextView sevenDayDistanceTextView;

    public AthleteLandingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        globalPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        toolbarTextView = (TextView) getActivity().findViewById(R.id.MainActivityTextView);
        String welcomeString = "Hi, " + globalPreferences.getString("Name", "") + "!";
        toolbarTextView.setText(welcomeString);
        return inflater.inflate(R.layout.fragment_athlete_landing, container, false);
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

    private void initializeComponents(View currentView) {

    }

}
