package talitha_koum.milipade.com.app.afdis.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by TALITHA_KOUM on 3/9/2018.
 * file for the  Afdis. project
 * in talitha_koum.milipade.com.app.afdis.network
 */
///afdis-api.hurudzaafrica.com
public class ApiClient {
    //App.getPrefManager();//0773029947

    public static final String BASE_URL = "http://afdis-api.hurudzaafrica.com/v1/";
    //public static final String BASE_URL = "http://192.168.43.56/afdis_api/v1/";
    private static Retrofit retrofit = null;
    // private static Retrofit retrofit = null;
    private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

public static Retrofit getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).readTimeout(80, TimeUnit.SECONDS).connectTimeout(80, TimeUnit.SECONDS).addInterceptor(interceptor).build();


        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    //.addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
