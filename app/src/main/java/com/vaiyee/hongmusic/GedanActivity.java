package com.vaiyee.hongmusic;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vaiyee.hongmusic.Adapter.GedanListAdapter;
import com.vaiyee.hongmusic.Utils.HttpClinet;
import com.vaiyee.hongmusic.bean.GedanList;
import com.vaiyee.hongmusic.http.HttpCallback;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class GedanActivity extends AppCompatActivity {

    private String title,img;
    private GradualTextView gtv;
    private ImageView bg;
    private RecyclerView recyclerView;
    private TextView time,total;
    private List<GedanList.Info> gedanlist = new ArrayList<>();
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
        Intent intent = getIntent();
        recyclerView = findViewById(R.id.gedan_list);
        title = intent.getStringExtra("title");
        img = intent.getStringExtra("img").replace("{size}","150");
        String comment = intent.getStringExtra("comment");
        Toolbar toolbar = findViewById(R.id.toolbar);
        net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collasping_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true); //显示返回按钮
        }
        collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#00F5FF"));
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.BLACK);
        collapsingToolbarLayout.setTitle(title);   //设置标题内容
        time = findViewById(R.id.publish_time);
        total = findViewById(R.id.total);
        time.setText(intent.getStringExtra("time"));
        bg = findViewById(R.id.gedan_bg);
        gtv = findViewById(R.id.comment);
        gtv.setText(comment);          //显示歌单描述
        Glide.with(this)
                .load(img)
                .dontAnimate()
                .placeholder(R.drawable.default_cover)
                .error(R.drawable.default_cover)
                .crossFade(1000)
                .bitmapTransform(new BlurTransformation(MyApplication.getQuanjuContext(),20,2))  // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .into(bg);
        getSonglist(intent.getStringExtra("id"));
    }


    //根据传过来的歌单ID获取队对应的歌曲列表
    private void getSonglist(String id) {
        HttpClinet.getKugouGedanList(id, new HttpCallback<GedanList>() {
            @Override
            public void onSuccess(GedanList response) {
                gedanlist.addAll(response.list1.list2.gedanlist);
                String i = String.valueOf(response.list1.list2.total);
                total.setText("本歌单共有 "+i+" 首歌");
                GedanListAdapter adapter = new GedanListAdapter(GedanActivity.this,gedanlist,GedanActivity.this);
                LinearLayoutManager manager = new LinearLayoutManager(GedanActivity.this);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFail(Exception e) {

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
}
