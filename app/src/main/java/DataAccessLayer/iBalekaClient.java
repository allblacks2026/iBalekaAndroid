package DataAccessLayer;

import RetroFitModels.Athlete;
import RetroFitModels.AthleteArray;
import RetroFitModels.ClubArray;
import RetroFitModels.ClubMember;
import RetroFitModels.EventRegistration;
import RetroFitModels.EventsArray;
import RetroFitModels.ResponseBody;
import RetroFitModels.RouteArray;
import RetroFitModels.Run;
import RetroFitModels.RunArray;
import RetroFitModels.TotalCaloriesResponseBody;
import RetroFitModels.TotalDistanceResponseBody;
import RetroFitModels.TotalEventRunResponseBody;
import RetroFitModels.TotalPersonalRunResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Okuhle on 8/30/2016.
 */
public interface iBalekaClient {

    //Get the selected athlete
    @GET("/Athlete/GetAthlete")
    Call<Athlete> getAthlete(@Path("athleteId") int athleteId);

    //Add new athlete to the system
    @POST("/Athlete/AddAthlete")
    Call<ResponseBody> registerAthlete(@Body Athlete newAthlete);

    //Get a list of events
    @GET("/Event/GetEvents")
    Call<EventsArray> getEvents();

    //Login the athlete
    @GET("/Athlete/Account/LoginAthlete")
    Call<AthleteArray> loginAthlete(@Query("username") String username, @Query("password") String password);

    //Get the athlete personal runs
    @GET("/Run/Athlete/Personal/GetAthletePersonalRuns")
    Call<RunArray> getAthletePersonalRuns(@Query("athleteId") int athleteId);

    //Get the athlete event runs
    @GET("/Run/Athlete/Event/GetAthleteEventRuns")
    Call<Run> getEventPersonalRuns(@Query("athleteId") int athleteId);

    //Get the selected route
    @GET("/Map/GetRoute/")
    Call<RouteArray> getRoute(@Query("routeId") int routeId);

    //Update the athlete
    @PUT("/Athlete/UpdateAthlete")
    Call<ResponseBody> updateAthlete(@Body Athlete updateAthlete);

    //Get all clubs
    @GET("/Club/GetAllClubs")
    Call<ClubArray> getAllClubs();

    //Get the total distance ran
    @GET("/Run/Stats/Distance/GetTotalDistanceRan")
    Call<TotalDistanceResponseBody> getTotalDistanceRan(@Query("athleteId") int athleteId);

    //get the total calories run over a date frame count
    @GET("/Run/Stats/Calories/GetCaloriesOverTime")
    Call<TotalCaloriesResponseBody> getCaloriesCountOverTime(@Query("athleteId") int athleteId, @Query("startDate") String startDate, @Query("endDate") String endDate);

    //get the athletes total personal run count
    @GET("/Run/Stats/Run/Personal/GetPersonalRunCount")
    Call<TotalPersonalRunResponseBody> getAthletePersonalRunCount(@Query("athleteId") int athleteId);

    //get the athlete total event run count
    @GET("/Run/Stats/Run/Event/GetEventRunCount")
    Call<TotalEventRunResponseBody> getAthleteEventRunCount(@Query("athleteId") int athleteId);

    //Get all runs from the specified athlete
    @GET("/Run/Athlete/GetAllRuns")
    Call<RunArray> getAllRuns(@Query("athleteId") int athleteId);

    //Register the athlete to the selected club
    @POST("ClubMember/RegisterMember")
    Call<ResponseBody> registerClubMember(@Body ClubMember newMember);

    //Register the athlete to the selected event
    @POST("/EventRegistration/Register")
    Call<ResponseBody> registerAthleteToEvent(@Body EventRegistration newRegistration);

}
