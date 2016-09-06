package BackgroundTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import DataAccessLayer.iBalekaClient;
import Fragments.EventDetailsFragment;
import RetroFitModels.Event;
import RetroFitModels.EventRoute;
import RetroFitModels.Route;
import RetroFitModels.RouteArray;
import allblacks.com.iBaleka.MainActivity;
import allblacks.com.iBaleka.R;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Okuhle on 9/2/2016.
 */
public class EventsAsyncTask extends AsyncTask<Boolean, Boolean, Boolean> {

    private ProgressDialog progressDialog;
    private Activity currentActivity;
    private Retrofit retrofitClient;
    private iBalekaClient client;
    private List<Integer> routeEventId = new ArrayList<Integer>();
    private Event selectedEvent;
    private List<EventRoute> eventRouteList = new ArrayList<>();
    private List<Route> routesList = new ArrayList<>();
    private List<Boolean> failed = new ArrayList<>();
    private boolean failureFound = false;

    public EventsAsyncTask(Activity currentActivity, Event selectedEvent) {
        this.currentActivity = currentActivity;
        this.selectedEvent = selectedEvent;
        eventRouteList = this.selectedEvent.getEventRoute();
        for (int a = 0; a < eventRouteList.size(); a++) {
            routeEventId.add(eventRouteList.get(a).getRouteId());
        }
        progressDialog = new ProgressDialog(currentActivity);
        retrofitClient = new Retrofit.Builder()
                .baseUrl("https://ibalekaapi.azurewebsites.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    @Override
    protected void onPreExecute() {
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.setTitle("Fetching Event Route Details");
                progressDialog.setMessage("Please wait while we fetch event route details...");
                progressDialog.show();
            }
        });
    }
    @Override
    protected Boolean doInBackground(Boolean... params) {
        try {
            client = retrofitClient.create(iBalekaClient.class);
            Response<RouteArray> routeArrayResponse = null;
            for (int a = 0; a < routeEventId.size(); a++) {
                routeArrayResponse = client.getRoute(routeEventId.get(a)).execute();
                if (routeArrayResponse.code() == 200) {
                    RouteArray currentRouteArray = routeArrayResponse.body();
                    Route currentRoute = currentRouteArray.getModel();
                    routesList.add(currentRoute);
                } else {
                    failed.add(true);
                }
            }
        } catch (Exception error) {
            displayMessage("Error Getting Route Information", error.getMessage());
            return false;

        }
        return true;
    }
    private void displayMessage(final String title, final String message) {
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder messageBox = new AlertDialog.Builder(currentActivity);
                messageBox.setTitle(title);
                messageBox.setMessage(message);
                messageBox.setPositiveButton("Got It", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                messageBox.show();
            }
        });

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        progressDialog.dismiss();
        if (aBoolean) {
            for (int a =0; a < failed.size(); a++) {
                if (!failed.get(a)) {
                    failureFound = true;
                }
            }
            if (failureFound) {
                displayMessage("Error Getting Route Data", "One of the routes were not found on the system");
            } else {
                loadEventDetailsScreen();
            }
        }
    }

    private void loadEventDetailsScreen()
    {
        ((MainActivity) currentActivity).setRoutesList(routesList);
        ((MainActivity) currentActivity).setSelectedEvent(selectedEvent);
        EventDetailsFragment fragment = new EventDetailsFragment();
        FragmentManager mgr = currentActivity.getFragmentManager();
        FragmentTransaction transaction = mgr.beginTransaction()
                .replace(R.id.MainActivityContentArea, fragment, "EventDetailsFragment");
        transaction.addToBackStack("EventDetailsFragment");
        transaction.commit();
    }
}
