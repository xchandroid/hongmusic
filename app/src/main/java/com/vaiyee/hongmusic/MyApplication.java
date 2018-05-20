package com.vaiyee.hongmusic;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.yatoooon.screenadaptation.ScreenAdapterTools;

import org.litepal.LitePalApplication;

/**
 * Created by Administrator on 2018/2/10.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePalApplication.initialize(context);  //初始化数据库
        ScreenAdapterTools.init(context);  //初始化屏幕工具类
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ScreenAdapterTools.getInstance().reset(this); //旋转适配,如果应用屏幕固定了某个方向不旋转的话,可不写.
    }

    public static Context getQuanjuContext()
    {
        return context;  //获取全局context
    }
}
