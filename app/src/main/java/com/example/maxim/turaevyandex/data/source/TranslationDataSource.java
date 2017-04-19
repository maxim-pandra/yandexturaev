package com.example.maxim.turaevyandex.data.source;

import android.support.annotation.NonNull;

/**
 * Created by maxim on 4/19/2017.
 */

public interface TranslationDataSource {

    interface GetTranslationCallback {

        void onTranslationLoaded(Translation translation);

        void onDataNotAvailable();
    }

    interface GetBookmarksCallback {

        void onBookmarksLoaded (List<Translation> bookmarkList);

        void onDataNotAvailable();
    }

    void getTranslation(@NonNull String translationId, @NonNull GetTranslationCallback callback);

    void getBookmarks(@NonNull GetBookmarksCallback callback);

    void deleteTranslation(@NonNull String translationId);

    void toggleBookmark(@NonNull String translationId);
}
