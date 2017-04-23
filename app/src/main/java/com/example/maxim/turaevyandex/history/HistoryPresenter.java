package com.example.maxim.turaevyandex.history;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutCompat;

import com.example.maxim.turaevyandex.data.Translation;
import com.example.maxim.turaevyandex.data.source.LoaderProvider;
import com.example.maxim.turaevyandex.data.source.TranslationsDataSource;
import com.example.maxim.turaevyandex.data.source.TranslationsRepository;

import java.util.List;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link HistoryFragment}), retrieves the data and updates the
 * UI as required. It is implemented as a non UI {@link Fragment} to make use of the
 * {@link LoaderManager} mechanism for managing loading and updating data asynchronously.
 */

public class HistoryPresenter implements HistoryContract.Presenter,
        TranslationsRepository.LoadDataCallback, TranslationsDataSource.GetBookmarksCallback,
        LoaderManager.LoaderCallbacks<Cursor> {

    public final static int HISTORY_LOADER = 1;

    private final HistoryContract.View historyView;

    @NonNull
    private final TranslationsRepository translationsRepository;

    @NonNull
    private final LoaderManager loaderManager;

    @NonNull
    private final LoaderProvider loaderProvider;

    private HistoryFilter currentFiltering;

    public HistoryPresenter(@NonNull LoaderProvider loaderProvider, @NonNull LoaderManager loaderManager, @NonNull TranslationsRepository translationsRepository, @NonNull HistoryContract.View historyView, @NonNull HistoryFilter historyFilter) {
        this.loaderProvider = checkNotNull(loaderProvider, "loaderProvider provider cannot be null");
        this.loaderManager = checkNotNull(loaderManager, "loaderManager provider cannot be null");
        this.translationsRepository = checkNotNull(translationsRepository, "tasksRepository provider cannot be null");
        this.historyView = checkNotNull(historyView, "tasksView cannot be null!");
        this.currentFiltering = checkNotNull(historyFilter, "taskFilter cannot be null!");
        this.historyView.setPresenter(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return loaderProvider.createFilteredTranslationsLoader(currentFiltering);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            if (data.moveToLast()) {
                onDataLoaded(data);
            } else {
                onDataEmpty();
            }
        } else {
            onDataNotAvailable();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        onDataReset();
    }

    @Override
    public void start() {
//        loadTranslations();
        loadBookmarks();
    }


    @Override
    public void onBookmarksLoaded(List<Translation> bookmarkList) {
        // we don't care about the result since the CursorLoader will load the data for us
        if (loaderManager.getLoader(HISTORY_LOADER) == null) {
            loaderManager.initLoader(HISTORY_LOADER, currentFiltering.getFilterExtras(), this);
        } else {
            loaderManager.restartLoader(HISTORY_LOADER, currentFiltering.getFilterExtras(), this);
        }
    }

    @Override
    public void loadTranslations() {
        historyView.setLoadingIndicator(true);
        translationsRepository.getBookmarks(this);
        //ToDO temporary loads bookmarks, should load history though
    }

    @Override
    public void loadBookmarks() {
        historyView.setLoadingIndicator(true);
        translationsRepository.getBookmarks(this);
    }

    @Override
    public void addBookmark(@NonNull Translation bookmarkedTranslation) {
        translationsRepository.setBookmark(bookmarkedTranslation.getId());
        historyView.showBookmarkAddedMessage();
    }

    @Override
    public void setFiltering(HistoryFilter historyFilter) {
        currentFiltering = historyFilter;
        loaderManager.restartLoader(HISTORY_LOADER, currentFiltering.getFilterExtras(), this);
    }

    @Override
    public void clearHistory() {
        translationsRepository.clearDataBase();
    }

    @Override
    public HistoryFilterType getFiltering() {
        return currentFiltering.getHistoryFilterType();
    }

    @Override
    public void onDataLoaded(Cursor data) {
        historyView.setLoadingIndicator(false);
        // Show the list of translations or bookmarks
        historyView.showTranslations(data);
        // Set the filter label's text.
        showFilterLabel();
    }

    private void showFilterLabel() {
        switch (currentFiltering.getHistoryFilterType()) {
            case ALL_TRANSLATIONS:
                historyView.showAllHistoryLabel();
                break;
            case MARKED_TRANSLATIONS:
                historyView.showBookmarksOnlyLabel();
                break;
            default:
                throw new UnsupportedOperationException("illegal filter type");
        }
    }

    @Override
    public void onDataEmpty() {
        historyView.setLoadingIndicator(false);
        // Show a message indicating there are no translations for that filter type.
        processEmptyTranslations();
    }

    private void processEmptyTranslations() {
        switch (currentFiltering.getHistoryFilterType()) {
            case MARKED_TRANSLATIONS:
                historyView.showNoBookmarks();
                break;
            case ALL_TRANSLATIONS:
                historyView.showNoTranslations();
                break;
            default:
                throw new UnsupportedOperationException("Illegal filter type");
        }
    }

    @Override
    public void onDataNotAvailable() {
        historyView.setLoadingIndicator(false);
        historyView.showLoadingTranslationError();
    }

    @Override
    public void onDataReset() {
        historyView.showTranslations(null);
    }
}
