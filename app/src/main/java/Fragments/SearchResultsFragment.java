package Fragments;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import allblacks.com.iBaleka.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchResultsFragment extends Fragment {
    private RecyclerView searchResultsRecyclerView;
    private SharedPreferences appSharedPreferences;
    private TabLayout searchTabLayout;
    private TextView toolbarTextView;

    public SearchResultsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_search_results, container, false);
        initializeComponents(currentView);
        return currentView;
    }

    private void initializeComponents(View thisView) {
        toolbarTextView = (TextView) getActivity().findViewById(R.id.MainActivityTextView);
        toolbarTextView.setText("View Search Results");
    }

    public void displayMessage(String title, String message) {
        AlertDialog.Builder dialogMessage = new AlertDialog.Builder(getActivity());
        dialogMessage.setTitle(title);
        dialogMessage.setMessage(message);
        dialogMessage.setPositiveButton("Got It", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogMessage.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        toolbarTextView.setText("View Search Results");
    }
}
