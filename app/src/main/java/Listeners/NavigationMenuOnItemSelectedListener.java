package Listeners;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import Fragments.AthleteLandingFragment;
import Fragments.ProfileFragment;
import Fragments.RegisteredEventsFragment;
import Fragments.ReportExplorerFragment;
import Fragments.RunningFragment;
import Fragments.SearchClubsFragment;
import Fragments.StartSearchFragment;
import Utilities.DeviceHardwareChecker;
import allblacks.com.iBaleka.R;

/**
 * Created by Okuhle on 3/28/2016.
 */
public class NavigationMenuOnItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {

    private Activity currentActivity;
    private FragmentManager mgr;
    private DrawerLayout drawerLayout;
    private TextView toolbarTextView;
    private NavigationView navigationView;

    public NavigationMenuOnItemSelectedListener(AppCompatActivity currentActivity)
    {
        this.currentActivity = currentActivity;
        mgr = currentActivity.getFragmentManager();
        drawerLayout = (DrawerLayout) currentActivity.findViewById(R.id.menuDrawerLayout);
        toolbarTextView = (TextView) currentActivity.findViewById(R.id.MainActivityTextView);
        navigationView = (NavigationView) currentActivity.findViewById(R.id.mainActivityNavigationView);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.athleteHomeOption:
                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.athlete_navigation_menu);
                AthleteLandingFragment homeFragment = new AthleteLandingFragment();
                FragmentTransaction trans = mgr.beginTransaction();
                trans.replace(R.id.MainActivityContentArea, homeFragment, "HomeFragment");
                trans.addToBackStack("HomeFragment");
                trans.commit();
                drawerLayout.closeDrawers();
                break;
            case R.id.athleteViewProfile:
                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.athlete_navigation_menu);
                ProfileFragment profileFragment = new ProfileFragment();
                FragmentTransaction thirdTrans = mgr.beginTransaction();
                thirdTrans.replace(R.id.MainActivityContentArea, profileFragment,
                        "ProfileFragment");
                thirdTrans.addToBackStack("ProfileFragment");
                drawerLayout.closeDrawers();
                thirdTrans.commit();
                break;
            case R.id.athleteStartRun:
                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.athlete_navigation_menu);
                RunningFragment startRunFragment = new RunningFragment();
                FragmentTransaction startRunTransaction = mgr.beginTransaction();
                startRunTransaction.replace(R.id.MainActivityContentArea, startRunFragment,
                        "RunningFragment");
                startRunTransaction.addToBackStack("RunningFragment");
                drawerLayout.closeDrawers();
                startRunTransaction.commit();
                break;
            case R.id.searchEvents:
                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.athlete_navigation_menu);
                StartSearchFragment searchFragment = new StartSearchFragment();
                FragmentTransaction searchFragmentTransaction = mgr.beginTransaction();
                searchFragmentTransaction.replace(R.id.MainActivityContentArea, searchFragment,
                        "SearchFragment");
                searchFragmentTransaction.addToBackStack("SearchFragment");
                drawerLayout.closeDrawers();
                searchFragmentTransaction.commit();
                break;
            case R.id.viewRegisteredEvents:
                DeviceHardwareChecker checker = new DeviceHardwareChecker(currentActivity);
                checker.checkNetworkConnection();
                if (checker.isConnectedToInternet()) {
                    drawerLayout.closeDrawers();
                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.athlete_navigation_menu);
                    RegisteredEventsFragment fragment = new RegisteredEventsFragment();
                    FragmentTransaction repFragment = mgr.beginTransaction();
                    repFragment.replace(R.id.MainActivityContentArea, fragment, "RegisteredEventsFragment");
                    repFragment.addToBackStack("RegisteredEvent");
                    repFragment.commit();
                } else {
                    drawerLayout.closeDrawers();
                }
                break;
            case R.id.reportExplorerOption:
                DeviceHardwareChecker checker1 = new DeviceHardwareChecker(currentActivity);
                checker1.checkNetworkConnection();
                if (checker1.isConnectedToInternet()) {
                    drawerLayout.closeDrawers();
                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.athlete_navigation_menu);
                    ReportExplorerFragment reportExplorerFragment = new ReportExplorerFragment();
                    FragmentTransaction reportTrans = mgr.beginTransaction();
                    reportTrans.replace(R.id.MainActivityContentArea, reportExplorerFragment, "ReportExplorerFragment");
                    reportTrans.addToBackStack("ReportExplorerFragment");
                    reportTrans.commit();
                } else {
                    drawerLayout.closeDrawers();
                }
                break;
            case R.id.joinClubOption:
                DeviceHardwareChecker checker2 = new DeviceHardwareChecker(currentActivity);
                checker2.checkNetworkConnection();
                if (checker2.isConnectedToInternet()){
                    drawerLayout.closeDrawers();
                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.athlete_navigation_menu);
                    SearchClubsFragment clubsFragment = new SearchClubsFragment();
                    FragmentTransaction transaction = mgr.beginTransaction();
                    transaction.replace(R.id.MainActivityContentArea, clubsFragment, "ClubsFragment");
                    transaction.addToBackStack("ClubsFragment");
                    transaction.commit();
                } else {
                    drawerLayout.closeDrawers();
                }
        }
        return true;
    }
}
