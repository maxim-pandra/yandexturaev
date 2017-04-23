package com.example.maxim.turaevyandex.history;

import android.os.Bundle;

import com.example.maxim.turaevyandex.BuildConfig;


public class HistoryFilter {

    public final static String KEY_TRANSLATION_FILTER = BuildConfig.APPLICATION_ID + "TRANSLATION_FILTER";
    private HistoryFilterType historyFilterType = HistoryFilterType.ALL_TRANSLATIONS;
    private Bundle filterExtras;

    protected HistoryFilter(Bundle extras) {
        this.filterExtras = extras;
        this.historyFilterType = (HistoryFilterType) extras.getSerializable(KEY_TRANSLATION_FILTER);
    }

    public static HistoryFilter from(HistoryFilterType historyFilterType){
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_TRANSLATION_FILTER, historyFilterType);
        return new HistoryFilter(bundle);
    }


    public HistoryFilterType getHistoryFilterType() {
        return historyFilterType;
    }

    public Bundle getFilterExtras() {
        return filterExtras;
    }
}
