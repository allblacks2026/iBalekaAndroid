package Fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

import Models.CaloriesBurnt;
import allblacks.com.iBaleka.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CaloriesBurntFragment extends Fragment {

    private BarChart caloriesBurntChart;
    private List<CaloriesBurnt> caloriesBurntList;
    private TextView toolbarTextView;

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
        caloriesBurntList = new ArrayList<>();
        caloriesBurntChart = (BarChart) currentView.findViewById(R.id.CaloriesBurntChart);
        toolbarTextView = (TextView) getActivity().findViewById(R.id.MainActivityTextView);
        toolbarTextView.setText("Your Calories Burnt");
        createSampleCalorieObjects();
        setupChart();
    }

    private void createSampleCalorieObjects()
    {
        CaloriesBurnt one = new CaloriesBurnt("2016/05/05", 490.28);
        CaloriesBurnt two = new CaloriesBurnt("2016/05/08", 490.11);
        CaloriesBurnt three = new CaloriesBurnt("2016/05/12", 394.18);
        CaloriesBurnt four = new CaloriesBurnt("2016/05/15", 400.23);
        CaloriesBurnt five = new CaloriesBurnt("2016/05/18", 412.11);
        CaloriesBurnt six = new CaloriesBurnt("2016/05/22", 333.54);
        CaloriesBurnt seven = new CaloriesBurnt("2016/05/25", 280.55);
        CaloriesBurnt eight = new CaloriesBurnt("2016/05/28", 290.67);
        CaloriesBurnt nine = new CaloriesBurnt("2016/05/30", 300.01);

        caloriesBurntList.add(one);
        caloriesBurntList.add(two);
        caloriesBurntList.add(three);
        caloriesBurntList.add(four);
        caloriesBurntList.add(five);
        caloriesBurntList.add(six);
        caloriesBurntList.add(seven);
        caloriesBurntList.add(eight);
        caloriesBurntList.add(nine);
    }



    private void setupChart()
    {
        List<String> dateEntries = new ArrayList<>();
       List<BarEntry> entries = new ArrayList<>();
        for (int a = 0; a < caloriesBurntList.size(); a++) {
            CaloriesBurnt current = caloriesBurntList.get(a);
            entries.add(new BarEntry(a, Float.valueOf(String.valueOf(current.getCalorieCount()))));
            dateEntries.add(current.getDateBurnt());
        }

        String [] dates = new String[dateEntries.size()];
        for (int a = 0; a < dateEntries.size(); a++) {
            dates[a] = dateEntries.get(a);
        }

        class MyXAxis implements AxisValueFormatter {

            private String [] values;
            public MyXAxis(String [] values) {
                this.values = values;
            }
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return values[(int) value];
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        }

        MyXAxis axisFormat = new MyXAxis(dates);
        BarDataSet dataSet = new BarDataSet(entries, "CaloriesBurnt");
        dataSet.setValueTextColor(Color.RED);
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f);
        dataSet.setColor(getResources().getColor(R.color.primary));
        caloriesBurntChart.setData(barData);
        caloriesBurntChart.setFitBars(true);
        XAxis chartXAxis = caloriesBurntChart.getXAxis();
        chartXAxis.setValueFormatter(axisFormat);
        chartXAxis.setLabelRotationAngle(70f);
        chartXAxis.setGranularity(1f);
        chartXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        caloriesBurntChart.setDescription("");
        caloriesBurntChart.setNoDataText("No Calorie Data Available");
        caloriesBurntChart.invalidate();
    }

}
