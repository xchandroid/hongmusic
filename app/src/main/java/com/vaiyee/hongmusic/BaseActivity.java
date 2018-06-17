package com.vaiyee.hongmusic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vaiyee.hongmusic.util.ActivityCollector;

public class BaseActivity extends AppCompatActivity {
 private ForceOffline forceOffline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.vaiyee.hongmusic.offline");
        forceOffline = new ForceOffline();
        registerReceiver(forceOffline,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (forceOffline!=null) {
            unregisterReceiver(forceOffline);
        }
        forceOffline = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    class ForceOffline extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {
            AlertDialog.Builder a = new AlertDialog.Builder(context);
            a.setCancelable(false);
            a.setTitle("提示");
            a.setMessage("谢昌宏在远程关闭了本软件");
            a.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCollector.ClearActivity();
                }
            });
            a.show();
        }
    }
}
