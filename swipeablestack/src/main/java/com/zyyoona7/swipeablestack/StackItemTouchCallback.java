package com.zyyoona7.swipeablestack;

import android.graphics.Canvas;
import android.util.Log;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * ItemTouchHelper.Callback 实现滑出和滑回操作
 * <p>
 * onSwiped方法直接从 Adapter 中移除数据从而达到滑出的效果，
 * 需要恢复时通过缓存的已删除列表中恢复达到滑回效果（有滑回动画需要配合{@link com.zyyoona7.swipeablestack.animator.DefaultSwipeItemAnimator 使用}）
 */
public class StackItemTouchCallback<T, ADAPTER extends BaseQuickAdapter<T, ? extends BaseViewHolder>> extends ItemTouchHelper.Callback {

    private static final String TAG = "StackItemTouchCallback";

    @FloatRange(from = 0f, to = 1f)
    private float mSwipeThreshold = 0.5f;
    private OnItemSwipedListener<T> mOnItemSwipedListener;
    private ADAPTER mAdapter;
    private final List<T> mRemovedList;
    private int mRemovedCacheSize;
    //当前动作的状态
    private int mCurrentActionState = ItemTouchHelper.ACTION_STATE_IDLE;
    //记录执行onSwiped时的滑动方向，方便做滑回动画
    private int mRemovedDirection;
    private OnItemSwipedDirectionChangedListener mDirectionChangedListener;

    public StackItemTouchCallback(ADAPTER adapter,
                                  OnItemSwipedListener<T> listener, int removedCacheSize) {
        this.mOnItemSwipedListener = listener;
        mAdapter = adapter;
        mRemovedCacheSize = removedCacheSize < 1 ? 1 : removedCacheSize;
        mRemovedList = new ArrayList<>(mRemovedCacheSize);
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.UP | ItemTouchHelper.DOWN);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        Log.d(TAG, "onSwiped: direction=" + direction);
        int position = viewHolder.getAdapterPosition();
        T item = null;
        if (mAdapter != null) {
            item = mAdapter.getItem(position - mAdapter.getHeaderLayoutCount());
            if (item != null) {
                if (mRemovedList.size() >= mRemovedCacheSize) {
                    mRemovedList.remove(0);
                }
                mRemovedList.add(item);
            }
            mAdapter.remove(position - mAdapter.getHeaderLayoutCount());
        }
        //滑动方向变化
        if (mDirectionChangedListener != null && mRemovedDirection != direction) {
            mDirectionChangedListener.onDirectionChanged(direction);
        }
        mRemovedDirection = direction;
        if (mOnItemSwipedListener != null) {
            mOnItemSwipedListener.onSwiped(viewHolder, direction, item, position);
        }
    }

    /**
     * 滑回最后删除的一个
     */
    public void recoverLast() {
        if (mRemovedList.isEmpty()) {
            return;
        }
        int lastIndex = mRemovedList.size() - 1;
        T item = mRemovedList.get(lastIndex);
        if (item == null) {
            return;
        }
        mAdapter.addData(0, item);
        mRemovedList.remove(lastIndex);
        if (mOnItemSwipedListener != null) {
            mOnItemSwipedListener.onRecover();
        }
    }

    /**
     * 滑回所有缓存的被移除的条目
     */
    public void recover() {
        if (mRemovedList.isEmpty()) {
            return;
        }
        mAdapter.addData(0, mRemovedList);
        mRemovedList.clear();
        if (mOnItemSwipedListener != null) {
            mOnItemSwipedListener.onRecover();
        }
    }

    /**
     * 从恢复列表中根据条件移除指定条目
     *
     * @param predicate predicate
     */
    public void removeFromRecover(Predicate<T> predicate) {
        if (predicate == null) {
            return;
        }
        int removeIndex = -1;
        for (int i = 0; i < mRemovedList.size(); i++) {
            T item = mRemovedList.get(i);
            if (predicate.test(item)) {
                removeIndex = i;
                break;
            }
        }
        if (removeIndex != -1) {
            mRemovedList.remove(removeIndex);
        }
    }

    /**
     * 获取最近被滑走的数据
     *
     * @return 最近被划走的数据
     */
    @Nullable
    public T getLatestSwipedItem() {
        if (mRemovedList.isEmpty()) {
            return null;
        }
        return mRemovedList.get(mRemovedList.size() - 1);
    }

    /**
     * 滑动的阈值，超过这个百分比(垂直是recyclerView高度百分比反之为宽度百分比)则划走，然后执行 onSwiped
     *
     * @param viewHolder view holder
     * @return threshold
     */
    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return mSwipeThreshold;
    }

    /*
      ---------- 这两个方法控制是否可以fling滑动 ----------
     */

    /**
     * 越大越难fling
     *
     * @param defaultValue 默认值
     * @return velocity
     */
    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        Log.d(TAG, "getSwipeEscapeVelocity: defaultValue=" + defaultValue);
        return 1200;
    }

    /**
     * 阻尼系数，越小越难fling
     *
     * @param defaultValue 默认值
     * @return velocity
     */
    @Override
    public float getSwipeVelocityThreshold(float defaultValue) {
        Log.d(TAG, "getSwipeVelocityThreshold: defaultValue=" + defaultValue);
        return defaultValue * 0.3f;
    }

    /*
      ---------- 这两个方法控制是否可以fling滑动 ----------
     */

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        Log.d(TAG, "onSelectedChanged: actionState=" + actionState);
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (mCurrentActionState != ItemTouchHelper.ACTION_STATE_SWIPE) {
                //swipe started
                if (mOnItemSwipedListener != null) {
                    mOnItemSwipedListener.onSwipeStart(viewHolder,
                            viewHolder == null ? -1 : viewHolder.getAdapterPosition());
                }
                mCurrentActionState = ItemTouchHelper.ACTION_STATE_SWIPE;
            }
        } else if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
            if (mCurrentActionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                //swipe release
                if (mOnItemSwipedListener != null) {
                    mOnItemSwipedListener.onSwipeRelease(viewHolder,
                            viewHolder == null ? -1 : viewHolder.getAdapterPosition());
                }
                mCurrentActionState = ItemTouchHelper.ACTION_STATE_IDLE;
            }
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        Log.d(TAG, "onChildDraw: dx=" + dX + ",dy=" + dY);
        int total = recyclerView.getHeight();
        float threshold = getSwipeThreshold(viewHolder) * total;
        float absDy = Math.abs(dY);
        float fraction = absDy / threshold;
        fraction = Math.min(1, fraction);
        if (mOnItemSwipedListener != null) {
            if (dY > 0) {
                mOnItemSwipedListener.onSwiping(viewHolder, ItemTouchHelper.DOWN,
                        fraction, threshold, absDy / total, total);
            } else if (dY < 0) {
                mOnItemSwipedListener.onSwiping(viewHolder, ItemTouchHelper.UP,
                        fraction, threshold, absDy / total, total);
            } else {
                mOnItemSwipedListener.onSwiping(viewHolder, 0,
                        fraction, threshold, absDy / total, total);
            }
        }
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (!(layoutManager instanceof StackLayoutManager)) {
            return;
        }
        Log.d(TAG, "onChildDraw: fraction=" + fraction);

    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setRotation(0);
        viewHolder.itemView.setScaleX(1);
        viewHolder.itemView.setScaleY(1);
    }

    void setDirectionChangedListener(OnItemSwipedDirectionChangedListener directionChangedListener) {
        mDirectionChangedListener = directionChangedListener;
    }

    public void setSwipeThreshold(@FloatRange(from = 0f, to = 1f) float swipeThreshold) {
        mSwipeThreshold = swipeThreshold;
    }
}
