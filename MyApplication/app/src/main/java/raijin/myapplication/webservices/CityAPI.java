package raijin.myapplication.webservices;

import java.util.List;

import raijin.myapplication.JsonCityModel;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by 1918 on 30-Nov-16.
 */

public interface CityAPI {
    @GET("/api/cityserv?t=0")
    Call<List<JsonCityModel>> callJsonCity();
}
