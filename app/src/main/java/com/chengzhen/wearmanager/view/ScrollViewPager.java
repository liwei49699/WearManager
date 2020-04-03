package com.chengzhen.wearmanager.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class ScrollViewPager extends ViewPager {

    private boolean mCanScroll = false;

    public ScrollViewPager(@NonNull Context context) {
        super(context);
    }

    public ScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!mCanScroll) {
            return false;
        } else {
            return super.onTouchEvent(ev);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!mCanScroll) {
            return false;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);
    }
}
