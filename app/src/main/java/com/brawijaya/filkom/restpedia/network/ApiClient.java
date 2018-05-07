package com.brawijaya.filkom.restpedia.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit mRetrofit;

    private ApiClient() {
        // Class is not publicly instantiate
    }

    public static ApiHelper create() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder().baseUrl("").client(client)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return mRetrofit.create(ApiHelper.class);
    }
}
