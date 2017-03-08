package raijin.taxi69.webservices;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 1918 on 20-Jan-17.
 */

public class PlacesServiceFactory {

    private Retrofit retrofit;
    private static final String BASE_URL = "https://maps.googleapis.com";
    private static PlacesServiceFactory inst;

    public static PlacesServiceFactory getInst() {
        if (inst == null) {
            inst = new PlacesServiceFactory();
        }
        return inst;
    }

    private PlacesServiceFactory() {
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public <ServiceClass> ServiceClass createService(Class<ServiceClass> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
