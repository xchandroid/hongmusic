package com.vaiyee.hongmusic;

import android.Manifest;
import android.annotation.SuppressLint;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.vaiyee.hongmusic.Adapter.songsAdapter;
import com.vaiyee.hongmusic.Utils.QuanjuUtils;
import com.vaiyee.hongmusic.Utils.getAudio;
import com.vaiyee.hongmusic.bean.Song;
import com.vaiyee.hongmusic.fragement.PlayMusicFragment;
import com.vaiyee.hongmusic.fragement.WangyiFragment;
import com.vaiyee.hongmusic.fragement.fragement1;
import com.vaiyee.hongmusic.fragement.fragment2;
import com.vaiyee.hongmusic.service.MyService;

import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static org.litepal.LitePalApplication.getContext;

public class MainActivity extends FragmentActivity implements View.OnTouchListener,View.OnClickListener {

    private ImageButton opendrawermenu,search;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ViewPager viewPager;
    private ColorTrackView t, tt;
    private LinearLayout linearLayout;
    private List<ColorTrackView> mTabs = new ArrayList<ColorTrackView>();
    private PlayMusicFragment playMusicFragment;
    private List<Fragment> list = new ArrayList<Fragment>();
    private Boolean isShowfragment =false,Pause=false;
    public static boolean firstopen = false;
    public static ImageView cover,play;
    private static ImageView next;
    public static TextView songname,singer,daojishi;
    public static final int LOCAL =0 ;
    public static final int ONLINE = 1;
    private static String a=null,b=null,time = null;
    private PlayMusic playMusic = new PlayMusic();
    private fragement1 f= new fragement1();
    public static Boolean firstplay = true,isPause=false,Xiumian = false;
    public static SharedPreferences sharedPreferences;
    public static MyService.MusicBinder musicBinder;
    private EditText editText;
    private boolean showpopwindow = false;
    private static Timer timer,timer2;
    private static diingShiTask task;
    private static Showtask showtask;
    private static int s;
    private static ImageView wether_ic;
    private static TextView city,wind,wind_speed,tmp,weinfo;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            musicBinder = (MyService.MusicBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("data",0);
        if (Build.VERSION.SDK_INT>=21)
        {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }



         //强制更新媒体库
        MediaScannerConnection.scanFile(this, new String[]{Environment
                .getExternalStorageDirectory().getAbsolutePath()}, null, null);
        songname = findViewById(R.id.play_bar_geming);
        singer =findViewById(R.id.play_bar_geshou);
        daojishi = findViewById(R.id.daojishi);
        cover = findViewById(R.id.play_bar_cover);
        play = findViewById(R.id.play_bar_star);
        next = findViewById(R.id.play_bar_next);
        play.setOnClickListener(this);
        next.setOnClickListener(this);
        initNav();
        t = findViewById(R.id.changeTextColorView);
        tt = findViewById(R.id.textView2);
        tt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
            }
        });
        mTabs.add(t);
        mTabs.add(tt);
        linearLayout = findViewById(R.id.play_bar);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showfragment();
            }
        });
        opendrawermenu = (ImageButton) findViewById(R.id.opendrawermenu);
        search = findViewById(R.id.music_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlaout);
        viewPager = findViewById(R.id.viewpager);
        list.add(new fragement1());
        list.add(new fragment2());
        viewPager.setAdapter(new fragmentAdapter(getSupportFragmentManager(), list));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset > 0) {

                    ColorTrackView left = mTabs.get(position);
                    ColorTrackView right = mTabs.get(position + 1);
                    left.setDirection(1);
                    right.setDirection(0);
                    left.setProgress(1 - positionOffset);
                    right.setProgress(positionOffset);

                }
            }

            @Override
            public void onPageSelected(int position) {
              /*
                switch (position)
                {
                    case 1:
                        tt.setTextColor(R.color.txtcolor);
                        t.setTextColor(R.color.txtcoler);
                        break;
                    case 0:
                        tt.setTextColor(R.color.txtcoler);
                        t.setTextColor(R.color.txtcolor);
                        break;
                }*/
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_night:
                        Toast.makeText(MainActivity.this, "更换主题将在下个版本推出", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.action_timer:
                        View contentview = LayoutInflater.from(MainActivity.this).inflate(R.layout.timerexit,null);
                        final PopupWindow popupWindow = new PopupWindow(contentview, 800,ViewGroup.LayoutParams.WRAP_CONTENT);
                        editText = contentview.findViewById(R.id.timeredit);
                        Button sure = contentview.findViewById(R.id.sure);
                        sure.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                 time = editText.getText().toString();
                                if (TextUtils.isEmpty(time))
                                {
                                    Toast.makeText(MainActivity.this,"请输入时间",Toast.LENGTH_LONG).show();
                                    return;
                                }
                                s = Integer.parseInt(time)*60*1000;  //倒计时，毫秒为单位
                                atTiemToExit(Integer.parseInt(time));
                                hintKeyboard();
                                popupWindow.dismiss();
                            }
                        });
                        Button cancel = contentview.findViewById(R.id.cancel);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow.dismiss();
                            }
                        });

                        popupWindow.setFocusable(true);
                        popupWindow.setOutsideTouchable(true);
                        popupWindow.setTouchable(true);

                        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                if (motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE &&!popupWindow.isFocusable())
                                {
                                    return true;  //点击弹窗外面拦截事件，是弹窗不能通过点击外面消失，貌似没效果
                                }
                                return false;
                            }
                        });

                        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                WindowManager.LayoutParams params1 = getWindow().getAttributes();
                                params1.alpha = 1.0f;
                                getWindow().setAttributes(params1);
                            }
                        });
                        if (!Xiumian)
                        {
                            final WindowManager.LayoutParams params = getWindow().getAttributes();
                            params.alpha = 0.5f;
                            getWindow().setAttributes(params);
                            popupWindow.showAtLocation(getWindow().getDecorView(),Gravity.CENTER,0,0);
                        }
                        else
                        {
                            cancelExit();
                        }
                        break;
                    case R.id.action_exit:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("确定要退出程序吗？");
                        builder.setTitle("提示");
                        builder.setIcon(R.drawable.tip);
                        builder.setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        android.os.Process.killProcess(android.os.Process.myPid());
                                        musicBinder.CancelNotification();
                                    }
                                });

                        builder.setNegativeButton("再等等",
                                new android.content.DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                        builder.create().show();
                        break;
                    case R.id.action_about:
                        showAbout();
                        break;
                    case R.id.myqq:
                       if(checkApkExist(MainActivity.this,"com.tencent.mobileqq"))
                       {
                           startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin="+String.valueOf("1208718872")+"&version=1")));
                       }
                       else
                       {
                           Toast.makeText(MainActivity.this,"本机未安装QQ",Toast.LENGTH_LONG).show();
                       }
                        break;
                       case R.id.weather_setting:
                           Intent intent = new Intent(MainActivity.this,WetMainActivity.class);
                           startActivity(intent);
                           break;
                    default:
                        break;
                }
                return false;
            }
        });
        opendrawermenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });
        //生成MediaPlayer实例,为播放界面第一次播放做准备
        playMusic.getInstanse();
        showfragment();
        hidefragment();
        bindService();
    }

    //弹出关于的窗口
    private void showAbout()
    {
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.aboutthis,null);
        Button desmiss = view.findViewById(R.id.desmiss);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        desmiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha =0.5f;
        getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });
        popupWindow.showAtLocation(getWindow().getDecorView(),Gravity.CENTER,0,0);

    }

    //同步播放条显示
    public void tongbuShow(String geming,String geshou,String coverUrl,int time,int type)
    {
        a = String.valueOf(Html.fromHtml(geming));
        b = String.valueOf(Html.fromHtml(geshou));
        loadAsBitmap(coverUrl);
        if (playMusicFragment==null)
        {
            playMusicFragment = new PlayMusicFragment();
        }
        switch (type)
        {
            case LOCAL:
            songname.setText(a);
            singer.setText(b);
           Glide.with(MyApplication.getQuanjuContext())
                   .load(coverUrl)
                   .placeholder(R.drawable.music_ic)
                   .error(R.drawable.music_ic)
                   .into(cover);
                playMusicFragment.setplayInfo(a,b,time,coverUrl,MainActivity.LOCAL);
            break;
            case ONLINE:
                songname.setText(a);
                singer.setText(b);
                Glide.with(MyApplication.getQuanjuContext())
                        .load(coverUrl)
                        .placeholder(R.drawable.music_ic)
                        .error(R.drawable.music_ic)
                        .into(cover);
                playMusicFragment.setplayInfo(a,b,time,coverUrl,MainActivity.ONLINE);
                break;

        }
    }

   //显示播放界面
    public void showfragment() {
        if (isShowfragment)
        {
            return;
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fragment_show,0);
        if (playMusicFragment==null)
        {
            playMusicFragment = new PlayMusicFragment();
            fragmentTransaction.replace(android.R.id.content,playMusicFragment);
            fragmentTransaction.addToBackStack(null);//把碎片加入当前Activity管理的返回栈
        }
        else
        {
            fragmentTransaction.show(playMusicFragment);
        }
        fragmentTransaction.commitAllowingStateLoss();
        isShowfragment =true;
        //监听回调接口
        playMusicFragment.setListener(new PlayMusicFragment.OnBacktoMainActiviListener() {
            @Override
            public void hide() {
                hidefragment();
            }

        });
    }
    //隐藏播放界面
    private void hidefragment()
    {
       FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
       fragmentTransaction.setCustomAnimations(0,R.anim.fragment_dimiss);
       fragmentTransaction.hide(playMusicFragment);
       fragmentTransaction.commitAllowingStateLoss();
       isShowfragment =false;
    }


    @Override
    public void onBackPressed() {
        if (isShowfragment&&playMusicFragment!=null)
        {
            hidefragment();
            return;
        }
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT))
        {
            drawerLayout.closeDrawers();
            return;
        }
        onBackTohome();
    }
    //返回桌面
    private void onBackTohome()
    {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.play_bar_star:
                if (!playMusic.mediaPlayer.isPlaying()&&firstplay)
                {
                    List<Song> songList = getAudio.getAllSongs(MyApplication.getQuanjuContext());
                    Song song = songList .get(0);
                    String path = song.getFileUrl();
                    String geming = song.getTitle();
                    String geshou = song.getSinger();
                    int endtime = song.getDuration();
                    playMusic.play(path,0);
                    MainActivity mainActivity = new MainActivity();
                    mainActivity.tongbuShow(geming,geshou,path,endtime,MainActivity.LOCAL);
                   // playMusicFragment.setPause();
                    isPause = false;
                    firstplay = false;
                }
                else {
                    playMusic.pause();
                }
                //setpause();
                 break;
            case R.id.play_bar_next:
                playMusic.playnext();
               // setpause();
                break;
        }

    }
    //绑定通知栏服务
    private void bindService()
    {
        Intent intent = new Intent(this,MyService.class);
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);
        startService(intent);//启动服务
    }
    public void setpause()
    {
        if (!Pause)
        {
            play.setImageResource(R.drawable.ic_play_bar_btn_pause);
            Pause =true;
        }
        else
        {
            play.setImageResource(R.drawable.play_bar_btn_play_pause_selector);
            Pause=false;
        }

    }
    //显示播放音乐的信息在通知栏
    public void sendNotification()
    {
        musicBinder.sendNotification();
    }
    //关闭通知栏服务
    public void cancelNOti()
    {
        musicBinder.CancelNotification();
    }
    //同步通知栏歌曲播放信息
    public void tongbuNoti(Bitmap url, String geming, String geshou)
    {
        musicBinder.tongbuShow(url,geming,geshou);
    }
    //同步通知栏播放按钮为暂停图标
    public static void setplayButtonpause()
    {
        musicBinder.setpause();
    }
    //同步通知栏播放按钮为开始播放图标
    public static void setplayButtonplay()
    {
        musicBinder.setplay();
    }


    @Override
    protected void onResume() {
        if (firstopen)
        {
            showfragment();
        }
        if (showpopwindow)
        {
            Timer timer = new Timer();
            showabouttask showabouttask = new showabouttask();
            timer.schedule(showabouttask,2000);
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        firstopen =false;
        showpopwindow = false;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this,MyService.class);
        stopService(intent);
        unbindService(serviceConnection);
        playMusic.audioFocusManager.abandonAudioFocus();
        super.onDestroy();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 获取到Activity下的Fragment
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments == null)
        {
            return;
        }
        // 查找在Fragment中onRequestPermissionsResult方法并调用
        for (Fragment fragment : fragments)
        {
            if (fragment != null)
            {
                // 这里就会调用我们Fragment中的onRequestPermissionsResult方法
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }

    }

    private void loadAsBitmap(String url)
    {
        Glide.with(MyApplication.getQuanjuContext())
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        tongbuNoti(resource,a,b);
                    }
                });

    }
    //定时退出
   private class diingShiTask extends TimerTask
    {

        @Override
        public void run() {
            Intent intent = new Intent(MainActivity.this,MyService.class);
            stopService(intent);
            unbindService(serviceConnection);
            playMusic.audioFocusManager.abandonAudioFocus();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
    private void atTiemToExit(int time)
    {
            timer = new Timer();
            task = new diingShiTask();
            timer.schedule(task, time * 60 * 1000);
            Toast.makeText(MainActivity.this,"程序将于"+time+"分钟后退出",Toast.LENGTH_LONG).show();
            showdaojishi();
            Xiumian = true;
    }
    private void cancelExit()
    {
        task.cancel();
        timer.cancel();
        showtask.cancel();
        timer2.cancel();
        Toast.makeText(MainActivity.this,"取消了定时休眠",Toast.LENGTH_LONG).show();
        Xiumian = false;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                daojishi.setText("");
            }
        });
    }
   private class Showtask extends TimerTask {
        int totaltime;
        public Showtask(int totaltime)
        {
            this.totaltime = totaltime;
        }
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   daojishi.setText("倒计时 "+ QuanjuUtils.formatTime("mm:ss",totaltime)+" 退出程序");
                  totaltime = totaltime -1000;
                }
            });
        }
    }
    private void showdaojishi()
    {
        timer2 = new Timer();
        showtask = new Showtask(s);
        timer2.schedule(showtask,200,1000);

    }
    private class showabouttask extends TimerTask
    {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showAbout();
                }
            });
        }
    }


    private void initNav()
    {
        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        View view = navigationView.getHeaderView(0);
        wether_ic = view.findViewById(R.id.weather_ico);
        city = view.findViewById(R.id.city);
        wind = view.findViewById(R.id.wind);
        tmp = view.findViewById(R.id.tmp);
        wind_speed = view.findViewById(R.id.wind_speed);
        weinfo = view.findViewById(R.id.weinfo);
    }

    public void setWeatherInfo(int code,String chengshi,String feng,String windLi,String windSpeed,String wendu,String info)
    {
        switch (code)
        {
            case 100:
                wether_ic.setImageResource(R.drawable.qing);
                break;
            case 101:
                wether_ic.setImageResource(R.drawable.duoyun);
                break;
            case 102:
                wether_ic.setImageResource(R.drawable.shaoyun);
                break;
            case 103:
                wether_ic.setImageResource(R.drawable.qingjianduoyun);
                break;
            case 104:
                wether_ic.setImageResource(R.drawable.yin);
                break;
            case 200:
                wether_ic.setImageResource(R.drawable.youfeng);
                break;
        }
        city.setText(chengshi);
        wind.setText(feng +windLi+"级");
        wind_speed.setText("风速"+windSpeed+"km/h");
        tmp.setText(wendu);
        weinfo.setText(info);
    }

    //判断用户是否安装了QQ,参数packageName为包名
    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    //隐藏软键盘
    private void hintKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null)
        {
            if (getCurrentFocus().getWindowToken()!=null)
            {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); }
        }
    }
/*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_HEADSETHOOK == keyCode) { //按下了耳机键
            if (event.getRepeatCount() == 0) {  //如果长按的话，getRepeatCount值会一直变大
                //短按,暂停播放
                playMusic.pause();
                return true;
            } else if (event.getRepeatCount()==1)
            {
                //双击，下一首
                playMusic.playnext();
                return true;
            }
            else {
                //长按，上一首
                playMusic.playPre();
                return true;
            }
        }
        if (KeyEvent.KEYCODE_BACK==keyCode)
        {
            onBackPressed();
            return true;
        }
        return false;
    }
    */
}



