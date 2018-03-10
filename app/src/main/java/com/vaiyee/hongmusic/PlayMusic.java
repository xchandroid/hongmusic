package com.vaiyee.hongmusic;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.widget.Toast;

import com.vaiyee.hongmusic.Utils.AudioFocusManager;
import com.vaiyee.hongmusic.Utils.HttpClinet;
import com.vaiyee.hongmusic.Utils.MediaSessionManager;
import com.vaiyee.hongmusic.Utils.NetUtils;
import com.vaiyee.hongmusic.Utils.getAudio;
import com.vaiyee.hongmusic.bean.KugouMusic;
import com.vaiyee.hongmusic.bean.KugouSearchResult;
import com.vaiyee.hongmusic.bean.Song;
import com.vaiyee.hongmusic.brocastReciver.MediaButtonReceiver;
import com.vaiyee.hongmusic.fragement.PlayMusicFragment;
import com.vaiyee.hongmusic.fragement.fragement1;
import com.vaiyee.hongmusic.http.HttpCallback;
import com.vaiyee.hongmusic.service.MyService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/2/7.
 */

public class PlayMusic {
    public   static MediaPlayer mediaPlayer;
    public static Boolean isPause=false,isRest = false;
    private static int playposition;
    private  List<Song>songList = new ArrayList<>();
    private  PlayMusicFragment p = new PlayMusicFragment();
    public static String geming;
    private MediaSessionManager mediaSessionManager;
    private  MainActivity mainActivity;
    public AudioFocusManager audioFocusManager;
    public void getInstanse()
    {
        mediaPlayer = new MediaPlayer();
    }
    public  void play(String path, final int position) {
        if (PlayMusicFragment.timerTask != null) {
            PlayMusicFragment.timerTask.cancel();  //取消定时任务，不然每次点击列表播放音乐会跳播
            PlayMusicFragment.timer.cancel();
        }
       // mediaSessionManager = new MediaSessionManager(this);
        audioFocusManager = new AudioFocusManager(this);
        if (audioFocusManager.requestAudioFocus())
        {

        if (mainActivity == null) {
            mainActivity = new MainActivity();
        }
        mainActivity.sendNotification();
        // MyService.noti.setImageViewResource(R.id.noti_play, R.drawable.ic_play_btn_pause);
        MainActivity.setplayButtonpause();
        p.zhuanquanuqna();
        PlayMusicFragment.play.setImageResource(R.drawable.play_btn_pause_selector);
        MainActivity.play.setImageResource(R.drawable.ic_play_bar_btn_pause);
        MainActivity.firstplay = false;
        PlayMusicFragment.firstplay = false;
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare();
                mediaPlayer.start();
                //isRest = false;
                playposition = position;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (mediaPlayer.isPlaying() || isPause) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                //isRest = true;

            }
            try {
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare();
                mediaPlayer.start();
               // isRest = false;
                playposition = position;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                PlayMusicFragment.timer.cancel();
                PlayMusicFragment.timerTask.cancel();
                p.resetLrcview();
                PlayMusicFragment.singlelrc.setLabel("暂无歌词");
                mediaPlayer.reset();
                playMode playMode = new playMode();
                switch (playMode.getMode()) {
                    case 0:
                        playnext();
                        break;
                    case 1:
                        SuijiPlay();
                        break;
                    case 2:
                        danqu();
                        break;
                    case 3:
                        xunhuanplay();
                }


            }
        });

    }
    }

    public void pause()
    {
        if (mediaPlayer==null)
        {
            return;
        }
        if (mediaPlayer.isPlaying()&&!isPause)
        {
            mediaPlayer.pause();
            p.stop();
            PlayMusicFragment. play.setImageResource(R.drawable.play_btn_play_pause_selector);
            MainActivity.play.setImageResource(R.drawable.play_bar_btn_play_pause_selector);
            if (mainActivity ==null) {
                mainActivity = new MainActivity();
            }
            mainActivity.sendNotification();
            //MyService.noti.setImageViewResource(R.id.noti_play, R.drawable.ic_play_btn_play);
            MainActivity.setplayButtonplay();
            isPause = true;
            return;
        }
        if (isPause)
        {
            mediaPlayer.start();
            p.zhuanquanuqna();
            PlayMusicFragment.play.setImageResource(R.drawable.play_btn_pause_selector);
            MainActivity.play.setImageResource(R.drawable.ic_play_bar_btn_pause);
            if (mainActivity ==null) {
                mainActivity = new MainActivity();
            }
            mainActivity.sendNotification();
            //MyService.noti.setImageViewResource(R.id.noti_play, R.drawable.ic_play_btn_pause);
            MainActivity.setplayButtonpause();
            isPause=false;
        }
    }
    public void playnext()
    {
        playMode playMode = new playMode();
        switch (playMode.getMode())
        {
            case 1:
                SuijiPlay();
                return;
        }
        songList = getAudio.getAllSongs(MyApplication.getQuanjuContext());
        if (playposition>=songList.size()-1)
        {
            Toast.makeText(MyApplication.getQuanjuContext(),"已经是最后一首歌了哦",Toast.LENGTH_LONG).show();
            return;
        }
        Song song = songList.get(playposition+1);
        playposition = playposition+1;
        String path = song.getFileUrl();
        geming = song.getTitle();
        String geshou = song.getSinger();
       // String coverUrl = song.getFileUrl();
        int endtime = song.getDuration();
        if (isPause)
        {
            mediaPlayer.reset();
        }
        play(path,playposition);
        getLrc(path,geming,geshou,endtime);

    }

    public void xunhuanplay()
    {

        songList = getAudio.getAllSongs(MyApplication.getQuanjuContext());
        if (playposition>=songList.size()-1)
        {
           playposition = -1;
        }
        Song song = songList.get(playposition+1);
        playposition = playposition+1;
        String path = song.getFileUrl();
        geming = song.getTitle();
        String geshou = song.getSinger();
        // String coverUrl = song.getFileUrl();
        int endtime = song.getDuration();
        if (isPause)
        {
            mediaPlayer.reset();
        }
        play(path,playposition);
        getLrc(path,geming,geshou,endtime);
    }

    public void playPre()
    {
        playMode playMode = new playMode();
        switch (playMode.getMode())
        {
            case 1:
                SuijiPlay();
                return;
        }
        songList = getAudio.getAllSongs(MyApplication.getQuanjuContext());
        if (playposition==0)
        {
            Toast.makeText(MyApplication.getQuanjuContext(),"已经是第一首音乐了哦",Toast.LENGTH_LONG).show();
            return;
        }
        Song song = songList.get(playposition-1);
        playposition = playposition-1;
        String path = song.getFileUrl();
        String geming = song.getTitle();
        String geshou = song.getSinger();
       // String coverUrl = song.getFileUrl();
        int endtime = song.getDuration();
        if (isPause)
        {
            mediaPlayer.reset();
        }
        play(path,playposition);
        getLrc(path,geming,geshou,endtime);
    }

    public void SuijiPlay()
    {
          songList = getAudio.getAllSongs(MyApplication.getQuanjuContext());
          playposition = getRandom();
          Song song = songList.get(playposition);
        String path = song.getFileUrl();
        String geming = song.getTitle();
        String geshou = song.getSinger();
        // String coverUrl = song.getFileUrl();
        int endtime = song.getDuration();
        if (isPause)
        {
            mediaPlayer.reset();
        }
        play(path,playposition);
        getLrc(path,geming,geshou,endtime);
    }

    public void danqu()
    {
        songList = getAudio.getAllSongs(MyApplication.getQuanjuContext());
        Song song = songList.get(playposition);
        String path = song.getFileUrl();
        String geming = song.getTitle();
        String geshou = song.getSinger();
        // String coverUrl = song.getFileUrl();
        int endtime = song.getDuration();
        if (isPause)
        {
            mediaPlayer.reset();
        }
        play(path,playposition);
        getLrc(path,geming,geshou,endtime);
    }

    private int getRandom()
    {
        songList = getAudio.getAllSongs(MyApplication.getQuanjuContext());
        int size = songList.size();
        int randomshu = (int)(Math.random()*size+1);
        return randomshu;
    }

    private void getLrc(final String path, final String geming, final String geshou, final int endtime)
    {
        switch (NetUtils.getNetType())
        {
            case NET_NONE:
                MainActivity mainActivity = new MainActivity();
                mainActivity.tongbuShow(geming,geshou,path,endtime,MainActivity.LOCAL);
                Toast.makeText(MyApplication.getQuanjuContext(),"当前无网络，获取歌手写真失败",Toast.LENGTH_LONG).show();
                return;
        }

        HttpClinet.KugouSearch(geming, 5, new HttpCallback<KugouSearchResult>() {
            @Override
            public void onSuccess(KugouSearchResult kugouSearchResult) {
                if (kugouSearchResult==null)
                {
                    MainActivity mainActivity = new MainActivity();
                    mainActivity.tongbuShow(geming,geshou,path,endtime,MainActivity.LOCAL);
                    Toast.makeText(MyApplication.getQuanjuContext(),"获取歌词失败，请检查网络再试",Toast.LENGTH_LONG).show();
                    return;
                }
                List<KugouSearchResult.lists> lists = kugouSearchResult.getResultList();
                String hash = lists.get(0).getFileHash();
                HttpClinet.KugouUrl(hash, new HttpCallback<KugouMusic>() {
                    @Override
                    public void onSuccess(KugouMusic kugouMusic) {
                        String coverUrl = kugouMusic.getData().getImg();
                        String Lrc = kugouMusic.getData().getLyrics();
                        SearchActivity.creatLrc(Lrc,geming);  //创建歌词文件，为了在Playmusicfragemet中能够定位歌词显示到Lrcview中
                        // play(path,playposition);
                        MainActivity mainActivity = new MainActivity();
                        mainActivity.tongbuShow(geming,geshou,coverUrl,endtime,MainActivity.LOCAL);
                    }

                    @Override
                    public void onFail(Exception e) {
                        MainActivity mainActivity = new MainActivity();
                        mainActivity.tongbuShow(geming,geshou,path,endtime,MainActivity.LOCAL);
                        Toast.makeText(MyApplication.getQuanjuContext(),"获取歌词失败，请检查网络再试",Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onFail(Exception e) {

            }
        });

    }

    public static class playMode
    {
       static int mode=3;

        public int getMode() {
            return mode;
        }

        public void setMode(int mode) {
            this.mode = mode;
        }
    }
}
