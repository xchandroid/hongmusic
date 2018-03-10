package com.vaiyee.hongmusic.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2018/3/9.
 */

public class MyCircleView extends View {
    public MyCircleView(Context context) {
        super(context);
    }

    public MyCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int pointX = getWidth()/2;
        int pointY = getHeight()/2;
        int r = 450;

        LinearGradient mLinearGradient = new LinearGradient(0,0,getMeasuredWidth(),0,new int[]{Color.parseColor("#00F5FF"),Color.parseColor("#FF3030"),Color.parseColor("#00F5FF")},new float[]{0,0.5f,1.0f}, Shader.TileMode.CLAMP); //设置外圈颜色渐变

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setAlpha((int) 0.5);
        paint.setShader(mLinearGradient);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#00F5FF"));
        canvas.drawCircle(pointX,pointY,r,paint);
    }
}
