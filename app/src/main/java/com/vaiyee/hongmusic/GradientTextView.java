package com.vaiyee.hongmusic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/2/11.
 */

public class GradientTextView extends android.support.v7.widget.AppCompatTextView {
    private LinearGradient mLinearGradient;
    private Matrix mGradientMatrix;
    private Paint mPaint;
    private int mViewWidth = 0;
    private int mTranslate = 0;

    private boolean mAnimating = true;

    public GradientTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mViewWidth == 0)
        {
            //getWidth得到是某个view的实际尺寸.
            //getMeasuredWidth是得到某view想要在parent view里面占的大小.
            mViewWidth = getMeasuredWidth();
            if (mViewWidth > 0)
            {
                mPaint = getPaint();
                //线性渐变
                mLinearGradient = new LinearGradient(-mViewWidth, 0, 0, 0, new int[]
                        {R.color.blue, R.color.black,R.color.red}, new float[]
                        { 0, 0.5f, 1 }, Shader.TileMode.CLAMP);
                mPaint.setShader(mLinearGradient);
                mGradientMatrix = new Matrix();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if (mAnimating && mGradientMatrix != null)
        {
            mTranslate += mViewWidth / 10;
            if (mTranslate > 2 * mViewWidth)
            {
                mTranslate = -mViewWidth;
            }
            mGradientMatrix.setTranslate(mTranslate, 0);
            mLinearGradient.setLocalMatrix(mGradientMatrix);
            //50ms刷新一次
            postInvalidateDelayed(50);
        }
    }
}
