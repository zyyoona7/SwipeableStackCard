package com.zyyoona7.swipeablestack;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class StackLayoutManager extends RecyclerView.LayoutManager {

    private int mMaxVisibleCount = 5;
    private float mScaleFraction = 0.9f;
    private float mTranslationYOffset = 0f;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0) {
            removeAndRecycleAllViews(recycler);
            return;
        }
        detachAndScrapAttachedViews(recycler);

        int startPosition = Math.min(mMaxVisibleCount, getItemCount()) - 1;
        startPosition = startPosition > 0 ? startPosition : 0;

        for (int position = startPosition; position >= 0; position--) {
            View view = recycler.getViewForPosition(position);
            addView(view);
            measureChildWithMargins(view, 0, 0);
            int widthSpace = getWidth() - getDecoratedMeasuredWidth(view);
            int heightSpace = getHeight() - getDecoratedMeasuredHeight(view);
            layoutDecorated(view, widthSpace / 2, heightSpace / 2,
                    widthSpace / 2 + getDecoratedMeasuredWidth(view),
                    heightSpace / 2 + getDecoratedMeasuredHeight(view));
//            if (position > 0) {
//                view.setScaleX(validateScale(mScaleFraction * position));
//                if (position < mMaxVisibleCount - 1) {
//                    view.setTranslationY(validateScale(mTranslationYOffset * position));
//                    view.setScaleY(validateScale( mScaleFraction * position));
//                } else {
//                    view.setTranslationY(validateTranslation(mTranslationYOffset * (position - 1)));
//                    view.setScaleY(validateScale( mScaleFraction * (position - 1)));
//                }
//            }
        }
    }

    private float validateTranslation(float value) {
        return Math.max(0, value);
    }

    private float validateScale(float value) {
        return Math.max(0, Math.min(1, value));
    }

    int getMaxVisibleCount() {
        return mMaxVisibleCount;
    }

    float getScaleFraction() {
        return mScaleFraction;
    }

    float getTranslationYOffset() {
        return mTranslationYOffset;
    }
}
