package com.vaiyee.hongmusic.CustomView;

/**
 * Created by Administrator on 2019/4/24.
 */
import android.content.Context;

import android.graphics.Bitmap;

import android.graphics.Bitmap.Config;

import android.graphics.Canvas;

import android.graphics.Paint;

import android.graphics.Path;
import android.graphics.RectF;

import android.graphics.PorterDuff.Mode;

import android.graphics.PorterDuffXfermode;

import android.graphics.Rect;

import android.graphics.drawable.BitmapDrawable;

import android.graphics.drawable.Drawable;

import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import android.view.View;
import android.widget.ImageView;



    /**

     * 自定义的圆角矩形ImageView，可以直接当组件在布局中使用。

     * @author caizhiming

     *

     */

    public class XCRoundRectImageView extends AppCompatImageView{

        float width, height;

        public XCRoundRectImageView(Context context) {
            this(context, null);
            init(context, null);
        }

        public XCRoundRectImageView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
            init(context, attrs);
        }

        public XCRoundRectImageView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init(context, attrs);
        }

        private void init(Context context, AttributeSet attrs) {
            if (Build.VERSION.SDK_INT < 18) {
                setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            super.onLayout(changed, left, top, right, bottom);
            width = getWidth();
            height = getHeight();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if (width >= 12 && height > 12) {
                Path path = new Path();
                //四个圆角
                path.moveTo(50, 0);
                path.lineTo(width - 50, 0);
                path.quadTo(width, 0, width, 50);
                path.lineTo(width, height - 50);
                path.quadTo(width, height, width - 50, height);
                path.lineTo(50, height);
                path.quadTo(0, height, 0, height - 50);
                path.lineTo(0, 50);
                path.quadTo(0, 0, 50, 0);
                canvas.clipPath(path);
            }
            super.onDraw(canvas);
        }

    }
