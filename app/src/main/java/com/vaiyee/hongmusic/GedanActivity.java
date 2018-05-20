package com.vaiyee.hongmusic;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vaiyee.hongmusic.Adapter.GedanListAdapter;
import com.vaiyee.hongmusic.Utils.GedanShouCangUtil;
import com.vaiyee.hongmusic.Utils.HttpClinet;
import com.vaiyee.hongmusic.bean.Gedan;
import com.vaiyee.hongmusic.bean.GedanList;
import com.vaiyee.hongmusic.bean.ShouCangGeDan;
import com.vaiyee.hongmusic.http.HttpCallback;
import com.vaiyee.hongmusic.util.Annotation;
import com.vaiyee.hongmusic.util.BindOnclick;
import com.vaiyee.hongmusic.util.BindView;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class GedanActivity extends SwipeBackActivity{

    private String title,img;
    private GradualTextView gtv;
    private ImageView bg;
    private RecyclerView recyclerView;
    private TextView time,total;
    private List<GedanList.Info> gedanlist = new ArrayList<>();
    private GedanListAdapter adapter;
    public RelativeLayout relativeLayout;
    public static Button cancel,start;
    @BindView(R.id.shoucang)
    private FloatingActionButton shoucang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT>=21)
        {
            View decorview = getWindow().getDecorView();
            decorview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_gedan);
        Annotation.bind(this);
        Intent intent = getIntent();
        recyclerView = (RecyclerView) findViewById(R.id.gedan_list);
        relativeLayout = (RelativeLayout) findViewById(R.id.bottom_pannel);
        cancel = (Button) findViewById(R.id.quxiao);
        start = (Button) findViewById(R.id.start_download);
        title = intent.getStringExtra("title");
        img = intent.getStringExtra("img").replace("{size}","150");
        String comment = intent.getStringExtra("comment");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout collapsingToolbarLayout = (net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout) findViewById(R.id.collasping_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true); //显示返回按钮
        }
        collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#00F5FF"));
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.BLACK);
        collapsingToolbarLayout.setTitle(title);   //设置标题内容
        shoucang.setVisibility(View.VISIBLE);
        time = (TextView) findViewById(R.id.publish_time);
        total = (TextView) findViewById(R.id.total);
        time.setText(intent.getStringExtra("time"));
        bg = (ImageView) findViewById(R.id.gedan_bg);
        gtv = (GradualTextView) findViewById(R.id.comment);
        gtv.setText(comment);          //显示歌单描述
        Glide.with(this)
                .load(img)
                .dontAnimate()
                .placeholder(R.drawable.default_cover)
                .error(R.drawable.default_cover)
                .crossFade(1000)
                .bitmapTransform(new BlurTransformation(MyApplication.getQuanjuContext(),20,2))  // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .into(bg);
        switch (intent.getStringExtra("type"))
        {
            case "gedan":
                getSonglist(intent.getStringExtra("id"));      //歌单的ID
                break;
            case "ablum":
                getAblumSonglist(String.valueOf(intent.getIntExtra("id",0)));  //专辑的ID
                break;
        }
    }

    private void getAblumSonglist(String ablumid) {
          HttpClinet.getAblumSongList(ablumid, new HttpCallback<GedanList>() {
              @Override
              public void onSuccess(GedanList response) {
                  gedanlist.addAll(response.data.infoList);
                  String i = String.valueOf(response.data.total);
                  total.setText("本专辑共有 "+i+" 首歌");
                  adapter = new GedanListAdapter(GedanActivity.this,gedanlist,GedanActivity.this);
                  LinearLayoutManager manager = new LinearLayoutManager(GedanActivity.this);
                  recyclerView.setLayoutManager(manager);
                  recyclerView.setAdapter(adapter);
                  adapter.setListener(new GedanListAdapter.ShowBottonPal() {
                      @Override
                      public void show() {
                          relativeLayout.setVisibility(View.VISIBLE);
                      }
                  });
              }

              @Override
              public void onFail(Exception e) {
                  Toast.makeText(GedanActivity.this,"获取歌单失败，请检查网络重试",Toast.LENGTH_LONG).show();
              }
          });
    }


    //根据传过来的歌单ID获取队对应的歌曲列表
    private void getSonglist(String id) {
        HttpClinet.getKugouGedanList(id, new HttpCallback<GedanList>() {
            @Override
            public void onSuccess(GedanList response) {
                gedanlist.addAll(response.data.infoList);
                String i = String.valueOf(response.data.total);
                total.setText("本歌单共有 "+i+" 首歌");
                adapter = new GedanListAdapter(GedanActivity.this,gedanlist,GedanActivity.this);
                LinearLayoutManager manager = new LinearLayoutManager(GedanActivity.this);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);
                adapter.setListener(new GedanListAdapter.ShowBottonPal() {
                    @Override
                    public void show() {
                        relativeLayout.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(GedanActivity.this,"获取歌单失败，请检查网络重试",Toast.LENGTH_LONG).show();
            }
        });
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


    @BindOnclick(R.id.shoucang)
    private void ShouCang()
    {
        String id = getIntent().getStringExtra("id");
        String comment = getIntent().getStringExtra("comment");
        String time = getIntent().getStringExtra("time");
        ShouCangGeDan shouCangGeDan = new ShouCangGeDan();
        shouCangGeDan.setId(id);
        shouCangGeDan.setComment(comment);
        shouCangGeDan.setImg(img);
        shouCangGeDan.setTime(time);
        shouCangGeDan.setTitle(title);
        GedanShouCangUtil util = new GedanShouCangUtil(GedanActivity.this,shouCangGeDan);
        util.setGedanList();
        Toast.makeText(GedanActivity.this,"已成功收藏该歌单",Toast.LENGTH_LONG).show();
    }
    @BindOnclick(R.id.quxiao)
    private void CancelDuoxuan()
    {
        GedanListAdapter.duoxuan = false;
        adapter.notifyDataSetChanged();
        relativeLayout.setVisibility(View.GONE);
    }

    @BindOnclick(R.id.start_download)
    private void startDownload()
    {
        adapter.startDownload();
        GedanListAdapter.duoxuan = false;
        adapter.notifyDataSetChanged();
        relativeLayout.setVisibility(View.GONE);
        Toast.makeText(this,"下载中...",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        GedanListAdapter.duoxuan = false;  //按返回键的时候重置为false，为了下一次进入不是显示多选状态
        super.onDestroy();
    }

}
