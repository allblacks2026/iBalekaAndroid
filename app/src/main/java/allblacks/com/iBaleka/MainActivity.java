package allblacks.com.iBaleka;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Fragments.AboutApplicationFragment;
import Fragments.ApplicationPreferencesFragment;
import Fragments.AthleteLandingFragment;
import Fragments.EditProfileFragment;
import Listeners.NavigationMenuOnItemSelectedListener;
import RetroFitModels.Athlete;
import RetroFitModels.Club;
import RetroFitModels.Event;
import RetroFitModels.Route;
import RetroFitModels.Run;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar mainActivityToolbar;
    private TextView toolbarTextView;
    private ImageView toolbarImage;
    private FragmentManager mgr;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private SharedPreferences activityPreferences;
    private SharedPreferences appSharedPreferences;
    private SharedPreferences.Editor editor;
    private List<Route> routesList = new ArrayList<>();
    private Event selectedEvent;
    private Athlete athlete;

    private List<Event> eventsList = new ArrayList<>();
    private List<Club> clubsList = new ArrayList<>();
    private List<Run> athleteEventRuns = new ArrayList<>();
    private List<Run> athletePersonalRuns = new ArrayList<>();

    public Event getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(Event selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            initializeControls();
            loadLandingScreenFragment();
        } else {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            initializeControls();
        }
    }

    public List<Route> getRoutesList() {
        return routesList;
    }

    public void setRoutesList(List<Route> routesList) {
        this.routesList = routesList;
    }

    public void initializeControls()
    {
        mainActivityToolbar = (Toolbar) findViewById(R.id.MainActivityToolbar);
        toolbarTextView = (TextView) findViewById(R.id.MainActivityTextView);
        toolbarImage = (ImageView) findViewById(R.id.MainActivityImageView);
        drawerLayout = (DrawerLayout) findViewById(R.id.menuDrawerLayout);
        navigationView = (NavigationView) findViewById(R.id.mainActivityNavigationView);
        setSupportActionBar(mainActivityToolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mainActivityToolbar, R.string
                .app_name, R
                .string
                .app_name);
        drawerLayout.setDrawerListener(drawerToggle);
        navigationView.inflateHeaderView(R.layout.navigation_menu_header);
        navigationView.inflateMenu(R.menu.athlete_navigation_menu);
        toolbarImage.setOnClickListener(this);
        appSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        activityPreferences = getSharedPreferences("iBaleka", MODE_PRIVATE);
        editor = activityPreferences.edit();
        NavigationMenuOnItemSelectedListener listener = new NavigationMenuOnItemSelectedListener
                (this);
        navigationView.setNavigationItemSelectedListener(listener);
        toolbarImage.setImageResource(R.drawable.ibaleka_logo);
        mgr = getFragmentManager();
        mapComponents();

    }
    public void loadLandingScreenFragment()
    {
        AthleteLandingFragment landingFragment = new AthleteLandingFragment();
        FragmentTransaction transaction = mgr.beginTransaction();
        transaction.replace(R.id.MainActivityContentArea, landingFragment, "UserStats");
        transaction.addToBackStack("UserStats");
        transaction.commit();
    }

    private void mapComponents()
    {
        View headerView = navigationView.getHeaderView(0);
        TextView athleteNameSurname = (TextView) headerView.findViewById(R.id.profileNameSurname);
        TextView emailAddress = (TextView) headerView.findViewById(R.id.profileEmailAddress);

        String nameSurname = appSharedPreferences.getString("name", "") + " "+ appSharedPreferences.getString("surname", "");
        athleteNameSurname.setText(nameSurname);
        emailAddress.setText(appSharedPreferences.getString("emailAddress", "").toLowerCase());

    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }
    @Override
    public void onBackPressed() {
        if (mgr.getBackStackEntryCount() > 0) {
            mgr.popBackStack();
        } else {
            super.onBackPressed();
        }

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onClick(View v) {
        drawerLayout.closeDrawers();
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.athlete_navigation_menu);
        AthleteLandingFragment thisOne = new AthleteLandingFragment();
        FragmentTransaction transaction = mgr.beginTransaction();
        transaction.replace(R.id.MainActivityContentArea, thisOne, "HomeFragment");
        transaction.addToBackStack("HomeFragment");
        toolbarTextView.setText("Welcome");
        transaction.commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.in_app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
           case (R.id.inAppApplicationPreferences):
               ApplicationPreferencesFragment preferencesFragment = new
                       ApplicationPreferencesFragment();
            FragmentTransaction preferencesTransaction = mgr.beginTransaction();
               preferencesTransaction.replace(R.id.MainActivityContentArea, preferencesFragment,
                       "PreferencesFragment");
               toolbarTextView.setText("Application Settings");
               preferencesTransaction.addToBackStack(null);
               preferencesTransaction.commit();
            break;
            case (R.id.inAppEditProfile) :
                EditProfileFragment editProfileFragment = new EditProfileFragment();
                FragmentTransaction profileTrans = mgr.beginTransaction();
                profileTrans.replace(R.id.MainActivityContentArea, editProfileFragment, "EditProfileFragment");
                profileTrans.addToBackStack(null);
                profileTrans.commit();
                break;
            case (R.id.inAppAboutApplication):
                AboutApplicationFragment aboutAppFragment = new AboutApplicationFragment();
                FragmentTransaction trans = mgr.beginTransaction();
                trans.replace(R.id.MainActivityContentArea, aboutAppFragment, "AboutApplicationFragment");
                trans.addToBackStack(null);
                toolbarTextView.setText("About Application");
                trans.commit();
                break;
        }
        return true;
    }

    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }

    public void setAthletePersonalRuns(List<Run> athletePersonalRuns) {
        this.athletePersonalRuns = athletePersonalRuns;
    }

    public void setAthleteEventRuns(List<Run> athleteEventRuns) {
        this.athleteEventRuns = athleteEventRuns;
    }

    public void setEventsList(List<Event> eventsList) {
        this.eventsList = eventsList;
    }

    public void setClubsList(List<Club> clubsList) {
        this.clubsList = clubsList;
    }

    public List<Event> getEventsList() {
        return eventsList;
    }

    public List<Club> getClubsList() {
        return clubsList;
    }

    public List<Run> getAthleteEventRuns() {
        return athleteEventRuns;
    }

    public List<Run> getAthletePersonalRuns() {
        return athletePersonalRuns;
    }
}
