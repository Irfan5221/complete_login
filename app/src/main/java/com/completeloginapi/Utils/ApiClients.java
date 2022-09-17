package com.completeloginapi.Utils;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClients {

    public static Retrofit retrofit;
    public static Retrofit getClient() {


        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();


        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiConstansts.BASE_URL)
                    .client(client)
                    .addConverterFactory( GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

}
