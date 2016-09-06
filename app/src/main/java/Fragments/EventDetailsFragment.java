package Fragments;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import DataAccessLayer.iBalekaClient;
import Listeners.MainActivityListener;
import RetroFitModels.Athlete;
import RetroFitModels.Checkpoint;
import RetroFitModels.Event;
import RetroFitModels.EventRegistration;
import RetroFitModels.EventRoute;
import RetroFitModels.ResponseBody;
import RetroFitModels.Route;
import allblacks.com.iBaleka.MainActivity;
import allblacks.com.iBaleka.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailsFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {

    private SharedPreferences eventPreferences;
    private TextView eventTitleTextView;
    private TextView eventLocationTextView;
    private TextView eventTimeTextView;
    private TextView eventDistanceTextView;
    private TextView eventConditionsTextView;
    private TextView startPointTextView;
    private TextView endPointTextView;
    private TextView eventDateTextView;
    private TextView eventRouteCountTextView;

    private Button registerEventButton;
    private TextView toolbarTextView;
    private MapView eventMapView;
    private GoogleMap mapObject;
    private List<Checkpoint> checkpointList;
    private List<Marker> markersList = new ArrayList<>();
    private MainActivityListener listener;

    private Event selectedEvent;
    private List<EventRoute> eventRoutes;
    private List<Route> eventRoutesList = new ArrayList<>();
    private List<RetroFitModels.Checkpoint> routeCheckpointList = new ArrayList<>();
    private SharedPreferences appPreferences;


    public EventDetailsFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View currentView = inflater.inflate(R.layout.fragment_event_details, container, false);
        initializeComponents(currentView, savedInstanceState);
        return currentView;
    }

    private void initializeComponents(View currentView, Bundle savedInstanceState) {
        listener = new MainActivityListener(getActivity());
        toolbarTextView = (TextView) getActivity().findViewById(R.id.MainActivityTextView);
        eventTitleTextView = (TextView) currentView.findViewById(R.id.EventDetails_EventNameLabel);
        eventTimeTextView = (TextView) currentView.findViewById(R.id.EventDetailsTimeLabel);
        eventLocationTextView = (TextView) currentView.findViewById(R.id.EventDetails_LocationLabel);
        eventDistanceTextView = (TextView) currentView.findViewById(R.id.EventDetailsDistanceLabel);
        eventConditionsTextView = (TextView) currentView.findViewById(R.id.EventDetailsConditionLabel);
        registerEventButton = (Button) currentView.findViewById(R.id.EventDetailsRegisterForEvent);
        registerEventButton.setOnClickListener(this);
        eventDateTextView = (TextView) currentView.findViewById(R.id.EventDetails_DateLabel);
        eventRouteCountTextView = (TextView) currentView.findViewById(R.id.EventDetailsRouteCountTextView);
        eventMapView = (MapView) currentView.findViewById(R.id.EventDetailsMappedRoute);
        eventMapView.onCreate(savedInstanceState);
        eventMapView.getMapAsync(this);
        appPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        getEventDetails();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        eventMapView.onResume();
    }

    @Override
    public void onPause() {
        eventMapView.onPause();
        super.onPause();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapObject = googleMap;
        mapObject.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        UiSettings mapSettings = mapObject.getUiSettings();
        mapSettings.setZoomControlsEnabled(true);
        mapSettings.setZoomGesturesEnabled(true);
        mapSettings.setCompassEnabled(true);
        mapSettings.setMapToolbarEnabled(true);

        if (eventRoutesList != null) {
            for (int a = 0; a < eventRoutesList.size(); a++) {
                plotRouteToMap(eventRoutesList.get(a).getCheckpoint());
            }
        }

    }

    @Override
    public void onDestroy() {
        mapObject.clear();
        super.onDestroy();
    }

    private void getEventDetails()
    {
        selectedEvent = ((MainActivity) getActivity()).getSelectedEvent();
        eventRoutesList = ((MainActivity) getActivity()).getRoutesList();
        if (selectedEvent != null && eventRoutesList != null) {
            setupRouteInformation();
        }
    }

    private void setupRouteInformation()
    {
        StringBuilder eventBuilder = new StringBuilder();
        int routeCount = routeCheckpointList.size() +1;
        eventTitleTextView.setText(selectedEvent.getTitle());
        eventLocationTextView.setText(selectedEvent.getLocation());
        eventTimeTextView.setText(selectedEvent.getTime());
        eventDateTextView.setText(selectedEvent.getDate());
        eventConditionsTextView.setText(selectedEvent.getDescription());
        eventRouteCountTextView.setText(Integer.toString(routeCount));
        DecimalFormat formatter = new DecimalFormat("#.##");
        for (int a = 0; a < eventRoutesList.size(); a++) {
            Route currentRoute = eventRoutesList.get(a);
            eventBuilder.append("Route "+(a+1) + ": "+Double.valueOf(formatter.format(currentRoute.getDistance() / 1000)) + "km\n");
            /*List<RetroFitModels.Checkpoint> routePoints = currentRoute.getCheckpoint();
            plotRouteToMap(routePoints);*/
        }
        eventDistanceTextView.setText(eventBuilder.toString());
    }

    private void plotRouteToMap(List<RetroFitModels.Checkpoint> checkpoints) {
                PolygonOptions lineOptions = new PolygonOptions();
                for (int a = 0; a < checkpoints.size(); a++) {

                    RetroFitModels.Checkpoint currentPoint = checkpoints.get(a);
                    if (a == 0) {
                        lineOptions.add(new LatLng(currentPoint.getLatitude(), currentPoint.getLongitude()));
                        mapObject.addMarker(new MarkerOptions().title("Start Point").position(new LatLng(currentPoint.getLatitude(), currentPoint.getLongitude())));
                    } else if (a == checkpoints.size() - 1) {
                        lineOptions.add(new LatLng(currentPoint.getLatitude(), currentPoint.getLongitude()));
                        mapObject.addMarker(new MarkerOptions().title("End Point").position(new LatLng(currentPoint.getLatitude(), currentPoint.getLongitude())));
                        mapObject.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentPoint.getLatitude(), currentPoint.getLongitude()), 17));
                    } else {
                        lineOptions.add(new LatLng(currentPoint.getLatitude(), currentPoint.getLongitude()));
                    }
                }

                Polygon polygon = mapObject.addPolygon(lineOptions);
                polygon.setStrokeColor(Color.BLUE);
                polygon.setFillColor(Color.BLUE);
    }

    @Override
    public void onClick(View v) {
        if (selectedEvent.getEventRoute().size() == 1) {
            //If there is only one route associated to the event, register them to the route
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setTitle("Registering To Event");
            dialog.setMessage("Please wait while we register you to the select event and route. This process may take a few seconds, depending on your internet connection");
            dialog.setCancelable(false);
            dialog.show();
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            int athleteId = appPreferences.getInt("athleteId", 0);
            List<EventRoute> eventRoute = selectedEvent.getEventRoute();
            EventRegistration eventRegistration = new EventRegistration("Registered", athleteId, dateFormat.format(Calendar.getInstance().getTime()), false, selectedEvent.getEventId(), eventRoute.get(0).getRouteId());
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://ibalekaapi.azurewebsites.net/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            iBalekaClient client = retrofit.create(iBalekaClient.class);
            final Call<ResponseBody> registerCall = client.registerAthleteToEvent(eventRegistration);
            registerCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (dialog.isShowing()) {
                        dialog.cancel();
                    }
                    if(response.code() == 200) {
                        ResponseBody body = response.body();
                        if (!body.isDidError()) {
                            displayMessage("Successful Registration", "You have successfully registered for the selected event!");
                        } else {
                            displayMessage("Error Registering for Event", "An Error occurred when registering for the event. \n"+response.message());           }
                    } else if (response.code() == 500) {
                        displayMessage("Error Registering for Event", "An Error occurred when registering for the event\n"+response.message());
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (dialog.isShowing()) {
                        dialog.cancel();
                    }
                    displayMessage("Critical Error Registering for Event", "A critical error occured when registering for the event.\n "+t.getMessage());
                }
            });


        } else if (selectedEvent.getEventRoute().size() > 1)
        { //If there are more than 1 routes, we must give the user the option of selecting the desired route
            Bundle newBundle = new Bundle();
            ArrayList<EventRoute> eventRoutes = new ArrayList<>(selectedEvent.getEventRoute());
            newBundle.putParcelableArrayList("EventRoute", eventRoutes);
            RouteDialogFragment routeDialogFragment = new RouteDialogFragment();
            routeDialogFragment.setArguments(newBundle);
            routeDialogFragment.show(getFragmentManager(), "RouteDialogFragment");
        } else {
            DialogInterface.OnClickListener yesListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            };

            DialogInterface.OnClickListener noListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            };
            displayMessage("No Routes Available", "The event you selected does not have any linked routes. Do you still want to register for the selected event?", new DialogInterface.OnClickListener[] {yesListener, noListener});
        }
    }

    private void displayMessage(String title, String message) {
        AlertDialog.Builder msgBox = new AlertDialog.Builder(getActivity());
        msgBox.setTitle(title).setMessage(message).setPositiveButton("Got It", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setCancelable(false).show();
    }

    private void displayMessage(String title, String message, DialogInterface.OnClickListener [] listeners) {
        AlertDialog.Builder msgBox = new AlertDialog.Builder(getActivity());
        msgBox.setTitle(title).setMessage(message).setPositiveButton("Yes", listeners[0]).setNegativeButton("No", listeners[1]);
        msgBox.setCancelable(false).show();
    }
}
