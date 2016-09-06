package RetroFitModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Okuhle on 8/19/2016.
 */
public class Athlete implements Serializable {

    @SerializedName("athleteId")
    @Expose
    private int athleteID;

    @SerializedName("dateOfBirth")
    @Expose
    private String dateOfBirth;

    @SerializedName("dateJoined")
    private String dateJoined;

    @SerializedName("deleted")
    @Expose
    private boolean isDeleted;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("userName")
    @Expose
    private String username;

    @SerializedName("gender")
    @Expose
    private int gender;
    @SerializedName("height")
    @Expose
    private double height;

    @SerializedName("weight")
    @Expose
    private double weight;
    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("emailAddress")
    @Expose
    private String emailAddress;

    @SerializedName("securityQuestion")
    @Expose
    private String securityQuestion;

    @SerializedName("securityAnswer")
    @Expose
    private String securityAnswer;

    @SerializedName("surname")
    @Expose
    private String surname;
    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("clubMember")
    @Expose
    private List<ClubMember> clubMember = new ArrayList<>();

    @SerializedName("eventRegistration")
    @Expose
    private List<EventRegistration> eventRegistration = new ArrayList<>();

    @SerializedName("run")
    @Expose
    private List<Run> run = new ArrayList<>();

    private JSONObject object;
    public Athlete(JSONObject object) {
        try {
            //this.athleteID = object.getInt("athleteId");
            this.dateOfBirth = object.getString("dateOfBirth");
            this.dateJoined = object.getString("dateJoined");
            this.isDeleted = object.getBoolean("deleted");
            this.name = object.getString("name");
            this.username = object.getString("userName");
            this.gender = object.getInt("gender");
            this.height = object.getDouble("height");
            this.weight = object.getDouble("weight");
            this.password = object.getString("password");
            this.emailAddress = object.getString("emailAddress");
            this.securityQuestion = object.getString("securityQuestion");
            this.securityAnswer = object.getString("securityAnswer");
            this.surname = object.getString("surname");
            this.country = object.getString("country");
            this.object = object;
        } catch (Exception error) {

        }
    }

    public Athlete(int athleteID, String dateJoined, String dateOfBirth, boolean isDeleted, String name, String username, int gender, double height, double weight, String password, String emailAddress, String securityQuestion, String securityAnswer, String surname, String country) {
        this.athleteID = athleteID;
        this.dateOfBirth = dateOfBirth;
        this.dateJoined = dateJoined;
        this.isDeleted = isDeleted;
        this.name = name;
        this.username = username;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.password = password;
        this.emailAddress = emailAddress;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.surname = surname;
        this.country = country;
    }

    public int getAthleteID() {
        return athleteID;
    }

    public void setAthleteID(int athleteID) {
        this.athleteID = athleteID;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<ClubMember> getClubMember() {
        return clubMember;
    }

    public void setClubMember(List<ClubMember> clubMember) {
        this.clubMember = clubMember;
    }

    public List<EventRegistration> getEventRegistration() {
        return eventRegistration;
    }

    public void setEventRegistration(List<EventRegistration> eventRegistration) {
        this.eventRegistration = eventRegistration;
    }

    public List<Run> getRun() {
        return run;
    }

    public void setRun(List<Run> run) {
        this.run = run;
    }

    public JSONObject getObject() {
        return object;
    }

    public void setObject(JSONObject object) {
        this.object = object;
    }

    public String getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(String dateJoined) {
        this.dateJoined = dateJoined;
    }
}
