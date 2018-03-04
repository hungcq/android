package com.online.foodplus.webservices;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 1918 on 23-Dec-16.
 */

public class PlacesServiceFactory {
    private Retrofit retrofit;
    private static final String BASE_URL = "http://test.bigvn.net";
    private static PlacesServiceFactory inst = new PlacesServiceFactory();

    public static PlacesServiceFactory getInst() {
        return inst;
    }

    public PlacesServiceFactory() {
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public <ServiceClass> ServiceClass createService(Class<ServiceClass> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
