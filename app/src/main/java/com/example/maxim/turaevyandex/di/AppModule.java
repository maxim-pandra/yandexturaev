package com.example.maxim.turaevyandex.di;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by maxim on 4/23/2017.
 */

@Module
public class AppModule {

    Application application;

    public AppModule(Application application) {
        application = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return application;
    }
}
