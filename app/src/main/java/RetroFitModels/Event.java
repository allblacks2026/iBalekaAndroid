package RetroFitModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Okuhle on 8/28/2016.
 */

@SuppressWarnings("serial")
public class Event implements Serializable{

    @SerializedName("eventId")
    @Expose
    private int eventId;

    @SerializedName("userId")
    @Expose
    private String userId;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("dateModified")
    @Expose
    private String dateModified;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("eventStatus")
    @Expose
    private String eventStatus;

    @SerializedName("deleted")
    @Expose
    private String deleted;

    @SerializedName("clubID")
    @Expose
    private String clubId;

    @SerializedName("eventRegistration")
    @Expose
    private List<EventRegistration> eventRegistration = new ArrayList<>();

    @SerializedName("eventRoute")
    @Expose
    private List<EventRoute>  eventRoute = new ArrayList<>();


    public Event(int eventId, String userId, String date, String time, String dateModified, String description, String location, String title, String eventStatus, String deleted, String clubId, List<EventRegistration> eventRegistration, List<EventRoute> eventRoute) {
        this.eventId = eventId;
        this.userId = userId;
        this.date = date;
        this.time = time;
        this.dateModified = dateModified;
        this.description = description;
        this.location = location;
        this.title = title;
        this.eventStatus = eventStatus;
        this.deleted = deleted;
        this.clubId = clubId;
        this.eventRegistration = eventRegistration;
        this.eventRoute = eventRoute;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getClubId() {
        return clubId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    public List<EventRegistration> getEventRegistration() {
        return eventRegistration;
    }

    public void setEventRegistration(List<EventRegistration> eventRegistration) {
        this.eventRegistration = eventRegistration;
    }

    public List<EventRoute> getEventRoute() {
        return eventRoute;
    }

    public void setEventRoute(List<EventRoute> eventRoute) {
        this.eventRoute = eventRoute;
    }
}
