package com.vaiyee.hongmusic.fragement;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
        View view = inflater.inflate(R.layout.fragment_wangyi,container,false);
        wyListview = view.findViewById(R.id.wangyilist);
        sheetList = new ArrayList<>();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final String[] titles = new String[]{ "网易飙升榜", "网易新歌榜", "网易原创榜", "网易热歌榜", "网易电音榜", "网易嘻哈榜", "抖音榜", "日本Oricon榜", "新电力榜", "美国Billboard榜单", "iTunes榜", "古典音乐榜", "KTV嗨榜", "台湾Hito榜","中国港台榜","中国内地榜","中国嘻哈榜"};
        final String[]type = new String[]{"19723756", "3779629", "2884035", "3778678", "1978921795", "991319590", "2250011882", "60131", "10520166", "60198", "11641012", "71384707", "21845217","112463", "112504", "64016", "1899724"};
        for (int i =0;i<titles.length;i++)
        {
            Sheet sheet = new Sheet();
            sheet.setTitle(titles[i]);
            sheet.setType(type[i]);
            sheetList.add(sheet);
        }
        wyListview.setAdapter( new WangYiAdapter(getContext(),R.layout.view_holder_sheet,sheetList,getActivity()));
        wyListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Sheet sheet = sheetList.get(i);
                Intent intent = new Intent(getContext(), WangyiBangActivity.class);
                intent.putExtra("WY",sheet);
                intent.putExtra("img","R.drawable."+type[i]);
                intent.putExtra("name",titles[i]);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onDestroy() {
        Log.d("Destroy了","网易这个碎片");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d("Detach了","网易这个碎片");
        super.onDetach();
    }

    @Override
    public void onPause() {
        Log.d("Pause了","网易这个碎片");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d("Stop了","网易这个碎片");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d("DestroyView了","网易这个碎片");
        super.onDestroyView();
    }
}
