package com.vaiyee.hongmusic.fragement;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vaiyee.hongmusic.Adapter.Onlinesheet;
import com.vaiyee.hongmusic.MainActivity;
import com.vaiyee.hongmusic.OnlineMusicActivity;
import com.vaiyee.hongmusic.R;
import com.vaiyee.hongmusic.bean.Sheet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/29.
 */

public class fragment2 extends Fragment {
    private ListView onlineList;
    private List<Sheet> sheetList;
    private TextView wangyi,baidu,kugou;
    private static WangyiFragment wangyiFragment;
    private static KugouFragment kugouFragment;
    private boolean isShow = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmen2_layout, null);
        onlineList = view.findViewById(R.id.online_sheetlist);
        wangyi = view.findViewById(R.id.wangyi);
        baidu = view.findViewById(R.id.baidu);
        kugou = view.findViewById(R.id.kugou);
        kugou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                if (kugouFragment==null)
                {
                    kugouFragment= new KugouFragment();
                    transaction.add(R.id.replace,kugouFragment);
                }
                else
                {
                    transaction.show(kugouFragment);
                }
                if (wangyiFragment!=null)
                {
                    transaction.hide(wangyiFragment);
                }
                transaction.commit();
                baidu.setTextColor(Color.parseColor("#8B8989"));
                wangyi.setTextColor(Color.parseColor("#8B8989"));
                kugou.setTextColor(Color.parseColor("#FFC125"));


            }
        });
        baidu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                if (kugouFragment!=null)
                transaction.hide(kugouFragment);
                if (wangyiFragment!=null)
                transaction.hide(wangyiFragment);
                transaction.commitAllowingStateLoss();
              baidu.setTextColor(Color.parseColor("#FFC125"));
              wangyi.setTextColor(Color.parseColor("#8B8989"));
              kugou.setTextColor(Color.parseColor("#8B8989"));

            }
        });
        wangyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                if (wangyiFragment==null)
                {
                    wangyiFragment = new WangyiFragment();
                    transaction.add(R.id.replace,wangyiFragment);
                }
                else
                {
                    transaction.show(wangyiFragment);
                }
                if (kugouFragment!=null)
                {
                    transaction.hide(kugouFragment);
                }
                transaction.commit();
                wangyi.setTextColor(Color.parseColor("#FFC125"));
                baidu.setTextColor(Color.parseColor("#8B8989"));
                kugou.setTextColor(Color.parseColor("#8B8989"));


            }
        });
        sheetList = new ArrayList<>();
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] titles = new String[]{"主打榜单", "百度热歌榜", "百度新歌榜", "华语金曲榜", "网络歌曲榜", "分类榜单", "欧美金曲榜", "影视金曲榜", "情歌对唱榜", "经典老歌榜", "摇滚榜", "媒体榜单", "KTV热歌榜", "Billboard", "Hito中文榜", "叱咤歌曲榜"};
        String[]type = new String[]{"#", "2", "1", "20","25", "#", "21", "24", "23", "22", "11", "#", "6", "8", "18", "7"};
        for(int i=0;i<titles.length;i++)
        {
          Sheet sheet = new Sheet();
            sheet.setTitle(titles[i]);
            sheet.setType(type[i]);
            sheetList.add(sheet);
        }
        onlineList.setAdapter(new Onlinesheet(getContext(),sheetList));
        onlineList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Sheet sheet = sheetList.get(i);
                Intent intent = new Intent(getContext(), OnlineMusicActivity.class);
                intent.putExtra("ABC",sheet);
                startActivity(intent);
            }
        });

        onlineList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

                switch (i)
                {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE: //滑动停止
                        MainActivity.linearLayout.setVisibility(View.VISIBLE);
                    break;
                    //滚动做出了抛的动作
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        MainActivity.linearLayout.setVisibility(View.GONE);
                        break;
                    //正在滚动
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        MainActivity.linearLayout.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
    }
}
