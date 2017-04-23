package com.example.maxim.turaevyandex.data.source.remote;

import android.support.annotation.NonNull;

import com.example.maxim.turaevyandex.data.Translation;
import com.example.maxim.turaevyandex.data.source.TranslationsDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the data source that adds async network calls.
 */

public class TranslationsRemoteDataSource implements TranslationsDataSource {

    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;
    private final static List<Translation> TRANSLATIONS_SERVICE_DATA;
    private static TranslationsRemoteDataSource INSTANCE;

    static {
        TRANSLATIONS_SERVICE_DATA = new ArrayList<>();
        addTranslation("Hello", "привет", "en-ru");
        addTranslation("World", "мир", "en-ru");
    }

    private static void addTranslation(String request, String translationText, String lang) {
        Translation translation = new Translation(request, translationText, lang);
        TRANSLATIONS_SERVICE_DATA.add(translation);
    }

    // Prevent direct instantiation.
    private TranslationsRemoteDataSource() {
    }

    public static TranslationsRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TranslationsRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getTranslation(@NonNull String request, @NonNull String lang, @NonNull GetTranslationCallback callback) {
        // Simulate network
        try {
            Thread.sleep(SERVICE_LATENCY_IN_MILLIS);
        } catch (InterruptedException e) {
        }
        callback.onTranslationLoaded(TRANSLATIONS_SERVICE_DATA.get(0));
    }

    @Override
    public void getBookmarks(@NonNull GetBookmarksCallback callback) {
        throw new UnsupportedOperationException("Bookmarks should be retrieved using local datasource");
    }

    @Override
    public void deleteTranslation(@NonNull String translationId) {

    }

    @Override
    public void setBookmark(@NonNull String translationId) {
        throw new UnsupportedOperationException("Bookmarks should be set in local datasource");
    }

    @Override
    public void saveTranslation(@NonNull Translation translation) {
        throw new UnsupportedOperationException("Translations should be saved in local datasource");
    }
}
