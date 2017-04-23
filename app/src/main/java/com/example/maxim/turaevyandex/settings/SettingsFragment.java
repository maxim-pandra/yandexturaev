package com.example.maxim.turaevyandex.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.maxim.turaevyandex.R;

/**
 * Created by maxim on 4/23/2017.
 */

public class SettingsFragment extends Fragment implements SettingsContract.View {

    private SettingsContract.Presenter presenter;
    private TextView settingsTextView;


    public SettingsFragment() {
        // Requires empty public constructor
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void setPresenter(SettingsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.settings_frag, container, false);

        settingsTextView = ((TextView) root.findViewById(R.id.settings_text));
        return root;
    }

    @Override
    public void showSettingsText(String text) {
        settingsTextView.setText(text);
    }
}
