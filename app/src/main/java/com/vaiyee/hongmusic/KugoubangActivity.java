package com.vaiyee.hongmusic;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.vaiyee.hongmusic.Adapter.KugouBangAdapter;
import com.vaiyee.hongmusic.Utils.HttpClinet;
import com.vaiyee.hongmusic.Utils.NetUtils;
import com.vaiyee.hongmusic.bean.KugouBang;
import com.vaiyee.hongmusic.bean.KugouBangList;
import com.vaiyee.hongmusic.bean.KugouMusic;
import com.vaiyee.hongmusic.bean.Sheet;
import com.vaiyee.hongmusic.http.HttpCallback;

import java.util.ArrayList;
import java.util.List;

public class KugoubangActivity extends AppCompatActivity {
    private Sheet sheet;
    private TextView sheetTitle;
    private ImageView back;
    private KugouBang kugouBang;
    private ProgressDialog progressDialog;
    private ListView onlineMusicList;
    private View HeaderView,footerView;
    private List<KugouBangList> list = new ArrayList<>();
    private KugouBangAdapter adapter;
    private static String geshou,geming,coverUrl,lrc;
    private static int duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_music);
        Init();
        onlineMusicList = findViewById(R.id.online_music_list);
        onlineMusicList.setVisibility(View.GONE);
        ShowProgress();
        getKugouBang(sheet);
        HeaderView = LayoutInflater.from(KugoubangActivity.this).inflate(R.layout.activity_online_music_list_header,null);
        footerView = LayoutInflater.from(KugoubangActivity.this).inflate(R.layout.online_musiclist_footer,null);
        onlineMusicList.addHeaderView(HeaderView);
        onlineMusicList.addFooterView(footerView);
        adapter = new KugouBangAdapter(KugoubangActivity.this,R.layout.localmusi_listitem,list,this);
        onlineMusicList.setAdapter(adapter);
        onlineMusicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                KugouBangList song = list.get(i-1);
                String[] s = song.filename.split("-");
                String hash = song.hash;
                geming = s[1];
                geshou = s[0];
                duration = song.duration*1000;
                getSongUrl(hash);
            }
        });
    }

    private void getSongUrl(final String hash) {
        AlertDialog.Builder builder;
        switch (NetUtils.getNetType()) {
            case NET_WIFI:
                HttpClinet.KugouUrl(hash, new HttpCallback<KugouMusic>() {
                    @Override
                    public void onSuccess(KugouMusic kugouMusic) {
                        String path = kugouMusic.getData().getPlay_url();
                        if (path != null) {
                            Log.d("歌曲地址是", path);
                            PlayMusic playMusic = new PlayMusic();
                            playMusic.play(path, 0);
                            coverUrl = kugouMusic.getData().getImg();
                            lrc = kugouMusic.getData().getLyrics();
                            SearchActivity.creatLrc(lrc, geming);
                            MainActivity mainActivity = new MainActivity();
                            mainActivity.tongbuShow(geming, geshou, coverUrl, duration, MainActivity.ONLINE);
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        CloseProgress();
                        Toast.makeText(KugoubangActivity.this, "获取在线音乐失败！请检查网络设置", Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case NET_4G:
                builder = new AlertDialog.Builder(KugoubangActivity.this);
                builder.setMessage("当前正在使用4G网络，是否使用数据流量播放在线音乐？");
                builder.setTitle("提示");
                builder.setIcon(R.drawable.tip);
                builder.setPositiveButton("流量多",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                HttpClinet.KugouUrl(hash, new HttpCallback<KugouMusic>() {
                                    @Override
                                    public void onSuccess(KugouMusic kugouMusic) {
                                        String path = kugouMusic.getData().getPlay_url();
                                        if (path != null) {
                                            Log.d("歌曲地址是", path);
                                            PlayMusic playMusic = new PlayMusic();
                                            playMusic.play(path, 0);
                                            coverUrl = kugouMusic.getData().getImg();
                                            lrc = kugouMusic.getData().getLyrics();
                                            SearchActivity.creatLrc(lrc, geming);
                                            MainActivity mainActivity = new MainActivity();
                                            mainActivity.tongbuShow(geming, geshou, coverUrl, duration, MainActivity.ONLINE);
                                        }
                                    }

                                    @Override
                                    public void onFail(Exception e) {
                                        CloseProgress();
                                        Toast.makeText(KugoubangActivity.this, "获取在线音乐失败！请检查网络设置", Toast.LENGTH_LONG).show();
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
                builder = new AlertDialog.Builder(KugoubangActivity.this);
                builder.setMessage("当前正在使用3G网络，是否使用数据流量播放在线音乐？");
                builder.setTitle("提示");
                builder.setIcon(R.drawable.tip);
                builder.setPositiveButton("流量多",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                HttpClinet.KugouUrl(hash, new HttpCallback<KugouMusic>() {
                                    @Override
                                    public void onSuccess(KugouMusic kugouMusic) {
                                        String path = kugouMusic.getData().getPlay_url();
                                        if (path != null) {
                                            Log.d("歌曲地址是", path);
                                            PlayMusic playMusic = new PlayMusic();
                                            playMusic.play(path, 0);
                                            coverUrl = kugouMusic.getData().getImg();
                                            lrc = kugouMusic.getData().getLyrics();
                                            SearchActivity.creatLrc(lrc, geming);
                                            MainActivity mainActivity = new MainActivity();
                                            mainActivity.tongbuShow(geming, geshou, coverUrl, duration, MainActivity.ONLINE);
                                        }
                                    }

                                    @Override
                                    public void onFail(Exception e) {
                                        CloseProgress();
                                        Toast.makeText(KugoubangActivity.this, "获取在线音乐失败！请检查网络设置", Toast.LENGTH_LONG).show();
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
                builder = new AlertDialog.Builder(KugoubangActivity.this);
                builder.setMessage("当前正在使用2G网络，缓冲较慢，确定是否播放在线音乐？");
                builder.setTitle("提示");
                builder.setIcon(R.drawable.tip);
                builder.setPositiveButton("流量多",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                HttpClinet.KugouUrl(hash, new HttpCallback<KugouMusic>() {
                                    @Override
                                    public void onSuccess(KugouMusic kugouMusic) {
                                        String path = kugouMusic.getData().getPlay_url();
                                        if (path != null) {
                                            Log.d("歌曲地址是", path);
                                            PlayMusic playMusic = new PlayMusic();
                                            playMusic.play(path, 0);
                                            coverUrl = kugouMusic.getData().getImg();
                                            lrc = kugouMusic.getData().getLyrics();
                                            SearchActivity.creatLrc(lrc, geming);
                                            MainActivity mainActivity = new MainActivity();
                                            mainActivity.tongbuShow(geming, geshou, coverUrl, duration, MainActivity.ONLINE);
                                        }
                                    }

                                    @Override
                                    public void onFail(Exception e) {
                                        CloseProgress();
                                        Toast.makeText(KugoubangActivity.this, "获取在线音乐失败！请检查网络设置", Toast.LENGTH_LONG).show();
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

        }
    }

    private void getKugouBang(Sheet sheet)
    {
        HttpClinet.getKugoubang(sheet.getType(), 1, new HttpCallback<KugouBang>() {
            @Override
            public void onSuccess(KugouBang mkugouBang) {
                kugouBang =mkugouBang;
                CloseProgress();
                list.addAll(kugouBang.song.kugouBangList);
                adapter.notifyDataSetChanged();
                InitHeader(HeaderView);
                sheetTitle.setText(kugouBang.info.rankname);
                onlineMusicList.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(KugoubangActivity.this,"获取数据失败",Toast.LENGTH_LONG).show();
            }
        });
    }

    //获取传过来的intent中的Sheet对象
    private void Init()
    {
        Intent intent =getIntent();
        sheet = (Sheet) intent.getSerializableExtra("Kugou");
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

        String img = kugouBang.info.imgurl.replace("{size}","150");
        Glide.with(KugoubangActivity.this)
                .load(img)
                .placeholder(R.drawable.default_cover)
                .into(Cover);

        title.setText(kugouBang.info.rankname);
        //updateTime.setText(kugouBang.info.intro);
        content.setText(kugouBang.info.intro);

    }



    //显示正在加载数据对话框
    private void ShowProgress()
    {
        if (progressDialog==null)
        {
            progressDialog = new ProgressDialog(KugoubangActivity.this);
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
