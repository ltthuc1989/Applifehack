package  com.ezyplanet.core.ui.widget.pager

sealed class RVPageScrollState {
    class Idle: RVPageScrollState()
    class Dragging: RVPageScrollState()
    class Settling: RVPageScrollState()
}