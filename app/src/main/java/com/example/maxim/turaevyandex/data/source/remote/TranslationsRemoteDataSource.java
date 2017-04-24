package com.example.maxim.turaevyandex.data.source.remote;

import android.support.annotation.NonNull;

import com.example.maxim.turaevyandex.BuildConfig;
import com.example.maxim.turaevyandex.data.Translation;
import com.example.maxim.turaevyandex.data.models.ApiResponse;
import com.example.maxim.turaevyandex.data.source.TranslationsDataSource;
import com.example.maxim.turaevyandex.network.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Implementation of the data source that adds async network calls.
 */

public class TranslationsRemoteDataSource implements TranslationsDataSource {

    private static TranslationsRemoteDataSource INSTANCE;

    private final RestClient restClient;

    private static void addTranslation(String request, String translationText, String lang) {
        Translation translation = new Translation(request, translationText, lang);
    }

    // Prevent direct instantiation.
    private TranslationsRemoteDataSource() {
        restClient = new RestClient();
    }

    public static TranslationsRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TranslationsRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getTranslation(@NonNull final String request, @NonNull String lang, @NonNull final GetTranslationCallback callback) {
        // Simulate network
        Call<ApiResponse> translate = restClient.getApiService().translate(request, lang, BuildConfig.YANDEX_API_KEY);
        translate.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    callback.onTranslationLoaded(Translation.from(request, response.body()));
                } else {
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Timber.e(t);
                callback.onDataNotAvailable();
            }
        });
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
    public void removeBookmark(@NonNull String translationId) {
        throw new UnsupportedOperationException("Bookmarks should be removed in local datasource");
    }

    @Override
    public void clearDataBase() {
        throw new UnsupportedOperationException("We don't have remote database");
    }

    @Override
    public void saveTranslation(@NonNull Translation translation) {
        throw new UnsupportedOperationException("Translations should be saved in local datasource");
    }
}
