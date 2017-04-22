package com.example.maxim.turaevyandex.data.source;

import android.content.ContentValues;

import com.example.maxim.turaevyandex.data.Translation;
import com.example.maxim.turaevyandex.data.source.local.TranslationsPersistenceContract;


public class TranslationValues {

    public static ContentValues from(Translation translation) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_ENTRY_ID, translation.getId());
        contentValues.put(TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_REQUEST, translation.getRequest());
        contentValues.put(TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_TRANSLATION_TEXT, translation.getTranslationText());
        contentValues.put(TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_LANG, translation.getLang());
        contentValues.put(TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_BOOKMARKED, translation.isBookmarked() ? 1 : 0);

        return contentValues;
    }

}
