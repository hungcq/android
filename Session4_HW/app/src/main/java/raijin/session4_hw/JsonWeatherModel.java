package raijin.session4_hw;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 1918 on 06-Aug-16.
 */
public class JsonWeatherModel {
    private static final String MAIN = "main";
    private static final String DESCREPTION = "description";

    @SerializedName(MAIN)
    private String main;
    @SerializedName(DESCREPTION)
    private String description;

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
