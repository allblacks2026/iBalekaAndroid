package Fragments;


import android.app.Fragment;
import android.os.Bundle;
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
public class EventsPersonalRunFragment extends Fragment {

    private Button startDateButton;
    private Button endRunButton;
    private TextView startRunLabel;
    private TextView endRunLabel;
    private Button generateReportButton;
    private BarChart chart;


    public EventsPersonalRunFragment() {

    }

    private void initializeComponents(View currentView) {
        startDateButton = (Button) currentView.findViewById(R.id.EventPersonalStartButton);
        endRunButton = (Button) currentView.findViewById(R.id.EventPersonalEndButton);
        startRunLabel = (TextView) currentView.findViewById(R.id.EventPersonalStartLabel);
        endRunLabel = (TextView) currentView.findViewById(R.id.EventPersonalEndLabel);
        generateReportButton = (Button) currentView.findViewById(R.id.EventPersonalGenerateReportButton);
        chart = (BarChart) currentView.findViewById(R.id.EventPersonalBarChart);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events_personal_run, container, false);
    }

}
