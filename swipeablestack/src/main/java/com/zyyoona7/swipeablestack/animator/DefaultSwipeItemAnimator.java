package com.zyyoona7.swipeablestack.animator;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class DefaultSwipeItemAnimator extends BaseSwipeItemAnimator {

    private int mSwipeDirection = ItemTouchHelper.UP;

    /**
     * 不需要remove动画，如果有remove动画，界面会显示一下被移除的item
     *
     * @param holder holder
     */
    @Override
    protected void preAnimateRemoveImpl(RecyclerView.ViewHolder holder) {
        holder.itemView.setAlpha(0f);
    }

    @Override
    protected void animateRemoveImpl(RecyclerView.ViewHolder holder) {
        ViewCompat.animate(holder.itemView)
                .translationY(-holder.itemView.getHeight())
                .setDuration(getRemoveDuration())
                .setInterpolator(mInterpolator)
                .setListener(new DefaultRemoveVpaListener(holder))
                .setStartDelay(getRemoveDelay(holder))
                .start();
    }

    @Override
    protected void preAnimateAddImpl(RecyclerView.ViewHolder holder) {
        if (mSwipeDirection == ItemTouchHelper.UP || mSwipeDirection == ItemTouchHelper.DOWN) {
            int translationY = mSwipeDirection == ItemTouchHelper.UP
                    ? -holder.itemView.getHeight() : holder.itemView.getHeight();
            holder.itemView.setTranslationY(translationY);
            holder.itemView.setAlpha(0.5f);
        }
    }

    @Override
    protected void animateAddImpl(final RecyclerView.ViewHolder holder) {
        if (mSwipeDirection == ItemTouchHelper.UP || mSwipeDirection == ItemTouchHelper.DOWN) {
            ViewCompat.animate(holder.itemView)
                    .translationY(0)
                    .alpha(1)
                    .setDuration(getAddDuration())
                    .setInterpolator(mInterpolator)
                    .setListener(new DefaultAddVpaListener(holder))
                    .setStartDelay(getAddDelay(holder))
                    .start();
        }
    }

    public void setSwipeDirection(int direction) {
        mSwipeDirection = direction;
    }
}
