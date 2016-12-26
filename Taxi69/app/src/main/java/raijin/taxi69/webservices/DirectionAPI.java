package raijin.taxi69.webservices;

import raijin.taxi69.models.jsondirectionmodels.JsonDirectionModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by 1918 on 23-Dec-16.
 */

public interface DirectionAPI {
    @GET("/maps/api/directions/json")
    Call<JsonDirectionModel> callDirection(@Query("origin") String from, @Query("destination") String to, @Query("key") String APIkey);
}