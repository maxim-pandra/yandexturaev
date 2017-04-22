package com.example.maxim.turaevyandex.translations;

import android.os.Bundle;

import com.example.maxim.turaevyandex.BuildConfig;


public class TranslationFilter {

    public final static String KEY_TRANSLATION_FILTER = BuildConfig.APPLICATION_ID + "TRANSLATION_FILTER";
    private TranslationsFilterType translationsFilterType = TranslationsFilterType.ALL_TRANSLATIONS;
    private Bundle filterExtras;

    protected TranslationFilter(Bundle extras) {
        this.filterExtras = extras;
        this.translationsFilterType = (TranslationsFilterType) extras.getSerializable(KEY_TRANSLATION_FILTER);
    }

    public static TranslationFilter from(TranslationsFilterType translationsFilterType){
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_TRANSLATION_FILTER, translationsFilterType);
        return new TranslationFilter(bundle);
    }


    public TranslationsFilterType getTranslationsFilterType() {
        return translationsFilterType;
    }

    public Bundle getFilterExtras() {
        return filterExtras;
    }
}
