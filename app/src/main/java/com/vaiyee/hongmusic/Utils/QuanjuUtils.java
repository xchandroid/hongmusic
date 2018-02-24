package com.vaiyee.hongmusic.Utils;

import android.text.format.DateUtils;

import java.util.Locale;

/**
 * Created by Administrator on 2018/2/11.
 */

public class QuanjuUtils {
    public static String formatTime(String pattern, int totaltime) {
        int m = (int) (totaltime / DateUtils.MINUTE_IN_MILLIS);
        int s = (int) ((totaltime / DateUtils.SECOND_IN_MILLIS) % 60);
        String mm = String.format(Locale.getDefault(), "%02d", m);
        String ss = String.format(Locale.getDefault(), "%02d", s);
        return pattern.replace("mm", mm).replace("ss", ss);
    }
}
