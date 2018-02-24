package com.vaiyee.hongmusic.fragement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vaiyee.hongmusic.ColorTrackView;
import com.vaiyee.hongmusic.MainActivity;
import com.vaiyee.hongmusic.MyApplication;
import com.vaiyee.hongmusic.PlayMusic;
import com.vaiyee.hongmusic.R;
import com.vaiyee.hongmusic.SearchActivity;
import com.vaiyee.hongmusic.Utils.HttpClinet;
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

    private TextView textView;
  private ListView listView;
 private static List<Song> songs;
 private  List<ColorTrackView> mTabs = new ArrayList<ColorTrackView>();
 private ColorTrackView t,tt;
public static String coverUrl,geming;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1_layout,null);
       // textView = view.findViewById(R.id.textView4);
        songs =  getAudio.getAllSongs(MyApplication.getQuanjuContext());
        listView = view.findViewById(R.id.localmusic_list);
        final songsAdapter adapter = new songsAdapter(getContext(),R.layout.localmusi_listitem,songs);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {


                switch (i)
                {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE: //滑动停止
                     adapter.setScrollState(false);
                        //当前屏幕中listview的子项的个数
                        int count = absListView.getChildCount();
                        for (int j = 0; j < count; j++) {
                            //获取到item的图片显示的Imageview控件
                            ImageView iv_show= (ImageView) absListView.getChildAt(j).findViewById(R.id.zj_id);
                            if (!iv_show.getTag().equals("1")){//如果等于1说明图片资源已加载过，不等于说明没有去getTag()的图片url

                                //直接从Tag中取出我们存储的数据image——url
                                String image_url = iv_show.getTag().toString();
                                if (image_url != null) {//这个判断是防止图片的url是否为空，为空的话给默认图片。
                                    iv_show.setImageBitmap(songsAdapter.setArtwork(getContext(),image_url));
                                    //设置为已加载过数据
                                    iv_show.setTag("1");
                                } else {
                                    iv_show.setImageResource(R.drawable.music_ic);
                                    iv_show.setTag("1");
                                }

                            }
                        }

                    break;

                    //滚动做出了抛的动作
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                    {
                        //设置为正在滚动
                        adapter.setScrollState(true);
                        break;
                    }
                    //正在滚动
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    {
                        //设置为正在滚动
                        adapter.setScrollState(true);
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
        /*
        View view1 = inflater.inflate(R.layout.localmusi_listitem,null);
        t= view1.findViewById(R.id.geming);
        tt=view1.findViewById(R.id.geshou);
        mTabs.add(t);
        mTabs.add(tt);

        /*
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            private SparseArray recordSp = new SparseArray(0);
            private int mCurrentfirstVisibleItem = 0;
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch(i){
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://空闲状态

                  break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING://滚动状态

                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://触摸后滚动

                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mCurrentfirstVisibleItem = firstVisibleItem;
                View firstView = absListView.getChildAt(0);
                if (null != firstView) {
                    ItemRecod itemRecord = (ItemRecod) recordSp.get(firstVisibleItem);
                    if (null == itemRecord) {
                        itemRecord = new ItemRecod();
                    }
                    itemRecord.height = firstView.getHeight();
                    itemRecord.top = firstView.getTop();
                    recordSp.append(firstVisibleItem, itemRecord);

                }
                int h = getScrollY();
                ColorTrackView left = mTabs.get(0);
                ColorTrackView right = mTabs.get(1);

                left.setDirection(2);
                right.setDirection(3);
                //Log.e("TAG", positionOffset+"");
                left.setProgress(1 - h);
                right.setProgress(h);

            }


            private int getScrollY() {
                int height = 0;
                for (int i = 0; i < mCurrentfirstVisibleItem; i++) {
                    ItemRecod itemRecod = (ItemRecod) recordSp.get(i);
                    height += itemRecod.height;
                }
                ItemRecod itemRecod = (ItemRecod) recordSp.get(mCurrentfirstVisibleItem);
                if (null == itemRecod) {
                    itemRecod = new ItemRecod();
                }
                return height - itemRecod.top;
            }


            class ItemRecod {
                int height = 0;
                int top = 0;
            }
        });
*/

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final Song song = songs.get(i);
                geming = song.getTitle().toString();
                getLrc(geming,song,i);
            }
        });
        return view;
    }

    private void getLrc(final String geming, final Song song, final int i) {
        HttpClinet.KugouSearch(geming, 5, new HttpCallback<KugouSearchResult>() {
            @Override
            public void onSuccess(KugouSearchResult kugouSearchResult) {
                List<KugouSearchResult.lists> resultList= kugouSearchResult.getResultList();
                if (resultList==null)
                {
                    return;
                }
                String hash = resultList.get(0).getFileHash();
                HttpClinet.KugouUrl(hash, new HttpCallback<KugouMusic>() {
                    @Override
                    public void onSuccess(KugouMusic kugouMusic) {
                        String lrc = kugouMusic.getData().getLyrics();
                        SearchActivity.creatLrc(lrc,geming);
                        coverUrl = kugouMusic.getData().getImg();
                        String geshou = song.getSinger().toString();
                        final String path = song.getFileUrl();
                        int time = song.getDuration();
                        PlayMusic playMusic = new PlayMusic();
                        playMusic.play(path,i);
                        MainActivity mainActivity = new MainActivity();
                        mainActivity.tongbuShow(geming,geshou,coverUrl,time,MainActivity.LOCAL);
                        mainActivity.setpause();
                    }

                    @Override
                    public void onFail(Exception e) {

                    }
                });
            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //textView.setText("sad防护镜撒返回带回家的");
    }

}
