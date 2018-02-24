package com.vaiyee.hongmusic;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2018/2/10.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
    public static Context getQuanjuContext()
    {
        return context;
    }
}
