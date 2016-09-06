package Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

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
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import Adapters.SearchClubsAdapter;
import DataAccessLayer.iBalekaClient;
import RetroFitModels.Club;
import RetroFitModels.ClubArray;
import allblacks.com.iBaleka.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchClubsFragment extends Fragment implements PlaceSelectionListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    private static final int REQUEST_LOCATION = 400;
    private static final int REQUEST_LOCATION_UPDATES = 450;
    private static final int REQUEST_GPS = 500;
    private CheckBox searchByLocation;
    private GoogleApiClient googleApiClient;
    private PlaceAutocompleteFragment searchBox;
    private TextView toolbarTextView;
    private Location locationObject;
    private LocationRequest locationRequest;
    private RecyclerView searchClubsRecyclerView;

    public SearchClubsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_search_clubs, container, false);
        initializeComponents(currentView, savedInstanceState);
        createLocationRequest();
        buildGoogleApiClient();
        checkLocationSettings();
        return currentView;
    }

    private void initializeComponents(View currentView, Bundle savedInstanceState) {
        searchClubsRecyclerView = (RecyclerView) currentView.findViewById(R.id.SearchClubsRecyclerView);
        searchClubsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchBox = new PlaceAutocompleteFragment();
        toolbarTextView = (TextView) getActivity().findViewById(R.id.MainActivityTextView);
        toolbarTextView.setText("Search For Clubs");
        FragmentTransaction transaction = getFragmentManager().beginTransaction().replace(R.id.SearchClubTextBox, searchBox, "SearchFragment");
        transaction.commit();
        searchBox.setOnPlaceSelectedListener(this);
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(1500);
        locationRequest.setInterval(3000);
    }

    @Override
    public void onResume() {
        super.onResume();
        buildGoogleApiClient();
    }

    private void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getActivity()).addApi(LocationServices.API)
                .addOnConnectionFailedListener(this).addConnectionCallbacks(this).build();
        googleApiClient.connect();
    }

    @Override
    public void onPlaceSelected(Place place) {

        String address = place.getAddress().toString();
        String name = place.getAddress().toString();
        LatLng currentLocation = place.getLatLng();

        List<String> searchParameters = new ArrayList<>();
        searchParameters.add(address);
        searchParameters.add(name);
        processSearch(searchParameters);
    }

    @Override
    public void onError(Status status) {

    }

    private void processSearch(final List<String> searchParameters) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Searching for Clubs");
        dialog.setMessage("Please wait while we search our system for available clubs");
        dialog.setCancelable(false);
        dialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ibalekaapi.azurewebsites.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        iBalekaClient client = retrofit.create(iBalekaClient.class);
        Call<ClubArray> clubArrayCall = client.getAllClubs();
        clubArrayCall.enqueue(new Callback<ClubArray>() {
            @Override
            public void onResponse(Call<ClubArray> call, Response<ClubArray> response) {
                List<Club> clubList = new ArrayList<Club>();
                List<Club> foundClubs = new ArrayList<Club>();
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                if (response.code() == 200) {
                    ClubArray array = response.body();
                    clubList = array.getModel();
                    if (array.getModel() != null || clubList.size() != 0) {
                        for (int a = 0; a < clubList.size(); a++) {
                            Club currentClub = clubList.get(a);
                            currentClub.setName(currentClub.getName().toLowerCase());
                            currentClub.setDescription(currentClub.getDateCreated().toLowerCase());
                            currentClub.setLocation(currentClub.getLocation().toLowerCase());
                            for (int b = 0; b < searchParameters.size(); b++) {
                                String thisParameter = searchParameters.get(b);
                                thisParameter = thisParameter.toLowerCase();
                                if (currentClub.getName().contains(thisParameter) || currentClub.getDescription().contains(thisParameter) || currentClub.getLocation().contains(thisParameter)) {
                                   foundClubs.add(currentClub);
                                }
                            }
                        }
                        if (foundClubs.size() == 0) {
                            displayMessage("No Clubs Found", "No clubs were found. Please try again using different keywords");
                        } else {
                            SearchClubsAdapter adapter = new SearchClubsAdapter(getActivity());
                            adapter.setClubsList(foundClubs);
                            searchClubsRecyclerView.setAdapter(adapter);
                        }
                    } else {
                        displayMessage("No Clubs Were Found", "No clubs were found. Try searching again with different keywords");
                    }
                }
            }

            @Override
            public void onFailure(Call<ClubArray> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                displayMessage("Error Searching Clubs", "An Error occurred while searching for clubs\n"+t.getMessage());
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getLastKnownLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        locationObject = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                getLastKnownLocation();
            } else {
                displayMessage("Permissions Denied", "You have denied permissions to use your current location. Some features may not work as expected");
            }
        } else if (requestCode == REQUEST_LOCATION_UPDATES) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults [1] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates();
            } else {
                displayMessage("Permissions Denied", "You have denied permissions to use your current location. Some features may not work as expected");
            }
        }
    }

    public void displayMessage(String title, String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(title).setMessage(message).setPositiveButton("Got It", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }

    public void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String [] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_UPDATES);

        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        locationObject = location;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_GPS) {
            if (resultCode == Activity.RESULT_OK){
                requestLocationUpdates();
            } else {
                displayMessage("Unable to get GPS", "For best performance, please turn on your GPS");
            }
        }
    }
}