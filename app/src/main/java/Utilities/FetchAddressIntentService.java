package Utilities;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Okuhle on 8/30/2016.
 */
public class FetchAddressIntentService extends IntentService {

    public static final int SUCCESS = 1;
    public static final int FAILURE = 0;
    public static final String PACKAGE_NAME = "com.google.android.gms.location.sample.locationaddress";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".DATA_RESULT_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +".LOCATION_DATA_EXTRA";


    public FetchAddressIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String errorMessage = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        Location location = intent.getParcelableExtra(LOCATION_DATA_EXTRA);

        List<Address> addressList = new ArrayList<>();
        try {
            addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (Exception error) {
            errorMessage = error.getMessage();
        }

        if (addressList.size() == 0){
            errorMessage = "No Addresses were found";

        } else {
            Address address = addressList.get(0);

        }
    }
}
