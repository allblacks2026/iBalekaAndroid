package RetroFit;

import RetroFitModels.Athlete;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Okuhle on 8/19/2016.
 */
public interface iBalekaApi {

    //Add a new athlete to the system
    @POST("/Athlete/AddAthlete/")
    Call<Athlete> addAthlete(@Body Athlete newAthlete);

    //Get the selected athlete from the system
    @GET("/Athlete/GetAthlete/")
    Call<Athlete> getAthlete(@Path("athleteID") int athleteID);

    //

}
