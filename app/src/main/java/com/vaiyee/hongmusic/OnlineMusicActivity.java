package com.vaiyee.hongmusic;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vaiyee.hongmusic.Adapter.OnlineMusicAdapter;
import com.vaiyee.hongmusic.Utils.HttpClinet;
import com.vaiyee.hongmusic.Utils.NetUtils;
import com.vaiyee.hongmusic.bean.DownloadInfo;
import com.vaiyee.hongmusic.bean.KugouMusic;
import com.vaiyee.hongmusic.bean.OnlineLrc;
import com.vaiyee.hongmusic.bean.OnlineMusic;
import com.vaiyee.hongmusic.bean.OnlineMusiclist;
import com.vaiyee.hongmusic.bean.Sheet;
import com.vaiyee.hongmusic.bean.Song;
import com.vaiyee.hongmusic.http.HttpCallback;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OnlineMusicActivity extends SwipeBackActivity{

    private RecyclerView onlineMusicList;
    private View HeaderView,footerView;
    private Sheet sheet;//榜单列表
    private int offset = 0;
    private  List<OnlineMusic> onlineMusics = new ArrayList<>();
    private OnlineMusiclist onlineMusiclistcallback;
    private OnlineMusicAdapter onlineMusicAdapter;
    private ProgressDialog progressDialog;
    private ImageView back,Cover;
    private List<Song> songList;
    private TextView updateTime;
    private GradualTextView content;
    private net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT>=21)
        {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_gedan);
        Intent intent =getIntent();
        sheet = (Sheet) intent.getSerializableExtra("ABC");    //获取传过来的intent中的Sheet对象
        Cover = (ImageView) findViewById(R.id.gedan_bg);
        updateTime = (TextView) findViewById(R.id.publish_time);
        content = (GradualTextView) findViewById(R.id.comment);
        onlineMusicList = (RecyclerView) findViewById(R.id.gedan_list);
        ShowProgress();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collasping_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true); //显示返回按钮
        }
        collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#00F5FF"));  //标题字体展开的颜色
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.BLACK);                         //标题字体收缩的颜色
        getOnlineMusiclist(sheet);
        /*//根据传过来的type获取相应的榜单列表
        onlineMusicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int i, long l) {

                final OnlineMusic onlineMusic = onlineMusics.get(i-1);
                final String songID = onlineMusic.getSong_id();
                final String geming = onlineMusic.getTitle();
                HttpClinet.getLrc(songID, new HttpCallback<OnlineLrc>() {
                    @Override
                    public void onSuccess(OnlineLrc onlineLrc) {
                        Lrccontent = onlineLrc.getLrcContent();
                        SearchActivity.creatLrc(Lrccontent,geming);

                    }

                    @Override
                    public void onFail(Exception e) {

                    }
                });

                AlertDialog.Builder builder;
               switch (NetUtils.getNetType())
               {
                   case NET_WIFI:
                       HttpClinet.getMusicUrl(songID, new HttpCallback<DownloadInfo>() {
                           @Override
                           public void onSuccess(DownloadInfo downloadInfo) {
                               if (downloadInfo == null || downloadInfo.getBitrate() == null) {
                                   onFail(null);
                                   return;
                               }
                               String path = downloadInfo.getBitrate().getFile_link();
                               PlayMusic playMusic =new PlayMusic();
                               playMusic.play(path,i-1);
                               String songname = onlineMusic.getTitle().toString();
                               String geshou = onlineMusic.getArtist_name().toString();
                               String coverUrl = onlineMusic.getPic_big().toString();
                               Log.d("路径是",path);
                               int time = downloadInfo.getBitrate().getFile_duration()*1000;
                               MainActivity mainActivity = new MainActivity();
                               mainActivity.tongbuShow(songname,geshou,coverUrl,time,MainActivity.ONLINE);
                               addToNowPlaylist();

                           }

                           @Override
                           public void onFinish() {

                           }

                           @Override
                           public void onFail(Exception e) {

                           }

                       });
                       break;
                   case NET_4G:
                       builder = new AlertDialog.Builder(OnlineMusicActivity.this);
                       builder.setMessage("当前正在使用4G网络，是否使用数据流量播放在线音乐？");
                       builder.setTitle("提示");
                       builder.setIcon(R.drawable.tip);
                       builder.setPositiveButton("流量多",
                               new DialogInterface.OnClickListener() {
                                   public void onClick(DialogInterface dialog, int which) {
                                       HttpClinet.getMusicUrl(songID, new HttpCallback<DownloadInfo>() {
                                           @Override
                                           public void onSuccess(DownloadInfo downloadInfo) {
                                               if (downloadInfo == null || downloadInfo.getBitrate() == null) {
                                                   onFail(null);
                                                   return;
                                               }
                                               String path = downloadInfo.getBitrate().getFile_link();
                                               PlayMusic playMusic =new PlayMusic();
                                               playMusic.play(path,i-1);
                                               String songname = onlineMusic.getTitle().toString();
                                               String geshou = onlineMusic.getArtist_name().toString();
                                               String coverUrl = onlineMusic.getPic_big().toString();
                                               Log.d("路径是",path);
                                               int time = downloadInfo.getBitrate().getFile_duration()*1000;
                                               MainActivity mainActivity = new MainActivity();
                                               mainActivity.tongbuShow(songname,geshou,coverUrl,time,MainActivity.ONLINE);
                                               addToNowPlaylist();
                                           }

                                           @Override
                                           public void onFinish() {

                                           }

                                           @Override
                                           public void onFail(Exception e) {

                                           }

                                       });
                                       dialog.dismiss();
                                   }
                               });

                       builder.setNegativeButton("伤不起",
                               new android.content.DialogInterface.OnClickListener() {
                                   public void onClick(DialogInterface dialog, int which) {
                                       dialog.dismiss();
                                   }
                               });
                       builder.create().show();

                       break;
                   case NET_3G:
                       builder = new AlertDialog.Builder(OnlineMusicActivity.this);
                       builder.setMessage("当前正在使用3G网络，是否使用数据流量播放在线音乐？");
                       builder.setTitle("提示");
                       builder.setIcon(R.drawable.tip);
                       builder.setPositiveButton("流量多",
                               new DialogInterface.OnClickListener() {
                                   public void onClick(DialogInterface dialog, int which) {
                                       HttpClinet.getMusicUrl(songID, new HttpCallback<DownloadInfo>() {
                                           @Override
                                           public void onSuccess(DownloadInfo downloadInfo) {
                                               if (downloadInfo == null || downloadInfo.getBitrate() == null) {
                                                   onFail(null);
                                                   return;
                                               }
                                               String path = downloadInfo.getBitrate().getFile_link();
                                               PlayMusic playMusic =new PlayMusic();
                                               playMusic.play(path,i-1);
                                               String songname = onlineMusic.getTitle().toString();
                                               String geshou = onlineMusic.getArtist_name().toString();
                                               String coverUrl = onlineMusic.getPic_big().toString();
                                               Log.d("路径是",path);
                                               int time = downloadInfo.getBitrate().getFile_duration()*1000;
                                               MainActivity mainActivity = new MainActivity();
                                               mainActivity.tongbuShow(songname,geshou,coverUrl,time,MainActivity.ONLINE);
                                               addToNowPlaylist();
                                           }

                                           @Override
                                           public void onFinish() {

                                           }

                                           @Override
                                           public void onFail(Exception e) {

                                           }

                                       });
                                       dialog.dismiss();
                                   }
                               });

                       builder.setNegativeButton("伤不起",
                               new android.content.DialogInterface.OnClickListener() {
                                   public void onClick(DialogInterface dialog, int which) {
                                       dialog.dismiss();
                                   }
                               });
                       builder.create().show();

                       break;
                   case NET_2G:
                       builder = new AlertDialog.Builder(OnlineMusicActivity.this);
                       builder.setMessage("当前正在使用2G网络，播放在线音乐会卡顿哟");
                       builder.setTitle("提示");
                       builder.setIcon(R.drawable.tip);
                       builder.setPositiveButton("流量多",
                               new DialogInterface.OnClickListener() {
                                   public void onClick(DialogInterface dialog, int which) {
                                       HttpClinet.getMusicUrl(songID, new HttpCallback<DownloadInfo>() {
                                           @Override
                                           public void onSuccess(DownloadInfo downloadInfo) {
                                               if (downloadInfo == null || downloadInfo.getBitrate() == null) {
                                                   onFail(null);
                                                   return;
                                               }
                                               String path = downloadInfo.getBitrate().getFile_link();
                                               PlayMusic playMusic =new PlayMusic();
                                               playMusic.play(path,i-1);
                                               String songname = onlineMusic.getTitle().toString();
                                               String geshou = onlineMusic.getArtist_name().toString();
                                               String coverUrl = onlineMusic.getPic_big().toString();
                                               Log.d("路径是",path);
                                               int time = downloadInfo.getBitrate().getFile_duration()*1000;
                                               MainActivity mainActivity = new MainActivity();
                                               mainActivity.tongbuShow(songname,geshou,coverUrl,time,MainActivity.ONLINE);
                                               addToNowPlaylist();
                                           }

                                           @Override
                                           public void onFinish() {

                                           }

                                           @Override
                                           public void onFail(Exception e) {

                                           }

                                       });
                                       dialog.dismiss();
                                   }
                               });

                       builder.setNegativeButton("伤不起",
                               new android.content.DialogInterface.OnClickListener() {
                                   public void onClick(DialogInterface dialog, int which) {
                                       dialog.dismiss();
                                   }
                               });
                       builder.create().show();

                       break;
                       default:
                           break;
               }

            }
        });
        */
        //HeaderView = LayoutInflater.from(OnlineMusicActivity.this).inflate(R.layout.activity_online_music_list_header,null);
        //footerView = LayoutInflater.from(OnlineMusicActivity.this).inflate(R.layout.online_musiclist_footer,null);
        //onlineMusicList.addHeaderView(HeaderView);
        //onlineMusicList.addFooterView(footerView);
        //onlineMusicAdapter =new OnlineMusicAdapter(OnlineMusicActivity.this,R.layout.localmusi_listitem,onlineMusics,this);
        //onlineMusicList.setAdapter(onlineMusicAdapter);
    }



    //初始化CollapsingToolbarLayout内容
    private void InitHeader(View headerView)
    {

        Glide.with(OnlineMusicActivity.this)
                .load(onlineMusiclistcallback.getBillboard().getPic_s640())
                .placeholder(R.drawable.default_cover)
                .error(R.drawable.default_cover)
                .crossFade(1000)
                .bitmapTransform(new BlurTransformation(OnlineMusicActivity.this,20,3))
                .into(Cover);
        updateTime.setText("更新时间："+onlineMusiclistcallback.getBillboard().getUpdate_date());
        content.setText("数据来源：百度音乐"+"\n"+onlineMusiclistcallback.getBillboard().getComment());
        collapsingToolbarLayout.setTitle(onlineMusiclistcallback.getBillboard().getName());

    }
    //根据传过来的sheet对象相应的Type获取在线音乐列表
    private void getOnlineMusiclist(Sheet sheet)
    {
        HttpClinet.getOnlineMusicList(sheet.getType(), 300, offset, new HttpCallback<OnlineMusiclist>() {
            @Override
            public void onSuccess(OnlineMusiclist response) {
                onlineMusiclistcallback = response;
                if (offset == 0 && response == null) {
                    return;
                }

                if (response == null || response.getSong_list() == null || response.getSong_list().size() == 0) {
                    return;
                }
                InitHeader(HeaderView);
                onlineMusics.addAll(response.getSong_list());
                onlineMusicAdapter =new OnlineMusicAdapter(OnlineMusicActivity.this,R.layout.localmusi_listitem,onlineMusics,OnlineMusicActivity.this);
                LinearLayoutManager manager = new LinearLayoutManager(OnlineMusicActivity.this);
                onlineMusicList.setLayoutManager(manager);                                                   //给RecycleView设置Layoutmanager必须要，否则数据将不能显示
                onlineMusicList.setAdapter(onlineMusicAdapter);
                CloseProgress();
            }

            @Override
            public void onFail(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CloseProgress();
                        Toast.makeText(OnlineMusicActivity.this,"获取在线音乐失败！请检查网络设置",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }




    //显示正在加载数据对话框
    private void ShowProgress()
    {
        if (progressDialog==null)
        {
            progressDialog = new ProgressDialog(OnlineMusicActivity.this);
            progressDialog.setMessage("正在加载数据");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
    }
    //关闭正在加载对话框
    private  void CloseProgress()
    {
        if (progressDialog!=null)
        {
            progressDialog.dismiss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
