package com.vaiyee.hongmusic.brocastReciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.vaiyee.hongmusic.PlayMusic;


/**
 * Created by Administrator on 2018/2/28.
 */

public class PlayModeReeceiver extends BroadcastReceiver {
    private PlayMusic.playMode playMode = null;
    @Override
    public void onReceive(Context context, Intent intent) {

        playMode = new PlayMusic.playMode();
        int mode = intent.getIntExtra("mode",6);
        switch (mode)
        {
            case 0:
                playMode.setMode(0); //顺序播放
                Toast.makeText(context,"顺序播放",Toast.LENGTH_LONG).show();
                break;
            case 1:
                playMode.setMode(1); //随机播放
                Toast.makeText(context,"随机播放",Toast.LENGTH_LONG).show();
                break;
            case 2:
                playMode.setMode(2); //单曲循环
                Toast.makeText(context,"单曲循环",Toast.LENGTH_LONG).show();
                break;
            case 3:
                playMode.setMode(3);
                Toast.makeText(context,"列表循环",Toast.LENGTH_LONG).show();
                break;
        }

    }
}

