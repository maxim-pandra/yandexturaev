package com.example.maxim.turaevyandex.translator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.maxim.turaevyandex.Injection;
import com.example.maxim.turaevyandex.R;
import com.example.maxim.turaevyandex.common.util.ActivityUtils;
import com.example.maxim.turaevyandex.data.source.LoaderProvider;
import com.example.maxim.turaevyandex.history.HistoryActivity;
import com.example.maxim.turaevyandex.settings.SettingsActivity;

import timber.log.Timber;

public class TranslatorActivity extends AppCompatActivity implements SetTitleCallback {

    private static final String CURRENT_TRANSLATION_DIRECTION_KEY = "CURRENT_TRANSLATION_DIRECTION_KEY";

    private TranslatorPresenter translatorPresenter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_translate:
                    return true;
                case R.id.navigation_history:
                    intent = new Intent(TranslatorActivity.this, HistoryActivity.class);
                    TranslatorActivity.this.startActivity(intent);
                    return true;
                case R.id.navigation_settings:
                    intent = new Intent(TranslatorActivity.this, SettingsActivity.class);
                    TranslatorActivity.this.startActivity(intent);
                    return true;
            }
            return false;
        }

    };
    private ActionBar ab;
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translator_act);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ab = getSupportActionBar();


        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        this.overridePendingTransition(0, 0);

        TranslatorFragment translatorFragment =
                (TranslatorFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (translatorFragment == null) {
            translatorFragment = TranslatorFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), translatorFragment, R.id.contentFrame);
        }

        // Create the presenter
        LoaderProvider loaderProvider = new LoaderProvider(this);

        //Load previously saved state, if available.
        TranslationDirection translationDirection = TranslationDirection.from(TranslationDirectionType.EN_RU);
        if (savedInstanceState != null) {
            TranslationDirectionType currentDirection =
                    (TranslationDirectionType) savedInstanceState.getSerializable(CURRENT_TRANSLATION_DIRECTION_KEY);
            translationDirection = TranslationDirection.from(currentDirection);
        }

        translatorPresenter = new TranslatorPresenter(
                loaderProvider,
                getSupportLoaderManager(),
                Injection.provideTranslationsRepository(getApplicationContext()),
                translatorFragment,
                translationDirection,
                this
        );

        Timber.d("Translator activity created");
    }


    @Override
    protected void onStart() {
        super.onStart();
        navigation.setSelectedItemId(R.id.navigation_translate);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CURRENT_TRANSLATION_DIRECTION_KEY, translatorPresenter.getTranslationDirection());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setTitle(String title) {
        ab.setTitle(title);
    }
}
