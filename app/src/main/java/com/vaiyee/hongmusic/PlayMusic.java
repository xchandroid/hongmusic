package com.vaiyee.hongmusic;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.EnvironmentalReverb;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Virtualizer;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;

import com.vaiyee.hongmusic.Utils.AudioFocusManager;
import com.vaiyee.hongmusic.Utils.HttpClinet;
import com.vaiyee.hongmusic.Utils.ListDataSave;
import com.vaiyee.hongmusic.Utils.MediaSessionManager;
import com.vaiyee.hongmusic.Utils.NetUtils;
import com.vaiyee.hongmusic.Utils.getAudio;
import com.vaiyee.hongmusic.bean.DownloadInfo;
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
 * MediaPlayer并没有提供设置播放速度相关的API，可以使用SoundPool 调用setRate (int streamID, float rate) rate在0.5和2之间
 */


public class PlayMusic {
    public static MediaPlayer mediaPlayer;
    public static Boolean isPause=false;
    public static int playposition;
    private static List<Song>songList = new ArrayList<>();
    private  PlayMusicFragment p = new PlayMusicFragment();
    public static String geming,geshou;
    private MediaSessionManager mediaSessionManager;
    private  MainActivity mainActivity;
    public AudioFocusManager audioFocusManager;
    public static SharedPreferences.Editor editor;
    private static final String Path = "http://music.163.com/song/media/outer/url?id=";
    private static BassBoost bassBoost;
    private static Virtualizer virtualizer;
    private static EnvironmentalReverb environmentalReverb;
    private static int mbassBoostStrength = 0;
    public void getInstanse()
    {
        mediaPlayer = new MediaPlayer();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
            editor = MyApplication.getQuanjuContext().getSharedPreferences("p",0).edit();
            editor.putInt("i",position);
            editor.apply();
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
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                        }
                    });
                    //mediaPlayer.start();
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
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                        }
                    });
                    //mediaPlayer.prepare();
                    //mediaPlayer.start();
                    // isRest = false;
                    playposition = position;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (PlayMusicFragment.timerTask!=null)
                    {
                        PlayMusicFragment.timerTask.cancel();
                        PlayMusicFragment.timer.cancel();
                    }
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
        //songList = getAudio.getAllSongs(MyApplication.getQuanjuContext());
        PlayList playList = new PlayList();
        songList = playList.getPlaylist();
        if (playposition>=songList.size()-1)
        {
            Toast.makeText(MyApplication.getQuanjuContext(),"已经是最后一首歌了哦",Toast.LENGTH_LONG).show();
            return;
        }
        final Song song = songList.get(playposition+1);
        playposition = playposition+1;
        switch (playList.getBang())
        {
            case 0:     //表示本地音乐列表
                String path = song.getFileUrl();
                geming = song.getTitle();
                geshou = song.getSinger();
                // String coverUrl = song.getFileUrl();
                int endtime = song.getDuration();
                if (isPause) {
                    mediaPlayer.reset();
                }
                play(path, playposition);
                getLrc(path, geming, geshou, endtime);
                break;
            case 1:    //表示百度音乐列表
                geming = song.getTitle();
                geshou = song.getSinger();
                HttpClinet.getMusicUrl(song.getFileUrl(), new HttpCallback<DownloadInfo>() {
                    @Override
                    public void onSuccess(DownloadInfo downloadInfo) {
                        if (isPause) {
                            mediaPlayer.reset();
                        }
                        play(downloadInfo.getBitrate().getFile_link(),playposition);
                        getLrc(downloadInfo.getBitrate().getFile_link(),geming,geshou,downloadInfo.getBitrate().getFile_duration()*1000);
                    }

                    @Override
                    public void onFail(Exception e) {
                        Toast.makeText(MyApplication.getQuanjuContext(),"播放在线歌曲失败，请检查网络重试",Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case 2:    //酷狗音乐列表
                geming = song.getTitle();
                geshou = song.getSinger();
                HttpClinet.KugouUrl(song.getFileUrl(), new HttpCallback<KugouMusic>() {
                    @Override
                    public void onSuccess(KugouMusic kugouMusic) {
                        if (isPause)
                        {
                            mediaPlayer.reset();
                        }
                        play(kugouMusic.getData().getPlay_url(),playposition);
                        getLrc(kugouMusic.getData().getPlay_url(),geming,geshou,song.getDuration());
                    }

                    @Override
                    public void onFail(Exception e) {
                        Toast.makeText(MyApplication.getQuanjuContext(),"播放在线歌曲失败，请检查网络重试",Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case 3:    //网易云音乐列表
                geming =song.getTitle();
                geshou = song.getSinger();
                play(Path+song.getFileUrl()+".mp3",playposition);
                getLrc(Path+song.getFileUrl()+".mp3",geming,geshou,song.getDuration());
                break;
        }

    }

    public void xunhuanplay()
    {

        //songList = getAudio.getAllSongs(MyApplication.getQuanjuContext());
        PlayList playList = new PlayList();
        songList = playList.getPlaylist();
        if (playposition>=songList.size()-1)
        {
            playposition = -1;
        }
        final Song song = songList.get(playposition+1);
        playposition = playposition+1;
        switch (playList.getBang())
        {
            case 0:
                String path = song.getFileUrl();
                geming = song.getTitle();
                geshou = song.getSinger();
                // String coverUrl = song.getFileUrl();
                int endtime = song.getDuration();
                if (isPause) {
                    mediaPlayer.reset();
                }
                play(path, playposition);
                getLrc(path, geming, geshou, endtime);
                break;
            case 1:
                geming = song.getTitle();
                geshou = song.getSinger();
                HttpClinet.getMusicUrl(song.getFileUrl(), new HttpCallback<DownloadInfo>() {
                    @Override
                    public void onSuccess(DownloadInfo downloadInfo) {
                        if (isPause) {
                            mediaPlayer.reset();
                        }
                        play(downloadInfo.getBitrate().getFile_link(),playposition);
                        getLrc(downloadInfo.getBitrate().getFile_link(),geming,geshou,downloadInfo.getBitrate().getFile_duration()*1000);
                    }

                    @Override
                    public void onFail(Exception e) {
                        Toast.makeText(MyApplication.getQuanjuContext(),"获取在线歌曲失败，请检查网络重试",Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case 2:
                geming = song.getTitle();
                geshou = song.getSinger();
                HttpClinet.KugouUrl(song.getFileUrl(), new HttpCallback<KugouMusic>() {
                    @Override
                    public void onSuccess(KugouMusic kugouMusic) {
                        if (isPause)
                        {
                            mediaPlayer.reset();
                        }
                        play(kugouMusic.getData().getPlay_url(),playposition);
                        getLrc(kugouMusic.getData().getPlay_url(),geming,geshou,song.getDuration());
                    }

                    @Override
                    public void onFail(Exception e) {
                        Toast.makeText(MyApplication.getQuanjuContext(),"播放在线歌曲失败，请检查网络重试",Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case 3:
                geming =song.getTitle();
                geshou = song.getSinger();
                play(Path+song.getFileUrl()+".mp3",playposition);
                getLrc(Path+song.getFileUrl()+".mp3",geming,geshou,song.getDuration());
                break;
        }

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
        //songList = getAudio.getAllSongs(MyApplication.getQuanjuContext());
        PlayList playList = new PlayList();
        songList = playList.getPlaylist();
        if (playposition==0)
        {
            Toast.makeText(MyApplication.getQuanjuContext(),"已经是第一首音乐了哦",Toast.LENGTH_LONG).show();
            return;
        }
        final Song song = songList.get(playposition-1);
        playposition = playposition-1;
        switch (playList.getBang())
        {
            case 0:
                String path = song.getFileUrl();
                geming = song.getTitle();
                geshou = song.getSinger();
                // String coverUrl = song.getFileUrl();
                int endtime = song.getDuration();
                if (isPause) {
                    mediaPlayer.reset();
                }
                play(path, playposition);
                getLrc(path, geming, geshou, endtime);
                break;
            case 1:
                geming = song.getTitle();
                geshou = song.getSinger();
                HttpClinet.getMusicUrl(song.getFileUrl(), new HttpCallback<DownloadInfo>() {
                    @Override
                    public void onSuccess(DownloadInfo downloadInfo) {
                        if (isPause) {
                            mediaPlayer.reset();
                        }
                        play(downloadInfo.getBitrate().getFile_link(),playposition);
                        getLrc(downloadInfo.getBitrate().getFile_link(),geming,geshou,downloadInfo.getBitrate().getFile_duration()*1000);
                    }

                    @Override
                    public void onFail(Exception e) {
                        Toast.makeText(MyApplication.getQuanjuContext(),"播放在线歌曲失败，请检查网络重试",Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case 2:
                geming = song.getTitle();
                geshou = song.getSinger();
                HttpClinet.KugouUrl(song.getFileUrl(), new HttpCallback<KugouMusic>() {
                    @Override
                    public void onSuccess(KugouMusic kugouMusic) {
                        if (isPause)
                        {
                            mediaPlayer.reset();
                        }
                        play(kugouMusic.getData().getPlay_url(),playposition);
                        getLrc(kugouMusic.getData().getPlay_url(),geming,geshou,song.getDuration());
                    }

                    @Override
                    public void onFail(Exception e) {
                        Toast.makeText(MyApplication.getQuanjuContext(),"播放在线歌曲失败，请检查网络重试",Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case 3:
                geming =song.getTitle();
                geshou = song.getSinger();
                play(Path+song.getFileUrl()+".mp3",playposition);
                getLrc(Path+song.getFileUrl()+".mp3",geming,geshou,song.getDuration());
                break;
        }


    }

    public void SuijiPlay()
    {
        //songList = getAudio.getAllSongs(MyApplication.getQuanjuContext());
        PlayList playList = new PlayList();
        songList = playList.getPlaylist();
        playposition = getRandom();
        final Song song = songList.get(playposition);
        switch (playList.getBang())
        {
            case 0:
                String path = song.getFileUrl();
                geming = song.getTitle();
                geshou = song.getSinger();
                // String coverUrl = song.getFileUrl();
                int endtime = song.getDuration();
                if (isPause) {
                    mediaPlayer.reset();
                }
                play(path, playposition);
                getLrc(path, geming, geshou, endtime);
                break;
            case 1:    //百度列表
                geming = song.getTitle();
                geshou = song.getSinger();
                HttpClinet.getMusicUrl(song.getFileUrl(), new HttpCallback<DownloadInfo>() {
                    @Override
                    public void onSuccess(DownloadInfo downloadInfo) {
                        if (isPause) {
                            mediaPlayer.reset();
                        }
                        play(downloadInfo.getBitrate().getFile_link(),playposition);
                        getLrc(downloadInfo.getBitrate().getFile_link(),geming,geshou,downloadInfo.getBitrate().getFile_duration()*1000);
                    }

                    @Override
                    public void onFail(Exception e) {
                        Toast.makeText(MyApplication.getQuanjuContext(),"播放在线歌曲失败，请检查网络重试",Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case 2:
                geming = song.getTitle();
                geshou = song.getSinger();
                HttpClinet.KugouUrl(song.getFileUrl(), new HttpCallback<KugouMusic>() {
                    @Override
                    public void onSuccess(KugouMusic kugouMusic) {
                        if (isPause)
                        {
                            mediaPlayer.reset();
                        }
                        play(kugouMusic.getData().getPlay_url(),playposition);
                        getLrc(kugouMusic.getData().getPlay_url(),geming,geshou,song.getDuration());
                    }

                    @Override
                    public void onFail(Exception e) {
                        Toast.makeText(MyApplication.getQuanjuContext(),"播放在线歌曲失败，请检查网络重试",Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case 3:
                geming =song.getTitle();
                geshou = song.getSinger();
                play(Path+song.getFileUrl()+".mp3",playposition);
                getLrc(Path+song.getFileUrl()+".mp3",geming,geshou,song.getDuration());
                break;
        }



    }

    public void danqu()
    {
        //songList = getAudio.getAllSongs(MyApplication.getQuanjuContext());
        PlayList playList = new PlayList();
        songList = playList.getPlaylist();
        final Song song = songList.get(playposition);

        switch (playList.getBang())
        {
            case 0:
                String path = song.getFileUrl();
                geming = song.getTitle();
                geshou = song.getSinger();
                // String coverUrl = song.getFileUrl();
                int endtime = song.getDuration();
                if (isPause) {
                    mediaPlayer.reset();
                }
                play(path, playposition);
                getLrc(path, geming, geshou, endtime);
                break;
            case 1:
                geming = song.getTitle();
                geshou = song.getSinger();
                HttpClinet.getMusicUrl(song.getFileUrl(), new HttpCallback<DownloadInfo>() {
                    @Override
                    public void onSuccess(DownloadInfo downloadInfo) {
                        if (isPause) {
                            mediaPlayer.reset();
                        }
                        play(downloadInfo.getBitrate().getFile_link(),playposition);
                        getLrc(downloadInfo.getBitrate().getFile_link(),geming,geshou,downloadInfo.getBitrate().getFile_duration()*1000);
                    }

                    @Override
                    public void onFail(Exception e) {
                        Toast.makeText(MyApplication.getQuanjuContext(),"播放在线歌曲失败，请检查网络重试",Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case 2:
                geming = song.getTitle();
                geshou = song.getSinger();
                HttpClinet.KugouUrl(song.getFileUrl(), new HttpCallback<KugouMusic>() {
                    @Override
                    public void onSuccess(KugouMusic kugouMusic) {
                        if (isPause)
                        {
                            mediaPlayer.reset();
                        }
                        play(kugouMusic.getData().getPlay_url(),playposition);
                        getLrc(kugouMusic.getData().getPlay_url(),geming,geshou,song.getDuration());
                    }

                    @Override
                    public void onFail(Exception e) {
                        Toast.makeText(MyApplication.getQuanjuContext(),"播放在线歌曲失败，请检查网络重试",Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case 3:
                geming =song.getTitle();
                geshou = song.getSinger();
                play(Path+song.getFileUrl()+".mp3",playposition);
                getLrc(Path+song.getFileUrl()+".mp3",geming,geshou,song.getDuration());
                break;
        }


        /*
        String path = song.getFileUrl();
        String geming = song.getTitle();
        String geshou = song.getSinger();
        int endtime = song.getDuration();
        if (isPause)                                             //这是原来没有加入当前播放列表使的播放逻辑
        {
            mediaPlayer.reset();
        }
        play(path,playposition);
        getLrc(path,geming,geshou,endtime);
        */
    }

    private int getRandom()
    {
        PlayList playList = new PlayList();
        songList = playList.getPlaylist();
        int size = songList.size();
        int randomshu = (int)(Math.random()*size);
        return randomshu;
    }

    public void getLrc(final String path, final String geming, final String geshou, final int endtime)
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


    //设置重低音音效
    public static void setBassBoost(int value)
    {
        if (bassBoost==null)
        {
            bassBoost = new BassBoost(0,mediaPlayer.getAudioSessionId());
        }
        bassBoost.setEnabled(true);
        if (bassBoost.getStrengthSupported()) //这里应该是获取修改音效的权限
        {
            mbassBoostStrength = bassBoost.getRoundedStrength();   //保存没改变之前的值
            bassBoost.setStrength((short) value);
            Toast.makeText(MyApplication.getQuanjuContext(),"设置重低音成功",Toast.LENGTH_LONG).show();
        }
    }
    //设置环绕音音效
    public static void setVirtualizer(int value)
    {
        if (virtualizer==null)
        {
            virtualizer = new Virtualizer(0,mediaPlayer.getAudioSessionId());
        }
        virtualizer.setEnabled(true);
        if (virtualizer.getStrengthSupported()) //这里应该是获取修改音效的权限
        {
            mbassBoostStrength = virtualizer.getRoundedStrength();   //保存没改变之前的值
            virtualizer.setStrength((short) value);
            Toast.makeText(MyApplication.getQuanjuContext(),"设置环绕音成功",Toast.LENGTH_LONG).show();
        }
    }


    //以下方法为设置各种混响
    public static void setDecayHFRatio(int value)
    {
        if(environmentalReverb==null)
        {
            environmentalReverb = new EnvironmentalReverb(0,mediaPlayer.getAudioSessionId());
        }
        environmentalReverb.setEnabled(true); //启用
        environmentalReverb.setDecayHFRatio((short) value);

    }
    public static void setDecayTime(int value)
    {
        if(environmentalReverb==null)
        {
            environmentalReverb = new EnvironmentalReverb(0,mediaPlayer.getAudioSessionId());
        }
        environmentalReverb.setEnabled(true); //启用
        environmentalReverb.setDecayTime((short) value);
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

    public static class PlayList
    {
        private static   List<Song> playlist = new ArrayList<>();
        private static int bang = 0;

        public int getBang() {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getQuanjuContext());
            return preferences.getInt("bang",6);
            //return bang;
        }

        public void setBang(int bang) {
            this.bang = bang;
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getQuanjuContext()).edit();
            editor.putInt("bang",this.bang);
            editor.apply();   //最后一定要保存

        }

        public List<Song> getPlaylist() {
            ListDataSave save = new ListDataSave(MyApplication.getQuanjuContext(),"Playlist");
            return save.getDataList("Playlist");

        }

        public void setPlaylist(List<Song> playlist) {
            this.playlist = playlist;
            ListDataSave save = new ListDataSave(MyApplication.getQuanjuContext(),"Playlist");
            save.setDataList("Playlist",playlist);
        }
    }
}
