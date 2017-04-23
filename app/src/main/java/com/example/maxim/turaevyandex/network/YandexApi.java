package com.example.maxim.turaevyandex.network;

import com.example.maxim.turaevyandex.data.models.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YandexApi {

    @GET("/api/v1.5/tr.json/translate")
    Call<ApiResponse> translate(@Query("text") String request, @Query("lang") String lang, @Query("key") String key);
}
