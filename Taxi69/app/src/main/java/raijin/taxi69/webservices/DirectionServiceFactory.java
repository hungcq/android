package raijin.taxi69.webservices;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 1918 on 23-Dec-16.
 */

public class DirectionServiceFactory {

    private Retrofit retrofit;
    private static final String BASE_URL = "https://maps.googleapis.com";
    private static DirectionServiceFactory inst;

    public static DirectionServiceFactory getInst() {
        if (inst == null) {
            inst = new DirectionServiceFactory();
        }
        return inst;
    }

    private DirectionServiceFactory() {
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public <ServiceClass> ServiceClass createService(Class<ServiceClass> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
