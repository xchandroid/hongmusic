package com.vaiyee.hongmusic.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vaiyee.hongmusic.GedanActivity;
import com.vaiyee.hongmusic.R;
import com.vaiyee.hongmusic.bean.Ablum;

import java.util.List;

/**
 * Created by Administrator on 2018/4/9.
 */

public class AblumAdapter extends RecyclerView.Adapter<AblumAdapter.ViewHolder> {

    private Context context;
    private List<Ablum.DataBean.InfoBean> ablumList;
    public AblumAdapter(Context context,List<Ablum.DataBean.InfoBean> ablumList)
    {
        this.context = context;
        this.ablumList = ablumList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ablum_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Ablum.DataBean.InfoBean ablum = ablumList.get(position);
        holder.name.setText(ablum.getAlbumname());
        holder.time.setText(ablum.getPublishtime().split(" ")[0]+"   共"+ablum.getSongcount()+"首");
        Glide.with(context).load(ablum.getImgurl().replace("{size}","150")).placeholder(R.drawable.default_cover).into(holder.cover);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GedanActivity.class);
                intent.putExtra("type","ablum");
                intent.putExtra("title",ablum.getAlbumname());
                intent.putExtra("id",ablum.getAlbumid());
                intent.putExtra("time","发布时间："+ablum.getPublishtime().split(" ")[0]);
                intent.putExtra("img",ablum.getImgurl());
                intent.putExtra("comment",ablum.getAlbumname());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ablumList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder
    {
        ImageView cover;
        TextView name,time;
        View view;
        public ViewHolder(View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.ablum_cover);
            name = itemView.findViewById(R.id.ablum_name);
            time = itemView.findViewById(R.id.publish_time);
            view = itemView;
        }
    }
}
