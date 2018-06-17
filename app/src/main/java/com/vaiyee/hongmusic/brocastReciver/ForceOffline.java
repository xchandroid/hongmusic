package com.vaiyee.hongmusic.brocastReciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Process;
import android.support.v7.app.AlertDialog;

import com.vaiyee.hongmusic.MyApplication;
import com.vaiyee.hongmusic.util.ActivityCollector;

/**
 * Created by Administrator on 2018/6/17.
 */

public class ForceOffline extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        AlertDialog.Builder a = new AlertDialog.Builder(MyApplication.getInstance().getCurrentActivity());
        a.setCancelable(false);
        a.setTitle("提示");
        a.setMessage("谢昌宏在远程关闭了本软件");
        a.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                android.os.Process.killProcess(Process.myPid());
            }
        });
        a.show();

    }
}
