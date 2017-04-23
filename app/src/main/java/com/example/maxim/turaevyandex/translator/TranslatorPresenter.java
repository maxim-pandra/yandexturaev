package com.example.maxim.turaevyandex.translator;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.example.maxim.turaevyandex.data.Translation;
import com.example.maxim.turaevyandex.data.source.LoaderProvider;
import com.example.maxim.turaevyandex.data.source.TranslationsDataSource;
import com.example.maxim.turaevyandex.data.source.TranslationsRepository;

import timber.log.Timber;

/**
 * Created by maxim on 4/23/2017.
 */

class TranslatorPresenter implements TranslatorContract.Presenter,
        TranslationsRepository.LoadDataCallback, TranslationsDataSource.GetTranslationCallback,
        LoaderManager.LoaderCallbacks<Cursor> {

    public final static int HISTORY_LOADER = 1;
    private static final java.lang.String EXTRA_REQUEST = "extra_request";
    private static final String EXTRA_TRANSLATION_DIRECTION = "extra_translation_direction";

    private final TranslatorContract.View translatorView;

    @NonNull
    private final TranslationsRepository translationsRepository;

    @NonNull
    private final LoaderManager loaderManager;

    @NonNull
    private final LoaderProvider loaderProvider;

    @NonNull
    private final SetTitleCallback titleCallback;

    private TranslationDirection translationDirection;

    public TranslatorPresenter(@NonNull LoaderProvider loaderProvider,
                               @NonNull LoaderManager supportLoaderManager,
                               @NonNull TranslationsRepository translationsRepository,
                               TranslatorFragment translatorFragment,
                               TranslationDirection translationDirection,
                               @NonNull SetTitleCallback titleCallback) {
        this.translatorView = translatorFragment;
        this.translationsRepository = translationsRepository;
        this.loaderManager = supportLoaderManager;
        this.loaderProvider = loaderProvider;
        this.translationDirection = translationDirection;
        this.titleCallback = titleCallback;
        this.translatorView.setPresenter(this);
    }

    @Override
    public void start() {
        titleCallback.setTitle(translationDirection.getStringDirection());
        translatorView.showEmptyView();
        Timber.d("Translator presenter starts");
    }

    @Override
    public void loadTranslation(String request) {
        translatorView.setLoadingIndicator(true);
        translationsRepository.getTranslation(request, translationDirection.getStringDirection(), this);
//        loaderProvider.createTranslationLoader(request, translationDirection);
    }

    @Override
    public void updateTranslationBookmarkState(Translation translation) {
        translationsRepository.setBookmark(translation.getId());
    }

    @Override
    public void setTranslationDirection(TranslationDirection lang) {
        titleCallback.setTitle(lang.getStringDirection());
        translationDirection = lang;
    }

    @Override
    public void onTranslationLoaded(Translation translation) {
        translatorView.setLoadingIndicator(false);
        translatorView.showTranslation(translation);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String request = args.getString(EXTRA_REQUEST, "");
        TranslationDirection translationDirection = ((TranslationDirection) args.getSerializable(EXTRA_TRANSLATION_DIRECTION));
        return loaderProvider.createTranslationLoader(request, translationDirection);
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
    public void onDataLoaded(Cursor data) {
        translatorView.setLoadingIndicator(false);
        translatorView.showTranslation(Translation.from(data));
    }

    @Override
    public void onDataEmpty() {
        translatorView.setLoadingIndicator(false);
        translatorView.showEmptyView();
    }

    @Override
    public void onDataNotAvailable() {
        translatorView.setLoadingIndicator(false);
        translatorView.showLoadingTranslationError();
    }

    @Override
    public void onDataReset() {
        translatorView.showTranslation(null);
    }

    @Override
    public TranslationDirectionType getTranslationDirection() {
        return translationDirection.getTranslationDirectionType();
    }
}
