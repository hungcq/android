package raijin.session15_retrofit;

import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by 1918 on 07-Aug-16.
 */
public interface GetService {
    @POST("/category/home")
    @Headers({"Content-Type: application/json; charset=utf-8"})
    Call<JsonItemList> callJson();
}
