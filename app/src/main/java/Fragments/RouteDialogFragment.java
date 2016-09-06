package Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import DataAccessLayer.iBalekaClient;
import RetroFitModels.EventRegistration;
import RetroFitModels.EventRoute;
import RetroFitModels.ResponseBody;
import RetroFitModels.Route;
import allblacks.com.iBaleka.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Okuhle on 9/5/2016.
 */
public class RouteDialogFragment extends DialogFragment {

    private List<EventRoute> selectedRoutes;
    private LayoutInflater inflater;
    private AlertDialog.Builder builder;
    private MaterialSpinner routeSpinner;
    private List<String> routeDetails = new ArrayList<>();
    private SharedPreferences appPreferences;
    private AlertDialog alertDialog;
    private View currentView;

    public RouteDialogFragment()
    {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        inflater = getActivity().getLayoutInflater();
        currentView = inflater.inflate(R.layout.route_selection_layout, null);
        appPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        builder.setView(currentView);
        routeSpinner = (MaterialSpinner) currentView.findViewById(R.id.EventRouteSpinner);
        builder.setTitle("Route Selection");
        builder.setMessage("Please select the route you wish to register for");
        builder.setPositiveButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectedRouteIndex = routeSpinner.getSelectedIndex();
                EventRoute selectedRoute = selectedRoutes.get(selectedRouteIndex);
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                EventRegistration registration = new EventRegistration("Joined", appPreferences.getInt("athleteId", 0), dateFormat.format(Calendar.getInstance().getTime()), false, selectedRoute.getEventId(), selectedRoute.getRouteId());
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://ibalekaapi.azurewebsites.net/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                iBalekaClient client = retrofit.create(iBalekaClient.class);
                Call<ResponseBody> registerCall = client.registerAthleteToEvent(registration);
                registerCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() == 200) {
                            ResponseBody body = response.body();
                            if (!body.isDidError()) {
                                displayMessage("Registration Successful", "You have successfully registered for the selected event and route", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alertDialog.dismiss();
                                    }
                                });
                            }
                        } else {
                            displayMessage("Error Registering For Event", "An Error Occured with registering with event\n" + response.message(), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.dismiss();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        displayMessage("Error Registering For Event", t.getMessage(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                            }
                        });
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        ArrayList<EventRoute> routes =  getArguments().getParcelableArrayList("EventRoute");
        selectedRoutes = new ArrayList<>(routes);
        for (EventRoute route : selectedRoutes) {
            String currentRoute = route.getTitle();
            routeDetails.add(currentRoute);
        }
        routeSpinner.setItems(routeDetails);
       alertDialog = builder.create();
        return alertDialog;
    }

    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder box = new AlertDialog.Builder(currentView.getContext());
        box.setTitle(title);
        box.setMessage(message);
        box.setPositiveButton("Got It", listener);
        box.show();
    }
}
