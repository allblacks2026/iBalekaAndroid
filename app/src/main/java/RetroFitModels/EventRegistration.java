package RetroFitModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

/**
 * Created by Okuhle on 8/28/2016.
 */
public class EventRegistration{

    @SerializedName("registrationId")
    @Expose
    private int registrationId;

    @SerializedName("eventStatus")
    @Expose
    private String eventStatus;

    @SerializedName("athleteId")
    @Expose
    private int athleteId;

    @SerializedName("dateRegistered")
    @Expose
    private String dateRegistered;

    @SerializedName("deleted")
    @Expose
    private boolean deleted;

    @SerializedName("eventId")
    @Expose
    private int eventId;

    @SerializedName("selectedRoute")
    @Expose
    private int selectedRoute;

    @SerializedName("athlete")
    @Expose
    private Athlete athlete;

    public EventRegistration(int registrationId, String eventStatus, int athleteId, String dateRegistered, boolean deleted, int eventId, int selectedRoute, Athlete athlete) {
        this.registrationId = registrationId;
        this.eventStatus = eventStatus;
        this.athleteId = athleteId;
        this.dateRegistered = dateRegistered;
        this.deleted = deleted;
        this.eventId = eventId;
        this.selectedRoute = selectedRoute;
        this.athlete = athlete;
    }

    public EventRegistration(String eventStatus, int athleteId, String dateRegistered, boolean deleted, int eventId, int selectedRoute) {
        this.eventStatus = eventStatus;
        this.athleteId = athleteId;
        this.dateRegistered = dateRegistered;
        this.deleted = deleted;
        this.eventId = eventId;
        this.selectedRoute = selectedRoute;
    }

    public EventRegistration(String eventStatus, int athleteId, String dateRegistered, boolean deleted, int eventId, int selectedRoute, Athlete athlete) {
        this.eventStatus = eventStatus;
        this.athleteId = athleteId;
        this.dateRegistered = dateRegistered;
        this.deleted = deleted;
        this.eventId = eventId;
        this.selectedRoute = selectedRoute;
        this.athlete = athlete;
    }


    public int getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(int registrationId) {
        this.registrationId = registrationId;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public int getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(int athleteId) {
        this.athleteId = athleteId;
    }

    public String getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(String dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getSelectedRoute() {
        return selectedRoute;
    }

    public void setSelectedRoute(int selectedRoute) {
        this.selectedRoute = selectedRoute;
    }

    public Athlete getAthlete() {
        return athlete;
    }

    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }

    public EventRegistration(JSONObject object) {

    }
}
