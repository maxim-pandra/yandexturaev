package com.example.maxim.turaevyandex.translator;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.maxim.turaevyandex.R;
import com.example.maxim.turaevyandex.data.Translation;
import com.example.maxim.turaevyandex.common.ScrollChildSwipeRefreshLayout;

import timber.log.Timber;

/**
 * Display a grid of {@link Translation}s. User can choose to view all,  or bookmarked translations.
 */

public class TranslatorFragment extends Fragment implements TranslatorContract.View {

    private TranslatorContract.Presenter presenter;

    private EditText translatorText;
    private View emptyView;
    private TextView emptyText;
    private LinearLayout translatorView;
    private CheckBox bookmarkBox;
    private TextView translationTextView;

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //Do nothing
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //Do nothing
        }

        @Override
        public void afterTextChanged(Editable s) {
            Timber.d("loadTranslation called with parameter s = '%s'", s.toString());
            if (s.toString().length() > 2) {
                presenter.loadTranslation(s.toString());
            } else {
                showEmptyView();
            }
        }
    };
    private Translation currentTranslation;
    private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                presenter.updateTranslationBookmarkState(currentTranslation);
                showBookmarkAddedMessage();
            } else {
                Timber.w("remove bookmark from history is not supported yet");
            }
        }
    };
    private ScrollChildSwipeRefreshLayout swipeRefreshLayout;

    public TranslatorFragment() {
        // Requires empty public constructor
    }

    public static TranslatorFragment newInstance() {
        return new TranslatorFragment();
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
        if (translatorText.getText().toString().length() > 2) {
            presenter.loadTranslation(translatorText.getText().toString());
        }
    }

    @Override
    public void setPresenter(TranslatorContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.translator_frag, container, false);


        translatorView = (LinearLayout) root.findViewById(R.id.translatorLL);

        // Set up  no translations view
        emptyView = root.findViewById(R.id.emptyView);
        emptyText = (TextView) root.findViewById(R.id.emptyViewTitle);
        translationTextView = (TextView) root.findViewById(R.id.translationTextView);
        bookmarkBox = (CheckBox) root.findViewById(R.id.translatorBookmark);
        bookmarkBox.setOnCheckedChangeListener(checkedChangeListener);
        translatorText = (EditText) root.findViewById(R.id.translatorEditText);
        translatorText.addTextChangedListener(watcher);
        // Set up progress indicator
        swipeRefreshLayout = (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));

        //todo do something with scroll here
        // Set the scrolling view in the custom SwipeRefreshLayout.
//        swipeRefreshLayout.setScrollUpChild(listView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadTranslation(translatorText.getText().toString());
            }
        });

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                presenter.loadTranslation(translatorText.getText().toString());
                break;
            case R.id.menu_lang:
                showLangDirPopUpMenu();
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.translator_fragment_menu, menu);
    }

    @Override
    public void showLangDirPopUpMenu() {
        PopupMenu popup = new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_lang));
        popup.getMenuInflater().inflate(R.menu.translation_direction, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.en_ru:
                        presenter.setTranslationDirection(TranslationDirection.from(TranslationDirectionType.EN_RU));
                        break;
                    case R.id.ru_en:
                        presenter.setTranslationDirection(TranslationDirection.from(TranslationDirectionType.RU_EN));
                        break;
                    default:
                        throw new IllegalStateException("popup illegal state");
                }
                String request = TranslatorFragment.this.translatorText.getText().toString();
                if (request.length() > 2) {
                    presenter.loadTranslation(request);
                } else {
                    showEmptyView();
                }
                return true;
            }
        });

        popup.show();
    }

    @Override
    public void showBookmarkAddedMessage() {
        showMessage(getResources().getString(R.string.bookmark_added));
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void showTranslator() {
        //ToDO SwapCursors?         listAdapter.swapCursor(translations);

        translatorView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }

    @Override
    public void setTitle(String title) {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    @Override
    public void showEmptyView() {
        translatorView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);

        emptyText.setText(R.string.translator_empty_text);
    }

    @Override
    public void showLoadingTranslationError() {
        showMessage(getString(R.string.loading_translation_error));
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showTranslation(Translation translation) {
        currentTranslation = translation;
        translatorView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        translationTextView.setText(translation.getTranslationText());
        bookmarkBox.setChecked(translation.isBookmarked());
    }

}
