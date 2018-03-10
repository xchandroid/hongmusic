package com.vaiyee.hongmusic.Utils;

import android.media.AudioManager;
import android.support.annotation.NonNull;

import com.vaiyee.hongmusic.MyApplication;
import com.vaiyee.hongmusic.PlayMusic;

import static android.content.Context.AUDIO_SERVICE;

/**
 * Created by Administrator on 2018/3/3.
 */

public class AudioFocusManager implements AudioManager.OnAudioFocusChangeListener {
    private PlayMusic mPlayService;
    private AudioManager mAudioManager;
    private boolean isPausedByFocusLossTransient;
    private int mVolumeWhenFocusLossTransientCanDuck;

    public AudioFocusManager(@NonNull PlayMusic playService) {
        mPlayService = playService;
        mAudioManager = (AudioManager) MyApplication.getQuanjuContext().getSystemService(AUDIO_SERVICE);
    }

    /**
     * 播放音乐前先请求音频焦点
     */
    public boolean requestAudioFocus() {
        return mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
                == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    /**
     * 退出播放器后不再占用音频焦点
     */
    public void abandonAudioFocus() {
        mAudioManager.abandonAudioFocus(this);
    }

    /**
     * 音频焦点监听回调
     */
    @Override
    public void onAudioFocusChange(int focusChange) {
        int volume;
        switch (focusChange) {
            // 重新获得焦点
            case AudioManager.AUDIOFOCUS_GAIN:
                if (!willPlay() && isPausedByFocusLossTransient) {
                    // 通话结束，恢复播放
                    mPlayService.pause();
                }

                volume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                if (mVolumeWhenFocusLossTransientCanDuck > 0 && volume == mVolumeWhenFocusLossTransientCanDuck / 2) {
                    // 恢复音量
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mVolumeWhenFocusLossTransientCanDuck,
                            AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                }

                isPausedByFocusLossTransient = false;
                mVolumeWhenFocusLossTransientCanDuck = 0;
                break;
            // 永久丢失焦点，如被其他播放器抢占
            case AudioManager.AUDIOFOCUS_LOSS:
                if (willPlay()) {
                   mPlayService.pause();
                }
                break;
            // 短暂丢失焦点，如来电
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                if (!willPlay()) {
                    if (PlayMusic.mediaPlayer.isPlaying()) {
                        mPlayService.pause();
                        isPausedByFocusLossTransient = true;
                    }
                }

                break;
            // 瞬间丢失焦点，如通知
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // 音量减小为一半
                volume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                if (!willPlay() && volume > 0) {
                    mVolumeWhenFocusLossTransientCanDuck = volume;
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mVolumeWhenFocusLossTransientCanDuck / 2,
                            AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                }
                break;
        }
    }

    private boolean willPlay() {
        return  mPlayService.mediaPlayer.isPlaying()&&PlayMusic.isPause;
    }
}
