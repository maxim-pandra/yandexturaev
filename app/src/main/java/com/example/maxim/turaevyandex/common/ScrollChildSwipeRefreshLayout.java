package com.example.maxim.turaevyandex.common;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by maxim on 4/22/2017.
 */

public class ScrollChildSwipeRefreshLayout extends SwipeRefreshLayout {

    private View scrollUpChild;

    public ScrollChildSwipeRefreshLayout(Context context) {
        super(context);
    }

    public ScrollChildSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean canChildScrollUp() {
        if (scrollUpChild != null) {
            return ViewCompat.canScrollVertically(scrollUpChild, -1);
        }
        return super.canChildScrollUp();
    }

    public void setScrollUpChild(View view) {
        scrollUpChild = view;
    }

}
