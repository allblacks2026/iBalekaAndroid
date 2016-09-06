package Fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import DataAccessLayer.iBalekaClient;
import RetroFitModels.Club;
import RetroFitModels.ClubMember;
import RetroFitModels.ResponseBody;
import allblacks.com.iBaleka.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClubDetailsFragment extends Fragment implements View.OnClickListener {

    private TextView clubNameTextView;
    private TextView clubDescriptionTextView;
    private TextView clubLocationTextView;
    private TextView clubMembersTextView;
    private TextView clubEventsTextView;
    private TextView toolbarTextView;
    private Button joinClubButton;
    private Club selectedClub;

    public ClubDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_club_details, container, false);
        initializeComponents(currentView);
        return currentView;
    }

    private void initializeComponents(View currentView) {
        clubNameTextView = (TextView) currentView.findViewById(R.id.ClubResultsClubNameTextView);
        clubDescriptionTextView = (TextView) currentView.findViewById(R.id.ClubResultsClubDescriptionTextView);
        clubLocationTextView = (TextView) currentView.findViewById(R.id.ClubResultsClubLocationTextView);
        clubMembersTextView = (TextView) currentView.findViewById(R.id.ClubResultsTotalMembersTextView);
        clubEventsTextView = (TextView) currentView.findViewById(R.id.ClubResultsTotalEventsTextView);
        toolbarTextView = (TextView) getActivity().findViewById(R.id.MainActivityTextView);
        joinClubButton = (Button) currentView.findViewById(R.id.ClubResultsJoinClubButton);
        joinClubButton.setOnClickListener(this);

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectedClub = (Club) getArguments().getSerializable("Club");
        toolbarTextView.setText(selectedClub.getName());
        setupClubDetails();
    }

    private void setupClubDetails()
    {
        clubNameTextView.setText(selectedClub.getName());
        clubDescriptionTextView.setText(selectedClub.getDescription());
        clubLocationTextView.setText(selectedClub.getLocation());
        if (selectedClub.getClubMember() != null) {
            clubMembersTextView.setText(selectedClub.getClubMember().size());
        }
        if (selectedClub.getEvent() != null) {
            clubEventsTextView.setText(selectedClub.getEvent().size());
        }
    }

    @Override
    public void onClick(View v) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Joining The Club");
        dialog.setMessage("Please wait while we register you to the club");
        dialog.setCancelable(false);
        dialog.show();
       if (selectedClub != null) {
           DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
           SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
           SharedPreferences.Editor editor = appPreferences.edit();
           ClubMember newMember = new ClubMember(appPreferences.getInt("athleteId", 0), selectedClub.getClubId(), format.format(Calendar.getInstance().getTimeInMillis()), "", "Joined");
           Retrofit retrofit = new Retrofit.Builder()
                   .baseUrl("https://ibalekaapi.azurewebsites.net/")
                   .addConverterFactory(GsonConverterFactory.create())
                   .build();
           iBalekaClient client = retrofit.create(iBalekaClient.class);
           final Call<ResponseBody> registerAthleteCall = client.registerClubMember(newMember);
           registerAthleteCall.enqueue(new Callback<ResponseBody>() {
               @Override
               public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                   if (dialog.isShowing()) {
                       dialog.cancel();
                   }
                   if (response.code() == 200) {
                       ResponseBody body = response.body();
                       if (!body.isDidError()) {
                          displayMessage("Successful Club Registration", "You have successfully registered for the selected club!");
                       } else {
                           displayMessage("Registration Error", "An Error occurred with registration. Please check that you are not registered to the selected club");
                       }
                   } else {
                       displayMessage("Error Registering for Club", "An Error occurred with club registration: \n"+response.message());
                   }
               }

               @Override
               public void onFailure(Call<ResponseBody> call, Throwable t) {
                   if (dialog.isShowing()) {
                       dialog.cancel();
                   }

                   displayMessage("Error Registering for Club", t.getMessage());

               }
           });
       } else {
           displayMessage("Error Joining Club", "The system did not receive the club you wanting to join. Please try again");
       }
    }

    public void displayMessage(String title, String message) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(getActivity());
        messageBox.setTitle(title).setMessage(message).setPositiveButton("Got it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }
}
