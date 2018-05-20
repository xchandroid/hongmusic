package com.vaiyee.hongmusic.CustomView;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/6.
 */

public class MyLrcView extends View {

    private Paint txtpaint;
    private Lrc mlrc;
    private float offset;  //Y轴的偏移量（实现歌词滚动）
    private float downX,downY;  //按下时的坐标点
    private float lastoffset;//按下屏幕时记录上次滚动的偏移量,下次滚动歌词的偏移量基于(加上)这个基础
    private float totalHeight;//歌词的总高度
    public MyLrcView(Context context) {
        super(context);
        init();
    }

    public MyLrcView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyLrcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        txtpaint = new Paint();
        txtpaint.setColor(Color.parseColor("#4FC5C7"));
        txtpaint.setTextSize(getRawSize(TypedValue.COMPLEX_UNIT_SP,20));//其实值就是60
        txtpaint.setAntiAlias(true);
        txtpaint.setDither(true);//不设置这个的话，每行的最后一个一个字会有切割现象
        txtpaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
            if (mlrc!=null&&mlrc.lineList.size()>0) {
                for (int i = 0; i < mlrc.lineList.size(); i++) {
                    float y = i * 90+getMeasuredHeight()/2+offset; // 绘画每行歌词的基线位置,第一行从Y轴中间开始,90为行高（包括行间距）
                    canvas.drawText(mlrc.lineList.get(i).content, getMeasuredWidth() / 2, y, txtpaint); //画出每一行歌词
                }
                Log.d("歌词总高度",String.valueOf(totalHeight = mlrc.lineList.size()*90));
            }
            else
            {
                canvas.drawText("暂无歌词",getMeasuredWidth()/2,getMeasuredHeight()/2+getMeasuredHeight()/2,txtpaint);
            }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                lastoffset = offset;
                break;
            case MotionEvent.ACTION_MOVE:
                float nowX = event.getX();
                float nowY = event.getY();
                if (Math.abs(offset =lastoffset+( nowY-downY))<=totalHeight) {
                    if (offset>500)  //防止下划歌词滚出屏幕
                    {
                        offset = 500;
                    }
                    else {
                        offset = lastoffset + (nowY - downY); //加上上一次的偏移量，不然点击view时会回到原点（即offset为0.1252）,上划为负，下划为正
                    }
                }
                else
                {
                    offset =-totalHeight;  //防止歌词上划滚出屏幕
                }
                Log.d("偏移量",String.valueOf(offset));  //其实手指点击一下view就已经发生偏移了
                RefreshView();
                break;

        }
        return true;
    }

    class Lrc
    {
        List<Line> lineList;

    }
    class Line
    {
        String content;
        long time;  //每一行的开始时间
    }

    public void LoadLrcFile(File file)
    {
        try {
            FileInputStream inputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            Lrc lrc = new Lrc();  //歌词实例对象
            lrc.lineList = new ArrayList<>();
            String s ="";
            while ((s=bufferedReader.readLine())!=null)
            {
                if (s.trim().length()>10)
                {
                    Line line = new Line(); //每行歌词的实例,这里记得要new实例化每行歌词对象，不然只有一个line对象，导致添加进List都是同一个对象，也就是所有歌词行都是最后一行
                    line.content = s.substring(10,s.length());
                    line.time = CalculateTime(s.substring(1,10));
                    lrc.lineList.add(line);
                }
            }
            mlrc = lrc;  //将歌词对象赋值为全局成员，因为要把一行行歌词绘画出来
            inputStream.close();
            inputStreamReader.close();
            bufferedReader.close();
            RefreshView();  //调用onDraw()重绘view
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void RefreshView()
    {
        if (Looper.getMainLooper()==Looper.myLooper())   //如果当前是UI线程直接刷新,否则post刷新
        {
            invalidate();
        }
        else
        {
            postInvalidate();
        }
    }
    private long CalculateTime(String s)
    {
         long minute = Long.parseLong(s.substring(0,2));
         long second = Long.parseLong(s.substring(3,5));
         long millisecond = Long.parseLong(s.substring(6,8));
         return minute*60*1000+second*1000+millisecond;
    }

    //适配字体尺寸
    private float getRawSize(int unit, float size) {
        Context context = getContext();
        Resources resources;
        if (context == null) {
            resources = Resources.getSystem();
        } else {
            resources = context.getResources();
        }
        return TypedValue.applyDimension(unit, size, resources.getDisplayMetrics()); //这个方法的作用是 把Android系统中的非标准度量尺寸转变为标准度量尺寸 (Android系统中的标准尺寸是px, 即像素)
    }
}
