package raijin.session16_weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetService getService = ServiceFactory.getInst().createService(GetService.class);
        Call<JsonModel> call = getService.callJson();
        call.enqueue(new Callback<JsonModel>() {
            @Override
            public void onResponse(Call<JsonModel> call, Response<JsonModel> response) {
                if(response.code() == HttpURLConnection.HTTP_OK) {
                    JsonModel jsonModel = response.body();
                    for(JsonMainModel jsonItem : jsonItemList.getJsonItemList()) {
                        listUrl.add(jsonItem.getJsonThumbItem().getUrl());
                    }
                    fillData();
                }
            }

            @Override
            public void onFailure(Call<JsonModel> call, Throwable t) {

            }
        });
    }
}
