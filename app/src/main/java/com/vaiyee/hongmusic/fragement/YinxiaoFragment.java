package com.vaiyee.hongmusic.fragement;


import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import com.vaiyee.hongmusic.PlayMusic;
import com.vaiyee.hongmusic.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class YinxiaoFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {
    private SeekBar zero,one,two,three,four,five,six,seven,eight,nigh;
    private static Equalizer equalizer1,equalizer2;
    private short pinduan,maxEqualizer,minEqualizer;
    private Button hide;
    public YinxiaoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.yinxiao_set,container,false);
      hide = view.findViewById(R.id.finish_set);
        zero = view.findViewById(R.id.setDecayHFRatio);
        zero.setOnSeekBarChangeListener(this);
        one = view.findViewById(R.id.setDecayTime);
        one.setOnSeekBarChangeListener(this);
        two = view.findViewById(R.id.setDensity);
        two.setOnSeekBarChangeListener(this);
        three = view.findViewById(R.id.setDiffusion);
        three.setOnSeekBarChangeListener(this);
        four = view.findViewById(R.id.setReflectionsDelay);
        four.setOnSeekBarChangeListener(this);
        five = view.findViewById(R.id.setReflectionsLevel);
        five.setOnSeekBarChangeListener(this);
        six = view.findViewById(R.id.setReverbLevel);
        six.setOnSeekBarChangeListener(this);
        seven =view.findViewById(R.id.setRoomHFLevel);
        seven.setOnSeekBarChangeListener(this);
        eight = view.findViewById(R.id.setRoomLevel);
        eight.setOnSeekBarChangeListener(this);
        nigh = view.findViewById(R.id.nigh);
        nigh.setOnSeekBarChangeListener(this);
        equalizer1 = new Equalizer(0, PlayMusic.mediaPlayer.getAudioSessionId());
        //equalizer2 = new Equalizer(1, PlayMusic.mediaPlayer.getAudioSessionId());
        equalizer1.setEnabled(true); //启用
        //equalizer2.setEnabled(true);
        pinduan = equalizer1.getNumberOfBands(); //获取支持的频段数量
        minEqualizer = equalizer1.getBandLevelRange()[0];   //第一个下标为最低的均衡器限度范围
        maxEqualizer = equalizer1.getBandLevelRange()[1];  //第二个下标为最高的均衡器限度范围
        zero.setMax(maxEqualizer);
        one.setMax(maxEqualizer);

        two.setMax(maxEqualizer);
        three.setMax(maxEqualizer);

        four.setMax(maxEqualizer);
        five.setMax(maxEqualizer);

        six.setMax(maxEqualizer);
        seven.setMax(maxEqualizer);

        eight.setMax(maxEqualizer);
        nigh.setMax(maxEqualizer);

      return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.hide(YinxiaoFragment.this);
                transaction.commitAllowingStateLoss();
            }
        });
        super.onActivityCreated(savedInstanceState);
    }


    //此方法在seebar拖动过程一直调用
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

        switch (seekBar.getId())
        {
            case R.id.setDecayHFRatio:
                equalizer1.setBandLevel((short) 0, (short) (i+minEqualizer));
                break;
            case R.id.setDecayTime:
                equalizer1.setBandLevel((short) 0, (short) i);   // equalizer1.getCenterFreq(pinduans[0])获取相应频段的中间频率
                break;

            case R.id.setDensity:
                equalizer1.setBandLevel((short) 1, (short) (i+ minEqualizer));
                break;
            case R.id.setDiffusion:
                equalizer1.setBandLevel((short) 1, (short) i);
                break;

            case R.id.setReflectionsDelay:
                equalizer1.setBandLevel((short) 2, (short)( i+minEqualizer));
                break;
            case R.id.setReflectionsLevel:
                equalizer1.setBandLevel((short) 2, (short) i);
                break;

            case R.id.setReverbLevel:
                equalizer1.setBandLevel((short) 3, (short) (i+minEqualizer));
                break;
            case R.id.setRoomHFLevel:
                equalizer1.setBandLevel((short) 3, (short) i);
                break;

            case R.id.setRoomLevel:
                equalizer1.setBandLevel((short) 4, (short) (i+minEqualizer));
                break;
            case R.id.nigh:
                equalizer1.setBandLevel((short) 4, (short)i);
                break;
            default:
                break;

        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {


    }
}
