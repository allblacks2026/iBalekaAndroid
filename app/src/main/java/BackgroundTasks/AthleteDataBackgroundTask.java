package BackgroundTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import DataAccessLayer.iBalekaClient;
import RetroFitModels.Club;
import RetroFitModels.Event;
import RetroFitModels.Run;
import RetroFitModels.TotalCaloriesResponseBody;
import RetroFitModels.TotalDistanceResponseBody;
import RetroFitModels.TotalEventRunResponseBody;
import RetroFitModels.TotalPersonalRunResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AthleteDataBackgroundTask extends AsyncTask<Boolean, Boolean, Boolean> {

    private List<Run> personalRunsList = new ArrayList<>();
    private List<Run> eventRunsList = new ArrayList<>();
    private List<Event> eventsList = new ArrayList<>();
    private List<Club> clubsList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private Activity currentActivity;
    private Retrofit retrofitClient;
    private iBalekaClient client;
    private SharedPreferences appSharedPreferences;
    private SharedPreferences.Editor editor;

    public AthleteDataBackgroundTask(Activity currentActivity) {
        this.currentActivity = currentActivity;
        progressDialog = new ProgressDialog(this.currentActivity);
        retrofitClient = new Retrofit.Builder().baseUrl("https://ibalekaapi.azurewebsites.net/").addConverterFactory(GsonConverterFactory.create()).build();
        client = retrofitClient.create(iBalekaClient.class);
        appSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.currentActivity);
        editor = appSharedPreferences.edit();
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setTitle("Fetching Athlete and System Data");
        progressDialog.setMessage("Please wait while the system fetches more athlete and system data. This may take a few seconds to complete, depending on your internet connection");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(Boolean... params) {
        try {

            getAthleteTotalDistanceRan();
            getAthleteTotalCaloriesThisMonth();
            processGetAthletePersonalRunCount();
            getAthleteEventRunCount();

            return true;

        } catch (Exception error) {
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            displayMessage("Error Fetching Athlete and System Data", error.getMessage());
            return false;
        }

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

    private void processGetAthletePersonalRunCount() throws Exception
    {
        Response<TotalPersonalRunResponseBody> personalRunCountCall = client.getAthletePersonalRunCount(appSharedPreferences.getInt("athleteId", 0)).execute();
        if (personalRunCountCall.code() == 200) {
            TotalPersonalRunResponseBody responseBody = personalRunCountCall.body();
            editor.putFloat("totalPersonalRuns", (float) responseBody.getModel());
            editor.commit();
        }
    }


    private void getAthleteTotalDistanceRan() throws Exception
    {
        Response<TotalDistanceResponseBody> totalDistanceCall = client.getTotalDistanceRan(appSharedPreferences.getInt("athleteId", 0)).execute();
        if (totalDistanceCall.code() == 200) {
            TotalDistanceResponseBody totalDistanceResponse = totalDistanceCall.body();
            editor.putFloat("totalDistance", (float) totalDistanceResponse.getModel());
            editor.commit();
        }
    }

    private void getAthleteTotalCaloriesThisMonth() throws Exception
    {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = 1;
        String startDate = year + "/"+month+"/"+day;
        String endDate = year+"/"+month+"/"+calendar.get(Calendar.DAY_OF_MONTH);
        Response<TotalCaloriesResponseBody> responseCall = client.getCaloriesCountOverTime(appSharedPreferences.getInt("athleteId", 0), startDate, endDate).execute();
        if (responseCall.code() == 200) {
            TotalCaloriesResponseBody totalCaloriesBody = responseCall.body();
            editor.putFloat("totalCalories", (float) totalCaloriesBody.getModel());
            editor.commit();
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    private void getAthleteEventRunCount() throws Exception
    {
        Response<TotalEventRunResponseBody> eventRunsCall = client.getAthleteEventRunCount(appSharedPreferences.getInt("athleteId", 0)).execute();
        if (eventRunsCall.code() == 200) {
            TotalEventRunResponseBody eventRunsBody = eventRunsCall.body();
            editor.putFloat("totalEventRuns", (float) eventRunsBody.getModel());
            editor.commit();
        }
    }
}
