package com.vaiyee.hongmusic.fragement;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.vaiyee.hongmusic.Adapter.WangYiAdapter;
import com.vaiyee.hongmusic.OnlineMusicActivity;
import com.vaiyee.hongmusic.R;
import com.vaiyee.hongmusic.WangyiBangActivity;
import com.vaiyee.hongmusic.bean.Sheet;

import java.util.ArrayList;
import java.util.List;

public class WangyiFragment extends Fragment {


    private ListView wyListview;
    private List<Sheet> sheetList;

    public WangyiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wangyi,null);
        wyListview = view.findViewById(R.id.wangyilist);
        sheetList = new ArrayList<>();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] titles = new String[]{ "网易飙升榜", "网易新歌榜", "网易原创榜", "网易热歌榜", "网易电音榜", "网易嘻哈榜", "ACG榜", "日本Oricon榜", "华语金曲榜", "美国Billboard榜单", "iTunes榜", "韩国Melon排行榜周榜", "KTV嗨榜", "台湾Hito榜","中国港台榜","中国内地榜","中国嘻哈榜"};
        String[]type = new String[]{"3", "0", "2", "1", "4", "23", "22", "10", "17", "6", "8", "11", "7","20", "14", "15", "18"};
        for (int i =0;i<titles.length;i++)
        {
            Sheet sheet = new Sheet();
            sheet.setTitle(titles[i]);
            sheet.setType(type[i]);
            sheetList.add(sheet);
        }
        wyListview.setAdapter( new WangYiAdapter(getContext(),R.layout.view_holder_sheet,sheetList));
        wyListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Sheet sheet = sheetList.get(i);
                Intent intent = new Intent(getContext(), WangyiBangActivity.class);
                intent.putExtra("WY",sheet);
                startActivity(intent);
            }
        });
    }



}
