package Fragments;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.util.ArrayList;
import java.util.List;

import Adapters.SearchEventsAdapter;
import DataAccessLayer.iBalekaClient;
import RetroFitModels.Event;
import RetroFitModels.EventsArray;
import Utilities.DeviceHardwareChecker;
import allblacks.com.iBaleka.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StartSearchFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    private static final int REQUEST_LIKELY_PLACES = 400;
    private static final int REQUEST_LOCATION_UPDATES = 500;
    private static final int REQUEST_GPS = 550;
    private RecyclerView searchResultsRecyclerView;
    private Place selectedPlace;
    private String currentLocation;
    private GoogleApiClient googleApiClient;
    private boolean resolvingError = false;
    private static final int RESOLVE_CONNECTION_ERROR = 100;
    private static final String DIALOG_ERROR = "dialog_error";
    private static final String RESOLVING_ERROR = "resolving_error";
    private boolean permissionGranted = false;
    private static final int ACCESS_FINE_LOCATION_PERMISSION = 150;
    private List<Place> likelyPlaces = new ArrayList<>();
    private PlaceAutocompleteFragment autoCompleteFragment;
    private TextView toolbarTextView;
    private RecyclerView quickSearchRecyclerView;
    private List<Event> eventsList = new ArrayList<>();
    private Location locationObject;
    private LocationRequest locationRequest;

    public StartSearchFragment() {

    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setFastestInterval(1500);
        locationRequest.setInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String [] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_UPDATES);
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View currentView = inflater.inflate(R.layout.fragment_start_search, container, false);

        initializeComponents(currentView, savedInstanceState);
        resolvingError = savedInstanceState != null && savedInstanceState.getBoolean(RESOLVING_ERROR, false);
        buildGoogleApi();
        createLocationRequest();
        getDeviceSuggestedLocations();
        return currentView;
    }

    private void initializeComponents(View currentView, Bundle savedInstanceState) {
        searchResultsRecyclerView = (RecyclerView) currentView.findViewById(R.id.EventSearchResultsRecyclerView);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        autoCompleteFragment = new PlaceAutocompleteFragment();
        toolbarTextView = (TextView) getActivity().findViewById(R.id.MainActivityTextView);
        toolbarTextView.setText("Search For Event");
        autoCompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                List<String> searchParameters = new ArrayList<String>();
                if (place.getName() != null){
                    searchParameters.add(place.getName().toString());
                }

                if (place.getAddress() != null) {
                    searchParameters.add(place.getAddress().toString());
                }

                fetchEventsFromSystem(searchParameters);
            }

            @Override
            public void onError(Status status) {
                displayMessage("Error Getting Location", "Your search parameter yields no valid results");
            }
        });

        FragmentManager mgr = getFragmentManager();
        FragmentTransaction transaction = mgr.beginTransaction();
        transaction.replace(R.id.GoogleSearchFragment, autoCompleteFragment, "AutoSearchFragment");
        transaction.commit();
        quickSearchRecyclerView = (RecyclerView) currentView.findViewById(R.id.EventSearchResultsRecyclerView);
        quickSearchRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private void processSearch() {
        DeviceHardwareChecker checker = new DeviceHardwareChecker(getActivity());
        checker.checkNetworkConnection();
        if (checker.isConnectedToInternet()) {

            clearRecyclerView();
            String placeName = selectedPlace.getName().toString();
            String telephoneNumber = selectedPlace.getPhoneNumber().toString();


        } else {
            displayMessage("Check Your Internet Connection", "You are not connected to the internet. Please check your internet connection");
        }
    }

    private void clearRecyclerView() {
        searchResultsRecyclerView.removeAllViews();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(RESOLVING_ERROR, resolvingError);
    }

    private void buildGoogleApi() {
        googleApiClient = new GoogleApiClient.Builder(getActivity(), this, this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(getActivity(), "Connected to Google", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        if (resolvingError) {
            return;
        } else if (connectionResult.hasResolution()) {
            try {
                resolvingError = true;
                connectionResult.startResolutionForResult(getActivity(), RESOLVE_CONNECTION_ERROR);
            } catch (Exception error) {
                displayMessage("Error Resolving Google Connection", error.getMessage());
                googleApiClient.connect();
            }
        } else {
            resolvingError = true;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESOLVE_CONNECTION_ERROR) {
            resolvingError = false;

            if (resultCode == getActivity().RESULT_OK) {
                if (!googleApiClient.isConnecting() && !googleApiClient.isConnected()) {
                    googleApiClient.connect();
                }
            }
        } else if (requestCode == REQUEST_GPS) {
            if (resultCode == Activity.RESULT_OK) {
                requestLocationUpdates();
            }
        }
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

    public void handlePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getDeviceSuggestedLocations();
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    displayMessage("Permission to use GPS", "The iBaleka app needs access to your GPS receiver");
                }
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_PERMISSION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == ACCESS_FINE_LOCATION_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                displayMessage("Search Not Available", "The Search feature will not be as effective as it should be due to permission being denied");
            }
        } else if (requestCode == REQUEST_LOCATION_UPDATES) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates();
            }
        }
    }

    private void checkLocationSettings()
    {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status locationSettingsStatus = locationSettingsResult.getStatus();
                LocationSettingsStates locationSettingsStates = locationSettingsResult.getLocationSettingsStates();
                switch (locationSettingsStatus.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.CANCELED:
                        displayMessage("Denied Permission", "Please turn on your mobile GPS to receive best service");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            locationSettingsStatus.startResolutionForResult(getActivity(), REQUEST_GPS);
                        } catch (Exception error) {}
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        displayMessage("Unable to get GPS", "For best performance, please enable your GPS");
                        break;
                }
            }
        });
    }

    private void getDeviceSuggestedLocations() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String [] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LIKELY_PLACES);
        }
        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(googleApiClient, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(@NonNull PlaceLikelihoodBuffer placeLikelihoods) {
                for (PlaceLikelihood placeLikelihood : placeLikelihoods)
                {
                    likelyPlaces.add(placeLikelihood.getPlace());
                }
                placeLikelihoods.release();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        buildGoogleApi();
        toolbarTextView.setText("Search For Event");
    }

    private void fetchEventsFromSystem(final List<String> searchParameters) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Processing Event Search");
        dialog.setMessage("Please wait while we process your event search request");
        dialog.setCancelable(false);
        dialog.show();

        final List<Event> foundEvents = new ArrayList<>();
        Retrofit retrofitClient = new Retrofit.Builder()
                .baseUrl("https://ibalekaapi.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        iBalekaClient client = retrofitClient.create(iBalekaClient.class);
        Call<EventsArray> eventsArrayCall = client.getEvents();
        eventsArrayCall.enqueue(new Callback<EventsArray>() {
            @Override
            public void onResponse(Call<EventsArray> call, Response<EventsArray> response) {
                switch (response.code()) {
                    case 200:
                        EventsArray array = response.body();
                        SearchEventsAdapter adapter = new SearchEventsAdapter(getActivity());
                        eventsList = array.getModel();

                        for (int a = 0; a< eventsList.size(); a++) {
                            Event currentEvent = eventsList.get(a);
                            currentEvent.setTitle(currentEvent.getTitle().toLowerCase());
                            currentEvent.setDescription(currentEvent.getDescription().toLowerCase());
                            currentEvent.setLocation(currentEvent.getLocation().toLowerCase());

                            for (int b = 0; b < searchParameters.size(); b++) {
                                String parameter = searchParameters.get(b).toLowerCase();
                                if (currentEvent.getTitle().contains(parameter) || currentEvent.getDescription().contains(parameter) || currentEvent.getLocation().contains(parameter)) {
                                    foundEvents.add(currentEvent);
                                }
                            }
                        }

                        if (foundEvents.size() == 0 || searchParameters.size() == 0) {
                            dialog.cancel();
                            displayMessage("No Events Found", "No events were found. Try searching again with different keywords");
                        } else {
                            dialog.cancel();
                            adapter.setEventsList(foundEvents);
                            quickSearchRecyclerView.setAdapter(adapter);
                        }
                }
            }
            @Override
            public void onFailure(Call<EventsArray> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                displayMessage("Error Getting Events", t.getMessage());
            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {
        locationObject = location;
    }

    private void resolveToLocation()
    {
        if (locationObject != null) {

        }
    }
}
