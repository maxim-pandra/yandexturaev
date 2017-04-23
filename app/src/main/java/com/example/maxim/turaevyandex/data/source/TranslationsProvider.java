package com.example.maxim.turaevyandex.data.source;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.maxim.turaevyandex.data.source.local.TranslationsDbHelper;
import com.example.maxim.turaevyandex.data.source.local.TranslationsPersistenceContract;

import java.util.List;

import timber.log.Timber;


public class TranslationsProvider extends ContentProvider {

    private static final int TRANSLATION = 100;
    private static final int TRANSLATION_ITEM = 101;
    private static final int TRANSLATION_ITEM_BY_REQUEST = 102;
    private static final UriMatcher uriMatcher = buildUriMatcher();
    private TranslationsDbHelper translationsDbHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = TranslationsPersistenceContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, TranslationsPersistenceContract.TranslationEntry.TABLE_NAME, TRANSLATION);
        matcher.addURI(authority, TranslationsPersistenceContract.TranslationEntry.TABLE_NAME + "/*", TRANSLATION_ITEM);
        matcher.addURI(authority, TranslationsPersistenceContract.TranslationEntry.TABLE_NAME + "/*" + "/*", TRANSLATION_ITEM_BY_REQUEST);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        Timber.d("onCreate() called on provider");
        translationsDbHelper = new TranslationsDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Timber.d("Provider query method called with uri = '%s'", uri.toString());
        Cursor retCursor;
        switch (uriMatcher.match(uri)) {
            case TRANSLATION:
                retCursor = translationsDbHelper.getReadableDatabase().query(
                        TranslationsPersistenceContract.TranslationEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case TRANSLATION_ITEM:
                String[] where = {uri.getLastPathSegment()};
                retCursor = translationsDbHelper.getReadableDatabase().query(
                        TranslationsPersistenceContract.TranslationEntry.TABLE_NAME,
                        projection,
                        TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_ENTRY_ID + " = ?",
                        where,
                        null,
                        null,
                        sortOrder
                );
                break;
            case TRANSLATION_ITEM_BY_REQUEST:
                List<String> pathSegments = uri.getPathSegments();
                String[] where2 = {pathSegments.get(pathSegments.size()-2), pathSegments.get(pathSegments.size()-1)};
                retCursor = translationsDbHelper.getReadableDatabase().query(
                        TranslationsPersistenceContract.TranslationEntry.TABLE_NAME,
                        projection,
                        TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_REQUEST + " = ? AND " +
                                TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_LANG + " = ?",
                        where2,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Timber.d("getType called with uri = '%s'", uri.toString());
        final int match = uriMatcher.match(uri);
        switch (match) {
            case TRANSLATION:
                return TranslationsPersistenceContract.CONTENT_TRANSLATION_TYPE;
            case TRANSLATION_ITEM:
                return TranslationsPersistenceContract.CONTENT_TRANSLATION_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = translationsDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case TRANSLATION:
                Cursor exists = db.query(
                        TranslationsPersistenceContract.TranslationEntry.TABLE_NAME,
                        new String[] {TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_ENTRY_ID},
                        TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_ENTRY_ID + " = ?",
                        new String[] {values.getAsString(TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_ENTRY_ID)},
                        null,
                        null,
                        null
                );
                if (exists.moveToLast()) {
                    long affectedRows = db.update(
                            TranslationsPersistenceContract.TranslationEntry.TABLE_NAME,
                            values,
                            TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_ENTRY_ID + " = ?",
                            new String[] {values.getAsString(TranslationsPersistenceContract.TranslationEntry.COLUMN_NAME_ENTRY_ID)}
                    );
                    Timber.d("inserting data with uri = '%s', updated '%d' rows", uri.toString(), affectedRows);
                    if (affectedRows > 0) {
                        returnUri = TranslationsPersistenceContract.TranslationEntry.buildTranslationsUriWith(affectedRows);
                    } else {
                        throw new SQLException("Failed to update row at " + uri);
                    }
                } else {
                    long _id = db.insert(TranslationsPersistenceContract.TranslationEntry.TABLE_NAME, null, values);
                    if (_id > 0) {
                        returnUri = TranslationsPersistenceContract.TranslationEntry.buildTranslationsUriWith(_id);
                    } else  {
                        throw new SQLException("Failed to insert row into " + uri);
                    }
                }
                exists.close();
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = translationsDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsDeleted;

        switch (match) {
            case TRANSLATION:
                rowsDeleted = db.delete(
                        TranslationsPersistenceContract.TranslationEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = translationsDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case TRANSLATION:
                rowsUpdated = db.update(TranslationsPersistenceContract.TranslationEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
