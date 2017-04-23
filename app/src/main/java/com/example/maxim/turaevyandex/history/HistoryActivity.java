package com.example.maxim.turaevyandex.history;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.maxim.turaevyandex.Injection;
import com.example.maxim.turaevyandex.R;
import com.example.maxim.turaevyandex.data.source.LoaderProvider;
import com.example.maxim.turaevyandex.settings.SettingsActivity;
import com.example.maxim.turaevyandex.util.ActivityUtils;

import timber.log.Timber;

/**
 * Created by maxim on 4/23/2017.
 */

public class HistoryActivity extends AppCompatActivity {

    private static final String CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY";

    private HistoryPresenter historyPresenter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_translate:
                    Toast.makeText(HistoryActivity.this, "go home", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.navigation_history:
                   Toast.makeText(HistoryActivity.this, "already here", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.navigation_settings:
                    Intent intent = new Intent(HistoryActivity.this, SettingsActivity.class);
                    HistoryActivity.this.startActivity(intent);
                    Toast.makeText(HistoryActivity.this, "go to settings", Toast.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_act);

        Timber.d("hello from timber");

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_history);
        this.overridePendingTransition(0, 0);

        HistoryFragment historyFragment =
                (HistoryFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (historyFragment == null) {
            historyFragment = HistoryFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), historyFragment, R.id.contentFrame);
        }

        // Create the presenter
        LoaderProvider loaderProvider = new LoaderProvider(this);

        //Load previously saved state, if available.
        HistoryFilter historyFilter = HistoryFilter.from(HistoryFilterType.ALL_TRANSLATIONS);
        if (savedInstanceState != null) {
            HistoryFilterType currentFiltering =
                    (HistoryFilterType) savedInstanceState.getSerializable(CURRENT_FILTERING_KEY);
            historyFilter = HistoryFilter.from(currentFiltering);
        }

        historyPresenter = new HistoryPresenter(
                loaderProvider,
                getSupportLoaderManager(),
                Injection.provideTranslationsRepository(getApplicationContext()),
                historyFragment,
                historyFilter
        );
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CURRENT_FILTERING_KEY, historyPresenter.getFiltering());
        super.onSaveInstanceState(outState);
    }
}
