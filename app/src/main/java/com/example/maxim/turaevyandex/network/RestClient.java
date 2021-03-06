package com.example.maxim.turaevyandex.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RestClient {
    private static final String API_BASE_URL = "https://translate.yandex.net";
    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private YandexApi apiService;

    public RestClient() {
        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        Retrofit retrofit = builder.client(httpClient.build()).build();

        apiService = retrofit.create(YandexApi.class);
    }

    public YandexApi getApiService() {
        return apiService;
    }
}
