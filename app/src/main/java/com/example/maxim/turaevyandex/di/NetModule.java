package com.example.maxim.turaevyandex.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Rest adapter will be instantiated in this module when we will add Dagger to the application
 * Created by maxim on 4/23/2017.
 */

@Module
public class NetModule {
    String baseUrl;

    // Constructor needs one parameter to instantiate.
    public NetModule(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .client(new OkHttpClient())
                .build();
    }

}
