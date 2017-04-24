package com.example.maxim.turaevyandex.settings;

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
import com.example.maxim.turaevyandex.history.HistoryActivity;
import com.example.maxim.turaevyandex.common.util.ActivityUtils;
import com.example.maxim.turaevyandex.translator.TranslatorActivity;

import timber.log.Timber;

public class SettingsActivity extends AppCompatActivity {
    private SettingsPresenter presenter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_translate:
                    intent = new Intent(SettingsActivity.this, TranslatorActivity.class);
                    SettingsActivity.this.startActivity(intent);
                    return true;
                case R.id.navigation_history:
                    intent = new Intent(SettingsActivity.this, HistoryActivity.class);
                    SettingsActivity.this.startActivity(intent);
                    return true;
                case R.id.navigation_settings:
                    return true;
            }
            return false;
        }

    };
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_act);
        Timber.d("Settings activity created");

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        this.overridePendingTransition(0, 0);

        SettingsFragment settingsFragment =
                (SettingsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (settingsFragment == null) {
            settingsFragment = SettingsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), settingsFragment, R.id.contentFrame);
        }

        presenter = new SettingsPresenter(settingsFragment, Injection.provideTranslationsRepository(getApplicationContext()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        navigation.setSelectedItemId(R.id.navigation_settings);
    }
}
