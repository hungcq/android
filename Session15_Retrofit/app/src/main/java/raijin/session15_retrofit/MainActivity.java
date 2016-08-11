package raijin.session15_retrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private List<ImageView> list;
    private List<String> listUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = new ArrayList<>();
        list.add((ImageView) findViewById(R.id.image_1));
        list.add((ImageView) findViewById(R.id.image_2));
        list.add((ImageView) findViewById(R.id.image_3));
        list.add((ImageView) findViewById(R.id.image_4));
        list.add((ImageView) findViewById(R.id.image_5));
        list.add((ImageView) findViewById(R.id.image_6));
        list.add((ImageView) findViewById(R.id.image_7));

        listUrl = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetService getService = ServiceFactory.getInst().createService(GetService.class);
        Call<JsonItemList> call = getService.callJson();
        call.enqueue(new Callback<JsonItemList>() {
            @Override
            public void onResponse(Call<JsonItemList> call, Response<JsonItemList> response) {
                if(response.code() == HttpURLConnection.HTTP_OK) {
                    JsonItemList jsonItemList = response.body();
                    for(JsonWeatherItem jsonItem : jsonItemList.getJsonItemList()) {
                        listUrl.add(jsonItem.getJsonThumbItem().getUrl());
                    }
                    fillData();
                }
            }

            @Override
            public void onFailure(Call<JsonItemList> call, Throwable t) {

            }
        });
    }

    private void fillData() {
        if(listUrl.size() != 0) {
            for(int i = 0; i < listUrl.size(); i++) {
                Picasso.with(this).load(listUrl.get(i)).into(list.get(i));
            }
        }
    }
}
