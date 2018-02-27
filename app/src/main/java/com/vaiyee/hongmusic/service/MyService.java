package com.vaiyee.hongmusic.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.vaiyee.hongmusic.MainActivity;
import com.vaiyee.hongmusic.MyApplication;
import com.vaiyee.hongmusic.R;

import java.util.TimerTask;

public class MyService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public MyService() {
    }

    MusicBinder binder = new MusicBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;       //当服务与活动绑定时返回binder对象，在活动中通过binder对象调用服务中的方法，在这里是发起通知或关闭通知
    }
    static RemoteViews noti;
    public  class MusicBinder extends Binder {
        //RemoteViews noti;
        Notification notification;

        public void sendNotification() {
            //NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);   //获取NotificationManager实例
            notification = new Notification();
            notification.icon = R.drawable.music_ic;
            notification.when = System.currentTimeMillis();
            notification.priority =Notification.PRIORITY_HIGH;
            notification.flags = Notification.FLAG_FOREGROUND_SERVICE;  //设置当前通知为前台通知
            //点击通知跳转到播放界面
            Intent in = new Intent("com.vaiyee.showplayfragment");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, in, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.contentIntent = pendingIntent;
            //通过Remoview加载通知的布局
            noti = new RemoteViews(getPackageName(), R.layout.notification);
            notification.contentView = noti;
            tongbuButton();
            setclickNext();
            setclickPre();
            startForeground(1, notification);
        }

        public void CancelNotification() {
            stopForeground(true);
        }

        public void tongbuShow(Bitmap bitmap, String geming, String geshou) {
            noti.setImageViewBitmap(R.id.noti_cover, bitmap);
            noti.setTextViewText(R.id.noti_geming, geming);
            noti.setTextViewText(R.id.noti_geshou, geshou);
            startForeground(1, notification);
        }

        public void tongbuButton() {
            Intent intent = new Intent("com.vaiyee.asynbutton");
            intent.putExtra("button",1);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            noti.setOnClickPendingIntent(R.id.noti_play, pendingIntent);
        }
        public void setclickNext()
        {
            Intent intent = new Intent("com.vaiyee.asynbutton");
            intent.putExtra("button",2);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            noti.setOnClickPendingIntent(R.id.noti_next,pendingIntent);
        }
        public void setclickPre()
        {
            Intent intent = new Intent("com.vaiyee.asynbutton");
            intent.putExtra("button",3);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 3, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            noti.setOnClickPendingIntent(R.id.noti_pre,pendingIntent);
        }

        public void setpause() {
            noti.setImageViewResource(R.id.noti_play, R.drawable.ic_play_btn_pause);
        }

        public void setplay() {
            noti.setImageViewResource(R.id.noti_play, R.drawable.ic_play_btn_play);

        }
    }
}
