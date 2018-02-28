package com.vaiyee.hongmusic.brocastReciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.vaiyee.hongmusic.MainActivity;
import com.vaiyee.hongmusic.MyApplication;

/**
 * Created by Administrator on 2018/2/26.
 */

public class Myreceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        MainActivity.firstopen = true;
        context.startActivity(new Intent(context,MainActivity.class));
    }
}
