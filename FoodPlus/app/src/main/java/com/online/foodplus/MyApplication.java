package com.online.foodplus;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

/**
 * Created by 1918 on 06-Dec-16.
 */

public class MyApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);

      /*  Realm.init(getApplicationContext());
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm.deleteRealm(realmConfiguration);
        initiateRealmDatabase();*/
    }

/*    private void initiateRealmDatabase() {
        CityAPI cityAPI = CityServiceFactory.getInst().createService(CityAPI.class);
        cityAPI.callCity().enqueue(new Callback<CityModelContainer>() {
            @Override
            public void onResponse(Call<CityModelContainer> call, Response<CityModelContainer> response) {
               // System.out.println("----------------------onResponse");
                if (response.isSuccessful()) {
                    CityModelContainer cityModelContainer = response.body();
                    List<CityModel> cityModelList = cityModelContainer.getData();
                    for (CityModel cityModel : cityModelList) {
                        RealmHandler.getInst().addCityToRealm(cityModel);
                    }
                }
            }

            @Override
            public void onFailure(Call<CityModelContainer> call, Throwable t) {
               // System.out.println("----------------------onFailure");

            }
        });

        RealmHandler.getInst().addCategoryToRealm();
    }*/
}
