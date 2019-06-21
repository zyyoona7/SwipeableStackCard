package com.zyyoona7.swipeablestack;

import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zyyoona7.swipeablestack.animator.DefaultSwipeItemAnimator;

public class StackItemTouchHelper<T, ADAPTER extends BaseQuickAdapter<T, ? extends BaseViewHolder>>
        implements OnItemSwipedDirectionChangedListener {

    private final StackItemTouchCallback<T, ADAPTER> mStackItemTouchCallback;
    private final ItemTouchHelper mItemTouchHelper;
    private DefaultSwipeItemAnimator mDefaultSwipeItemAnimator;

    public StackItemTouchHelper(ADAPTER adapter,
                                OnItemSwipedListener<T> listener) {
        this(adapter, listener, 5);
    }

    public StackItemTouchHelper(ADAPTER adapter,
                                OnItemSwipedListener<T> listener, int removedCacheSize) {
        mStackItemTouchCallback = new StackItemTouchCallback<>(adapter, listener, removedCacheSize);
        mItemTouchHelper = new ItemTouchHelper(mStackItemTouchCallback);
        mStackItemTouchCallback.setDirectionChangedListener(this);
    }

    public void bindToRecyclerView(@NonNull RecyclerView recyclerView) {
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        mDefaultSwipeItemAnimator = new DefaultSwipeItemAnimator();
        mDefaultSwipeItemAnimator.setAddDuration(250);
        mDefaultSwipeItemAnimator.setRemoveDuration(250);
        mDefaultSwipeItemAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        recyclerView.setItemAnimator(mDefaultSwipeItemAnimator);
    }

    /**
     * 恢复最后一个滑走的
     */
    public void recoverLast() {
        mStackItemTouchCallback.recoverLast();
    }

    /**
     * 恢复所有缓存中的数据
     */
    public void recover() {
        mStackItemTouchCallback.recover();
    }

    /**
     * 从恢复的缓存中根据条件删除指定条目
     *
     * @param predicate predicate
     */
    public void removeFromRecover(Predicate<T> predicate) {
        mStackItemTouchCallback.removeFromRecover(predicate);
    }

    public StackItemTouchCallback getStackItemTouchCallback() {
        return mStackItemTouchCallback;
    }

    public ItemTouchHelper getItemTouchHelper() {
        return mItemTouchHelper;
    }

    @Nullable
    public T getLatestSwipedItem(){
        return mStackItemTouchCallback.getLatestSwipedItem();
    }

    public void setSwipeThreshold(@FloatRange(from = 0f, to = 1f) float swipeThreshold) {
        mStackItemTouchCallback.setSwipeThreshold(swipeThreshold);
    }

    @Override
    public void onDirectionChanged(int direction) {
        if (mDefaultSwipeItemAnimator != null) {
            mDefaultSwipeItemAnimator.setSwipeDirection(direction);
        }
    }
}
