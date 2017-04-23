package com.example.maxim.turaevyandex.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.maxim.turaevyandex.data.models.ApiResponse;
import com.example.maxim.turaevyandex.data.source.local.TranslationsPersistenceContract;

import java.util.UUID;

/**
 * Immutable class for a Translation
 */

public class Translation {

    @NonNull
    private final String id;

    @NonNull
    private final String request;

    @NonNull
    private final String translationText;

    @NonNull
    private final String lang;

    private final boolean isBookmarked;

    /**
     * Use this constructor to create a new Translation
     *
     * @param request
     * @param translationText
     * @param lang
     */
    public Translation (@NonNull String request, @NonNull String translationText, @NonNull String lang) {
        id = UUID.randomUUID().toString();
        this.request = request;
        this.translationText = translationText;
        this.lang = lang;
        isBookmarked = false;
    }

    /**
     * Use this constructor to create a new bookmarked Translation
     *
     * @param request
     * @param translationText
     * @param lang
     */
    public Translation (@NonNull String request, @NonNull String translationText, @NonNull String lang, boolean isBookmarked) {
        id = UUID.randomUUID().toString();
        this.request = request;
        this.translationText = translationText;
        this.lang = lang;
        this.isBookmarked = isBookmarked;
    }

    /**
     * Use this constructor to create a new bookmarked Translation
     *
     * @param request
     * @param translationText
     * @param lang
     */
    public Translation (@NonNull String request, @NonNull String translationText, @NonNull String lang, @NonNull String id, boolean isBookmarked) {
        this.id = id;
        this.request = request;
        this.translationText = translationText;
        this.lang = lang;
        this.isBookmarked = isBookmarked;
    }

    /**
     * Use this constructor to return a Translation from a Cursor
     *
     * @return
     */
    public static Translation from(Cursor cursor) {
        String entryId = cursor.getString(cursor.getColumnIndexOrThrow(
                TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_ENTRY_ID));
        String request = cursor.getString(cursor.getColumnIndexOrThrow(
                TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_REQUEST));
        String translationText = cursor.getString(cursor.getColumnIndexOrThrow(
                TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_TRANSLATION_TEXT));
        String lang = cursor.getString(cursor.getColumnIndexOrThrow(
                TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_LANG));
        boolean completed = cursor.getInt(cursor.getColumnIndexOrThrow(
                TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_BOOKMARKED)) == 1;
        return new Translation(request, translationText, lang, entryId, completed);
    }

    public static Translation from(ContentValues values) {
        String entryId = values.getAsString(TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_ENTRY_ID);
        String request = values.getAsString(TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_REQUEST);
        String translationText = values.getAsString(TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_TRANSLATION_TEXT);
        String lang = values.getAsString(TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_LANG);
        boolean bookmarked = values.getAsInteger(TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_BOOKMARKED) == 1;

        return new Translation(request, translationText, lang, entryId, bookmarked);
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getRequest() {
        return request;
    }

    @NonNull
    public String getTranslationText() {
        return translationText;
    }

    @NonNull
    public String getLang() {
        return lang;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Translation that = (Translation) o;

        if (isBookmarked != that.isBookmarked) return false;
        if (!id.equals(that.id)) return false;
        if (!request.equals(that.request)) return false;
        if (!translationText.equals(that.translationText)) return false;
        return lang.equals(that.lang);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + request.hashCode();
        result = 31 * result + translationText.hashCode();
        result = 31 * result + lang.hashCode();
        result = 31 * result + (isBookmarked ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Translation{" +
                "request='" + request + '\'' +
                ", lang='" + lang + '\'' +
                '}';
    }

    public static Translation from(String request, ApiResponse response) {
        return new Translation(request,
                TextUtils.join(" ", response.text),
                response.lang);
    }
}
