package Models;

/**
 * Created by Okuhle on 8/20/2016.
 */
public class CaloriesBurnt {

    private String dateBurnt;
    private double calorieCount;

    public CaloriesBurnt(String dateBurnt, double calorieCount) {
        this.dateBurnt = dateBurnt;
        this.calorieCount = calorieCount;
    }

    public String getDateBurnt() {
        return dateBurnt;
    }

    public void setDateBurnt(String dateBurnt) {
        this.dateBurnt = dateBurnt;
    }

    public double getCalorieCount() {
        return calorieCount;
    }

    public void setCalorieCount(double calorieCount) {
        this.calorieCount = calorieCount;
    }
}
