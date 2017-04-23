package com.example.maxim.turaevyandex;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.maxim.turaevyandex.data.source.TranslationsDataSource;
import com.example.maxim.turaevyandex.data.source.TranslationsRepository;
import com.example.maxim.turaevyandex.data.source.local.TranslationsLocalDataSource;
import com.example.maxim.turaevyandex.data.source.remote.TranslationsRemoteDataSource;

import static dagger.internal.Preconditions.checkNotNull;

public class Injection {

    public static TranslationsRepository provideTranslationsRepository(Context context){
        return TranslationsRepository.getInstance(provideRemoteDataSource(), provideLocalDataSource(context));
    }

    public static TranslationsDataSource provideRemoteDataSource() {
        return TranslationsRemoteDataSource.getInstance();
    }

    public static TranslationsLocalDataSource provideLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        return TranslationsLocalDataSource.getInstance(context.getContentResolver());
    }

}
