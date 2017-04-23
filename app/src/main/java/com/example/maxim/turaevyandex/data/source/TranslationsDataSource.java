package com.example.maxim.turaevyandex.data.source;

import android.support.annotation.NonNull;

import com.example.maxim.turaevyandex.data.Translation;

import java.util.List;


public interface TranslationsDataSource {


    interface GetTranslationCallback {

        void onTranslationLoaded(Translation translation);

        void onDataNotAvailable();
    }

    interface GetBookmarksCallback {

        void onBookmarksLoaded (List<Translation> bookmarkList);

        void onDataNotAvailable();
    }

    void clearDataBase();

    void getTranslation(@NonNull String request, @NonNull String lang, @NonNull GetTranslationCallback callback);

    void getBookmarks(@NonNull GetBookmarksCallback callback);

    void deleteTranslation(@NonNull String translationId);

    void setBookmark(@NonNull String translationId);

    void saveTranslation(@NonNull Translation translation);

}
