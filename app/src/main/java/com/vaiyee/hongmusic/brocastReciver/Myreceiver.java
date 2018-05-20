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
        Intent intent1 = new Intent(context,MainActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent1);
    }
}
