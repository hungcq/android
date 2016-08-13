package raijin.session16_weatherapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by 1918 on 07-Aug-16.
 */
public interface GetService {
//    http://api.openweathermap.org/data/2.5/weather?q=hanoi&&APPID=1d0c9ee28484e62b8e883736a30b7468
    @GET("/data/2.5/weather")
    Call<JsonModel> callJson(@Query("q") String location, @Query("appid") String key);
}
