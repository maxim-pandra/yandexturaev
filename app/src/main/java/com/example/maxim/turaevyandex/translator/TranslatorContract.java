package com.example.maxim.turaevyandex.translator;

import com.example.maxim.turaevyandex.BasePresenter;
import com.example.maxim.turaevyandex.BaseView;
import com.example.maxim.turaevyandex.data.Translation;
import com.example.maxim.turaevyandex.history.HistoryFilter;
import com.example.maxim.turaevyandex.history.HistoryFilterType;

public class TranslatorContract {

    interface  View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showTranslation(Translation translation);
    }

    interface Presenter extends BasePresenter {

        void loadTranslation(String request, String lang);

        void setFiltering(HistoryFilter requestType);

        HistoryFilterType getFiltering();
    }
}
