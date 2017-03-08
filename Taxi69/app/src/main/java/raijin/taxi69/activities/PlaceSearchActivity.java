package raijin.taxi69.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import raijin.taxi69.Constants;
import raijin.taxi69.R;
import raijin.taxi69.adapters.ListPlaceAdapter;
import raijin.taxi69.models.jsonplacesmodels.JsonPlacesModel;
import raijin.taxi69.models.jsonplacesmodels.Result;
import raijin.taxi69.webservices.PlacesAPI;
import raijin.taxi69.webservices.PlacesServiceFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceSearchActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private ListPlaceAdapter adapter;
    private List<Result> resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_search);
        init();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initData();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        double latitude = bundle.getDouble(Constants.INTENT_LATITUDE);
        double longitude = bundle.getDouble(Constants.INTENT_LONGITUDE);
        String location = latitude + "," + longitude;
        PlacesAPI placesAPI = PlacesServiceFactory.getInst().createService(PlacesAPI.class);
        placesAPI.callNearbyPlaces(location, 2000, getString(R.string.api_key))
                .enqueue(new Callback<JsonPlacesModel>() {
                    @Override
                    public void onResponse(Call<JsonPlacesModel> call, Response<JsonPlacesModel> response) {
                        if (response.isSuccessful()) {
                            JsonPlacesModel jsonPlacesModel = response.body();
                            resultList = jsonPlacesModel.getResults();
                            adapter = new ListPlaceAdapter(resultList);
                            recyclerView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonPlacesModel> call, Throwable t) {

                    }
                });
    }
}
