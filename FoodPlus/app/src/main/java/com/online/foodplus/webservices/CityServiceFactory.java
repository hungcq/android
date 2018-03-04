package com.online.foodplus.webservices;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 1918 on 12-Jan-17.
 */

public class CityServiceFactory {

    private Retrofit retrofit;
    private static final String BASE_URL = "http://api.xplay.vn";
    private static CityServiceFactory inst = new CityServiceFactory();

    public static CityServiceFactory getInst() {
        return inst;
    }

    private CityServiceFactory() {
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public <ServiceClass> ServiceClass createService(Class<ServiceClass> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
