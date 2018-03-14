package com.vaiyee.hongmusic.fragement;


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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
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
import com.vaiyee.hongmusic.Utils.QuanjuUtils;
import com.vaiyee.hongmusic.Utils.getAudio;
import com.vaiyee.hongmusic.bean.Song;

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
private static ImageView pre,next,playmode,hide,playmusicibg,ci;
public static ImageView playbg,play;
private static MyCircleView bantouming;
private OnBacktoMainActiviListener listener;
private boolean isPause=false;
public static boolean firstplay=true;
public static PlayMusic playMusic;
private static TextView geming=null,geshou=null,startTime=null,endTime = null;
private static SeekBar seekBar;
public static Timer timer = new Timer() ;
public static MyTimerTask timerTask;
private static ViewPager viewPager;
private ImageView one,tow;
public static LrcView singlelrc;
private static LyricView lyricView;
private boolean isFirstpager = true;
private int mode = 0;
private PopupWindow popupWindow;
private OnlineMusicActivity onlineMusicActivity = new OnlineMusicActivity();

    public PlayMusicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_play_music, container, false);
       play = view.findViewById(R.id.play_pause);
       pre = view.findViewById(R.id.pre);
       playmode = view.findViewById(R.id.playmode);
       hide = view.findViewById(R.id.hidefragment);
       hide.setOnClickListener(this);
       next = view .findViewById(R.id.next);
       seekBar = view.findViewById(R.id.seekbar);
       seekBar.setOnSeekBarChangeListener(this);
       playmusicibg = view.findViewById(R.id.play_music_bg);
       geming = view.findViewById(R.id.play_fragment_geming);
       geshou = view.findViewById(R.id.play_fragment_geshou);
       play.setOnClickListener(this);
       pre.setOnClickListener(this);
       playmode.setOnClickListener(this);
       next.setOnClickListener(this);
       view.setOnTouchListener(this);
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
        bantouming = page1.findViewById(R.id.bantouming);
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
        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFirstpager)
                {
                    viewPager.setCurrentItem(1);
                    isFirstpager = false;
                }
                else
                {
                    viewPager.setCurrentItem(0);
                    isFirstpager = true;
                }
            }
        });
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
        playMusic = new PlayMusic();
        if (playMusic.mediaPlayer==null)
        {
            return;
        }
        if (playMusic.mediaPlayer.isPlaying())
        {
            play.setImageResource(R.drawable.ic_play_btn_pause);
        }
    }

    @Override
    public void onClick(View view) {
        MainActivity mainActivity = new MainActivity();
        switch (view.getId())
        {
            case R.id.play_pause:

                if (!isPause) {
                    if (!playMusic.mediaPlayer.isPlaying()&&firstplay)
                    {
                        List<Song> songList = getAudio.getAllSongs(MyApplication.getQuanjuContext());
                        Song song = songList .get(0);
                        String path = song.getFileUrl();
                        String geming = song.getTitle();
                        String geshou = song.getSinger();
                        int time = song.getDuration();
                        playMusic.play(path,0);
                        mainActivity.tongbuShow(geming,geshou,path,time,MainActivity.LOCAL);
                       // setPause();
                       // mainActivity.setpause();
                        isPause = false;
                        firstplay = false;
                        return;
                    }
                    playMusic.pause();
                  //  play.setImageResource(R.drawable.play_btn_play_pause_selector);
                    isPause = true;
                }
                else
                {
                    playMusic.pause();
                  // setPause();
                   //ainActivity.setpause();
                }
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
                lyricView.setmDefaultColor(Color.parseColor("#FAEBD7"));
                break;
            case R.id.s_fen_red:
                lyricView.setmDefaultColor(Color.parseColor("#CDC8B1"));
                break;
            case R.id.s_green:
                lyricView.setmDefaultColor(Color.parseColor("#CAFF70"));
                break;
            case R.id.s_dark_blue:
                lyricView.setmDefaultColor(Color.parseColor("#EEAEEE"));
                break;
            case R.id.s_yellow:
                lyricView.setmDefaultColor(Color.parseColor("#545454"));
                break;
            case R.id.finish:
                popupWindow.dismiss();
                break;

        }
    }

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
        if (playMusic.mediaPlayer.isPlaying()) {
            playMusic.mediaPlayer.seekTo(seekBar.getProgress());
        }
    }

    @Override
    public void onPlayerClicked(long progress, String content) {
          playMusic.mediaPlayer.seekTo((int) progress);
    }

    public interface OnBacktoMainActiviListener
   {
       void hide();
   }

   public void setplayInfo(String a,String b,int endtime,String coverUrl,int type)
   {
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
                       .crossFade(1000)
                       .bitmapTransform(new BlurTransformation(MyApplication.getQuanjuContext(),15,1))  // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
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
                       .bitmapTransform(new BlurTransformation(MyApplication.getQuanjuContext(),25,1))  // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                        .into(playmusicibg);
               break;
       }
   }



   //定位歌词文件
   public static void locatetoLrc(String geming)
    {
        File file = new File("/storage/emulated/0/HonchenMusic/Lrc/" + geming+".lrc");
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
        Animation animation = AnimationUtils.loadAnimation(MyApplication.getQuanjuContext(),R.anim.zhuanqunaquan);
        LinearInterpolator interpolator = new LinearInterpolator();//匀速旋转
        animation.setInterpolator(interpolator);
        playbg.startAnimation(animation);
        //bantouming.startAnimation(animation);

    }
    public void stop()
    {
        playbg.clearAnimation();
        //bantouming.clearAnimation();
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

    public void resetLrcview()
    {
        lyricView.reset("暂时没发现歌词哦...");
    }
}
