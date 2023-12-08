package com.example.rtcvision.Main.Main.set_api;


import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class RetrofitClient {
    public static final String BASE_URL = "http://10.20.29.92:80/";
    public static final String BASE_IMG = "http://10.20.29.92:80/api/";

    private static Retrofit retrofit = null;
    public static Retrofit RetrofitInstance () {
        if (retrofit == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

            retrofit = builder.client(httpClient.build()).build();
        }
        return retrofit;
    }




}
