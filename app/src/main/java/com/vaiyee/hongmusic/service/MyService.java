package com.vaiyee.hongmusic.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;
import com.vaiyee.hongmusic.R;


import java.util.TimerTask;

public class MyService extends Service {

    private static NotificationChannel channel;
    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel =new NotificationChannel("11","下载载", NotificationManager.IMPORTANCE_DEFAULT);
        }
    }

    @Override
        public void onCreate() {
        super.onCreate();
    }

    MusicBinder binder = new MusicBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;       //当服务与活动绑定时返回binder对象，在活动中通过binder对象调用服务中的方法，在这里是发起通知或关闭通知
    }
  //public  static RemoteViews noti;
    public  class MusicBinder extends Binder {
             RemoteViews noti;
       // NotificationCompat.Builder builder =null;
        Notification notification;

        public void sendNotification() {
            //NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);   //获取NotificationManager实例
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("PlayNoti","渠道1",NotificationManager.IMPORTANCE_LOW);//8.0以上系统需要创建通知渠道
                channel.enableLights(false);
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);//设置锁屏仍然可见
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(channel);//通过系统通知管理者创建通知信道
                notification = new Notification.Builder(MyService.this,"PlayNoti").build();
            }
            else
            {
                notification = new Notification();

            }
            notification.icon = R.drawable.huakuai;
            notification.when = System.currentTimeMillis();
            //notification.priority =Notification.PRIORITY_HIGH;
            notification.flags = Notification.FLAG_FOREGROUND_SERVICE;  //设置当前通知为前台通知
            //点击通知跳转到播放界面
            Intent in = new Intent("com.vaiyee.showplayfragment");
            in.setComponent(new ComponentName(getBaseContext().getPackageName(),"com.vaiyee.hongmusic.brocastReciver.Myreceiver"));
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, in, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.contentIntent = pendingIntent;
            //通过Remoview加载通知的布局
            noti = new RemoteViews(getPackageName(), R.layout.notification);
            notification.contentView = noti;
            //向系统注册通知渠道，注册后不能改变重要性以及其他通知行为
//            NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                notificationManager.createNotificationChannel(channel);
//            }



            //在创建的通知渠道上发送通知
//            Intent in = new Intent("com.vaiyee.showplayfragment");
//            in.setComponent(new ComponentName(getBaseContext().getPackageName(),"com.vaiyee.hongmusic.brocastReciver.Myreceiver"));
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, in, PendingIntent.FLAG_UPDATE_CURRENT);
//            noti = new RemoteViews(getPackageName(), R.layout.notification);  //初始化Remoteview
//            builder = new NotificationCompat.Builder(getBaseContext(),"11");
//            builder.setSmallIcon(R.drawable.huakuai) //设置通知图标              //这种通知是每次都弹出来提醒的通知
//                    .setCustomContentView(noti)
//                    .setSound(null)
//                    .setVibrate(null)
//                    .setContentIntent(pendingIntent);
//            //向系统注册通知渠道，注册后不能改变重要性以及其他通知行为
//            NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                notificationManager.createNotificationChannel(channel);
//            }
            tongbuButton();
            setclickNext();
            setclickPre();
            startForeground(1,notification);
        }

        public void CancelNotification() {
            stopForeground(true);
        }

        public void tongbuShow(Bitmap bitmap, String geming, String geshou) {
            noti.setImageViewBitmap(R.id.noti_cover, bitmap);
            noti.setTextViewText(R.id.noti_geming, geming);
            noti.setTextViewText(R.id.noti_geshou, geshou);
            startForeground(1, notification);  //刷新ID为 1 的通知，这一步很重要，否则不能同步通知栏的歌曲信息
        }

        public void tongbuButton() {
            Intent intent = new Intent("com.vaiyee.asynbutton");
            intent.setComponent(new ComponentName(getBaseContext().getPackageName(),"com.vaiyee.hongmusic.brocastReciver.AsynButton"));
            intent.putExtra("button",1);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            noti.setOnClickPendingIntent(R.id.noti_play, pendingIntent);
        }
        public void setclickNext()
        {
            Intent intent = new Intent("com.vaiyee.asynbutton");
            intent.setComponent(new ComponentName(getBaseContext().getPackageName(),"com.vaiyee.hongmusic.brocastReciver.AsynButton"));
            intent.putExtra("button",2);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            noti.setOnClickPendingIntent(R.id.noti_next,pendingIntent);
        }
        public void setclickPre()
        {
            Intent intent = new Intent("com.vaiyee.asynbutton");
            intent.setComponent(new ComponentName(getBaseContext().getPackageName(),"com.vaiyee.hongmusic.brocastReciver.AsynButton"));
            intent.putExtra("button",3);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 3, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            noti.setOnClickPendingIntent(R.id.noti_pre,pendingIntent);
        }

        public void setpause() {
            noti.setImageViewResource(R.id.noti_play, R.drawable.play_btn_pause_selector);
            startForeground(1, notification); //刷新ID为 1 的通知，这一步很重要，否则不能同步通知栏的歌曲信息
        }

        public void setplay() {
            noti.setImageViewResource(R.id.noti_play,R.drawable.play_btn_play_pause_selector);
            startForeground(1, notification); //刷新ID为 1 的通知，这一步很重要，否则不能同步通知栏的歌曲信息
        }

    }
}
