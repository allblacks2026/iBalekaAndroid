package Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import Adapters.AthleteActivityAdapter;
import DataAccessLayer.iBalekaClient;
import RetroFitModels.Event;
import RetroFitModels.Run;
import RetroFitModels.RunArray;
import allblacks.com.iBaleka.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserActivityTabFragment extends Fragment {


    private RecyclerView activityRecyclerView;
    private CardView activityCardView;
    private TextView title, distance, dateDone;
    private SharedPreferences appPreferences;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.user_activity_fragment, container, false);
        activityRecyclerView = (RecyclerView) myView.findViewById(R.id.userActivityRecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this.getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        activityRecyclerView.setLayoutManager(manager);
        activityCardView = (CardView) myView.findViewById(R.id.userActivityCardView);
        title = (TextView) myView.findViewById(R.id.titleTextView);
        distance = (TextView) myView.findViewById(R.id.totalDistanceTextView);
        dateDone = (TextView) myView.findViewById(R.id.dateDoneTextView);
        appPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        getAthleteRuns();
        return myView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void getAthleteRuns()
    {

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Getting Running Data");
        dialog.setMessage("Please wait while we fetch your running data. This may take a few seconds, depending on your internet connection");
        dialog.setCancelable(false);
        dialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ibalekaapi.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        iBalekaClient client = retrofit.create(iBalekaClient.class);
        Call<RunArray> runArrayCall = client.getAllRuns(appPreferences.getInt("athleteId", 0));
        runArrayCall.enqueue(new Callback<RunArray>() {
            @Override
            public void onResponse(Call<RunArray> call, Response<RunArray> response) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                List<Run> athleteRunsList = new ArrayList<Run>();
                if (response.code() == 200) {

                    RunArray array = response.body();
                    athleteRunsList = array.getModel();
                    if (athleteRunsList.size() == 0 || array.getModel() == null || array.getModel().size() == 0 ||athleteRunsList.size() == 0) {
                        displayToast("No Runs Found. Start Running!");
                    } else {
                        AthleteActivityAdapter adapter = new AthleteActivityAdapter();
                        adapter.setAthleteActivityList(athleteRunsList);
                        activityRecyclerView.setAdapter(adapter);
                    }
                } else if (response.code() == 500) {
                    displayToast("No Runs were found. Start Running today!");
                } else {
                    displayToast("No runs were found. Start Running today");
                }
            }
            @Override
            public void onFailure(Call<RunArray> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                displayMessage("Error Getting Runs", t.getMessage());
            }
        });

    }
    public void displayToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    public void displayMessage(String title, String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(title).setMessage(message).setPositiveButton("Got It", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setCancelable(false).show();
    }


}
