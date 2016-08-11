package raijin.session16_weatherapp;

import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by 1918 on 07-Aug-16.
 */
public interface GetService {
    @POST("/data/2.5/weather?q=hanoi&&APPID=1d0c9ee28484e62b8e883736a30b7468")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Call<JsonModel> callJson();
}
