package raijin.session16_weatherapp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 1918 on 06-Aug-16.
 */
public class JsonMainModel {
    private static final String TEMP = "temp";
    private static final String PRESSURE = "pressure";
    private static final String HUMIDITY = "humidity";
    private static final String TEMP_MIN = "temp_min";
    private static final String TEMP_MAX = "temp_max";

    @SerializedName(TEMP)
    private double temp;
    @SerializedName(PRESSURE)
    private double pressure;
    @SerializedName(HUMIDITY)
    private int humidity;
    @SerializedName(TEMP_MIN)
    private double minTemp;
    @SerializedName(TEMP_MAX)
    private double maxTemp;

    public double getMaxTemp() {
        return maxTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
}
