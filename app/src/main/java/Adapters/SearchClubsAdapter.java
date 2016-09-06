package Adapters;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Fragments.ClubDetailsFragment;
import Fragments.SearchClubsFragment;
import RetroFitModels.Club;
import allblacks.com.iBaleka.R;

/**
 * Created by Okuhle on 9/5/2016.
 */
public class SearchClubsAdapter extends RecyclerView.Adapter<SearchClubsAdapter.SearchClubResults>{

    private List<Club> clubsList = new ArrayList<>();
    private Activity currentActivity;
    private LayoutInflater inflater;

    public SearchClubsAdapter(Activity currentActivity) {
        this.currentActivity = currentActivity;
        inflater = LayoutInflater.from(this.currentActivity);
    }
    @Override
    public SearchClubResults onCreateViewHolder(ViewGroup parent, int viewType) {
        View currentView = inflater.inflate(R.layout.search_club_results, parent, false);
        return new SearchClubResults(currentView);
    }

    @Override
    public void onBindViewHolder(SearchClubResults holder, int position) {
        holder.clubName.setText(clubsList.get(position).getName());
        holder.clubLocation.setText(clubsList.get(position).getLocation());
        holder.clubDescription.setText(clubsList.get(position).getDescription());
    }

    public List<Club> getClubsList() {
        return clubsList;
    }

    public void setClubsList(List<Club> clubsList) {
        this.clubsList = clubsList;
    }

    @Override
    public int getItemCount() {
        return clubsList.size();
    }

    public Club getSelectedClub(int position) {
        return clubsList.get(position);
    }

    class SearchClubResults extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CardView clubCardView;
        private TextView clubName;
        private TextView clubDescription;
        private TextView clubLocation;

        public SearchClubResults(View itemView) {
            super(itemView);
            clubCardView = (CardView) itemView.findViewById(R.id.SearchClubCardView);
            clubName = (TextView) itemView.findViewById(R.id.ClubTitleTextView);
            clubDescription = (TextView) itemView.findViewById(R.id.ClubDescriptionTextView);
            clubLocation = (TextView) itemView.findViewById(R.id.ClubLocationTextView);
            clubCardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Club selectedClub = getSelectedClub(position);
            if (selectedClub != null) {
                ClubDetailsFragment fragment = new ClubDetailsFragment();
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                bundle.putSerializable("Club", selectedClub);
                FragmentManager mgr = currentActivity.getFragmentManager();
                FragmentTransaction transaction = mgr.beginTransaction();
                transaction.replace(R.id.MainActivityContentArea, fragment, "ClubDetailsFragment");
                transaction.addToBackStack("ClubDetailsFragment");
                transaction.commit();
            }

        }
    }
}
