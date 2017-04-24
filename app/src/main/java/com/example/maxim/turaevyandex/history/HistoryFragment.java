package com.example.maxim.turaevyandex.history;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.ResourceCursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maxim.turaevyandex.R;
import com.example.maxim.turaevyandex.common.ScrollChildSwipeRefreshLayout;
import com.example.maxim.turaevyandex.data.Translation;

import timber.log.Timber;

/**
 * Display a grid of {@link com.example.maxim.turaevyandex.data.Translation}s. User can choose to view all,  or bookmarked translations.
 */

public class HistoryFragment extends Fragment implements HistoryContract.View {

    private HistoryContract.Presenter presenter;

    /**
     * Listener for clicks on translations in the ListView.
     */
    TranslationItemListener itemListener = new TranslationItemListener() {
        @Override
        public void onTranslationClick(Translation clickedTranslation) {
            //do nothing
        }

        @Override
        public void onMarkTranslationClick(Translation bookmarkedTranslation, boolean flag) {
            if (flag) {
                presenter.addBookmark(bookmarkedTranslation);
            } else {
                presenter.removeBookmark(bookmarkedTranslation);
            }
        }
    };

    private TranslationsCursorAdapter listAdapter;
    private View noTranslationsView;
    private ImageView noTranslationIcon;
    private TextView noTranslationMainView;
    private LinearLayout translationsView;
    private TextView filteringLabelView;
    private ScrollChildSwipeRefreshLayout swipeRefreshLayout;

    public HistoryFragment() {
        // Requires empty public constructor
    }

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void setPresenter(HistoryContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.history_frag, container, false);

        // Set up translations view
        listAdapter = new TranslationsCursorAdapter(getActivity(), itemListener);
        ListView listView = (ListView) root.findViewById(R.id.historyBookmarksList);
        listView.setAdapter(listAdapter);

        filteringLabelView = (TextView) root.findViewById(R.id.filteringLabel);
        translationsView = (LinearLayout) root.findViewById(R.id.historyLL);

        // Set up  no translations view
        noTranslationsView = root.findViewById(R.id.emptyView);
        noTranslationIcon = (ImageView) root.findViewById(R.id.noHistoryIcon);
        noTranslationMainView = (TextView) root.findViewById(R.id.noHistoryMain);

        // Set up progress indicator
        swipeRefreshLayout = (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(listView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadBookmarks();
            }
        });

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                presenter.clearHistory();
                showMessage(getString(R.string.history_cleared));
                break;
            case R.id.menu_filter:
                showFilteringPopUpMenu();
                break;
            case R.id.menu_refresh:
                presenter.loadBookmarks();
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.history_fragment_menu, menu);
    }

    @Override
    public void showBookmarkAddedMessage() {
        Snackbar mySnackbar = Snackbar.make(swipeRefreshLayout,
                R.string.bookmark_added, Snackbar.LENGTH_SHORT);
        mySnackbar.show();
    }

    @Override
    public void showBookmarkRemovedMessage() {
        showMessage(getString(R.string.bookmark_removed));
    }

    @Override
    public void showFilteringPopUpMenu() {
        PopupMenu popup = new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_filter));
        popup.getMenuInflater().inflate(R.menu.filter_tasks, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bookmarks:
                        presenter.setFiltering(HistoryFilter.from(HistoryFilterType.MARKED_TRANSLATIONS));
                        break;
                    case R.id.history:
                        presenter.setFiltering(HistoryFilter.from(HistoryFilterType.ALL_TRANSLATIONS));
                        break;
                    default:
                        throw new IllegalStateException("popup illegal state");
                }
                presenter.loadBookmarks();
                return true;
            }
        });

        popup.show();
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
    public void showTranslations(Cursor translations) {
        listAdapter.swapCursor(translations);

        translationsView.setVisibility(View.VISIBLE);
        noTranslationsView.setVisibility(View.GONE);
    }

    @Override
    public void showBookmarks(Cursor bookmarks) {
        //Todo support show bookmarks action maybe
        throw new UnsupportedOperationException("showBookmarks unsupported");
    }

    @Override
    public void showLoadingTranslationError() {
        showMessage(getString(R.string.loading_translation_error));
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showNoTranslations() {
        showNoTranslationsViews(
                getResources().getString(R.string.no_history),
                R.drawable.ic_history_black_24dp
        );
    }

    private void showNoTranslationsViews(String mainText, int iconRes) {
        translationsView.setVisibility(View.GONE);
        noTranslationsView.setVisibility(View.VISIBLE);

        noTranslationMainView.setText(mainText);
        noTranslationIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), iconRes, null));
    }

    @Override
    public void showNoBookmarks() {
        showNoTranslationsViews(
                getResources().getString(R.string.no_translations_bookmarked),
                R.drawable.ic_bookmark_border_black_24dp
        );
    }

    @Override
    public void showBookmarksOnlyLabel() {
        filteringLabelView.setText(R.string.bookmark_filter_label);
    }

    @Override
    public void showAllHistoryLabel() {
        filteringLabelView.setText(R.string.all_history_filter_label);
    }

    @Override
    public void showTranslationMarked() {
        showMessage(getContext().getResources().getString(R.string.bookmark_added));
    }

    public interface TranslationItemListener {

        void onTranslationClick(Translation clickedTranslation);

        void onMarkTranslationClick(Translation bookmarkedTranslation, boolean flag);
    }

    private static class TranslationsCursorAdapter extends CursorAdapter {

        private final TranslationItemListener itemListener;

        public TranslationsCursorAdapter(Context context, TranslationItemListener itemListener) {
            super(context, null, 0);
            this.itemListener = itemListener;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.history_item, parent, false);

            ViewHolder viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);

            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            final ViewHolder viewHolder = ((ViewHolder) view.getTag());

            final Translation translation = Translation.from(cursor);

            viewHolder.requestView.setText(translation.getRequest());
            viewHolder.translationView.setText(translation.getTranslationText());
            // marked/not_marked translation UI
            viewHolder.bookmarkedBox.setChecked(translation.isBookmarked());
            viewHolder.bookmarkedBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!translation.isBookmarked()) {
                        itemListener.onMarkTranslationClick(translation, true);
                    } else {
                        itemListener.onMarkTranslationClick(translation, false);
                    }
                }
            });

            viewHolder.rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.bookmarkedBox.performClick();
                }
            });
        }

        public static class ViewHolder {
            public final View rowView;
            public final TextView requestView;
            public final TextView translationView;
            public final CheckBox bookmarkedBox;

            public ViewHolder(View view) {
                rowView = view;
                requestView = (TextView) view.findViewById(R.id.request);
                translationView = ((TextView) view.findViewById(R.id.translation_text));
                bookmarkedBox = (CheckBox) view.findViewById(R.id.bookmark);
            }
        }
    }
}
