package com.vaiyee.hongmusic.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vaiyee.hongmusic.R;
import com.vaiyee.hongmusic.bean.OnlineMusic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/2/6.
 */

public class OnlineMusicAdapter extends ArrayAdapter {
    private int resId;
    private List<OnlineMusic> onlineMusics;
    Context context;
    public OnlineMusicAdapter(Context context, int resId, List<OnlineMusic> onlineMusics)
    {
        super(context,resId,onlineMusics);
        this.context=context;
        this.resId = resId;
        this.onlineMusics = onlineMusics;
    }

    @Override
    public int getCount() {
        return onlineMusics.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return onlineMusics.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        OnlineMusic onlineMusic = onlineMusics.get(position);
        View view =null;
        ViewHolder viewHolder = null;
        if (convertView == null)
        {
            view =  LayoutInflater.from(context).inflate(resId,parent,false);
            viewHolder = new ViewHolder();
            view.setTag(viewHolder);
        }
        else
        {
            view = convertView;
            viewHolder =(ViewHolder) view.getTag();
        }
        viewHolder.ablumCover = view.findViewById(R.id.zj_id);
        viewHolder.more = view.findViewById(R.id.more);
        viewHolder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"更多更能敬请期待",Toast.LENGTH_LONG).show();
            }
        });
        viewHolder.songaName = view.findViewById(R.id.geming);
        viewHolder.singger = view.findViewById(R.id.geshou);
        viewHolder.ablum = view.findViewById(R.id.ablum);
        Glide.with(context)
                .load(onlineMusic.getPic_big())
                .placeholder(R.drawable.music_ic)
                .error(R.drawable.music_ic)
                .into(viewHolder.ablumCover);
        viewHolder.songaName.setText(onlineMusic.getTitle());
        viewHolder.singger.setText(onlineMusic.getArtist_name());
        viewHolder.ablum.setText("《"+onlineMusic.getAlbum_title()+"》");
        return view;
    }
    class ViewHolder
    {
        ImageView ablumCover,more;
        TextView songaName,singger,ablum;
    }
}
