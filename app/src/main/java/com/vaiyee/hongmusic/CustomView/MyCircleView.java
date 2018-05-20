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
        int pointY = getHeight()/2;   //XY为画圆的坐标原点，getHeight()方法是获取XML文件中设置的宽高
        int r = 450;        //半径450 刚好合适

        LinearGradient mLinearGradient = new LinearGradient(0,0,getMeasuredWidth(),0,new int[]{Color.parseColor("#FFFFFF"),Color.parseColor("#FF3030"),Color.parseColor("#FFFFFF")},new float[]{0,0.5f,1.0f}, Shader.TileMode.CLAMP); //设置外圈颜色渐变

        Paint paint = new Paint();  //实例化画笔对象
        paint.setAntiAlias(true);
        paint.setAlpha((int) 0.5);
        paint.setShader(mLinearGradient);
        paint.setStrokeWidth(15); //圆边的宽度
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#00F5FF"));
        canvas.drawCircle(pointX,pointY,r,paint);
    }
}
