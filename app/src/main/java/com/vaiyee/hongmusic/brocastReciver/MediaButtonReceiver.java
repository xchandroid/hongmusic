package com.vaiyee.hongmusic.brocastReciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.widget.Toast;

import com.vaiyee.hongmusic.PlayMusic;

/**
 * Created by Administrator on 2018/3/3.
 */

public class MediaButtonReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PlayMusic playMusic = new PlayMusic();
        //获取动作
        String action = intent.getAction();
        //获取 耳机按钮事件
        KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        if(Intent.ACTION_MEDIA_BUTTON.equals(action))
        {
            // 获得按键码
            int keycode = event.getKeyCode();
            switch (keycode)
            {
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    //耳机键触发了下一首事件
                    playMusic.playnext();
                    break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                    //耳机键触发了上一首事件
                    playMusic.playPre();
                    break;
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
                    //耳机键触发了暂停事件
                    playMusic.pause();
                    break;
                    default:
                        break;
            }
        }
    }
}
