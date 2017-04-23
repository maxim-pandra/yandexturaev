package com.example.maxim.turaevyandex.settings;

import android.support.annotation.NonNull;

import com.example.maxim.turaevyandex.data.source.TranslationsRepository;


/**
 * Listens to user actions from the UI ({@link SettingsFragment}), retrieves the data and updates the
 * UI as required.
 **/

public class SettingsPresenter implements SettingsContract.Presenter {

    private final SettingsContract.View settingsView;

    @NonNull
    private final TranslationsRepository translationsRepository;

    public SettingsPresenter(SettingsContract.View settingsView, @NonNull TranslationsRepository translationsRepository) {
        this.settingsView = settingsView;
        this.translationsRepository = translationsRepository;
        settingsView.setPresenter(this);
    }

    @Override
    public void start() {
        settingsView.showSettingsText("Settings 123");
    }
}
