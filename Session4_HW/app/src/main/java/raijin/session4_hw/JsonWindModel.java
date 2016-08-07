package raijin.session4_hw;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 1918 on 07-Aug-16.
 */
public class JsonWindModel {
    private static final String SPEED = "speed";

    @SerializedName(SPEED)
    private double speed;

    public double getSpeed() {
        return speed;
    }
}
