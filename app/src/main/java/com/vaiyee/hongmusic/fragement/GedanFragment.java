package com.vaiyee.hongmusic.fragement;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.vaiyee.hongmusic.Adapter.GedanAdapter;
import com.vaiyee.hongmusic.R;
import com.vaiyee.hongmusic.Utils.HttpClinet;
import com.vaiyee.hongmusic.bean.Gedan;
import com.vaiyee.hongmusic.http.HttpCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GedanFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayout loadingfooter;
    private static List<Gedan.Info> infoList = new ArrayList<>();
    private int page = 1;
    private boolean loading = false;   //是否正在加载的标志位，防止内存泄漏程序崩溃
    private GridLayoutManager manager;
    private GedanAdapter adapter;
    public GedanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HttpClinet.getKugouGedan(page, new HttpCallback<Gedan>() {
            @Override
            public void onSuccess(Gedan response) {
                infoList.addAll(response.plist.liebiao.infoList);
                adapter = new GedanAdapter(getContext(),infoList);
                recyclerView.setAdapter(adapter);  //这里给你recycleview 设置适配器并不会导致recycleview为null，因为这里是在网络get成功后在执行的，而onCreateView（）方法会紧跟onCreat()方法执行
            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(getContext(),"获取歌单失败",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_gedan,container,false);
       recyclerView = view.findViewById(R.id.gedan_recview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);  //这里再设置一次是因为当这个碎片由不见变为可见时会执行这个方法
        loadingfooter = view.findViewById(R.id.loading_view);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) //向下滚动
                {
                    manager = (GridLayoutManager) recyclerView.getLayoutManager();
                    int visibleItemCount = manager.getChildCount();	//得到显示屏幕内显示的item数量
                    int totalItemCount = manager.getItemCount();	//得到item的总数量
                    int pastVisiblesItems = manager.findFirstVisibleItemPosition();//得到显示屏内的第一个可见item的位置索引

                    if (!loading && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {     //判断是否划到底部
                        loading = true;    //将标志位设置为true,表示正在加载更多，防止滑动到底部时无限调用loadMoreDate()导致程序崩溃
                        loadingfooter.setVisibility(View.VISIBLE);
                        page+=1;
                        loadMoreDate();
                    }
                }
            }
        });
        return view;
    }

    private void loadMoreDate() {
        HttpClinet.getKugouGedan(page, new HttpCallback<Gedan>() {
            @Override
            public void onSuccess(Gedan response) {
                infoList.addAll(response.plist.liebiao.infoList);
                adapter.notifyDataSetChanged();
                loading = false;
                loadingfooter.setVisibility(View.GONE);
            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(getContext(),"获取歌单失败",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        Log.d("Destroy了","这个碎片");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d("Detach了","这个碎片");
        super.onDetach();
    }

    @Override
    public void onPause() {
        Log.d("Pause了","这个碎片");
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        Log.d("DestroyView了","这个碎片");
        super.onDestroyView();
    }
}
