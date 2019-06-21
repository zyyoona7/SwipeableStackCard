package com.zyyoona7.swipeablestack.animator;

import android.view.View;

import androidx.core.view.ViewCompat;

final class ViewHelper {

    static void clear(View v) {
        if (v == null) {
            return;
        }
        v.setAlpha(1);
        v.setScaleY(1);
        v.setScaleX(1);
        v.setTranslationY(0);
        v.setTranslationX(0);
        v.setRotation(0);
        v.setRotationY(0);
        v.setRotationX(0);
        v.setPivotY(v.getMeasuredHeight() / 2f);
        v.setPivotX(v.getMeasuredWidth() / 2f);
        ViewCompat.animate(v).setInterpolator(null).setStartDelay(0);
    }
}