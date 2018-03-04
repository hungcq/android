package com.online.foodplus.webservices;

import com.online.foodplus.models.Base;
import com.online.foodplus.models.Base2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by 1918 on 10-Jan-17.
 */

public interface PlacesAPI {
    @GET("/api/mapsrv")
    Call<List<Base2>> callPlaces(@Query("lat") double lat, @Query("long") double lng,
                                 @Query("dis") double distance);
}
