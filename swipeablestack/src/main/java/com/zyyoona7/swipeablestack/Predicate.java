package com.zyyoona7.swipeablestack;

import androidx.annotation.NonNull;

public interface Predicate<T> {

    boolean test(@NonNull T t);
}