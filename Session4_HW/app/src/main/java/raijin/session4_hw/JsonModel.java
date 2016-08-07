package raijin.session4_hw;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 1918 on 06-Aug-16.
 */
public class JsonModel {
    private static final String WEATHER = "weather";
    private static final String MAIN = "main";
    private static final String WIND = "wind";

    @SerializedName(WEATHER)
    private List<JsonWeatherModel> jsonWeatherModel;
    @SerializedName(MAIN)
    private JsonMainModel jsonMainModel;
    @SerializedName(WIND)
    private JsonWindModel jsonWindModel;

    public JsonWindModel getJsonWindModel() {
        return jsonWindModel;
    }

    public List<JsonWeatherModel> getJsonWeatherModel() {
        return jsonWeatherModel;
    }

    public void setJsonWeatherModel(List<JsonWeatherModel> jsonWeatherModel) {
        this.jsonWeatherModel = jsonWeatherModel;
    }

    public JsonMainModel getJsonMainModel() {
        return jsonMainModel;
    }

    public void setJsonMainModel(JsonMainModel jsonMainModel) {
        this.jsonMainModel = jsonMainModel;
    }
}
