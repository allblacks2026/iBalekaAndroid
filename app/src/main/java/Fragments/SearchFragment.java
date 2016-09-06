package Fragments;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;

import java.util.ArrayList;
import java.util.List;

import RetroFitModels.Event;
import allblacks.com.iBaleka.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private PlaceAutocompleteFragment autoCompleteFragment;
    private RecyclerView quickEventsRecyclerView;
    private List<Event> eventsList = new ArrayList<>();

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_search, container, false);
        initializeComponents(currentView);

        return currentView;
    }

    private void initializeComponents(View currentView) {

        quickEventsRecyclerView = (RecyclerView) currentView.findViewById(R.id.quickEventsRecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        quickEventsRecyclerView.setLayoutManager(manager);
    }


    public void displayMessage(String title, String message) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(getActivity());
        messageBox.setTitle(title);
        messageBox.setMessage(message);
        messageBox.setPositiveButton("Got It", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        messageBox.show();
    }

}
