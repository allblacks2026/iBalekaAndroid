package RetroFitModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Okuhle on 8/19/2016.
 */
public class Athlete {

    @SerializedName("AthleteID")
    @Expose
    private int athleteID;

    @SerializedName("DateOfBirth")
    @Expose
    private Date dateOfBirth;

    @SerializedName("Deleted")
    @Expose
    private boolean isDeleted;

    @SerializedName("Name")
    @Expose
    private String name;

    @SerializedName("UserName")
    @Expose
    private String username;

    @SerializedName("Gender")
    @Expose
    private int gender;

    @SerializedName("Height")
    @Expose
    private double height;

    @SerializedName("Weight")
    @Expose
    private double weight;

    @SerializedName("Password")
    @Expose
    private String password;

    @SerializedName("EmailAddress")
    @Expose
    private String emailAddress;

    @SerializedName("SecurityQuestion")
    @Expose
    private String securityQuestion;

    @SerializedName("SecurityAnswer")
    @Expose
    private String securityAnswer;

    @SerializedName("Surname")
    @Expose
    private String surname;

    @SerializedName("Country")
    @Expose
    private String country;

    public Athlete(int athleteID, Date dateOfBirth, boolean isDeleted, String name, String username, int gender, double height, double weight, String password, String emailAddress, String securityQuestion, String securityAnswer, String surname, String country) {
        this.athleteID = athleteID;
        this.dateOfBirth = dateOfBirth;
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

    public Athlete(JSONObject athleteObject) {

    }

    public int getAthleteID() {
        return athleteID;
    }

    public void setAthleteID(int athleteID) {
        this.athleteID = athleteID;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
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
}
