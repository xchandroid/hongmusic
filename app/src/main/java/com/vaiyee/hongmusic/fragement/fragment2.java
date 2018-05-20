package com.vaiyee.hongmusic.fragement;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.vaiyee.hongmusic.GeshouActivity;
import com.vaiyee.hongmusic.MainActivity;
import com.vaiyee.hongmusic.OnlineMusicActivity;
import com.vaiyee.hongmusic.R;
import com.vaiyee.hongmusic.bean.Sheet;
import com.vaiyee.hongmusic.util.BindOnclick;
import com.vaiyee.hongmusic.util.BindView;

import java.util.ArrayList;
import java.util.List;

import io.vov.vitamio.utils.Log;

/**
 * Created by Administrator on 2018/1/29.
 */

public class fragment2 extends Fragment {
    private ListView onlineList;
    private List<Sheet> sheetList;
    private TextView wangyi,baidu,kugou;
    private static WangyiFragment wangyiFragment;
    private FloatingActionButton open;
    private static KugouFragment kugouFragment;
    private static boolean isShowKG = false,isShowWY = false;      //这个标志位用于：当fragment2 这个碎片从缓存中移除再重新显示时，重新显示网易榜或者酷狗榜时的标志位
    private static boolean isFirstopen = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sheetList = new ArrayList<>();
        String[] titles = new String[]{"主打榜单", "百度热歌榜", "百度新歌榜", "华语金曲榜", "网络歌曲榜", "分类榜单", "欧美金曲榜", "影视金曲榜", "情歌对唱榜", "经典老歌榜", "摇滚榜", "媒体榜单", "KTV热歌榜", "Billboard", "Hito中文榜", "叱咤歌曲榜"};
        String[]type = new String[]{"#", "2", "1", "20","25", "#", "21", "24", "23", "22", "11", "#", "6", "8", "18", "7"};
        for(int i=0;i<titles.length;i++)
        {
            Sheet sheet = new Sheet();
            sheet.setTitle(titles[i]);
            sheet.setType(type[i]);
            sheetList.add(sheet);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmen2_layout, null);
        onlineList = view.findViewById(R.id.online_sheetlist);
        wangyi = view.findViewById(R.id.wangyi);
        baidu = view.findViewById(R.id.baidu);
        kugou = view.findViewById(R.id.kugou);
        open = view.findViewById(R.id.open_geshou);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), GeshouActivity.class);
                startActivity(intent);
            }
        });
        kugou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                if (kugouFragment==null)
                {
                    kugouFragment= new KugouFragment();
                    transaction.add(R.id.replace,kugouFragment);
                }
                else if (isShowKG)
                {
                    kugouFragment= new KugouFragment();
                    transaction.add(R.id.replace,kugouFragment);             //当滑动到推荐MV时，fragment不在缓存中，需要重新add()才能显示
                    isShowKG = false;
                    Log.d("执行了酷狗这句");
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
                else if (isShowWY)
                {
                    wangyiFragment = new WangyiFragment();
                    transaction.add(R.id.replace,wangyiFragment);
                    isShowWY = false;
                    Log.d("执行了网易这句");
                }
                else
                {
                    transaction.show(wangyiFragment);
                }
                if (kugouFragment!=null)
                {
                    transaction.hide(kugouFragment);
                }
                transaction.commitAllowingStateLoss();
                wangyi.setTextColor(Color.parseColor("#FFC125"));
                baidu.setTextColor(Color.parseColor("#8B8989"));
                kugou.setTextColor(Color.parseColor("#8B8989"));


            }
        });
            onlineList.setAdapter(new Onlinesheet(getContext(), sheetList));
            onlineList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Sheet sheet = sheetList.get(i);
                    Intent intent = new Intent(getContext(), OnlineMusicActivity.class);
                    intent.putExtra("ABC",sheet);
                    startActivity(intent);
                }
            });

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        isShowKG = true;
        isShowWY = true;
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        if (wangyiFragment!=null)
            transaction.remove(wangyiFragment);
        if(kugouFragment!=null)
            transaction.remove(kugouFragment);
        transaction.commitAllowingStateLoss();
        super.onDestroyView();
    }

}
