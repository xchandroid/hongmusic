package com.vaiyee.hongmusic.fragement;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.vaiyee.hongmusic.Adapter.PlayListAdapter;
import com.vaiyee.hongmusic.Adapter.ViewPagerAdapter;
import com.vaiyee.hongmusic.Adapter.songsAdapter;
import com.vaiyee.hongmusic.CircleImageView;
import com.vaiyee.hongmusic.CustomView.LyricView;
import com.vaiyee.hongmusic.CustomView.MyCircleView;
import com.vaiyee.hongmusic.CustomView.NoScrollViewPager;
import com.vaiyee.hongmusic.GlideRoundTransform;
import com.vaiyee.hongmusic.MainActivity;
import com.vaiyee.hongmusic.MyApplication;
import com.vaiyee.hongmusic.OnlineMusicActivity;
import com.vaiyee.hongmusic.PlayMusic;
import com.vaiyee.hongmusic.R;
import com.vaiyee.hongmusic.SearchActivity;
import com.vaiyee.hongmusic.Utils.HttpClinet;
import com.vaiyee.hongmusic.Utils.QuanjuUtils;
import com.vaiyee.hongmusic.Utils.getAudio;
import com.vaiyee.hongmusic.bean.DownloadInfo;
import com.vaiyee.hongmusic.bean.KugouMusic;
import com.vaiyee.hongmusic.bean.OnlineLrc;
import com.vaiyee.hongmusic.bean.OnlineMusic;
import com.vaiyee.hongmusic.bean.Song;
import com.vaiyee.hongmusic.bean.WangyiLrc;
import com.vaiyee.hongmusic.http.HttpCallback;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import org.apache.http.params.HttpParams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import jp.wasabeef.glide.transformations.BlurTransformation;
import me.wcy.lrcview.LrcView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayMusicFragment extends Fragment implements View.OnClickListener,View.OnTouchListener,SeekBar.OnSeekBarChangeListener, LyricView.OnPlayerClickListener{
    private static ImageView pre,next,playmode,hide,playmusicibg,ci,playlist;
    public static ImageView playbg,play;
    private OnBacktoMainActiviListener listener;
    private boolean isPause=false,isFirstopen=true;
    public static boolean firstplay=true;
    public static PlayMusic playMusic;
    private static TextView geming=null,geshou=null,startTime=null,endTime = null;
    private static SeekBar seekBar,yinxiao;
    public static Timer timer = new Timer() ;
    public static MyTimerTask timerTask;
    private static ViewPager viewPager;
    private ImageView one,tow;
    public static LrcView singlelrc;
    private static LyricView lyricView;
    private boolean isFirstpager = true;
    private int mode = 0;
    public static int lastCurrentposition=0,lastduration=0;
    private PopupWindow popupWindow;
    private static final String Path = "http://music.163.com/song/media/outer/url?id=";
    private static String coverUrl = null,lrc=null,songname=null,singger=null,Lrccontent=null;
    private OnlineMusicActivity onlineMusicActivity = new OnlineMusicActivity();
    private static View bantouming;

    public PlayMusicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_play_music, container, false);
        ScreenAdapterTools.getInstance().loadView((ViewGroup) view);   //拿到布局填充器返回的view后再加上适配语句
        play = view.findViewById(R.id.play_pause);
        pre = view.findViewById(R.id.pre);
        playmode = view.findViewById(R.id.playmode);
        hide = view.findViewById(R.id.hidefragment);
        hide.setOnClickListener(this);
        next = view .findViewById(R.id.next);
        seekBar = view.findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(this);
        /*
        yinxiao = view.findViewById(R.id.yinxiao);
        yinxiao.setOnSeekBarChangeListener(this);
        yinxiao.setMax(1000);
        */
        playmusicibg = view.findViewById(R.id.play_music_bg);
        geming = view.findViewById(R.id.play_fragment_geming);
        geshou = view.findViewById(R.id.play_fragment_geshou);
        play.setOnClickListener(this);
        pre.setOnClickListener(this);
        playmode.setOnClickListener(this);
        next.setOnClickListener(this);
        view.setClickable(true);
        startTime = view.findViewById(R.id.star_time);
        endTime = view.findViewById(R.id.end_time);
        initViewpager(view);
        return view;
    }

    private void initViewpager(View view) {
        one = view.findViewById(R.id.one);
        one.setOnClickListener(this);
        tow = view .findViewById(R.id.second);
        tow.setOnClickListener(this);
        View page1 = LayoutInflater.from(MyApplication.getQuanjuContext()).inflate(R.layout.page1,null);
        View page2 = LayoutInflater.from(MyApplication.getQuanjuContext()).inflate(R.layout.pager2,null);
        viewPager = view.findViewById(R.id.viewpager);
        playbg = page1.findViewById(R.id.play_bg);
        playlist = view.findViewById(R.id.play_list);
        playlist.setOnClickListener(this);
        bantouming = page1.findViewById(R.id.btm);
        singlelrc = page1.findViewById(R.id.lrc_view_single);
        lyricView = page2.findViewById(R.id.lrcview);
        ci = page2.findViewById(R.id.ci);
        ci.setOnClickListener(this);
        int color = Color.rgb(0 ,255, 255);
        lyricView.setHighLightTextColor(color);
        lyricView.setTextSize(20);
        lyricView.setOnPlayerClickListener(this);
        List<View> viewList = new ArrayList<>();
        viewList.add(page1);
        viewList.add(page2);
        viewPager.setAdapter(new ViewPagerAdapter(viewList));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1)
                {
                    tow.setImageResource(R.drawable.ic_play_page_indicator_selected);
                    one.setImageResource(R.drawable.ic_play_page_indicator_unselected);
                }
                else
                {
                    tow.setImageResource(R.drawable.ic_play_page_indicator_unselected);
                    one.setImageResource(R.drawable.ic_play_page_indicator_selected);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        playMusic = new PlayMusic();  //实例化playMusic，以便可以播放上下首、暂停音乐
        ShowLastMusic(MainActivity.lastgeming,MainActivity.lastgeshou);
        SharedPreferences sharedPreferences = MyApplication.getQuanjuContext().getSharedPreferences("p",0);
        lastCurrentposition=sharedPreferences.getInt("b",0);
        lastduration = sharedPreferences.getInt("k",0);
        Log.d("上次总时长是",String.valueOf(lastduration));
        seekBar.setMax(lastduration);
        seekBar.setProgress( lastCurrentposition);
        startTime.setText(QuanjuUtils.formatTime("mm:ss",lastCurrentposition));
        endTime.setText(QuanjuUtils.formatTime("mm:ss",lastduration));
        Glide.with(MyApplication.getQuanjuContext())
                .load(MainActivity.coverurl)
                .dontAnimate()//防止设置placeholder导致第一次不显示网络图片,只显示默认图片的问题
                .error(R.drawable.music_ic)
                .placeholder(R.drawable.music_ic)
                .transform(new GlideRoundTransform(getContext(),300)).into(playbg);

        Glide.with(MyApplication.getQuanjuContext())
                .load(MainActivity.coverurl)
                .crossFade(1000)
                .bitmapTransform(new BlurTransformation(MyApplication.getQuanjuContext(),25,4))  // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .into(playmusicibg);
        locatetoLrc(MainActivity.lastgeming);
        singlelrc.updateTime(lastCurrentposition);
        lyricView.setCurrentTimeMillis(lastCurrentposition);
    }


    public void ShowLastMusic(String songname,String singger)
    {
        geming.setText(songname);
        geshou.setText(singger);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.play_pause:
                    if (!playMusic.mediaPlayer.isPlaying()&&firstplay)
                    {
                        PlayMusic.PlayList playList = new PlayMusic.PlayList();
                        List<Song> songList = playList.getPlaylist();
                        final Song song = songList .get(MainActivity.position);
                        songname = song.getTitle();
                        singger = song.getSinger();
                        switch (playList.getBang())
                        {
                            case 0:
                                final String path = song.getFileUrl();
                                playMusic.play(path,MainActivity.position);
                                playMusic.mediaPlayer.seekTo(lastCurrentposition);
                                fragement1.getLrc(songname,song,MainActivity.position);
                                break;
                            case 1:
                                final String geming = song.getTitle();
                                final String geshou = song.getSinger();
                                HttpClinet.getMusicUrl(song.getFileUrl(), new HttpCallback<DownloadInfo>() {
                                    @Override
                                    public void onSuccess(DownloadInfo downloadInfo) {
                                        playMusic.play(downloadInfo.getBitrate().getFile_link(),MainActivity.position);
                                        playMusic.mediaPlayer.seekTo(lastCurrentposition);
                                        playMusic.getLrc(downloadInfo.getBitrate().getFile_link(),geming,geshou,downloadInfo.getBitrate().getFile_duration()*1000);
                                    }

                                    @Override
                                    public void onFail(Exception e) {
                                        Toast.makeText(MyApplication.getQuanjuContext(),"播放在线歌曲失败，请检查网络重试",Toast.LENGTH_LONG).show();
                                    }
                                });
                                break;
                            case 2:
                                String hash = song.getFileUrl();
                                HttpClinet.KugouUrl(hash, new HttpCallback<KugouMusic>() {
                                    @Override
                                    public void onSuccess(KugouMusic kugouMusic) {
                                        String path = kugouMusic.getData().getPlay_url();
                                        if (path != null) {
                                            Log.d("歌曲地址是", path);
                                            playMusic.play(path, MainActivity.position);
                                            playMusic.mediaPlayer.seekTo(lastCurrentposition);
                                            coverUrl = kugouMusic.getData().getImg();
                                            lrc = kugouMusic.getData().getLyrics();
                                            SearchActivity.creatLrc(lrc, songname);
                                            MainActivity mainActivity = new MainActivity();
                                            mainActivity.tongbuShow(songname, singger, coverUrl, song.getDuration(), MainActivity.ONLINE);
                                        }
                                    }

                                    @Override
                                    public void onFail(Exception e) {

                                    }
                                });
                                break;
                            case 3:
                                final String id = song.getFileUrl();
                                String path1 = Path +id+ ".mp3";
                                playMusic.play(path1,MainActivity.position);
                                playMusic.mediaPlayer.seekTo(lastCurrentposition);
                                playMusic.getLrc(path1,song.getTitle(),song.getSinger(),song.getDuration());
                                /*
                                HttpClinet.WangyiLrc(id, new HttpCallback<WangyiLrc>() {
                                    @Override
                                    public void onSuccess(WangyiLrc wangyiLrc) {
                                        String lrcContent = wangyiLrc.lrc.lyric;
                                        SearchActivity.creatLrc(lrcContent,songname);
                                        String path = Path +id+ ".mp3";
                                        PlayMusic playMusic =new PlayMusic();
                                        playMusic.play(path,MainActivity.position);
                                        playMusic.mediaPlayer.seekTo(lastCurrentposition);
                                        playMusic.getLrc(path,song.getTitle(),song.getSinger(),song.getDuration());

                                    }

                                    @Override
                                    public void onFail(Exception e) {

                                    }
                                });

                                */
                                break;
                        }

                        firstplay = false;
                        return;
                    }
                    playMusic.pause();


                break;
            case R.id.pre:
                playMusic.playPre();
                break;
            case R.id.next:
                playMusic.playnext();
                break;
            case R.id.playmode:
                switch (mode) {
                    case 0:
                        Intent intent = new Intent("com.vaiyee.playmode");
                        intent.putExtra("mode",1); //随机播放
                        getActivity().sendBroadcast(intent);
                        playmode.setImageResource(R.drawable.play_btn_shuffle_selector);
                        mode+=1;
                        break;
                    case 1:
                        Intent intent1 = new Intent("com.vaiyee.playmode");
                        intent1.putExtra("mode",0); //顺序播放
                        getActivity().sendBroadcast(intent1);
                        playmode.setImageResource(R.drawable.shunxu);
                        mode = mode+1;
                        break;
                    case 2:
                        Intent intent2 = new Intent("com.vaiyee.playmode");
                        intent2.putExtra("mode",2); //单曲循环
                        getActivity().sendBroadcast(intent2);
                        playmode.setImageResource(R.drawable.play_btn_one_selector);
                        mode = mode+1;
                        break;
                    case 3:
                        Intent intent3 = new Intent("com.vaiyee.playmode");
                        intent3.putExtra("mode",3); //列表循环
                        getActivity().sendBroadcast(intent3);
                        playmode.setImageResource(R.drawable.play_btn_loop_selector);
                        mode = mode-3;
                        break;

                }
                break;
            case R.id.hidefragment:
                listener.hide();
                break;
            case R.id.one:
                viewPager.setCurrentItem(0);
                break;
            case R.id.second:
                viewPager.setCurrentItem(1);
                break;
            case R.id.ci:
                ShowpopupWindow();
                break;
            case R.id.blue:
                lyricView.setHighLightTextColor(Color.parseColor("#00F5FF"));
                break;
            case R.id.red:
                lyricView.setHighLightTextColor(Color.parseColor("#FF0000"));
                break;
            case R.id.fen_red:
                lyricView.setHighLightTextColor(Color.parseColor("#FF1493"));
                break;
            case R.id.green:
                lyricView.setHighLightTextColor(Color.parseColor("#00FF7F"));
                break;
            case R.id.dark_blue:
                lyricView.setHighLightTextColor(Color.parseColor("#9400D3"));
                break;
            case R.id.yellow:
                lyricView.setHighLightTextColor(Color.parseColor("#EEEE00"));
                break;
            case R.id.s_blue:
                lyricView.setmDefaultColor(Color.parseColor("#FFFFFF"));
                break;
            case R.id.s_red:
                lyricView.setmDefaultColor(Color.parseColor("#FFB90F"));
                break;
            case R.id.s_fen_red:
                lyricView.setmDefaultColor(Color.parseColor("#FFB6C1"));
                break;
            case R.id.s_green:
                lyricView.setmDefaultColor(Color.parseColor("#CAFF70"));
                break;
            case R.id.s_dark_blue:
                lyricView.setmDefaultColor(Color.parseColor("#EEAEEE"));
                break;
            case R.id.s_yellow:
                lyricView.setmDefaultColor(Color.parseColor("#000000"));
                break;
            case R.id.finish:
                popupWindow.dismiss();
                break;
            case R.id.play_list:
                ShowPlaylist();
                break;
            default:
                break;

        }
    }


    //弹出当前播放列表的popupwindow
    private void ShowPlaylist()
    {
        View contentview = LayoutInflater.from(getContext()).inflate(R.layout.play_list_layout,null);
        initPlaylistview(contentview);
        PopupWindow popupWindow = new PopupWindow(contentview,getActivity().getWindow().getDecorView().getWidth(),getActivity().getWindow().getDecorView().getHeight()/2);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.popfrombottom);
        /*
        final WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.alpha=0.6f;
        getActivity().getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override                                                                          //弹出popup时背景变暗，这里不用效果会更好
            public void onDismiss() {
                params.alpha=1.0f;
                getActivity().getWindow().setAttributes(params);
            }
        });*/
        popupWindow.showAtLocation(getActivity().getWindow().getDecorView(),Gravity.BOTTOM,0,0);

    }

    private void initPlaylistview(View contentview) {
        final PlayMusic.PlayList playList = new PlayMusic.PlayList();
        final List<Song> list = playList.getPlaylist();
        ListView listView =contentview.findViewById(R.id.play_listview);
        final PlayListAdapter adapter = new PlayListAdapter(getContext(),R.layout.play_listview_item,list);
        listView.setAdapter(adapter);
        if (isFirstopen)
        {
            listView.setSelection(MainActivity.position);
            isFirstopen = false;
        }
        else
        {
            listView.setSelection(PlayMusic.playposition);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final Song song = list.get(i);
                songname = song.getTitle();
                singger = song.getSinger();
                final PlayMusic playMusic = new PlayMusic();
                switch (playList.getBang())
                {
                    case 0:
                        final String path = song.getFileUrl();
                        playMusic.play(path,i);
                        adapter.notifyDataSetChanged();
                        fragement1.getLrc(songname,song,i);
                        break;
                    case 1:
                        final String geming = song.getTitle();
                        final String geshou = song.getSinger();
                        HttpClinet.getMusicUrl(song.getFileUrl(), new HttpCallback<DownloadInfo>() {
                            @Override
                            public void onSuccess(DownloadInfo downloadInfo) {
                                playMusic.play(downloadInfo.getBitrate().getFile_link(),i);
                                playMusic.getLrc(downloadInfo.getBitrate().getFile_link(),geming,geshou,downloadInfo.getBitrate().getFile_duration()*1000);
                            }

                            @Override
                            public void onFail(Exception e) {
                                Toast.makeText(MyApplication.getQuanjuContext(),"播放在线歌曲失败，请检查网络重试",Toast.LENGTH_LONG).show();
                            }
                        });
                        break;
                    case 2:
                        String hash = song.getFileUrl();
                        HttpClinet.KugouUrl(hash, new HttpCallback<KugouMusic>() {
                            @Override
                            public void onSuccess(KugouMusic kugouMusic) {
                                String path = kugouMusic.getData().getPlay_url();
                                if (path != null) {
                                    Log.d("歌曲地址是", path);
                                    playMusic.play(path, i);
                                    adapter.notifyDataSetChanged();
                                    coverUrl = kugouMusic.getData().getImg();
                                    lrc = kugouMusic.getData().getLyrics();
                                    SearchActivity.creatLrc(lrc, songname);
                                    MainActivity mainActivity = new MainActivity();
                                    mainActivity.tongbuShow(songname, singger, coverUrl, song.getDuration(), MainActivity.ONLINE);
                                }
                            }

                            @Override
                            public void onFail(Exception e) {

                            }
                        });
                        break;
                    case 3:
                        final String id = song.getFileUrl();
                        String path1 = Path +id+ ".mp3";
                        playMusic.play(path1,i);
                        adapter.notifyDataSetChanged();
                        playMusic.getLrc(path1,song.getTitle(),song.getSinger(),song.getDuration());

                        /*
                        HttpClinet.WangyiLrc(id, new HttpCallback<WangyiLrc>() {
                            @Override
                            public void onSuccess(WangyiLrc wangyiLrc) {

                                String lrcContent = wangyiLrc.lrc.lyric;
                                SearchActivity.creatLrc(lrcContent,songname);
                                String path = Path +id+ ".mp3";
                                PlayMusic playMusic =new PlayMusic();
                                playMusic.play(path,i);
                                adapter.notifyDataSetChanged();
                                playMusic.getLrc(path,song.getTitle(),song.getSinger(),song.getDuration());

                            }

                            @Override
                            public void onFail(Exception e) {

                            }
                        });
                        */
                }


            }
        });

    }

    //弹出设置歌词颜色的popupwindow
    private void ShowpopupWindow() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.geci_edit,null);
        initcontentView(contentView);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM,0,0);
    }

    private void initcontentView(View view) {
        ImageView blue = view.findViewById(R.id.blue);
        ImageView red = view.findViewById(R.id.red);
        ImageView fenred = view.findViewById(R.id.fen_red);
        ImageView green = view.findViewById(R.id.green);
        ImageView darkblue = view.findViewById(R.id.dark_blue);
        ImageView yellow = view.findViewById(R.id.yellow);
        ImageView sblue = view.findViewById(R.id.s_blue);
        ImageView sred = view.findViewById(R.id.s_red);
        ImageView sfenred = view.findViewById(R.id.s_fen_red);
        ImageView sgreen = view.findViewById(R.id.s_green);
        ImageView syellow = view.findViewById(R.id.s_yellow);
        ImageView sdarkblue = view.findViewById(R.id.s_dark_blue);
        Button finish = view.findViewById(R.id.finish);
        finish.setOnClickListener(this);
        blue.setOnClickListener(this);
        sblue.setOnClickListener(this);
        red.setOnClickListener(this);
        sred.setOnClickListener(this);
        fenred.setOnClickListener(this);
        sfenred.setOnClickListener(this);
        green.setOnClickListener(this);
        sgreen.setOnClickListener(this);
        darkblue.setOnClickListener(this);
        sdarkblue.setOnClickListener(this);
        yellow.setOnClickListener(this);
        syellow.setOnClickListener(this);
    }

    public void setListener(OnBacktoMainActiviListener listener)
    {
        this.listener = listener;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }

    //该方法拖动进度条进度改变的时候调用
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }
    //该方法拖动进度条开始拖动的时候调用。
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }
    //该方法拖动进度条停止拖动的时候调用
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId()) {
            case R.id.seekbar:
            if (playMusic.mediaPlayer.isPlaying()) {
                playMusic.mediaPlayer.seekTo(seekBar.getProgress());
            }
            break;
        }
    }

    @Override
    public void onPlayerClicked(long progress, String content) {
        playMusic.mediaPlayer.seekTo((int) progress);   //歌词拖动播放
    }

    public interface OnBacktoMainActiviListener
    {
        void hide();
    }

    public void setplayInfo(String a,String b,int endtime,String coverUrl,int type)
    {
        PlayMusic.editor.putInt("k",endtime);      //保存上次退出时的歌曲时长
        PlayMusic.editor.apply();
        switch (type)
        {
            case MainActivity.LOCAL:
                locatetoLrc(a);
                geming.setText(a);
                geshou.setText(b);
                endTime.setText(QuanjuUtils.formatTime("mm:ss",endtime));
                seekBar.setMax(endtime);
                timerTask = new MyTimerTask();
                timer = new Timer();
                timer.schedule(timerTask,200,500);
                Glide.with(MyApplication.getQuanjuContext())
                        .load(coverUrl)
                        .dontAnimate()//防止设置placeholder导致第一次不显示网络图片,只显示默认图片的问题
                        .error(R.drawable.music_ic)
                        .placeholder(R.drawable.music_ic)
                        .transform(new GlideRoundTransform(getContext(),100)).into(playbg);


                Glide.with(MyApplication.getQuanjuContext())
                        .load(coverUrl)
                        .crossFade(1000)  //加载图片淡入淡出效果
                        .bitmapTransform(new BlurTransformation(MyApplication.getQuanjuContext(),25,4))  // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                        .into(playmusicibg);


                break;
            case MainActivity.ONLINE:
                locatetoLrc(a);
                geming.setText(a);
                geshou.setText(b);
                seekBar.setMax(endtime);
                timerTask = new MyTimerTask();
                timer = new Timer();
                timer.schedule(timerTask,200,500);
                endTime.setText(QuanjuUtils.formatTime("mm:ss",endtime));
                Glide.with(MyApplication.getQuanjuContext())
                        .load(coverUrl)
                        .dontAnimate()//防止设置placeholder导致第一次不显示网络图片,只显示默认图片的问题
                        .error(R.drawable.music_ic)
                        .placeholder(R.drawable.music_ic)
                        .transform(new GlideRoundTransform(getContext(),300)).into(playbg);



                Glide.with(MyApplication.getQuanjuContext())
                        .load(coverUrl)
                        .crossFade(1000)
                        .bitmapTransform(new BlurTransformation(MyApplication.getQuanjuContext(),25,4))  // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                        .into(playmusicibg);
                break;
        }
    }



    //定位歌词文件
    public static void locatetoLrc(String geming)
    {
        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/HonchenMusic/Lrc/" + geming+".lrc");
        if (file.exists()) {
            lyricView.setLyricFile(file);
            singlelrc.loadLrc(file);
        }
    }

    public static Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what)
            {
                case 1:
                    Bundle bundle = msg.getData();
                    int nowposition = bundle.getInt("currentposition");
                    seekBar.setProgress(nowposition);
                    lyricView.setCurrentTimeMillis(playMusic.mediaPlayer.getCurrentPosition());
                    singlelrc.updateTime(playMusic.mediaPlayer.getCurrentPosition());
                    startTime.setText(QuanjuUtils.formatTime("mm:ss",nowposition));
                    break;
            }
        }
    };


    public class MyTimerTask extends TimerTask
    {

        @Override
        public void run() {
            int currnentPosition = playMusic.mediaPlayer.getCurrentPosition();
            PlayMusic.editor.putInt("b",currnentPosition);
            PlayMusic.editor.apply();
            Bundle bundle = new Bundle();
            bundle.putInt("currentposition",currnentPosition);
            Message message = new Message();
            message.what = 1;
            message.setData(bundle);
            handler.sendMessage(message);
        }

        @Override
        public boolean cancel() {
            return super.cancel();
        }
    }
    public void zhuanquanuqna()
    {
       Animation animation1 = AnimationUtils.loadAnimation(MyApplication.getQuanjuContext(),R.anim.zhuanqunaquan); //这里是加载anim文件夹中的动画
        int pivotType = Animation.RELATIVE_TO_SELF; // 相对于自身
        float pivotX = 0.5f; // 取自身区域在X轴上的中心点
        float pivotY = 0.5f; // 取自身区域在Y轴上的中心点
        Animation animation = new RotateAnimation(0,360,pivotType, pivotX, pivotType, pivotY);  // 围绕自身的中心点进行旋转
        animation.setDuration(30000);
        animation.setRepeatCount(-1);  //表示一直旋转
        animation.setRepeatMode(Animation.RESTART);
        LinearInterpolator interpolator = new LinearInterpolator();//匀速旋转
        animation.setInterpolator(interpolator);
        animation1.setInterpolator(interpolator);  //设置拦截器，使其匀速旋转
        bantouming.startAnimation(animation);
        playbg.startAnimation(animation1);


    }
    public void stop()
    {
        bantouming.clearAnimation();
        playbg.clearAnimation();

    }

    //画圆
    public static Bitmap makeRoundCorner(Bitmap bitmap)
    {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int left = 0, top = 0, right = width, bottom = height;
        float roundPx = height/2;
        if (width > height) {
            left = (width - height)/2;
            top = 0;
            right = left + height;
            bottom = height;
        } else if (height > width) {
            left = 0;
            top = (height - width)/2;
            right = width;
            bottom = top + width;
            roundPx = width/2;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(left, top, right, bottom);
        RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
    //创建歌词文件
    private void createLrc(String lrcContent)
    {
        String filePath = null;
        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);//是否有外置SD卡
        if (hasSDCard) {
            filePath =Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator+"HonchenMusic"+File.separator+"geci.lrc";
        } else
            filePath =Environment.getDownloadCacheDirectory().toString() + File.separator +"hello.txt";

        try {
            File file = new File(filePath);
            if (!file.exists()) {   //如果文件不存在
                File dir = new File(file.getParent());  //获取file文件在内置或外置SD卡  getParent()与getParentFile()的区别：getParentFile()的返回值是File型的。而getParent() 的返回值是String型的。mkdirs是File类里面的方法，所以当然得用f.getParentFile().mkdirs();getParentFile()的返回值是File型的。而getParent() 的返回值是String型的。mkdirs是File类里面的方法，所以当然得用f.getParentFile().mkdirs();
                dir.mkdirs();  //先创建文件夹
                file.createNewFile();//创建文件
            }
            FileOutputStream outStream = new FileOutputStream(file);//创建文件字节输出流对象，以字节形式写入所创建的文件中
            outStream.write(lrcContent.getBytes());//开始写入文件（也就是把文件写入内存卡中）
            Log.d("歌词路径",filePath);
            Log.d("歌词内容是",lrcContent);
            Log.d("看看这是什么鬼",String.valueOf(lrcContent.getBytes()));
            outStream.close();//关闭文件字节输出流
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setClickNull()
    {
        if(!this.isVisible())
        {

        }
    }

    public void resetLrcview()
    {
        lyricView.reset("暂时没发现歌词哦...");
    }
}

