package com.ezyplanet.core.util.extension

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.ezyplanet.core.ui.listener.OnSnapPositionChangeListener
import com.ezyplanet.core.ui.widget.pager.SnapOnScrollListener

fun RecyclerView.attachSnapHelperWithListener(
        snapHelper: SnapHelper,
        behavior: SnapOnScrollListener.Behavior = SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,
        onSnapPositionChangeListener: OnSnapPositionChangeListener) {
    snapHelper.attachToRecyclerView(this)
    val snapOnScrollListener = SnapOnScrollListener(snapHelper,behavior, onSnapPositionChangeListener)
    addOnScrollListener(snapOnScrollListener)
}