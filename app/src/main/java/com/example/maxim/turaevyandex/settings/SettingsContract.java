package com.example.maxim.turaevyandex.settings;

import com.example.maxim.turaevyandex.BasePresenter;
import com.example.maxim.turaevyandex.BaseView;

public class SettingsContract {

    interface  View extends BaseView<Presenter> {

       void showSettingsText();

    }

    interface Presenter extends BasePresenter {

        //nothing here
    }
}
