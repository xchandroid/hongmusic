package com.vaiyee.hongmusic.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vaiyee.hongmusic.PlayMusic;
import com.vaiyee.hongmusic.R;
import com.vaiyee.hongmusic.bean.Song;

import java.util.List;

/**
 * Created by Administrator on 2018/3/16.
 */

public class PlayListAdapter extends BaseAdapter {
    private Context context;
    private int resId;
    private List<Song> list;
    public PlayListAdapter(Context context, int resId, List<Song>list)
    {
        this.context=context;
        this.resId = resId;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View contentview, ViewGroup viewGroup) {
        View view = null;
        ViewHolder viewHolder = null;
        Song song = list.get(i);
        if (contentview ==null)
        {
            view = LayoutInflater.from(context).inflate(resId,viewGroup,false);
            viewHolder = new ViewHolder();
            viewHolder.geming = view.findViewById(R.id.geming);
            viewHolder.geshou = view.findViewById(R.id.geshou);
            viewHolder.tip = view.findViewById(R.id.left_tip);
            view.setTag(viewHolder);
        }
        else
        {
            view = contentview;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.geming.setText(1+i+"."+song.getTitle());
        viewHolder.geshou.setText(song.getSinger());
        viewHolder.tip.setVisibility(isShow(i)?View.VISIBLE:View.INVISIBLE);
        return view;
    }

    private boolean isShow(int i)
    {
        if (i== PlayMusic.playposition) {
            return true;
        }
        else
        {
            return false;
        }
    }
    private class ViewHolder
    {
        TextView geming,geshou;
        ProgressBar tip;
    }
}
