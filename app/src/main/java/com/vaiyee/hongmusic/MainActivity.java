package com.vaiyee.hongmusic;

import android.Manifest;
import android.annotation.SuppressLint;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Environment;
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
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vaiyee.hongmusic.Adapter.songsAdapter;
import com.vaiyee.hongmusic.Utils.getAudio;
import com.vaiyee.hongmusic.bean.Song;
import com.vaiyee.hongmusic.fragement.PlayMusicFragment;
import com.vaiyee.hongmusic.fragement.fragement1;
import com.vaiyee.hongmusic.fragement.fragment2;

import java.util.ArrayList;
import java.util.List;

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
    public static ImageView cover;
    private static ImageView play,next;
    public static TextView songname,singer;
    public static final int LOCAL =0 ;
    public static final int ONLINE = 1;
    private static String a=null,b=null;
    private PlayMusic playMusic = new PlayMusic();
    private fragement1 f= new fragement1();
    private Boolean firstplay = true,isPause=false;
    public static SharedPreferences sharedPreferences;

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
        // 申请权限
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        else
        {
            Toast.makeText(MainActivity.this,"授权通过",Toast.LENGTH_LONG).show();
        }
         //强制更新媒体库
        MediaScannerConnection.scanFile(this, new String[]{Environment
                .getExternalStorageDirectory().getAbsolutePath()}, null, null);
        songname = findViewById(R.id.play_bar_geming);
        singer =findViewById(R.id.play_bar_geshou);
        cover = findViewById(R.id.play_bar_cover);
        play = findViewById(R.id.play_bar_star);
        next = findViewById(R.id.play_bar_next);
        play.setOnClickListener(this);
        next.setOnClickListener(this);
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
                    //Log.e("TAG", positionOffset+"");
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
                    case R.id.action_setting:
                        Toast.makeText(MainActivity.this, "点击了" + item.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.action_night:
                        Toast.makeText(MainActivity.this, "点击了" + item.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.action_timer:
                        Toast.makeText(MainActivity.this, "点击了" + item.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.action_exit:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("听完这首再退出吧...？？？");
                        builder.setTitle("提示");
                        builder.setIcon(R.drawable.tip);
                        builder.setPositiveButton("我不",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        android.os.Process.killProcess(android.os.Process
                                                .myPid());
                                    }
                                });

                        builder.setNegativeButton("好吧",
                                new android.content.DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                        builder.create().show();
                        break;
                    case R.id.action_about:
                        Toast.makeText(MainActivity.this, "点击了" + item.getTitle(), Toast.LENGTH_LONG).show();
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
    }

    //同步播放条显示
    public void tongbuShow(String geming,String geshou,String coverUrl,int time,int type)
    {
        a = String.valueOf(Html.fromHtml(geming));
        b = String.valueOf(Html.fromHtml(geshou));
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
                   .into(cover);
                playMusicFragment.setplayInfo(a,b,time,coverUrl,MainActivity.LOCAL);
            break;
            case ONLINE:
                songname.setText(a);
                singer.setText(b);
                Glide.with(MyApplication.getQuanjuContext())
                        .load(coverUrl)
                        .placeholder(R.drawable.music_ic)
                        .into(cover);
                playMusicFragment.setplayInfo(a,b,time,coverUrl,MainActivity.ONLINE);
                break;

        }
    }

   //显示播放界面
    private void showfragment() {
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
         switch (requestCode)
         {
             case 1:
                 if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                     Toast.makeText(MainActivity.this,"授权通过",Toast.LENGTH_LONG).show();
                 } else {
                     // 权限被用户拒绝了，洗洗睡吧。
                     Toast.makeText(MainActivity.this,"拒绝了授权",Toast.LENGTH_LONG).show();
                 }
                 return;
         }
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
                    playMusicFragment.setPause();
                    isPause = false;
                    firstplay = false;
                }
                else {
                    playMusic.pause();
                }
                setpause();
                break;
            case R.id.play_bar_next:
                playMusic.playnext();
                setpause();
                break;
        }

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
}



