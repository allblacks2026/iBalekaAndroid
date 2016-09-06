package RetroFitModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Okuhle on 8/29/2016.
 */
public class Run implements Serializable{

    @SerializedName("runId")
    @Expose
    private int runId;

    @SerializedName("athleteId")
    @Expose
    private int athleteId;

    @SerializedName("caloriesBurnt")
    @Expose
    private double caloriesBurnt;

    @SerializedName("distance")
    @Expose
    private double distance;

    @SerializedName("dateRecorded")
    @Expose
    private String dateRecorded;

    @SerializedName("deleted")
    @Expose
    private boolean deleted;

    @SerializedName("endTime")
    @Expose
    private String endTime;

    @SerializedName("runType")
    @Expose
    private String runType;

    @SerializedName("eventId")
    @Expose
    private int eventId;

    @SerializedName("routeId")
    @Expose
    private int routeId;

    @SerializedName("startTime")
    @Expose
    private String startTime;

    @SerializedName("rating")
    @Expose
    private Rating rating;

    @SerializedName("event")
    @Expose
    private Event event;

    @SerializedName("route")
    @Expose
    private Route route;

    public Run(int runId, int athleteId, double caloriesBurnt, double distance, String dateRecorded, boolean deleted, String endTime, String runType, int eventId, int routeId, String startTime, Rating rating, Event event, Route route) {
        this.runId = runId;
        this.athleteId = athleteId;
        this.caloriesBurnt = caloriesBurnt;
        this.distance = distance;
        this.dateRecorded = dateRecorded;
        this.deleted = deleted;
        this.endTime = endTime;
        this.runType = runType;
        this.eventId = eventId;
        this.routeId = routeId;
        this.startTime = startTime;
        this.rating = rating;
        this.event = event;
        this.route = route;
    }

    public int getRunId() {
        return runId;
    }

    public void setRunId(int runId) {
        this.runId = runId;
    }

    public int getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(int athleteId) {
        this.athleteId = athleteId;
    }

    public double getCaloriesBurnt() {
        return caloriesBurnt;
    }

    public void setCaloriesBurnt(double caloriesBurnt) {
        this.caloriesBurnt = caloriesBurnt;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getDateRecorded() {
        return dateRecorded;
    }

    public void setDateRecorded(String dateRecorded) {
        this.dateRecorded = dateRecorded;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRunType() {
        return runType;
    }

    public void setRunType(String runType) {
        this.runType = runType;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
