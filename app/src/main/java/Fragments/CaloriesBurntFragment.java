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
import com.github.mikephil.charting.formatter.AxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import DataAccessLayer.iBalekaClient;
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
public class CaloriesBurntFragment extends Fragment implements View.OnClickListener {

    private BarChart caloriesBurntChart;
    private TextView toolbarTextView;
    private TextView startDateLabel;
    private TextView endDateLabel;
    private Button startDateButton;
    private Button endDateButton;
    private Button generateReportButton;
    private int [] startArray = new int[3];
    private int [] endArray = new int[3];

    public CaloriesBurntFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_calories_burnt, container, false);
        initializeComponents(currentView);
        return currentView;
    }

    private void initializeComponents(View currentView) {
        caloriesBurntChart = (BarChart) currentView.findViewById(R.id.CaloriesBurntChart);
        toolbarTextView = (TextView) getActivity().findViewById(R.id.MainActivityTextView);
        toolbarTextView.setText("Your Calories Burnt");
        startDateLabel = (TextView) currentView.findViewById(R.id.CaloriesBurntStartDateLabel);
        endDateLabel = (TextView) currentView.findViewById(R.id.CaloriesBurntEndDateLabel);
        startDateButton = (Button) currentView.findViewById(R.id.CaloriesBurntStartDateButton);
        endDateButton = (Button) currentView.findViewById(R.id.CaloriesBurntEndDateButton);
        generateReportButton = (Button) currentView.findViewById(R.id.CaloriesBurntGenerateReportButton);
        endDateButton.setOnClickListener(this);
        startDateButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.CaloriesBurntStartDateButton:
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        startArray[0] = year;
                        startArray[1] = monthOfYear + 1;
                        startArray[2] = dayOfMonth;
                    }
                }, 0,0,0);
                dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                dialog.show();
                break;
            case R.id.CaloriesBurntEndDateButton:
                DatePickerDialog dialog1 = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endArray[0] = year;
                        endArray[1] = monthOfYear + 1;
                        endArray[2] = dayOfMonth;
                    }
                }, 0,0,0);
                dialog1.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                dialog1.show();
                break;
            case R.id.CaloriesBurntGenerateReportButton:
                DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                boolean isValid = true;
                for (int a = 0; a < startArray.length; a++) {
                    if (startArray[a] == 0 || endArray[a] == 0) {
                        isValid = false;
                        break;
                    }
                }

                if (isValid) {
                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setTitle("Getting Calorie Information");
                    progressDialog.setMessage("Please wait while we fetch your calorie information. This may take a few seconds depending on your internet connection");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("htpps://ibalekaapi.azurewebsites.net/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    iBalekaClient client = retrofit.create(iBalekaClient.class);
                    Call<RunArray> responseBody = client.getAllRuns(appPreferences.getInt("ahtleteId", 0));
                    responseBody.enqueue(new Callback<RunArray>() {
                        @Override
                        public void onResponse(Call<RunArray> call, Response<RunArray> response) {
                            if (progressDialog.isShowing()) {
                                progressDialog.cancel();
                            }
                            if (response.code() == 200) {

                            } else {
                                displayMessage("Error Getting Calorie Data", "An error occurred when getting calorie data" +response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<RunArray> call, Throwable t) {

                        }
                    });
                } else {
                    displayMessage("Error Generating Report", "Please ensure you have selected a valid start date and a valid end date");
                }


                break;
        }
    }

    public void displayMessage(String title, String message) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(getActivity());
        messageBox.setTitle(title).setMessage(message).setPositiveButton("Got It", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }
}
