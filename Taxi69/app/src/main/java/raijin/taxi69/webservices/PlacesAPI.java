package raijin.taxi69.webservices;

import raijin.taxi69.models.jsonplacesmodels.JsonPlacesModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by 1918 on 20-Jan-17.
 */

public interface PlacesAPI {
    @GET("/maps/api/place/nearbysearch/json")
    Call<JsonPlacesModel> callNearbyPlaces(@Query("location") String latLng, @Query("radius") double radius,
                                           @Query("key") String key);

    @GET("/maps/api/place/nearbysearch/json")
    Call<JsonPlacesModel> calPlaces(@Query("location") String latLng, @Query("keyword") String keyword,
                                    @Query("rankby") String rankBy, @Query("key") String key);
}
