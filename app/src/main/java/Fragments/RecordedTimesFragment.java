package Fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import DataAccessLayer.iBalekaClient;
import RetroFitModels.Run;
import RetroFitModels.RunArray;
import allblacks.com.iBaleka.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordedTimesFragment extends Fragment implements View.OnClickListener {

    private Button startDateButton;
    private Button endRunButton;
    private TextView startRunLabel;
    private TextView endRunLabel;
    private Button generateReportButton;
    private BarChart chart;
    private DatePickerDialog datePickerDialog;
    private int [] startArray = new int[3];
    private int [] endArray = new int[3];

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
        startDateButton.setOnClickListener(this);
        endRunButton = (Button) currentView.findViewById(R.id.RecordedTimesEndButton);
        endRunButton.setOnClickListener(this);
        startRunLabel = (TextView) currentView.findViewById(R.id.RecordedTimesStartLabel);
        endRunLabel = (TextView) currentView.findViewById(R.id.RecordedTimesEndLabel);
        generateReportButton = (Button) currentView.findViewById(R.id.RecordedTimesGenerateReportButton);
        generateReportButton.setOnClickListener(this);
        chart = (BarChart) currentView.findViewById(R.id.RecordedTimesBarChart);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.RecordedTimesStartButton:
                datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        startArray[0] = year;
                        startArray[1] = monthOfYear;
                        startArray[2] = dayOfMonth;
                    }
                }, 0,0,0);
                datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                datePickerDialog.show();
                break;
            case R.id.RecordedTimesEndButton:
                datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endArray[0] = year;
                        endArray[1] = monthOfYear;
                        endArray[2] = dayOfMonth;
                    }
                }, 0, 0, 0);
                datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                datePickerDialog.show();
        }
    }

    private void processReportGeneration() {

        boolean canSendToServer = false;
        for (int a = 0; a < startArray.length; a++) {
            if (startArray[a] != 0 || endArray[a] != 0) {
                canSendToServer = true;
            } else {
                canSendToServer = false;
            }
        }
        if (canSendToServer) {
            try {
                final ProgressDialog dialog = new ProgressDialog(getActivity());
                dialog.setTitle("Processing Report Request");
                dialog.setMessage("Please wait while the system generates a report based on your specified dates. This may take a few seconds depending on your internet connection");
                dialog.setCancelable(false);
                dialog.show();
                final DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                String startDate = startArray[0] + "/" + startArray[1] + "/" + startArray[2];
                String endDate = endArray[0] + "/" + endArray[1] + "/" + endArray[2];
                final Date startDateObj = formatter.parse(startDate);
                final Date endDateObj = formatter.parse(endDate);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://ibalekaapi.azurewebsites.net")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                iBalekaClient client = retrofit.create(iBalekaClient.class);
                Call<RunArray> runArrayCall = client.getAthletePersonalRuns(preferences.getInt("athleteId", 0));
                runArrayCall.enqueue(new Callback<RunArray>() {
                    @Override
                    public void onResponse(Call<RunArray> call, Response<RunArray> response) {
                        List<Run> finalRunList = new ArrayList<Run>();
                        if (dialog.isShowing()) {
                            dialog.cancel();
                            if (response.code() == 200) {
                                RunArray runArray = response.body();
                                List<Run> runList = runArray.getModel();
                                if (runList != null && runList.size() > 0) {
                                    for (Run currentRun : runList) {
                                        try {
                                            Date thisDate = formatter.parse(currentRun.getDateRecorded());
                                            if (thisDate.compareTo(startDateObj) >= 0 && thisDate.compareTo(endDateObj) <= 0) {
                                                finalRunList.add(currentRun);
                                            }
                                        } catch (Exception error) {
                                            continue;
                                        }
                                    }

                                    populateGraph(finalRunList);

                                } else {
                                    displayMessage("No Runs Found", "You do not have any runs recorded on the system. Why don't you join the fun and start running?");
                                }
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<RunArray> call, Throwable t) {
                        if (dialog.isShowing()) {
                            dialog.cancel();
                            displayMessage("Error Generating Report", "A critical error occurred while generating the report. \n" + t.getMessage());
                        }
                    }
                });
            } catch (Exception error) {
                displayMessage("Error Generating Report", error.getMessage());
            }
        } else {

        }

    }
    private void displayMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title).setMessage(message).setCancelable(false).setPositiveButton("Got It", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void populateGraph(final List<Run> runList) {
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        List<BarEntry> entryList = new ArrayList<>();
        for (int a = 0; a < runList.size(); a++) {
            Run currentRun = runList.get(a);
            try {
                Date startTime = formatter.parse(currentRun.getStartTime());
                Date endTime = formatter.parse(currentRun.getEndTime());
                long timeDifference = endTime.getTime() - startTime.getTime();
                double time = (double) timeDifference;
                entryList.add(new BarEntry(a, (float) time));
            } catch (Exception error) {

            }

            BarDataSet barDataSet = new BarDataSet(entryList, "Run Times Graph");
            barDataSet.setColor(Color.BLUE);
            barDataSet.setValueTextColor(Color.RED);
            BarData barData = new BarData(barDataSet);
            barData.setBarWidth(0.9f);
            XAxis barXaxis = chart.getXAxis();
            barXaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            barXaxis.setTextColor(Color.RED);

            AxisValueFormatter chartFormatter = new AxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return runList.get((int) value).getDateRecorded();
                }

                @Override
                public int getDecimalDigits() {
                    return 0;
                }
            };
            barXaxis.setValueFormatter(chartFormatter);
            chart.setFitBars(true);
            chart.setData(barData);
            chart.invalidate();
        }

    }
}
