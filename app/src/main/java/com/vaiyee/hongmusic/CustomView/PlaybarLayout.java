package com.vaiyee.hongmusic.CustomView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2018/5/28.
 */

public class PlaybarLayout extends LinearLayout {

    private PlaybarOnclickListener listener;
    public PlaybarLayout(Context context) {
        super(context);
    }

    public PlaybarLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PlaybarLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;   //拦截事件
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                listener.click();
                return true;
        }
        return true;  //消费事件 ，子view将不能接收到触摸事件
    }
    public interface PlaybarOnclickListener
    {
        void click();
    }
    public void setListener(PlaybarOnclickListener listener)
    {
        this.listener = listener;
    }
}
