package com.example.maxim.turaevyandex.data.source.local;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.example.maxim.turaevyandex.data.Translation;
import com.example.maxim.turaevyandex.data.source.TranslationValues;
import com.example.maxim.turaevyandex.data.source.TranslationsDataSource;

import timber.log.Timber;


public class TranslationsLocalDataSource implements TranslationsDataSource {

    private static TranslationsLocalDataSource INSTANCE;

    private ContentResolver contentResolver;

    // Prevent direct instantiation.
    private TranslationsLocalDataSource(@NonNull ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public static TranslationsLocalDataSource getInstance(@NonNull ContentResolver contentResolver) {
        if (INSTANCE == null) {
            INSTANCE = new TranslationsLocalDataSource(contentResolver);
            INSTANCE.saveTranslation(new Translation("hello", "world", "ru-en"));
        }
        return INSTANCE;
    }

    @Override
    public void getTranslation(@NonNull String request, @NonNull String lang, @NonNull GetTranslationCallback callback) {
        // no-op since the data is loaded via Cursor Loader
    }

    @Override
    public void getBookmarks(@NonNull GetBookmarksCallback callback) {
        // no-op since the data is loaded via Cursor Loader
    }

    @Override
    public void saveTranslation(@NonNull Translation translation) {
        ContentValues values = TranslationValues.from(translation);
        contentResolver.insert(TranslationsPersistenceContract.TranslationEntry.buildTranslationsUri(), values);
    }

    @Override
    public void setBookmark(@NonNull String translationId) {
        ContentValues values = new ContentValues();
        values.put(TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_BOOKMARKED, 1);

        String selection = TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = {translationId};

        int rows = contentResolver.update(TranslationsPersistenceContract.TranslationEntry.buildTranslationsUri(), values, selection, selectionArgs);
        Timber.d("setBookmark called, %d rows updated", rows);

    }

    @Override
    public void deleteTranslation(@NonNull String translationId) {
        String selection = TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = {translationId};

        int delete = contentResolver.delete(TranslationsPersistenceContract.TranslationEntry.buildTranslationsUri(), selection, selectionArgs);
        Timber.d("DeleteTranslation called, %d raws deleted", delete);
    }

}
