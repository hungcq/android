package raijin.session16_weatherapp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 1918 on 06-Aug-16.
 */
public class JsonWeatherModel {
    private static final String MAIN = "main";
    private static final String DESCREPTION = "description";
    private static final String ICON = "icon";

    @SerializedName(MAIN)
    private String main;
    @SerializedName(DESCREPTION)
    private String description;
    @SerializedName(ICON)
    private String icon;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

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
