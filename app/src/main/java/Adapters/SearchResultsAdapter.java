package Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import RetroFitModels.Event;
import allblacks.com.iBaleka.R;

/**
 * Created by Okuhle on 7/12/2016.
 */
public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.SearchResult> {

    private List<Event> eventsList;
    private Activity currentActivity;
    private LayoutInflater inflater;

    public SearchResultsAdapter(Activity currentActivity) {
        this.currentActivity = currentActivity;
        inflater = LayoutInflater.from(currentActivity);
        eventsList = new ArrayList<>();
    }

    public void setEventsList(List<Event> eventsList) {
        this.eventsList = eventsList;
        notifyItemRangeChanged(0, eventsList.size() -1);
    }

    @Override
    public SearchResult onCreateViewHolder(ViewGroup parent, int viewType) {
        View currentView = inflater.inflate(R.layout.search_result, parent, false);
        return new SearchResult(currentView);
    }

    @Override
    public void onBindViewHolder(SearchResult holder, int position) {
        Event currentEvent = eventsList.get(position);
        holder.eventTitleTextView.setText(currentEvent.getTitle());
        holder.eventTimeTextView.setText(currentEvent.getTime());
        holder.eventDateTextView.setText(currentEvent.getDate());
        holder.locationTextView.setText(currentEvent.getLocation());
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public Event getSelectedEvent(int position) {
        return eventsList.get(position);
    }

    class SearchResult extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View view;
          private TextView eventTitleTextView;
          private TextView eventDateTextView;
          private TextView eventTimeTextView;
          private TextView locationTextView;
          private CardView currentCardView;

      public SearchResult(View itemView) {
          super(itemView);
          view = itemView;
          eventTitleTextView = (TextView) itemView.findViewById(R.id.EventSearchTitleTextView);
          eventDateTextView = (TextView) itemView.findViewById(R.id.EventSearchDateTextView);
          eventTimeTextView = (TextView) itemView.findViewById(R.id.EventSearchTimeTextView);
          locationTextView = (TextView) itemView.findViewById(R.id.EventSearchLocationTextView);
          currentCardView = (CardView) itemView.findViewById(R.id.EventSearchResultCardView);
          currentCardView.setOnClickListener(this);
      }
        @Override
        public void onClick(View v) {
            try {
                TextView toolbarTextView = (TextView) currentActivity.findViewById(R.id.MainActivityTextView);
                toolbarTextView.setText("View Event Details");
                int selectedPos = getAdapterPosition();
                Event selectedEvent = getSelectedEvent(selectedPos);
                SharedPreferences eventPreference = currentActivity.getSharedPreferences("EventPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = eventPreference.edit();

                editor.putInt("EventID", selectedEvent.getEventId());
                editor.putString("EventDescription", selectedEvent.getDescription());
                editor.putString("EventDate", selectedEvent.getDate());
                editor.putString("EventTime", selectedEvent.getTime());
                editor.putString("EventLocation", selectedEvent.getLocation());
                editor.commit();

            } catch (Exception error) {
                displayMessage("Error", error.getMessage());
            }
        }

        public void displayMessage(String title, String message) {
            AlertDialog.Builder messageBox = new AlertDialog.Builder(itemView.getContext());
            messageBox.setTitle(title);
            messageBox.setMessage(message);
            messageBox.setPositiveButton("Got It", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            messageBox.show();
        }
    }
}

