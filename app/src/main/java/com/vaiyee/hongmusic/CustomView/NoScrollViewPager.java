package com.vaiyee.hongmusic.CustomView;

/**
 * Created by Administrator on 2018/2/23.
 */

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 不可以滑动，但是可以setCurrentItem的ViewPager。
 */
public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        return false;//不消费点击事件
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return false;//不拦截点击事件
    }
}
