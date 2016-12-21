package raijin.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import raijin.myapplication.webservices.CityAPI;
import raijin.myapplication.webservices.ServiceFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final DBContext dbContext = DBContext.getInst(this);
        CityAPI cityAPI = ServiceFactory.getInst().createService(CityAPI.class);
        final Call<List<JsonCityModel>> call = cityAPI.callJsonCity();
        call.enqueue(new Callback<List<JsonCityModel>>() {
            @Override
            public void onResponse(Call<List<JsonCityModel>> call, Response<List<JsonCityModel>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    List<JsonCityModel> jsonCityModelList = response.body();
                    for (JsonCityModel jsonCityModel : jsonCityModelList) {
                        dbContext.addRecord(jsonCityModel.getID(), jsonCityModel.getName(), jsonCityModel.getAscName(), jsonCityModel.getZipCode(), jsonCityModel.getStatus(), jsonCityModel.getCountryCode());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<JsonCityModel>> call, Throwable t) {

            }
        });
    }
}
