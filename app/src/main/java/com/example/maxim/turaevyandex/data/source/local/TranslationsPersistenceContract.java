package com.example.maxim.turaevyandex.data.source.local;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.maxim.turaevyandex.BuildConfig;

/**
 * The contract used for the db to save the translations locally.
 */

public class TranslationsPersistenceContract {

    public static final String CONTENT_AUTHORITY = BuildConfig.APPLICATION_ID;
    public static final String CONTENT_TRANSLATION_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + TranslationEntry.TABLE_NAME;
    public static final String CONTENT_TRANSLATION_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + TranslationEntry.TABLE_NAME;
    public static final String VND_ANDROID_CURSOR_ITEM_VND = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + ".";
    private static final String CONTENT_SCHEME = "content://";
    public static final Uri BASE_CONTENT_URI = Uri.parse(CONTENT_SCHEME + CONTENT_AUTHORITY);
    private static final String VND_ANDROID_CURSOR_DIR_VND = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + ".";
    private static final String SEPARATOR = "/";

    //cannot be instantiated
    private TranslationsPersistenceContract() {
        throw new AssertionError();
    }

    public static Uri getBaseTranslationUri(String translationId) {
        return Uri.parse(CONTENT_SCHEME + CONTENT_TRANSLATION_ITEM_TYPE + SEPARATOR + translationId);
    }

    /* Inner class that defines the table contents */
    public static abstract class TranslationEntry implements BaseColumns {

        public static final String TABLE_NAME = "translation";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_REQUEST = "request";
        public static final String COLUMN_NAME_TRANSLATION_TEXT = "translation_text";
        public static final String COLUMN_NAME_LANG = "lang";
        public static final String COLUMN_NAME_BOOKMARKED = "bookmarked";
        public static final Uri CONTENT_TRANSLATION_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static String[] TRANSLATIONS_COLUMNS = new String[]{
                TranslationEntry._ID,
                TranslationEntry.COLUMN_NAME_ENTRY_ID,
                TranslationEntry.COLUMN_NAME_REQUEST,
                TranslationEntry.COLUMN_NAME_TRANSLATION_TEXT,
                TranslationEntry.COLUMN_NAME_LANG,
                TranslationEntry.COLUMN_NAME_BOOKMARKED};

        public static Uri buildTranslationsUriWith(long id) {
            return ContentUris.withAppendedId(CONTENT_TRANSLATION_URI, id);
        }

        public static Uri buildTranslationsUriWith(String id) {
            return CONTENT_TRANSLATION_URI.buildUpon().appendPath(id).build();
        }

        public static Uri buildTranslationsUri() {
            return CONTENT_TRANSLATION_URI.buildUpon().build();
        }

        public static Uri buildTranslationsUriWith(String request, String direction) {
            return CONTENT_TRANSLATION_URI.buildUpon()
                    .appendPath(request)
                    .appendPath(direction)
                    .build();
        }

    }
}
