package com.example.maxim.turaevyandex.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.UUID;

/**
 * Immutable class for a Translation
 */

public class Translation {

    @NonNull
    private final String id;

    @Nullable
    private final String request;

    @Nullable
    private final String translation;

    @Nullable
    private final String lang;

    private final boolean isBookmarked;

    /**
     * Use this constructor to create a new Translation
     *
     * @param request
     * @param translation
     * @param lang
     */
    public Translation (@Nullable String request, @Nullable String translation, @NonNull String lang) {
        id = UUID.randomUUID().toString();
        this.request = request;
        this.translation = translation;
        this.lang = lang;
        isBookmarked = false;
    }

    /**
     * Use this constructor to create a new bookmarked Translation
     *
     * @param request
     * @param translation
     * @param lang
     */
    public Translation (@Nullable String request, @Nullable String translation, @NonNull String lang, boolean isBookmarked) {
        id = UUID.randomUUID().toString();
        this.request = request;
        this.translation = translation;
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
                TasksPersistenceContract.TaskEntry.COLUMN_NAME_ENTRY_ID));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(
                TasksPersistenceContract.TaskEntry.COLUMN_NAME_TITLE));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(
                TasksPersistenceContract.TaskEntry.COLUMN_NAME_DESCRIPTION));
        boolean completed = cursor.getInt(cursor.getColumnIndexOrThrow(
                TasksPersistenceContract.TaskEntry.COLUMN_NAME_COMPLETED)) == 1;
        return new Translation(title, description, entryId, completed);
    }

    public static Translation from(ContentValues values) {
        String entryId = values.getAsString(TasksPersistenceContract.TaskEntry.COLUMN_NAME_ENTRY_ID);
        String title = values.getAsString(TasksPersistenceContract.TaskEntry.COLUMN_NAME_TITLE);
        String description = values.getAsString(TasksPersistenceContract.TaskEntry.COLUMN_NAME_DESCRIPTION);
        boolean completed = values.getAsInteger(TasksPersistenceContract.TaskEntry.COLUMN_NAME_COMPLETED) == 1;

        return new Translation(title, description, entryId, completed);
    }

}
