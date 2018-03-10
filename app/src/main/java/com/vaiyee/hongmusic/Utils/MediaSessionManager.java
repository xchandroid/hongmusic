package com.vaiyee.hongmusic.Utils;

import android.os.Build;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.vaiyee.hongmusic.MyApplication;
import com.vaiyee.hongmusic.PlayMusic;
import com.vaiyee.hongmusic.bean.Song;

/**
 * Created by Administrator on 2018/3/3.
 */


public class MediaSessionManager {
    private static final String TAG = "MediaSessionManager";
    private static final long MEDIA_SESSION_ACTIONS = PlaybackStateCompat.ACTION_PLAY
            | PlaybackStateCompat.ACTION_PAUSE
            | PlaybackStateCompat.ACTION_PLAY_PAUSE
            | PlaybackStateCompat.ACTION_SKIP_TO_NEXT
            | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
            | PlaybackStateCompat.ACTION_STOP
            | PlaybackStateCompat.ACTION_SEEK_TO;

    private PlayMusic mPlayService;
    private MediaSessionCompat mMediaSession;

    public MediaSessionManager(PlayMusic playService) {
        mPlayService = playService;
        setupMediaSession();
    }

    /**
     * 初始化并激活MediaSession
     */
    private void setupMediaSession() {
        if (mMediaSession == null) {
            mMediaSession = new MediaSessionCompat(MyApplication.getQuanjuContext(), TAG);
        }
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
                | MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS);
        mMediaSession.setCallback(callback);
        mMediaSession.setActive(true);
    }

    /**
     * 更新播放状态，播放/暂停/拖动进度条时调用
     */
    public void updatePlaybackState() {
        int state = (PlayMusic.mediaPlayer.isPlaying() || PlayMusic.isPause)
                ? PlaybackStateCompat.STATE_PLAYING : PlaybackStateCompat.STATE_PAUSED;
        mMediaSession.setPlaybackState(
                new PlaybackStateCompat.Builder()
                        .setActions(MEDIA_SESSION_ACTIONS)
                        .setState(state, PlayMusic.mediaPlayer.getCurrentPosition(), 1)
                        .build());
    }

    /**
     * 更新正在播放的音乐信息，切换歌曲时调用
     */
    public void updateMetaData(Song music) {
        if (music == null) {
            mMediaSession.setMetadata(null);
            return;
        }

        MediaMetadataCompat.Builder metaData = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, music.getTitle())
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, music.getSinger())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, music.getAlbum())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, music.getSinger())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, music.getDuration());
               // .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, CoverLoader.getInstance().loadThumbnail(music));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        { metaData.putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, getAudio.getAllSongs(MyApplication.getQuanjuContext()).size()); }

        mMediaSession.setMetadata(metaData.build());
    }

    /**
     * 释放MediaSession，退出播放器时调用
     */
    public void release() {
        mMediaSession.setCallback(null);
        mMediaSession.setActive(false);
        mMediaSession.release();
    }

    private MediaSessionCompat.Callback callback = new MediaSessionCompat.Callback() {
        @Override
        public void onPlay() {
            mPlayService.pause();
        }

        @Override
        public void onPause() {
            mPlayService.pause();
        }

        @Override
        public void onSkipToNext() {
            mPlayService.playnext();
        }

        @Override
        public void onSkipToPrevious() {
            mPlayService.playPre();
        }

        @Override
        public void onStop() {
           // mPlayService.stop();
        }

        @Override
        public void onSeekTo(long pos) {
           // mPlayService.seekTo((int) pos);
        }
    };
}