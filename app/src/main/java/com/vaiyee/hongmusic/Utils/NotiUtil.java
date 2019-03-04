package com.vaiyee.hongmusic.Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;


import com.vaiyee.hongmusic.DownloadListener;
import com.vaiyee.hongmusic.MainActivity;
import com.vaiyee.hongmusic.MyApplication;
import com.vaiyee.hongmusic.R;

/**
 * Created by Administrator on 2018/4/29.
 */

public class NotiUtil {
    private static NotificationChannel channel;
    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel =new NotificationChannel("3","下载中", NotificationManager.IMPORTANCE_DEFAULT);
        }
    }
    public static DownloadListener listener = new DownloadListener() {
        @Override
        public void onProgress(String geming, int progress,int notiID) {
            getManager().notify(notiID,getNotification(geming,progress));
        }


        @Override
        public void onSuccess(String geming,int notiID) {
            Intent intent = new Intent(MyApplication.getQuanjuContext(), MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(MyApplication.getQuanjuContext(),0,intent,0);
            NotificationManager manager = (NotificationManager) MyApplication.getQuanjuContext().getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(notiID);
            Notification notification = new NotificationCompat.Builder(MyApplication.getQuanjuContext(),"3")
            .setContentTitle(geming)
            .setContentText("下载完成！")
            .setSmallIcon(R.drawable.xiazai1)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true).build();//点击后清除通知
            // 向系统注册通知渠道，注册后不能改变重要性以及其他通知行为
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                manager.createNotificationChannel(channel);
            }
            manager.notify(notiID,notification);
        }

        @Override
        public void onFail() {

        }
    };
    public static NotificationManager getManager()
    {
        NotificationManager manager = (NotificationManager) MyApplication.getQuanjuContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // 向系统注册通知渠道，注册后不能改变重要性以及其他通知行为
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(channel);
        }
        return manager;
    }
    public static Notification getNotification(String geming, int progress)
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyApplication.getQuanjuContext(),"3");
        builder.setContentText("《"+geming+"》"+"  正在下载："+progress+"%");
        builder.setContentTitle("歌曲");
        builder.setSmallIcon(R.drawable.xiazai1);
        builder.setProgress(100, progress, false);
        builder.setPriority(Notification.PRIORITY_LOW);
        return builder.build();
    }
    public static int getRandom()
    {
        int size = 200;
        int randomshu = (int)(Math.random()*size);
        return randomshu;
    }
}
