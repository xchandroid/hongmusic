package com.vaiyee.hongmusic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.vaiyee.hongmusic.Adapter.AblumAdapter;
import com.vaiyee.hongmusic.Adapter.GedanListAdapter;
import com.vaiyee.hongmusic.Adapter.GeshouMvAdapter;
import com.vaiyee.hongmusic.Utils.HttpClinet;
import com.vaiyee.hongmusic.bean.Ablum;
import com.vaiyee.hongmusic.bean.GedanList;
import com.vaiyee.hongmusic.bean.GeshouMv;
import com.vaiyee.hongmusic.bean.SingerInfo;
import com.vaiyee.hongmusic.http.HttpCallback;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import io.vov.vitamio.utils.Log;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class GeshouInfoActivity extends SwipeBackActivity implements View.OnClickListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView img;
    private RecyclerView recyclerView,ablumList,mvlist;
    private LayoutInflater inflater;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private List<View> mViewList = new ArrayList<>();//页卡视图集合
    private View view1,view2,view3,view4;
    private List<GedanList.Info> songlist = new ArrayList<>();
    private List<Ablum.DataBean.InfoBean> ablumlist = new ArrayList<>();
    private List<GeshouMv.DataBean.InfoBean> mvliist = new ArrayList<>();
    private SwipeRefreshLayout refreshLayout;
    private TextView fans,info,html,hot,neww;
    private String name,singerid;
    private LinearLayout l1,l2,l3,l4;
    private GedanListAdapter adapter;
    private AblumAdapter ablumAdapter;
    private GeshouMvAdapter mvAdapter;
    private boolean isHot = true,isNew = false;
    private boolean isDanqu = false,isZhuanji = false,isMV = false,isXiangqing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT>=21)
        {
            View decorview = getWindow().getDecorView();
            decorview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_geshou_info);
        adapter = new GedanListAdapter(this,songlist,this);
        ablumAdapter = new AblumAdapter(this,ablumlist);
        mvAdapter = new GeshouMvAdapter(this,mvliist);
        viewPager = (ViewPager) findViewById(R.id.vp_view);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        img = (ImageView) findViewById(R.id.photo);
        img.setOnClickListener(this);
        fans = (TextView) findViewById(R.id.fans);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        refreshLayout.setEnabled(false);       //禁止SwipeRefreshLayout下拉
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
        collapsingToolbarLayout.setTitle(getIntent().getStringExtra("name"));   //设置标题歌手名字
        name = getIntent().getStringExtra("name");
        String imgurl = getIntent().getStringExtra("img");
        fans.setText("粉丝："+getIntent().getStringExtra("fans"));
        singerid = String.valueOf(getIntent().getIntExtra("id",0));
        loadIntoUseFitWidth(this,imgurl.replace("{size}","400"),R.drawable.default_artist,img);
        inflater = LayoutInflater.from(this);
        view1 = inflater.inflate(R.layout.danqu_layout,null);
        l1 = (LinearLayout)view1.findViewById(R.id.loading);
        recyclerView =view1.findViewById(R.id.danqu_list);
        hot = view1.findViewById(R.id.hot);
        hot.setOnClickListener(this);
        neww = view1.findViewById(R.id.neww);
        neww.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        view2 = inflater.inflate(R.layout.zhuanji_layout,null);
        l2 = (LinearLayout)view2.findViewById(R.id.loading);
        ablumList = view2.findViewById(R.id.ablum_list);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        ablumList.setLayoutManager(layoutManager1);
        ablumList.setAdapter(ablumAdapter);
        view3 = inflater.inflate(R.layout.mv_layout,null);
        l3 = (LinearLayout)view3.findViewById(R.id.loading);
        mvlist = view3.findViewById(R.id.mv_List);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        mvlist.setLayoutManager(layoutManager2);
        mvlist.setAdapter(mvAdapter);
        view4 = inflater.inflate(R.layout.details_layout,null);
        l4 = (LinearLayout)view4.findViewById(R.id.loading);
        info = (TextView) view4.findViewById(R.id.info);
        html = view4.findViewById(R.id.html);
        //添加页卡视图
        mViewList.add(view1);
        mViewList.add(view2);
        mViewList.add(view3);
        mViewList.add(view4);

        //添加页卡标题
        mTitleList.add("单曲");
        mTitleList.add("专辑");
        mTitleList.add("MV");
        mTitleList.add("详情");
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(2)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(3)));
        viewPager.setAdapter(new MyPagerAdapter(mViewList));
        tabLayout.setupWithViewPager(viewPager); //将TabLayout和ViewPager关联起来。
        tabLayout.setTabsFromPagerAdapter(new MyPagerAdapter(mViewList)); //给Tabs设置适配器
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        HttpClinet.getGeshouSonglist(singerid, 300, 2, new HttpCallback<GedanList>() {
            @Override
            public void onSuccess(GedanList gedanList) {
                if (gedanList.data!=null&&gedanList!=null) {
                    songlist.addAll(gedanList.data.infoList);
                    adapter.notifyDataSetChanged();
                    l1.setVisibility(View.GONE);
                    isDanqu = true;
                }
            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(GeshouInfoActivity.this,"获取单曲列表失败，请检查网络重试",Toast.LENGTH_LONG).show();
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition())
                {
                    case 0:
                        if (isDanqu)        //表示加载过数据了就不再重复访问网络加载数据
                        {
                            return;
                        }
                        HttpClinet.getGeshouSonglist(singerid, 300, 2, new HttpCallback<GedanList>() {
                            @Override
                            public void onSuccess(GedanList gedanList) {
                                songlist.clear();
                                songlist.addAll(gedanList.data.infoList);
                                adapter.notifyDataSetChanged();
                                l1.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFail(Exception e) {
                                Toast.makeText(GeshouInfoActivity.this,"获取单曲列表失败，请检查网络重试",Toast.LENGTH_LONG).show();
                            }
                        });
                        break;
                    case 1:
                        if (isZhuanji)              //表示加载过数据了就不再重复访问网络加载数据
                        {
                            return;
                        }
                        HttpClinet.getAblumlist(singerid, new HttpCallback<Ablum>() {
                            @Override
                            public void onSuccess(Ablum ablum) {
                                ablumlist.clear();
                                ablumlist.addAll(ablum.getData().getInfo());
                                ablumAdapter.notifyDataSetChanged();
                                l2.setVisibility(View.GONE);
                                isZhuanji = true;
                            }

                            @Override
                            public void onFail(Exception e) {
                                Toast.makeText(GeshouInfoActivity.this,"获取专辑列表失败，请检查网络重试",Toast.LENGTH_LONG).show();
                            }
                        });
                        break;
                    case 2:
                        if (isMV)           //表示加载过数据了就不再重复访问网络加载数据
                        {
                            return;
                        }
                        HttpClinet.getSinggerMV(name, singerid, new HttpCallback<GeshouMv>() {
                            @Override
                            public void onSuccess(GeshouMv geshouMv) {
                                mvliist.clear();
                                mvliist.addAll(geshouMv.getData().getInfo());
                                mvAdapter.notifyDataSetChanged();
                                l3.setVisibility(View.GONE);
                                isMV = true;
                            }

                            @Override
                            public void onFail(Exception e) {
                                Toast.makeText(GeshouInfoActivity.this,"获取MV列表失败，请检查网络重试",Toast.LENGTH_LONG).show();
                            }
                        });
                        break;
                    case 3:
                        html.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(GeshouInfoActivity.this,WebActivity.class);
                                intent.putExtra("URL","http://m.kugou.com/webapp/singerInfo/info.html?id="+singerid);
                                intent.putExtra("title",name+"的完整信息");
                                startActivity(intent);
                            }
                        });
                        if (isXiangqing)        //表示加载过数据了就不再重复访问网络加载数据
                        {
                           return;
                        }
                        HttpClinet.getSingerInfo(singerid, new HttpCallback<SingerInfo>() {
                            @Override
                            public void onSuccess(SingerInfo singerInfo) {
                                info.setText(singerInfo.getData().getIntro());
                                l4.setVisibility(View.GONE);
                                isXiangqing = true;
                            }

                            @Override
                            public void onFail(Exception e) {
                                Toast.makeText(GeshouInfoActivity.this,"获取歌手详情失败，请检查网络重试",Toast.LENGTH_LONG).show();
                            }
                        });
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.hot:
                if (!isHot) {
                    HttpClinet.getGeshouSonglist(singerid, 300, 2, new HttpCallback<GedanList>() {
                        @Override
                        public void onSuccess(GedanList gedanList) {
                            songlist.clear();
                            songlist.addAll(gedanList.data.infoList);
                            adapter.notifyDataSetChanged();
                            hot.setTextColor(Color.parseColor("#00F5FF"));
                            neww.setTextColor(Color.parseColor("#BEBEBE"));
                        }

                        @Override
                        public void onFail(Exception e) {
                            Toast.makeText(GeshouInfoActivity.this, "获取单曲列表失败，请检查网络重试", Toast.LENGTH_LONG).show();
                        }
                    });
                    isHot = true;
                    isNew = false;
                }
                break;
            case R.id.neww:
                if (!isNew) {
                    HttpClinet.getGeshouSonglist(singerid, 300, 1, new HttpCallback<GedanList>() {
                        @Override
                        public void onSuccess(GedanList gedanList) {
                            songlist.clear();
                            songlist.addAll(gedanList.data.infoList);
                            adapter.notifyDataSetChanged();
                            neww.setTextColor(Color.parseColor("#00F5FF"));
                            hot.setTextColor(Color.parseColor("#BEBEBE"));
                        }

                        @Override
                        public void onFail(Exception e) {
                            Toast.makeText(GeshouInfoActivity.this, "获取单曲列表失败，请检查网络重试", Toast.LENGTH_LONG).show();
                        }
                    });
                    isHot = false;
                    isNew = true;
                }
                break;
            case R.id.photo:
                tabLayout.setScrollPosition(3,0,true);
                viewPager.setCurrentItem(3,true);
                break;
        }
    }




    //ViewPager适配器
    class MyPagerAdapter extends PagerAdapter {
        private List<View> mViewList;

        public MyPagerAdapter(List<View> mViewList) {
            this.mViewList = mViewList;
        }

        @Override
        public int getCount() {
            return mViewList.size();//页卡数
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;//官方推荐写法
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));//添加页卡
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));//删除页卡
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);//页卡标题
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


  //Glide加载自动适应Imageview宽高的图片
    public static void loadIntoUseFitWidth(Context context, final String imageUrl, int errorImageId, final ImageView imageView) {
        Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (imageView == null) {
                            return false;
                        }
                        if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
                        int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
                        float scale = (float) vw / (float) resource.getIntrinsicWidth()-50.0f;
                        int vh = Math.round(resource.getIntrinsicHeight() * scale);
                        params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
                        imageView.setLayoutParams(params);
                        return false;
                    }
                })
                .placeholder(errorImageId)
                .error(errorImageId)
                .into(imageView);
    }
}
