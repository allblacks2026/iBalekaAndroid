package Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import BackgroundTasks.EventsAsyncTask;
import RetroFitModels.Event;
import allblacks.com.iBaleka.R;

/**
 * Created by Okuhle on 8/28/2016.
 */
public class SearchEventsAdapter extends RecyclerView.Adapter<SearchEventsAdapter.SearchResult> {

    private List<Event> eventsList;
    private Activity currentActivity;
    private LayoutInflater inflater;
    private boolean passedFetch = false;

    public SearchEventsAdapter(Activity currentActivity) {
        this.currentActivity = currentActivity;
        inflater = LayoutInflater.from(currentActivity);

    }
    @Override
    public SearchResult onCreateViewHolder(ViewGroup parent, int viewType) {
        View currentView = inflater.inflate(R.layout.search_result, parent, false);
        return new SearchResult(currentView);
    }

    @Override
    public void onBindViewHolder(SearchResult holder, int position) {
        Event currentEvent = eventsList.get(position);
        holder.titleTextView.setText(currentEvent.getTitle());
        holder.timeTextView.setText(currentEvent.getTime());
        holder.locationTextView.setText(currentEvent.getLocation());
        holder.dateTextView.setText(currentEvent.getDate());
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public List<Event> getEventsList() {
        return eventsList;
    }

    public void setEventsList(List<Event> eventsList) {
        this.eventsList = eventsList;
        notifyItemRangeChanged(0, eventsList.size() - 1);
    }

    public Event getSelectedEvent(int position) {
        return eventsList.get(position);
    }

    class SearchResult extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View currentView;
        private CardView resultCardView;
        private TextView titleTextView;
        private TextView timeTextView;
        private TextView locationTextView;
        private TextView dateTextView;

        public SearchResult(View itemView) {
            super(itemView);
            currentView = itemView;
            resultCardView = (CardView) itemView.findViewById(R.id.EventSearchResultCardView);
            titleTextView = (TextView) itemView.findViewById(R.id.EventSearchTitleTextView);
            timeTextView = (TextView) itemView.findViewById(R.id.EventSearchTimeTextView);
            locationTextView = (TextView) itemView.findViewById(R.id.EventSearchLocationTextView);
            dateTextView = (TextView) itemView.findViewById(R.id.EventSearchDateTextView);
            resultCardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
                    //Select the event and pass this to the event details fragment
                    int position = getAdapterPosition();
                    Event selectedEvent = getSelectedEvent(position);
                    EventsAsyncTask eventsTask = new EventsAsyncTask(currentActivity, selectedEvent);
                    try {
                        boolean response = eventsTask.execute().get();
                    } catch (Exception error) {

                    }


                /*final ProgressDialog dialog = new ProgressDialog(currentActivity);
                dialog.setTitle("Fetching Route Information");
                dialog.setMessage("Please wait while we fetch route information. This may take a few seconds to complete");
                dialog.show();

                Retrofit retrofitClient = new Retrofit.Builder()
                        .baseUrl("https://ibalekaapi.azurewebsites.net/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final iBalekaClient client = retrofitClient.create(iBalekaClient.class);

                for (int a =0; a < routeEventId.length; a++) {
                    Response<RouteArray> callResponse = null;
                              callResponse = client.getRoute(routeEventId[a]).execute();
                              if (callResponse.code() == 200) {

                                  RouteArray data = callResponse.body();
                                  Route selectedRoute = data.getModel();
                                  routesList.add(selectedRoute);
                                  passedFetch = true;
                              } else {
                                  passedFetch = false;
                              }
*/
                      /*  routeArrayCall.enqueue(new Callback<RouteArray>() {
                            @Override
                            public void onResponse(Call<RouteArray> call, Response<RouteArray> response) {
                                switch (response.code()) {
                                    case 200:
                                        RouteArray routeData = response.body();
                                        Route selectedRoute = routeData.getModel();
                                        routesList.add(selectedRoute);
                                        passedFetch = true;
                                        break;
                                    default:

                                        break;
                                }

                            }

                            @Override
                            public void onFailure(Call<RouteArray> call, Throwable t) {
                                dialog.cancel();
                                displayMessage("Error Getting Route Information", t.getMessage());
                            }
                        });*/
                 /*   }*/
/*
                if (routesList.size() != 0 && selectedEvent != null) {
                    searchBundle.putSerializable("SelectedEvent", selectedEvent);
                    //Send the routes list here
                    ((MainActivity) currentActivity).setRoutesList(routesList);

                }

            } catch (Exception error) {
                displayMessage("Error Finalizing Search Result", error.getMessage());
            }*/
        }

        public void displayMessage(String title, String message)
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(currentActivity);
            dialog.setTitle(title);
            dialog.setMessage(message);
            dialog.setPositiveButton("Got it", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.show();
        }
    }
}
