package com.vaiyee.hongmusic.fragement;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vaiyee.hongmusic.Adapter.KugouMvAdapter;
import com.vaiyee.hongmusic.Adapter.WangyiMvAdapter;
import com.vaiyee.hongmusic.MyApplication;
import com.vaiyee.hongmusic.R;
import com.vaiyee.hongmusic.Utils.HttpClinet;
import com.vaiyee.hongmusic.Utils.MyContentLinearLayoutManager;
import com.vaiyee.hongmusic.bean.KugouMv;
import com.vaiyee.hongmusic.bean.WangYiMv;
import com.vaiyee.hongmusic.http.HttpCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MVFragment extends BaseFragment implements View.OnClickListener{

    private RecyclerView mvlistview;
    private List<KugouMv.Info> mvlist = new ArrayList<>();
    private LinearLayout lodaing;
    private boolean isSuccess =false,isfirst = true;
    private KugouMvAdapter adapter;
    private LinearLayoutManager linearLayoutManager2;
    private int page = 1;
    private boolean loading = false;
    private TextView tuijian,zuixin,zuire;
    private int whichone = 1,sort =4;
    private SwipeRefreshLayout swipeRefreshLayout;

    public MVFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new KugouMvAdapter(getActivity(),mvlist,getActivity());
        HttpClinet.getKugouMv(page, sort, new HttpCallback<KugouMv>() {
            @Override
            public void onSuccess(KugouMv kugouMv) {
                mvlist.addAll(kugouMv.data.infoList);
                mvlistview.setLayoutManager(new MyContentLinearLayoutManager(getContext()));
                mvlistview.setAdapter(adapter);
                isSuccess = true;
                isfirst = false;
                lodaing.setVisibility(View.GONE);
            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(getContext(),"获取MV数据失败，请检查网络是否畅通",Toast.LENGTH_LONG).show();
            }
        });
        /*
        HttpClinet.getWangyiMV(new HttpCallback<WangYiMv>() {
            @Override
            public void onSuccess(WangYiMv wangYiMv) {
                mvlist.addAll(wangYiMv.mvlist);
                mvlistview.setAdapter(adapter);
                isSuccess = true;
                lodaing.setVisibility(View.GONE);
            }

            @Override
            public void onFail(Exception e) {

            }
        });
        */
    }
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mv,container,false);
        return view;
    }

    @Override
    protected void initView(View view) {
        mvlistview = view.findViewById(R.id.mv_recycleview);
        lodaing = view.findViewById(R.id.loading);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.blue);
        tuijian = view.findViewById(R.id.tuijian);
        zuixin = view.findViewById(R.id.zuixin);
        zuire = view.findViewById(R.id.zuire);
    }

    @Override
    protected void initListener() {
        tuijian.setOnClickListener(this);
        zuixin.setOnClickListener(this);
        zuire.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mvlist.clear();
                Refresh();
            }
        });
        mvlistview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                KugouMvAdapter.isAnimation =true; //播放动画
                if (dy > 0) //向下滚动
                {
                    linearLayoutManager2 = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int visibleItemCount = linearLayoutManager2.getChildCount();	//得到显示屏幕内显示的item数量
                    int totalItemCount = linearLayoutManager2.getItemCount();	//得到item的总数量
                    int pastVisiblesItems = linearLayoutManager2.findFirstVisibleItemPosition();//得到显示屏内的第一个可见item的位置索引

                    if (!loading && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {     //判断是否划到底部
                        loading = true;    //将标志位设置为true,表示正在加载更多，防止滑动到底部时无限调用loadMoreDate()导致程序崩溃
                        page+=1;
                        lodaing.setVisibility(View.VISIBLE);
                        AutoLoadMore();
                    }
                }
            }
        });
    }

    @Override
    protected void lazyLoad() {
        mvlistview.setLayoutManager(new MyContentLinearLayoutManager(getContext()));
        mvlistview.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isSuccess)
        {
            lodaing.setVisibility(View.GONE);
        }
        else
        {
            if (!isfirst) {
                Toast.makeText(getContext(), "获取MV数据失败,请检查网络是否畅通", Toast.LENGTH_LONG).show();
            }
        }

    }
    private void Refresh()
    {
        HttpClinet.getKugouMv(page, sort, new HttpCallback<KugouMv>() {
            @Override
            public void onSuccess(KugouMv kugouMv) {
                mvlist.addAll(kugouMv.data.infoList);
                adapter.notifyDataSetChanged();
                lodaing.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(),"刷新成功！！",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFail(Exception e) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(),"获取MV数据失败,请检查网络是否畅通",Toast.LENGTH_LONG).show();
            }
        });
    }

   private void AutoLoadMore()
    {
        HttpClinet.getKugouMv(page, sort, new HttpCallback<KugouMv>() {
            @Override
            public void onSuccess(KugouMv kugouMv) {
                mvlist.addAll(kugouMv.data.infoList);
                KugouMvAdapter.isAnimation = false; //加载更多不播放动画
                adapter.notifyDataSetChanged();
                loading = false;   //结束加载更多
                lodaing.setVisibility(View.GONE);
            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(getContext(),"获取MV数据失败,请检查网络是否畅通",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void switchType()
    {
        HttpClinet.getKugouMv(page, sort, new HttpCallback<KugouMv>() {
            @Override
            public void onSuccess(KugouMv kugouMv) {
                mvlist.clear();   // 如果在服务器返回成功前就清空list，滑动recyclerview就会导致adapter中的数据和外面的数据不一致导致奔溃
                mvlist.addAll(kugouMv.data.infoList);
                adapter.notifyDataSetChanged();
                loading = false;   //结束加载更多
                lodaing.setVisibility(View.GONE);
            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(getContext(),"获取MV数据失败,请检查网络是否畅通",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.tuijian:
                if (whichone==1)
                {
                    return;
                }
                whichone =1;
                tuijian.setTextColor(Color.parseColor("#00f5ff"));
                zuixin.setTextColor(Color.parseColor("#FF010101"));
                zuire.setTextColor(Color.parseColor("#FF010101"));
                page=1;
                sort =4;
                switchType();
                break;
            case R.id.zuixin:
                if (whichone==2)
                {
                    return;
                }
                whichone =2;
                tuijian.setTextColor(Color.parseColor("#FF010101"));
                zuixin.setTextColor(Color.parseColor("#00f5ff"));
                zuire.setTextColor(Color.parseColor("#FF010101"));
                page = 1;
                sort = 1;
                switchType();
                break;
            case R.id.zuire:
                if (whichone==3)
                {
                    return;
                }
                whichone =3;
                tuijian.setTextColor(Color.parseColor("#FF010101"));
                zuixin.setTextColor(Color.parseColor("#FF010101"));
                zuire.setTextColor(Color.parseColor("#00f5ff"));
                page=1;
                sort=3;
                switchType();
                break;

        }
    }
}
