package com.vaiyee.hongmusic;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.yatoooon.screenadaptation.ScreenAdapterTools;

import org.litepal.LitePalApplication;

/**
 * Created by Administrator on 2018/2/10.
 */

public class MyApplication extends Application {
    private static Context context;
    private static MyApplication mInstance;
    private static Activity app_activity = null;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        mInstance = this;
        LitePalApplication.initialize(context);  //初始化数据库
        ScreenAdapterTools.init(context);  //初始化屏幕工具类
        initGlobeActivity();
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

    private void initGlobeActivity() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                app_activity = activity;
                Log.e("onActivityCreated===", app_activity + "");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                //app_activity = activity;
                Log.e("onActivityDestroyed===", app_activity + "");
            }

            /** Unused implementation **/
            @Override
            public void onActivityStarted(Activity activity) {
                //app_activity = activity;
                Log.e("onActivityStarted===", app_activity + "");
            }

            @Override
            public void onActivityResumed(Activity activity) {
               //app_activity = activity;
                Log.e("onActivityResumed===", app_activity + "");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                //app_activity = activity;
                Log.e("onActivityPaused===", app_activity + "");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                //app_activity = activity;
                Log.e("onActivityStopped===", app_activity + "");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }
        });
    }

    /**
     * 获取实例
     * @return
     */
    public static MyApplication getInstance() {
        return mInstance;
    }

    /**
     * 公开方法，外部可通过 MyApplication.getInstance().getCurrentActivity() 获取到当前最上层的activity
     */
    public Activity getCurrentActivity() {
        return app_activity;
    }

}
