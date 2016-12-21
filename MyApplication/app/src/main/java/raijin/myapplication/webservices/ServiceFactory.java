package raijin.myapplication.webservices;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 1918 on 30-Nov-16.
 */

public class ServiceFactory {

    private Retrofit retrofit;
    private static final String BASE_URL = "http://api.xplay.vn";
    private static ServiceFactory inst = new ServiceFactory();

    public static ServiceFactory getInst() {
        return inst;
    }

    public ServiceFactory() {
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public <ServiceClass> ServiceClass createService(Class<ServiceClass> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
