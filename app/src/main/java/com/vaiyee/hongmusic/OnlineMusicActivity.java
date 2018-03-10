package com.vaiyee.hongmusic;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.vaiyee.hongmusic.http.HttpCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OnlineMusicActivity extends SwipeBackActivity{

    private ListView onlineMusicList;
    private View HeaderView,footerView;
    private Sheet sheet;//榜单列表
    private int offset = 0;
    private  List<OnlineMusic> onlineMusics = new ArrayList<>();
    private OnlineMusiclist onlineMusiclistcallback;
    private OnlineMusicAdapter onlineMusicAdapter;
    private ProgressDialog progressDialog;
    private TextView sheetTitle;
    private ImageView back;
    private static String Lrccontent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_music);
        Init();
        getOnlineMusiclist(sheet);
        onlineMusicList = (ListView) findViewById(R.id.online_music_list);
        onlineMusicList.setVisibility(View.GONE);
        ShowProgress();
        onlineMusicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
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
                               playMusic.play(path,0);
                               String songname = onlineMusic.getTitle().toString();
                               String geshou = onlineMusic.getArtist_name().toString();
                               String coverUrl = onlineMusic.getPic_big().toString();
                               Log.d("路径是",path);
                               int time = downloadInfo.getBitrate().getFile_duration()*1000;
                               MainActivity mainActivity = new MainActivity();
                               mainActivity.tongbuShow(songname,geshou,coverUrl,time,MainActivity.ONLINE);
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
                                               playMusic.play(path,0);
                                               String songname = onlineMusic.getTitle().toString();
                                               String geshou = onlineMusic.getArtist_name().toString();
                                               String coverUrl = onlineMusic.getPic_big().toString();
                                               Log.d("路径是",path);
                                               int time = downloadInfo.getBitrate().getFile_duration()*1000;
                                               MainActivity mainActivity = new MainActivity();
                                               mainActivity.tongbuShow(songname,geshou,coverUrl,time,MainActivity.ONLINE);
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
                                               playMusic.play(path,0);
                                               String songname = onlineMusic.getTitle().toString();
                                               String geshou = onlineMusic.getArtist_name().toString();
                                               String coverUrl = onlineMusic.getPic_big().toString();
                                               Log.d("路径是",path);
                                               int time = downloadInfo.getBitrate().getFile_duration()*1000;
                                               MainActivity mainActivity = new MainActivity();
                                               mainActivity.tongbuShow(songname,geshou,coverUrl,time,MainActivity.ONLINE);
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
                                               playMusic.play(path,0);
                                               String songname = onlineMusic.getTitle().toString();
                                               String geshou = onlineMusic.getArtist_name().toString();
                                               String coverUrl = onlineMusic.getPic_big().toString();
                                               Log.d("路径是",path);
                                               int time = downloadInfo.getBitrate().getFile_duration()*1000;
                                               MainActivity mainActivity = new MainActivity();
                                               mainActivity.tongbuShow(songname,geshou,coverUrl,time,MainActivity.ONLINE);
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
        HeaderView = LayoutInflater.from(OnlineMusicActivity.this).inflate(R.layout.activity_online_music_list_header,null);
        footerView = LayoutInflater.from(OnlineMusicActivity.this).inflate(R.layout.online_musiclist_footer,null);
        onlineMusicList.addHeaderView(HeaderView);
        onlineMusicList.addFooterView(footerView);
        onlineMusicAdapter =new OnlineMusicAdapter(OnlineMusicActivity.this,R.layout.localmusi_listitem,onlineMusics,this);
        onlineMusicList.setAdapter(onlineMusicAdapter);
    }
    //获取传过来的intent中的Sheet对象
    private void Init()
    {
        Intent intent =getIntent();
        sheet = (Sheet) intent.getSerializableExtra("ABC");
        sheetTitle = (TextView) findViewById(R.id.sheet_title);
        TextPaint paint = sheetTitle.getPaint();
        paint.setFakeBoldText(true);
        back = (ImageView) findViewById(R.id.backlast);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    //初始化ListHeader
    private void InitHeader(View headerView)
    {
        ImageView Cover = headerView.findViewById(R.id.iv_cover);
        TextView title,updateTime,content;
        title = headerView.findViewById(R.id.tv_title);
        updateTime = headerView.findViewById(R.id.tv_update_date);
        content = headerView.findViewById(R.id.tv_comment);
        Glide.with(OnlineMusicActivity.this)
                .load(onlineMusiclistcallback.getBillboard().getPic_s640())
                .placeholder(R.drawable.default_cover)
                .error(R.drawable.default_cover)
                .into(Cover);
        title.setText(onlineMusiclistcallback.getBillboard().getName());
        updateTime.setText(onlineMusiclistcallback.getBillboard().getUpdate_date());
        content.setText(onlineMusiclistcallback.getBillboard().getComment());

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
                } else if (offset == 0) {
                    InitHeader(HeaderView);
                }
                if (response == null || response.getSong_list() == null || response.getSong_list().size() == 0) {
                    return;
                }
                onlineMusics.addAll(response.getSong_list());
                onlineMusicAdapter.notifyDataSetChanged();
                String title = onlineMusiclistcallback.getBillboard().getName();
                CloseProgress();
                onlineMusicList.setVisibility(View.VISIBLE);
                sheetTitle.setText(title);
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

    /*


     Handler handler = new Handler()
     {
         @Override
         public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case ABC:
                    Bundle data = msg.getData();
                    String val = data.getString("value");
                    Log.d("歌曲长度",val);
                    totaltime = Integer.parseInt(val);
                break;
            }

         }
     };
    private void getLenth(final String Url)
    {
         new Thread(new Runnable() {
             @Override
             public void run() {
                 OkHttpClient ok = new OkHttpClient();
                 Request request = new Request.Builder().url(Url).build();
                 Response response = null;
                 try {
                     response = ok.newCall(request).execute();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
                 if (response!=null&& response.isSuccessful())
                 {
                     long contentLength = response.body().contentLength();
                     Message msg = new Message();
                     msg.what = ABC;
                     Bundle data = new Bundle();
                     data.putString("value", "154632");
                     msg.setData(data);
                     handler.sendMessage(msg);
                 }
             }
         });
    }
  */


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
    public void onBackPressed() {
        super.onBackPressed();
    }
}
