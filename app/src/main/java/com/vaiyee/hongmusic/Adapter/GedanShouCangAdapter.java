package com.vaiyee.hongmusic.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vaiyee.hongmusic.R;
import com.vaiyee.hongmusic.bean.ShouCangGeDan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/21.
 */

public class GedanShouCangAdapter extends BaseAdapter {
    private List<ShouCangGeDan> shouCangGeDanList = new ArrayList<>();
    private Context context;

    public GedanShouCangAdapter(Context context, List<ShouCangGeDan> shouCangGeDanList)
    {
        this.context = context;
        this.shouCangGeDanList = shouCangGeDanList;
    }
    @Override
    public int getCount() {
        return shouCangGeDanList.size();
    }

    @Override
    public Object getItem(int i) {
        return shouCangGeDanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        ShouCangGeDan shouCangGeDan = shouCangGeDanList.get(i);
        if (view==null)
        {
            view = LayoutInflater.from(context).inflate(R.layout.ablum_item,viewGroup,false);
            viewHolder = new ViewHolder();
            viewHolder.cover = view.findViewById(R.id.ablum_cover);
            viewHolder.title = view.findViewById(R.id.ablum_name);
            viewHolder.time = view.findViewById(R.id.publish_time);
            view.setTag(viewHolder);
        }
        else
        {
           viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.title.setText(shouCangGeDan.getTitle());
        viewHolder.time.setText(shouCangGeDan.getTime());
        Glide.with(context).load(shouCangGeDan.getImg()).placeholder(R.drawable.default_cover).into(viewHolder.cover);
        return view;
    }

    class ViewHolder
    {
        ImageView cover;
        TextView title,time;
    }

}
