package Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import DataAccessLayer.iBalekaService;
import RetroFitModels.Run;
import allblacks.com.iBaleka.R;
import cz.msebera.android.httpclient.Header;

public class DistanceRanFragment extends Fragment implements View.OnClickListener {


    private Button startDateButton;
    private Button endDateButton;
    private Button generateReportButton;
    private TextView startRunLabel;
    private TextView endRunLabel;
    private BarChart distanceRanChart;
    private int [] startDates = new int[3];
    private int [] endDates = new int[3];
    private DatePickerDialog datePickerDialog;
    private SharedPreferences appPreferences;

    private List<Run> runsList = new ArrayList<>();

    public DistanceRanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_distance_ran, container, false);

        return currentView;
    }

    private void initializeComponents(View currentView) {
        startDateButton = (Button) currentView.findViewById(R.id.DistanceRanStartDateButton);
        endDateButton = (Button) currentView.findViewById(R.id.DistanceRanEndDateButton);
        generateReportButton = (Button) currentView.findViewById(R.id.DistanceRanGenerateReportButton);
        startDateButton.setOnClickListener(this);
        endDateButton.setOnClickListener(this);
        generateReportButton.setOnClickListener(this);
        distanceRanChart = (BarChart) currentView.findViewById(R.id.DistanceRanBarChart);
        appPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.DistanceRanStartDateButton:
                datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        startDates[0] = year;
                        startDates[1] = monthOfYear;
                        startDates[2] = dayOfMonth;
                    }
                }, 0,0,0);
                datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                datePickerDialog.setTitle("Pick a Start Date");
                datePickerDialog.show();
                break;
            case R.id.DistanceRanEndDateButton:
                datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endDates[0] = year;
                        endDates[1] = monthOfYear;
                        endDates[2] = dayOfMonth;
                    }
                }, 0, 0, 0);
                datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                datePickerDialog.setTitle("Pick an End Date");
                datePickerDialog.show();
                break;
            case R.id.DistanceRanGenerateReportButton:
                boolean isValid = false;
                for (int a = 0; a < startDates.length; a++) {
                    if (startDates[a] == 0 || endDates[a] == 0) {
                        isValid = false;
                    } else {
                        isValid = true;
                    }
                }
                if (isValid) {
                    String startDate = startDates[0] + "-" + startDates[1] + "-"+startDates[2];
                    String endDate = endDates[0] +"-"+endDates[1] +"-"+endDates[2];
                    int athleteId = Integer.parseInt(appPreferences.getString("AthleteID", ""));


                }
        }
    }


    private void displayMessage(String title, String message) {
        AlertDialog.Builder dialogMessage = new AlertDialog.Builder(getActivity());
        dialogMessage.setTitle(title).setMessage(message).setPositiveButton("Got It", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogMessage.show();
    }
}
