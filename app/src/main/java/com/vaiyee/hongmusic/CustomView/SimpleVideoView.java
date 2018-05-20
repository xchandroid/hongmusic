package com.vaiyee.hongmusic.CustomView;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.vaiyee.hongmusic.PlayMvActivity;
import com.vaiyee.hongmusic.R;
import com.vaiyee.hongmusic.util.BindOnclick;

/**
 * Created by Administrator on 2018/4/13.
 */
public class SimpleVideoView extends FrameLayout implements OnClickListener{

    private Context context;
    private View mView;
    private VideoView mVideoView;//视频控件
    private ImageView mPlayBtn;//播放按钮
    private ImageView mFullScreenBtn;//全屏按钮
    private SeekBar mPlayProgressBar;//播放进度条
    private TextView mPlayTime;//播放时间
    private TextView title,qaulity;  //视频标题,清晰度
    private LinearLayout mControlPanel,topPanel;
    private ProgressBar loading;
    private Uri mVideoUri = null;

    private Animation outAnima;//控制面板出入动画
    private Animation inAnima;//控制面板出入动画

    private Animation in,out;

    private int mVideoDuration;//视频毫秒数
    private int mCurrentProgress;//毫秒数

    private Runnable mUpdateTask;
    private Thread mUpdateThread;

    private final int UPDATE_PROGRESS = 0;
    private final int EXIT_CONTROL_PANEL = 1;
    private boolean stopThread = true;//停止更新进度线程标志

    private Point screenSize = new Point();//屏幕大小
    private boolean mIsFullScreen = false;//是否全屏标志

    private static int mWidth;//控件的宽度
    private static int mHeigth;//控件的高度

    private ImageView back;
    private String []strings = new String[]{"标清","高清","超清","蓝光"};
    private String now;
    private boolean isfirstPlay = true;
    private PopupWindow popupWindow;
    private int position =0;

    public SimpleVideoView(Context context){
        super(context);
        init(context, null, 0);
    }
    public SimpleVideoView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context, attrs, 0);
    }
    public SimpleVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
                case UPDATE_PROGRESS:
                    mPlayProgressBar.setProgress(mCurrentProgress);
                    setPlayTime(mCurrentProgress);
                    break;
                case EXIT_CONTROL_PANEL:
                    //执行退出动画
                    if(mControlPanel.getVisibility() != View.GONE){
                        mControlPanel.startAnimation(outAnima);
                        mControlPanel.setVisibility(View.GONE);
                        topPanel.startAnimation(out);
                        topPanel.setVisibility(GONE);
                    }
                    break;
            }
        }
    };

    //初始化控件
    private void init(Context context, AttributeSet attrs, int defStyleAttr){
        this.context = context;
        mView = LayoutInflater.from(context).inflate(R.layout.simple_video_view, this);
        mPlayBtn = (ImageView) mView.findViewById(R.id.play_button);
        back = mView.findViewById(R.id.backk);
        back.setOnClickListener(this);
        mFullScreenBtn = (ImageView) mView.findViewById(R.id.full_screen_button);
        mPlayProgressBar = (SeekBar) mView.findViewById(R.id.progress_bar);
        mPlayTime = (TextView) mView.findViewById(R.id.time);
        mControlPanel = (LinearLayout) mView.findViewById(R.id.control_panel);
        topPanel = mView.findViewById(R.id.topPanel);
        mVideoView = (VideoView) mView.findViewById(R.id.video_view);
        title = mView.findViewById(R.id.title);
        qaulity = mView.findViewById(R.id.qaulity);
        qaulity.setOnClickListener(this);
        title.setText(PlayMvActivity.title);  //设置视频的标题
        loading = mView.findViewById(R.id.loading);
        //获取屏幕大小
        ((Activity) context).getWindowManager().getDefaultDisplay().getSize(screenSize);
        //加载动画
        outAnima = AnimationUtils.loadAnimation(context, R.anim.exit_from_bottom);
        inAnima = AnimationUtils.loadAnimation(context, R.anim.enter_from_bottom);
        in = AnimationUtils.loadAnimation(context,R.anim.top_in);
        out = AnimationUtils.loadAnimation(context,R.anim.top_out);
        //设置控制面板初始不可见
        mControlPanel.setVisibility(View.GONE);
        //设置媒体控制器
//		mMediaController = new MediaController(context);
//		mMediaController.setVisibility(View.GONE);
//		mVideoView.setMediaController(mMediaController);
        mVideoView.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //视频加载完成后才能获取视频时长
                initVideo();
            }
        });
        //视频播放完成监听器
        mVideoView.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlayBtn.setImageResource(R.drawable.ic_play_bar_btn_play);
                mVideoView.seekTo(0);
                mPlayProgressBar.setProgress(0);
                setPlayTime(0);
                stopThread = true;
                sendHideControlPanelMessage();
            }
        });

        mView.setOnClickListener(this);
    }

    //初始化视频，设置视频时间和进度条最大值
    private void initVideo(){
        //初始化时间和进度条
        mVideoDuration = mVideoView.getDuration();//毫秒数
        int seconds = mVideoDuration/1000;
        mPlayTime.setText("00:00/"+
                ((seconds/60>9)?(seconds/60):("0"+seconds/60))+":"+
                ((seconds%60>9)?(seconds%60):("0"+seconds%60)));
        mPlayProgressBar.setMax(mVideoDuration);
        mPlayProgressBar.setProgress(0);



        //更新进度条和时间任务
        mUpdateTask = new Runnable(){
            @Override
            public void run(){
                while(!stopThread){
                    mCurrentProgress = mVideoView.getCurrentPosition();
                    handler.sendEmptyMessage(UPDATE_PROGRESS);
                    try {
                        Thread.sleep(500);               //更新进度的线程每隔0.5秒执行一次
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        mVideoView.start();
        mVideoView.seekTo(position);  //切换视频清晰度时定位到正在播放的位置
        if (!isfirstPlay)
        {
            Toast.makeText(context,"已成功切换成 "+now,Toast.LENGTH_LONG).show();
        }
        mPlayBtn.setImageResource(R.drawable.ic_play_bar_btn_pause);
        //开始更新进度线程
        mUpdateThread = new Thread(mUpdateTask);                         //准备好就开始播放视频
        stopThread = false;
        mUpdateThread.start();
        loading.setVisibility(View.GONE);                 //开始播放视频就隐藏加载进度

        mPlayBtn.setOnClickListener(this);
        mFullScreenBtn.setOnClickListener(this);
        //进度条进度改变监听器
        mPlayProgressBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handler.sendEmptyMessageDelayed(EXIT_CONTROL_PANEL, 4000);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeMessages(EXIT_CONTROL_PANEL);
            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if(fromUser){
                    mVideoView.seekTo(progress);//设置视频进度
                    setPlayTime(progress);//设置时间
                }
            }
        });
        mWidth = this.getWidth();
        mHeigth = this.getHeight();
    }

    @Override
    public void onClick(View v) {
        if(v == mView){

            if(mControlPanel.getVisibility() == View.VISIBLE){
                //执行退出动画
                topPanel.startAnimation(out);
                topPanel.setVisibility(View.GONE);
                mControlPanel.startAnimation(outAnima);
                mControlPanel.setVisibility(View.GONE);
            }else {
                //执行进入动画
                mControlPanel.startAnimation(inAnima);
                mControlPanel.setVisibility(View.VISIBLE);
                topPanel.startAnimation(in);
                topPanel.setVisibility(View.VISIBLE);
                sendHideControlPanelMessage();
            }
        }

        else if(v.getId() == R.id.play_button){//播放/暂停按钮
            if(mVideoView.isPlaying()){
                mVideoView.pause();
                mPlayBtn.setImageResource(R.drawable.ic_play_bar_btn_play);
            }else{
                if(mUpdateThread == null || !mUpdateThread.isAlive()){
                    //开始更新进度线程
                    mUpdateThread = new Thread(mUpdateTask);
                    stopThread = false;
                    mUpdateThread.start();
                }
                mVideoView.start();
                mPlayBtn.setImageResource(R.drawable.ic_play_bar_btn_pause);
            }
            sendHideControlPanelMessage();
        }
        else if(v.getId() == R.id.full_screen_button){//全屏
            if(context.getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_PORTRAIT){
                setFullScreen();
            }else{
                setNoFullScreen();
            }
            sendHideControlPanelMessage();
        }
        else if (v.getId()==R.id.backk)
        {
            back();
        }
        else if (v.getId()==R.id.qaulity)
        {
            ShowSelectMenu(v);
        }
    }

    private void ShowSelectMenu(View v) {
        View contentview = LayoutInflater.from(context).inflate(R.layout.qaulity_menu,null);
        LinearLayout linearLayout = contentview.findViewById(R.id.q);
        for (int i=0;i<PlayMvActivity.q;i++)
        {
            TextView textView = new TextView(context);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(18);
            Drawable drawable = getResources().getDrawable(R.drawable.button_ripple);
            textView.setBackground(drawable);
            final int finalI = i;
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    position = getVideoProgress();
                    mVideoView.setVideoURI(Uri.parse(PlayMvActivity.paths[finalI]));
                    if (finalI==0)
                    {
                        qaulity.setText("标清");
                        now ="标清";
                        isfirstPlay = false;
                    }
                    else if (finalI==1)
                    {
                        qaulity.setText("高清");
                        now = "高清";
                        isfirstPlay = false;
                    }
                    else if (finalI==2)
                    {
                        qaulity.setText("超清");
                        now = "超清";
                        isfirstPlay = false;
                    }
                    else
                    {
                        qaulity.setText("蓝光");
                        now = "蓝光";
                        isfirstPlay = false;
                        io.vov.vitamio.utils.Log.d("视频路径"+PlayMvActivity.paths[finalI]);
                    }
                    popupWindow.dismiss();
                    loading.setVisibility(View.VISIBLE);
                }
            });
            textView.setPadding(0,0,0,10);
            textView.setText(strings[i]);
            linearLayout.addView(textView);
        }
        popupWindow = new PopupWindow(contentview, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0]-50, location[1]-popupWindow.getHeight()-400);


    }


    //设置当前时间
    private void setPlayTime(int millisSecond){
        int currentSecond = millisSecond/1000;
        String currentTime = ((currentSecond/60>9)?(currentSecond/60+""):("0"+currentSecond/60))+":"+
                ((currentSecond%60>9)?(currentSecond%60+""):("0"+currentSecond%60));
        StringBuilder text = new StringBuilder(mPlayTime.getText().toString());
        text.replace(0,  text.indexOf("/"), currentTime);
        mPlayTime.setText(text);
    }
    //设置控件的宽高
    private void setSize(){
        ViewGroup.LayoutParams lp = this.getLayoutParams();
        if(mIsFullScreen){
            lp.width = screenSize.y;
            lp.height = screenSize.x;
           // lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            //lp.height = ViewGroup.LayoutParams.MATCH_PARENT;        这样还是不能达到全屏效果，需要获取屏幕大小再设置才可以全屏
        }else{
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = dip2px(context,180);
        }
         this.setLayoutParams(lp);

    }
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale+0.5f);
    }

    //两秒后隐藏控制面板
    private void sendHideControlPanelMessage(){
        handler.removeMessages(EXIT_CONTROL_PANEL);
        handler.sendEmptyMessageDelayed(EXIT_CONTROL_PANEL, 4000);
    }

    //设置视频路径
    public void setVideoUri(Uri uri){
        this.mVideoUri = uri;
        mVideoView.setVideoURI(mVideoUri);
    }
    //获取视频路径
    public Uri getVideoUri(){
        return mVideoUri;
    }

    //设置视频初始画面
    public void setInitPicture(Drawable d){
        mVideoView.setBackground(d);
    }
    //挂起视频
    public void suspend(){
        if(mVideoView != null){
            mVideoView.suspend();
        }
    }
    //设置视频进度
    public void setVideoProgress(int millisSecond,boolean isPlaying){
        mVideoView.setBackground(null);
        mPlayProgressBar.setProgress(millisSecond);
        setPlayTime(millisSecond);
        if(mUpdateThread == null || !mUpdateThread.isAlive()){
            mUpdateThread = new Thread(mUpdateTask);
            stopThread = false;
            mUpdateThread.start();
        }
        mVideoView.seekTo(millisSecond);
        if(isPlaying){
            mVideoView.start();
            mPlayBtn.setImageResource(R.drawable.ic_play_bar_btn_pause);
        }else{
            mVideoView.pause();
            mPlayBtn.setImageResource(R.drawable.ic_play_bar_btn_play);
        }
    }
    //获取视频进度
    public int getVideoProgress(){
        return mVideoView.getCurrentPosition();
    }
    //判断视频是否正在播放
    public boolean isPlaying(){
        return mVideoView.isPlaying();
    }
    //判断是否为全屏状态
    public boolean isFullScreen(){
        return mIsFullScreen;
    }
    //设置竖屏
    public void setNoFullScreen(){
        this.mIsFullScreen = false;
        Activity ac = (Activity)context;
        ac.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ac.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setSize();
        PlayMvActivity.linearLayout.setVisibility(View.VISIBLE);
        mFullScreenBtn.setVisibility(View.VISIBLE);
        back.setVisibility(GONE);
    }
    //设置横屏
    public void setFullScreen(){
        this.mIsFullScreen = true;
        Activity ac = (Activity)context;
        ac.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ac.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setSize();
        PlayMvActivity.linearLayout.setVisibility(View.GONE);
        mFullScreenBtn.setVisibility(View.GONE);
        back.setVisibility(VISIBLE);
    }

    private void back()
    {
        PlayMvActivity playMvActivity = new PlayMvActivity();
        playMvActivity.onBackPressed();
    }

}

