package com.vaiyee.hongmusic.brocastReciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.vaiyee.hongmusic.PlayMusic;

/**
 * Created by Administrator on 2018/2/26.
 */

public class AsynButton extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PlayMusic playMusic = new PlayMusic();
        int type = intent.getIntExtra("button",4);
        switch (type)
        {
            case 1:
                playMusic.pause();
                break;
            case 2:
                playMusic.playnext();
                break;
            case 3:
                playMusic.playPre();
                break;
        }

    }
}
