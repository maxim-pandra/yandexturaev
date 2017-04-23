package com.example.maxim.turaevyandex.data.source;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.example.maxim.turaevyandex.data.source.local.TranslationsPersistenceContract;
import com.example.maxim.turaevyandex.history.HistoryFilter;

public class LoaderProvider {

    @NonNull
    private final Context context;

    public LoaderProvider(@NonNull Context context) {
        this.context = context;
    }

    public Loader<Cursor> createFilteredTranslationsLoader(HistoryFilter historyFilter) {
        String selection = null;
        String[] selectionArgs = null;
        switch (historyFilter.getHistoryFilterType()) {

            case ALL_TRANSLATIONS:
                selection = null;
                selectionArgs = null;
                break;
            case MARKED_TRANSLATIONS:
                selection = TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_BOOKMARKED + " = ?";
                selectionArgs = new String[]{String.valueOf(1)};
                break;
        }

        return new CursorLoader(
                context,
                TranslationsPersistenceContract.TranslationEntry.buildTranslationsUri(),
                TranslationsPersistenceContract.TranslationEntry.TRANSLATIONS_COLUMNS,
                selection,
                selectionArgs,
                null
        );
    }

    public Loader<Cursor> createTranslationLoader(String translationId) {
        return new CursorLoader(
                context,
                TranslationsPersistenceContract.TranslationEntry.buildTranslationsUriWith(translationId),
                null,
                null,
                new String[]{String.valueOf(translationId)},
                null
        );
    }
}
