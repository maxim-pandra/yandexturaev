package com.example.maxim.turaevyandex.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TranslationsDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Translations.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String INTEGER_TYPE = " INTEGER";

    private static final String BOOLEAN_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TranslationsPersistenceContract.TranslationEntry.TABLE_NAME + " (" +
                    TranslationsPersistenceContract.TranslationEntry._ID + INTEGER_TYPE + " PRIMARY KEY," +
                    TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_REQUEST + TEXT_TYPE + COMMA_SEP +
                    TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_TRANSLATION_TEXT + TEXT_TYPE + COMMA_SEP +
                    TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_LANG + TEXT_TYPE + COMMA_SEP +
                    TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_BOOKMARKED + BOOLEAN_TYPE +
                    " )";

    public TranslationsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }
}
