package com.vaiyee.hongmusic.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/17.
 */

public class ActivityCollector {
    private static List<Activity> activityList= new ArrayList<>();
    public static void addActivity(Activity activity)
    {
        activityList.add(activity);
    }
    public static void removeActivity(Activity activity)
    {
        activityList.remove(activity);
    }
    public static void ClearActivity()
    {
        for (Activity activity:activityList)
        {
            activity.finish();
        }
    }
}
