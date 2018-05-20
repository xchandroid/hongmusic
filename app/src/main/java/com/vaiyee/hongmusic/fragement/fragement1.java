package com.vaiyee.hongmusic.fragement;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.vaiyee.hongmusic.ColorTrackView;
import com.vaiyee.hongmusic.MainActivity;
import com.vaiyee.hongmusic.MyApplication;
import com.vaiyee.hongmusic.PlayMusic;
import com.vaiyee.hongmusic.R;
import com.vaiyee.hongmusic.SearchActivity;
import com.vaiyee.hongmusic.Utils.HttpClinet;
import com.vaiyee.hongmusic.Utils.NetUtils;
import com.vaiyee.hongmusic.WebActivity;
import com.vaiyee.hongmusic.bean.Banner;
import com.vaiyee.hongmusic.bean.KugouMusic;
import com.vaiyee.hongmusic.bean.KugouSearchResult;
import com.vaiyee.hongmusic.bean.Song;
import com.vaiyee.hongmusic.Utils.getAudio;
import com.vaiyee.hongmusic.Adapter.songsAdapter;
import com.vaiyee.hongmusic.http.HttpCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/29.
 */

public class fragement1 extends Fragment {
    private static RecyclerView recyclerView;
    public static List<Song> songs;
    public static String coverUrl,geming,geshou;
    public static songsAdapter adapter;
    private List<Song> songList;
    private TextView tips;
    private RollPagerView rollPagerView;
    private  String[]imgs = new String[8];


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment1_layout,null);
        tips = view.findViewById(R.id.tips);
        //hearder = LayoutInflater.from(getContext()).inflate(R.layout.roll_pager_view,null);  //这里不要把ViewGroup container作为参数，否则造成布局参数错误导致崩溃
        recyclerView = view.findViewById(R.id.localmusic_list);
        //listView.addHeaderView(hearder);
        rollPagerView = view.findViewById(R.id.rollview);
        // 申请权限
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
        else
        {
           updateSonglist();  //已授过权了直接扫描音乐
        }

/*
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {


                switch (i)
                {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE: //滑动停止
                        // MainActivity.linearLayout.setVisibility(View.VISIBLE);
                     adapter.setScrollState(false);
                        //当前屏幕中listview的子项的个数
                        int count = absListView.getChildCount();
                        for (int j = 0; j < count; j++) {
                            //获取到item的图片显示的Imageview控件
                            ImageView iv_show = (ImageView) absListView.getChildAt(j).findViewById(R.id.zj_id);
                            if (iv_show != null)
                            {
                                if (!iv_show.getTag().equals("1")) {//如果等于1说明图片资源已加载过，不等于说明没有去getTag()的图片url

                                    //直接从Tag中取出我们存储的数据image——url
                                    String image_url = iv_show.getTag().toString();
                                    if (image_url != null) {//这个判断是防止图片的url是否为空，为空的话给默认图片。
                                        iv_show.setImageBitmap(songsAdapter.setArtwork(getContext(), image_url));
                                        //设置为已加载过数据
                                        iv_show.setTag("1");
                                    } else {
                                        iv_show.setImageResource(R.drawable.music_ic);
                                        iv_show.setTag("1");
                                    }

                                }
                        }
                        }

                    break;

                    //滚动做出了抛的动作
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                    {
                        //设置为正在滚动
                        adapter.setScrollState(true);
                        //MainActivity.linearLayout.setVisibility(View.GONE);
                        break;
                    }
                    //正在滚动
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    {
                        //设置为正在滚动
                        adapter.setScrollState(true);
                        //MainActivity.linearLayout.setVisibility(View.GONE);
                        break;
                    }
                    default:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
        *
        *
        *
        *
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final Song song = songs.get(i);
                geming = song.getTitle().toString();
                geshou = song.getSinger().toString();
                PlayMusic playMusic = new PlayMusic();
                final String path = song.getFileUrl();
                playMusic.play(path,i);
                getLrc(geming,song,i);
                songList = new ArrayList<>();
                songList.addAll(songs);
                PlayMusic.PlayList playList = new PlayMusic.PlayList();
                playList.setPlaylist(songList);
                playList.setBang(0);
            }
        });
        */
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case 2:
                if (grantResults.length>0&&grantResults[0] ==PackageManager.PERMISSION_GRANTED)
                {
                    updateSonglist();
                    Toast.makeText(getContext(),"授权通过",Toast.LENGTH_LONG).show();
                    tips.setVisibility(View.GONE);
                }
                else
                {
                    Toast.makeText(getContext(),"拒绝授权将不能扫描本地音乐",Toast.LENGTH_LONG).show();
                    tips.setVisibility(View.VISIBLE);
                }
        }
    }

    public static void getLrc(final String geming, final Song song, final int i) {
        switch (NetUtils.getNetType())
        {
            case NET_NONE:
                int time = song.getDuration();
                MainActivity mainActivity = new MainActivity();
                mainActivity.tongbuShow(geming,song.getSinger(),coverUrl,time,MainActivity.LOCAL);
                Toast.makeText(MyApplication.getQuanjuContext(),"获取歌手写真失败，请检查网络再试",Toast.LENGTH_LONG).show();
                return;
        }
        HttpClinet.KugouSearch(geming, 5, new HttpCallback<KugouSearchResult>() {
            @Override
            public void onSuccess(KugouSearchResult kugouSearchResult) {
                if (kugouSearchResult==null)
                {
                    int time = song.getDuration();
                    MainActivity mainActivity = new MainActivity();
                    mainActivity.tongbuShow(geming,song.getSinger(),coverUrl,time,MainActivity.LOCAL);
                    Toast.makeText(MyApplication.getQuanjuContext(),"获取歌词失败，请检查网络再试",Toast.LENGTH_LONG).show();
                    return;
                }
                List<KugouSearchResult.lists> resultList= kugouSearchResult.getResultList();

                String hash = resultList.get(0).getFileHash();
                HttpClinet.KugouUrl(hash, new HttpCallback<KugouMusic>() {
                    @Override
                    public void onSuccess(KugouMusic kugouMusic) {
                        String lrc = kugouMusic.getData().getLyrics();
                        SearchActivity.creatLrc(lrc,geming);
                        coverUrl = kugouMusic.getData().getImg();
                        int time = song.getDuration();
                        //PlayMusic playMusic = new PlayMusic();
                       // playMusic.play(path,i);
                        MainActivity mainActivity = new MainActivity();
                        mainActivity.tongbuShow(geming,song.getSinger(),coverUrl,time,MainActivity.LOCAL);
                    }

                    @Override
                    public void onFail(Exception e) {
                        int time = song.getDuration();
                        MainActivity mainActivity = new MainActivity();
                        mainActivity.tongbuShow(geming,song.getSinger(),coverUrl,time,MainActivity.LOCAL);
                        Toast.makeText(MyApplication.getQuanjuContext(),"获取歌词失败，请检查网络再试",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }




    //刷新音乐列表
    public void updateSonglist()
    {
       // getAudio.updateMedia();
        songs = getAudio.getAllSongs(MyApplication.getQuanjuContext()) ;
        adapter = new songsAdapter(MyApplication.getQuanjuContext(),R.layout.localmusi_listitem,songs,getActivity());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        HttpClinet.getBanner(new HttpCallback<Banner>() {
            @Override
            public void onSuccess(Banner banner) {
                final List<Banner.DataBean.InfoBean> bannerlist = banner.bean.getInfo();
                for (int i=0;i<bannerlist.size();i++)
                {
                    imgs[i] = bannerlist.get(i).getImg();
                }

                //设置每个图片的切换时间
                rollPagerView.setPlayDelay(3000);
                //设置图片切换动画时间
                rollPagerView.setAnimationDurtion(500);
                //设置指示器:
                //rollPV.setHintView(new IconHintView());
                //rollPV.setHintView(new IconHintView(this,R.mipmap.ic_launcher,R.mipmap.ic_launcher));
                rollPagerView.setHintView(new ColorPointHintView(getContext(),
                        getResources().getColor(R.color.blue),
                        Color.WHITE));
                //设置适配器
                rollPagerView.setAdapter(new RollPagerAdapter());            //获取图片url成功后再设置Adapter，防止空对象引用

                //设置每一个图片的点击事件
                rollPagerView.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {

                        Intent intent = new Intent(getContext(), WebActivity.class);
                        intent.putExtra("URL",bannerlist.get(position).getExtra().getUrl());
                        intent.putExtra("title",bannerlist.get(position).getTitle());
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(getContext(),"获取首页推荐失败",Toast.LENGTH_LONG).show();
            }
        });
    }


    //适配器
    private class RollPagerAdapter extends StaticPagerAdapter {

        @Override
        public View getView(ViewGroup container, int position) {

            ImageView view=new ImageView(getContext());
            //设置图片资源
            Glide.with(fragement1.this)
                    .load(imgs[position])
                    .dontAnimate()//防止设置placeholder导致第一次不显示网络图片,只显示默认图片的问题
                    .placeholder(R.drawable.default_cover)
                    .error(R.drawable.default_cover)
                    .into(view);
            //设置高度和宽度
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            //设置拉伸方式
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);  //以中心拉伸填充控件
            return view;
        }

        @Override
        public int getCount() {
            return imgs.length;
        }
    }


}
