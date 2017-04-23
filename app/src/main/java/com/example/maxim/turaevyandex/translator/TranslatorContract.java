package com.example.maxim.turaevyandex.translator;

import com.example.maxim.turaevyandex.BasePresenter;
import com.example.maxim.turaevyandex.BaseView;
import com.example.maxim.turaevyandex.data.Translation;
import com.example.maxim.turaevyandex.data.source.TranslationsDataSource;

public class TranslatorContract {

    interface  View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showTranslation(Translation translation);

        void showLoadingTranslationError();

        void showEmptyView();

        void showTranslator();

        void setTitle(String title);

        void showLangDirPopUpMenu();
    }

    interface Presenter extends BasePresenter {

        void loadTranslation(String request);

        void updateTranslationBookmarkState(Translation translation);

        void setTranslationDirection(TranslationDirection lang);

        TranslationDirectionType getTranslationDirection();
    }
}
