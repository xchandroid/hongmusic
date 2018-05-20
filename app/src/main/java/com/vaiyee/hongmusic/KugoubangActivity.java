package com.vaiyee.hongmusic;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vaiyee.hongmusic.Adapter.KugouBangAdapter;
import com.vaiyee.hongmusic.Adapter.OnlineMusicAdapter;
import com.vaiyee.hongmusic.Utils.HttpClinet;
import com.vaiyee.hongmusic.Utils.NetUtils;
import com.vaiyee.hongmusic.bean.KugouBang;
import com.vaiyee.hongmusic.bean.KugouBangList;
import com.vaiyee.hongmusic.bean.KugouMusic;
import com.vaiyee.hongmusic.bean.OnlineMusic;
import com.vaiyee.hongmusic.bean.OnlineMusiclist;
import com.vaiyee.hongmusic.bean.Sheet;
import com.vaiyee.hongmusic.bean.Song;
import com.vaiyee.hongmusic.http.HttpCallback;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class KugoubangActivity extends SwipeBackActivity{
    private RecyclerView onlineMusicList;
    private Sheet sheet;//榜单列表
    private LinearLayout footer;
    private List<KugouBangList> list = new ArrayList<>();
    private KugouBang kugouBang;
    private KugouBangAdapter adapter;
    private ProgressDialog progressDialog;
    private ImageView Cover;
    private GradualTextView content;
    private net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout collapsingToolbarLayout;
    private static int duration,lastVisibleItem = 0,page = 0;
    private boolean isFirstopen = true;
    private LinearLayoutManager manager;
    private boolean loading = false;   //是否正在加载的标志位，防止内存泄漏程序崩溃

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
        sheet = (Sheet) intent.getSerializableExtra("Kugou");    //获取传过来的intent中的Sheet对象
        Cover = (ImageView) findViewById(R.id.gedan_bg);
        content = (GradualTextView) findViewById(R.id.comment);
        onlineMusicList = (RecyclerView) findViewById(R.id.gedan_list);
        adapter = new KugouBangAdapter(KugoubangActivity.this,R.layout.localmusi_listitem,list,KugoubangActivity.this);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(KugoubangActivity.this);
        onlineMusicList.setLayoutManager(linearLayoutManager);
        onlineMusicList.setAdapter(adapter);
        ShowProgress();
        page =1;
        getKugouBang(sheet);
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
        footer = (LinearLayout) findViewById(R.id.loading_footer);
        onlineMusicList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) //向下滚动
                {
                    manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int visibleItemCount = manager.getChildCount();	//得到显示屏幕内显示的item数量
                    int totalItemCount = manager.getItemCount();	//得到item的总数量
                    int pastVisiblesItems = manager.findFirstVisibleItemPosition();//得到显示屏内的第一个可见item的位置索引

                    if (!loading && (visibleItemCount + pastVisiblesItems) >= totalItemCount-1) {     //判断是否划到底部
                        loading = true;    //将标志位设置为true,表示正在加载更多，防止滑动到底部时无限调用loadMoreDate()导致程序崩溃
                        page+=1;
                        //footer.setVisibility(View.VISIBLE);            显示正在加载会卡顿，没有Listview的footerview体验好
                        getKugouBang(sheet);
                    }
                }
            }
        });
    }



    //初始化CollapsingToolbarLayout内容
    private void InitHeader()
    {

        String img = kugouBang.info.imgurl.replace("{size}","150");
        Glide.with(KugoubangActivity.this)
                .load(img)
                .placeholder(R.drawable.default_cover)
                .crossFade(1000)
                .bitmapTransform(new BlurTransformation(KugoubangActivity.this,20,3))
                .into(Cover);

        collapsingToolbarLayout.setTitle(kugouBang.info.rankname);
        content.setText(kugouBang.info.intro);

    }

    private void getKugouBang(Sheet sheet)
    {
        HttpClinet.getKugoubang(sheet.getType(), page, new HttpCallback<KugouBang>() {
            @Override
            public void onSuccess(KugouBang mkugouBang) {
                kugouBang =mkugouBang;
                list.addAll(kugouBang.song.kugouBangList);
                adapter.notifyDataSetChanged();
                Log.d("page的值是",String.valueOf(page));
                loading = false;
                //footer.setVisibility(View.GONE);            显示正在加载会卡顿，没有Listview的footerview体验好
                if (isFirstopen)
                {
                    InitHeader();
                    CloseProgress();
                    isFirstopen = false;
                }
            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(KugoubangActivity.this,"获取数据失败",Toast.LENGTH_LONG).show();
                CloseProgress();
            }
        });
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

    @Override
    protected void onStop() {
        super.onStop();
        page = 1;
    }
}
