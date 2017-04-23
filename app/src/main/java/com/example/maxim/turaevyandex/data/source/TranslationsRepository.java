package com.example.maxim.turaevyandex.data.source;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.example.maxim.turaevyandex.data.Translation;
import com.example.maxim.turaevyandex.data.source.local.TranslationsLocalDataSource;

import java.util.List;

import timber.log.Timber;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Concrete implementation to load translations from the data sources into a cache.
 * <p>
 * For simplicity, this implements a dumb synchronisation between locally persisted data and data
 * obtained from the server, by using the remote data source only if the local database doesn't
 * exist.
 */

public class TranslationsRepository implements TranslationsDataSource {

    private static TranslationsRepository INSTANCE = null;

    private final TranslationsDataSource translationsRemoteDataSource;

    private final TranslationsDataSource translationsLocalDataSource;

    // Prevent direct instantiation.
    private TranslationsRepository(@NonNull TranslationsDataSource translationsRemoteDataSource,
                            @NonNull TranslationsDataSource translationsLocalDataSource) {
        this.translationsRemoteDataSource = checkNotNull(translationsRemoteDataSource);
        this.translationsLocalDataSource = checkNotNull(translationsLocalDataSource);
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param translationsRemoteDataSource the yandex data source
     * @param translationsLocalDataSource  the device storage data source
     * @return the {@link TranslationsRepository} instance
     */
    public static TranslationsRepository getInstance(TranslationsDataSource translationsRemoteDataSource,
                                                     TranslationsDataSource translationsLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new TranslationsRepository(translationsRemoteDataSource, translationsLocalDataSource);
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(TranslationsDataSource, TranslationsDataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * Gets translation from cache, local data source (SQLite) or remote data source, whichever is
     * available first.
     * <p/>
     * Note: {@link GetTranslationCallback#onDataNotAvailable()} is fired if all data sources fail to
     * get the data.
     */
    @Override
    public void getTranslation(@NonNull final String request, @NonNull final String lang, @NonNull final GetTranslationCallback callback) {
        //first try load from local storage
        Timber.d("trying to load data from local repository");

        translationsLocalDataSource.getTranslation(request, lang, new GetTranslationCallback() {
            @Override
            public void onTranslationLoaded(Translation translation) {
                callback.onTranslationLoaded(translation);
                Timber.d("data loaded form local repository");
            }

            @Override
            public void onDataNotAvailable() {
                Timber.d("data not available in local repository, performing call to remote API");
                performRemoteCall(request, lang, callback);
            }
        });
    }

    void performRemoteCall(@NonNull String request, @NonNull String lang, @NonNull final GetTranslationCallback callback) {
        // Load from server
        translationsRemoteDataSource.getTranslation(request, lang, new GetTranslationCallback() {
            @Override
            public void onTranslationLoaded(Translation translation) {
                Timber.d("data loaded from remote api, translation = '%s'", translation);
                saveTranslation(translation);
                callback.onTranslationLoaded(translation);
            }

            @Override
            public void onDataNotAvailable() {
                Timber.d("remote api is unavailable");
                callback.onDataNotAvailable();
            }
        });
    }

    /**
     * Gets marked translations from cache, local data source (SQLite) whichever is
     * available first.
     * <p/>
     * Note: {@link GetTranslationCallback#onDataNotAvailable()} is fired if all data sources fail to
     * get the data.
     */
    @Override
    public void getBookmarks(@NonNull final GetBookmarksCallback callback) {

        callback.onBookmarksLoaded(null);

//        // Load from local
//        translationsLocalDataSource.getBookmarks(new GetBookmarksCallback() {
//            @Override
//            public void onBookmarksLoaded(List<Translation> translations) {
//                //todo do something with loaded bookmarks
////                refreshLocalDataSource(translations);
//                callback.onBookmarksLoaded(null);
//            }
//
//            @Override
//            public void onDataNotAvailable() {
//                callback.onDataNotAvailable();
//            }
//        });

    }

    @Override
    public void saveTranslation(@NonNull Translation translation) {
        translationsLocalDataSource.saveTranslation(translation);
    }

    @Override
    public void deleteTranslation(@NonNull String translationId) {
        translationsLocalDataSource.deleteTranslation(translationId);
    }

    @Override
    public void setBookmark(@NonNull String translationId) {
        translationsLocalDataSource.setBookmark(translationId);
    }

    public interface LoadDataCallback {
        void onDataLoaded(Cursor data);

        void onDataEmpty();

        void onDataNotAvailable();

        void onDataReset();
    }

}
