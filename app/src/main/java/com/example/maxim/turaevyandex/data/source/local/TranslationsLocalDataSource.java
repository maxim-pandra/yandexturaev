package com.example.maxim.turaevyandex.data.source.local;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
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
        }
        return INSTANCE;
    }

    @Override
    public void getTranslation(@NonNull String request, @NonNull String lang, @NonNull GetTranslationCallback callback) {
        String selection;
        String[] selectionArgs;

        selection = TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_REQUEST + " = ? AND " +
                TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_LANG + " = ?";
        selectionArgs = new String[] { request, lang };

        Cursor query = contentResolver.query(TranslationsPersistenceContract.TranslationEntry.buildTranslationsUriWith(request, lang),
                null,
                selection,
                selectionArgs,
                null);

        if (query.moveToLast()) {
            callback.onTranslationLoaded(Translation.from(query));
        } else {
            callback.onDataNotAvailable();
        }
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
    public void clearDataBase() {
        int delete = contentResolver.delete(TranslationsPersistenceContract.TranslationEntry.buildTranslationsUri(), null, null);
        Timber.d("DeleteTranslation called, %d raws deleted", delete);
    }

    @Override
    public void deleteTranslation(@NonNull String translationId) {
        String selection = TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = {translationId};

        int delete = contentResolver.delete(TranslationsPersistenceContract.TranslationEntry.buildTranslationsUri(), selection, selectionArgs);
        Timber.d("DeleteTranslation called, %d raws deleted", delete);
    }

}
