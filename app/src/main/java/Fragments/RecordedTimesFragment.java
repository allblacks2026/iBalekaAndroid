package Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;

import allblacks.com.iBaleka.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordedTimesFragment extends Fragment {

    private Button startDateButton;
    private Button endRunButton;
    private TextView startRunLabel;
    private TextView endRunLabel;
    private Button generateReportButton;
    private BarChart chart;

    public RecordedTimesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_recorded_times, container, false);
        initializeComponents(currentView);
        return currentView;
    }

    private void initializeComponents(View currentView) {
        startDateButton = (Button) currentView.findViewById(R.id.RecordedTimesStartButton);
        endRunButton = (Button) currentView.findViewById(R.id.RecordedTimesEndButton);
        startRunLabel = (TextView) currentView.findViewById(R.id.RecordedTimesStartLabel);
        endRunLabel = (TextView) currentView.findViewById(R.id.RecordedTimesEndLabel);
        generateReportButton = (Button) currentView.findViewById(R.id.RecordedTimesGenerateReportButton);
        chart = (BarChart) currentView.findViewById(R.id.RecordedTimesBarChart);

    }

}
