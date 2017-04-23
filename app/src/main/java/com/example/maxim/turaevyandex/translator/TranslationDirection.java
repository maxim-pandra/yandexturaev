package com.example.maxim.turaevyandex.translator;

import android.os.Bundle;

import com.example.maxim.turaevyandex.BuildConfig;
import com.example.maxim.turaevyandex.history.HistoryFilterType;


public class TranslationDirection {

    public final static String KEY_TRANSLATION_DIRECTION_FILTER = BuildConfig.APPLICATION_ID + "TRANSLATION_DIRECTION";
    private TranslationDirectionType translationDirectionType = TranslationDirectionType.EN_RU;
    private Bundle directionExtras;

    protected TranslationDirection(Bundle extras) {
        this.directionExtras = extras;
        this.translationDirectionType = (TranslationDirectionType) extras.getSerializable(KEY_TRANSLATION_DIRECTION_FILTER);
    }

    public static TranslationDirection from(TranslationDirectionType translationDirectionType){
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_TRANSLATION_DIRECTION_FILTER, translationDirectionType);
        return new TranslationDirection(bundle);
    }

    public TranslationDirectionType getTranslationDirectionType() {
        return translationDirectionType;
    }

    public Bundle getDirectionExtras() {
        return directionExtras;
    }

    public String getStringDirection() {
        switch (translationDirectionType) {

            case EN_RU:
                return "en-ru";
            case RU_EN:
                return "ru-en";
            default:
                return "en-ru";
        }
    }
}
