package com.vaiyee.hongmusic;

import android.media.MediaPlayer;
import android.widget.Toast;

import com.vaiyee.hongmusic.Utils.HttpClinet;
import com.vaiyee.hongmusic.Utils.getAudio;
import com.vaiyee.hongmusic.bean.KugouMusic;
import com.vaiyee.hongmusic.bean.KugouSearchResult;
import com.vaiyee.hongmusic.bean.Song;
import com.vaiyee.hongmusic.fragement.PlayMusicFragment;
import com.vaiyee.hongmusic.fragement.fragement1;
import com.vaiyee.hongmusic.http.HttpCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/2/7.
 */

public class PlayMusic {
    public   static MediaPlayer mediaPlayer;
    public static Boolean isPause=false;
    private static int playposition;
    private fragement1 fragement = new fragement1();
    private  List<Song>songList = new ArrayList<>();
    private  PlayMusicFragment p = new PlayMusicFragment();
    public static String geming;
    public void getInstanse()
    {
        mediaPlayer = new MediaPlayer();
    }
    public  void play(String path, final int position)
    {
        if (PlayMusicFragment.timerTask!=null)
        {
            PlayMusicFragment.timerTask.cancel();
            PlayMusicFragment.timer.cancel();
        }

        p.zhuanquanuqna();
      if (mediaPlayer==null)
      {
          mediaPlayer = new MediaPlayer();
          try {
              mediaPlayer.setDataSource(path);
              mediaPlayer.prepare();
              mediaPlayer.start();
              playposition = position;
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
      else
      {
          if (mediaPlayer.isPlaying()||isPause)
          {
              mediaPlayer.stop();
              mediaPlayer.reset();
          }
          try {
              mediaPlayer.setDataSource(path);
              mediaPlayer.prepare();
              mediaPlayer.start();
              playposition = position;
          } catch (IOException e) {
              e.printStackTrace();
          }
      }


        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
          @Override
          public void onCompletion(MediaPlayer mediaPlayer) {
              PlayMusicFragment.timer.cancel();
              PlayMusicFragment .timerTask.cancel();
              p.resetLrcview();
              PlayMusicFragment.singlelrc.setLabel("暂无歌词");
              mediaPlayer.reset();
              playnext();

          }
      });
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
            isPause = true;
            return;
        }
        if (isPause)
        {
            mediaPlayer.start();
            p.zhuanquanuqna();
            isPause=false;
        }
    }
    public void playnext()
    {
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
        getLrc(path,geming,geshou,endtime);

    }

    public void playPre()
    {
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
        String coverUrl = song.getFileUrl();
        int endtime = song.getDuration();
        if (isPause)
        {
            mediaPlayer.reset();
        }
        play(path,playposition);
        MainActivity mainActivity = new MainActivity();
        mainActivity.tongbuShow(geming,geshou,coverUrl,endtime,MainActivity.LOCAL);
    }

    private void getLrc(final String path, final String geming, final String geshou, final int endtime)
    {
        HttpClinet.KugouSearch(geming, 5, new HttpCallback<KugouSearchResult>() {
            @Override
            public void onSuccess(KugouSearchResult kugouSearchResult) {
                List<KugouSearchResult.lists> lists = kugouSearchResult.getResultList();
                String hash = lists.get(0).getFileHash();
                HttpClinet.KugouUrl(hash, new HttpCallback<KugouMusic>() {
                    @Override
                    public void onSuccess(KugouMusic kugouMusic) {
                        String coverUrl = kugouMusic.getData().getImg();
                        String Lrc = kugouMusic.getData().getLyrics();
                        SearchActivity.creatLrc(Lrc,geming);
                        play(path,playposition);
                        MainActivity mainActivity = new MainActivity();
                        mainActivity.tongbuShow(geming,geshou,coverUrl,endtime,MainActivity.LOCAL);
                    }

                    @Override
                    public void onFail(Exception e) {

                    }
                });

            }

            @Override
            public void onFail(Exception e) {

            }
        });

    }
}
