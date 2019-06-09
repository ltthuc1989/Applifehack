package com.ezyplanet.core.ui.widget.scroll;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;

public class GridLayoutManagerWrapper extends GridLayoutManager {
    public GridLayoutManagerWrapper(Context context, int spanCount) {
        super(context, spanCount);
    }

    public GridLayoutManagerWrapper(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }



    public GridLayoutManagerWrapper(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }
}
