package com.example.maxim.turaevyandex.history;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.example.maxim.turaevyandex.BasePresenter;
import com.example.maxim.turaevyandex.BaseView;
import com.example.maxim.turaevyandex.data.Translation;

public class HistoryContract {

    interface  View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showTranslations(Cursor translations);

        void showBookmarks(Cursor bookmarks);

        void showLoadingTranslationError();

        void showNoTranslations();

        void showNoBookmarks();

        void showBookmarksOnlyLabel();

        void showAllHistoryLabel();

        void showTranslationMarked();

        void showFilteringPopUpMenu();

        void showBookmarkAddedMessage();

        void showBookmarkRemovedMessage();
    }

    interface Presenter extends BasePresenter {

        void loadTranslations();

        void loadBookmarks();

        void addBookmark(@NonNull Translation bookmarkedTranslation);

        void removeBookmark(@NonNull Translation translation);

        void setFiltering(HistoryFilter requestType);

        void clearHistory();

        HistoryFilterType getFiltering();
    }
}
