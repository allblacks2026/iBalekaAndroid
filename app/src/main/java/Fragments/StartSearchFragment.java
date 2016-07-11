package Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import allblacks.com.Activities.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartSearchFragment extends Fragment {

    private CheckBox currentLocationCheckBox;
    private CheckBox currentCityEventsCheckBox;
    private CheckBox sortResultsCheckBox;
    private EditText searchCriteriaCheckBox;
    private Button searchEventsButton;
    public StartSearchFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View currentView =  inflater.inflate(R.layout.fragment_start_search, container, false);
        initializeComponents(currentView);
        return currentView;
    }

    private void initializeComponents(View currentView) {
        searchCriteriaCheckBox = (EditText) currentView.findViewById(R.id.SearchCriteriaEditText);
        currentLocationCheckBox = (CheckBox) currentView.findViewById(R.id
                .SearchNearEventsCheckBox);
        sortResultsCheckBox = (CheckBox) currentView.findViewById(R.id.SortByDateCheckBox);
        currentCityEventsCheckBox = (CheckBox) currentView.findViewById(R.id.CurrentCityEvents);
        searchEventsButton = (Button) currentView.findViewById(R.id.SearchEventsButton);
    }

}
