package com.vaiyee.hongmusic.fragement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.vaiyee.hongmusic.Adapter.Onlinesheet;
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmen2_layout, null);
        onlineList = view.findViewById(R.id.online_sheetlist);
        sheetList = new ArrayList<>();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] titles = new String[]{"主打榜单", "百度热歌榜", "百度新歌榜", "分类榜单", "华语金曲榜", "欧美金曲榜", "影视金曲榜", "情歌对唱榜", "网络歌曲榜", "经典老歌榜", "摇滚榜", "媒体榜单", "KTV热歌榜", "Billboard", "Hito中文榜", "叱咤歌曲榜"};
        String[]type = new String[]{"#", "2", "1", "#", "20", "21", "24", "23", "25", "22", "11", "#", "6", "8", "18", "7"};
        for(int i=0;i<titles.length;i++)
        {
          Sheet  sheet = new Sheet();
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
    }
}
