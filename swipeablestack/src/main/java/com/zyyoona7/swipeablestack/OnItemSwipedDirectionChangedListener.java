package com.zyyoona7.swipeablestack;

/**
 * 执行 onSwiped 时，方向发生变化回调
 */
interface OnItemSwipedDirectionChangedListener {

    /**
     * 滑动方向变化
     *
     * @param direction direction
     */
    void onDirectionChanged(int direction);
}
