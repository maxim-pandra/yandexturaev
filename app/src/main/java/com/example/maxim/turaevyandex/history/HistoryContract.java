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
    }

    interface Presenter extends BasePresenter {

        void loadTranslations();

        void loadBookmarks();

        void addBookmark(@NonNull Translation bookmarkedTranslation);

        void setFiltering(HistoryFilter requestType);

        HistoryFilterType getFiltering();
    }
}
