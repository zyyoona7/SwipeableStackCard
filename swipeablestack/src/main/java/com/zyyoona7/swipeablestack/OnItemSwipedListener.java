package com.zyyoona7.swipeablestack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public interface OnItemSwipedListener<T> {

    /**
     * 滑动开始
     *
     * @param holder   viewHolder
     * @param position 滑动下标
     */
    void onSwipeStart(@Nullable RecyclerView.ViewHolder holder, int position);

    /**
     * 滑动释放
     *
     * @param holder   viewHolder
     * @param position 滑动下标
     */
    void onSwipeRelease(@Nullable RecyclerView.ViewHolder holder, int position);

    /**
     * 滑动完毕
     *
     * @param holder    viewHolder
     * @param direction 滑动方向
     * @param item      滑走的数据
     * @param position  滑走的下标
     */
    void onSwiped(@NonNull RecyclerView.ViewHolder holder, int direction,
                  @Nullable T item, int position);

    /**
     * 正在滑动中...
     *
     * @param holder         viewHolder
     * @param direction      滑动方向
     * @param thresholdRatio 滑动阈值百分比
     * @param threshold      滑动阈值
     * @param ratio          可滑动距离百分比
     * @param total          可滑动距离
     */
    void onSwiping(@NonNull RecyclerView.ViewHolder holder, int direction,
                   float thresholdRatio, float threshold, float ratio, int total);

    /**
     * 恢复条目 滑回
     */
    void onRecover();
}
