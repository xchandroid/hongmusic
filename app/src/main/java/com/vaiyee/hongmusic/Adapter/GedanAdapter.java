package com.vaiyee.hongmusic.Adapter;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vaiyee.hongmusic.GedanActivity;
import com.vaiyee.hongmusic.R;
import com.vaiyee.hongmusic.bean.Gedan;
import com.vaiyee.hongmusic.util.Animation;

import java.util.List;

/**
 * Created by Administrator on 2018/3/26.
 */

public class GedanAdapter extends RecyclerView.Adapter<GedanAdapter.ViewHolder> {
    private Context context;
    private List<Gedan.Info> gedanList;
    public static boolean isAnimation = true;

    public GedanAdapter(Context context,List<Gedan.Info>gedanList)
    {
        this.context = context;
        this.gedanList = gedanList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {   //加载子布局
        View view = LayoutInflater.from(context).inflate(R.layout.gedan_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;      //将子布局保存在viewholder中，优化性能
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {   //这个方法相当于getView()
        String url = gedanList.get(position).imgurl.replace("{size}","400");
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.default_cover)
                .error(R.drawable.default_cover)
                .dontAnimate()
                .into(holder.cover);                             //加载歌单封面
        holder.title.setText(gedanList.get(position).specialname); //设置歌单标题
        holder.playcount.setText("播放次数:"+gedanList.get(position).playcount); //设置播放次数
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,GedanActivity.class);
                intent.putExtra("title",gedanList.get(position).specialname);
                intent.putExtra("comment",gedanList.get(position).intro);
                intent.putExtra("img",gedanList.get(position).imgurl);
                intent.putExtra("time","创建时间："+gedanList.get(position).publishtime.split(" ")[0]);
                intent.putExtra("id",gedanList.get(position).specialid);
                intent.putExtra("type","gedan");
                context.startActivity(intent);
            }
        });
        if (isAnimation) {
            for (Animator animator : Animation.getAnimators(holder.itemView)) {
                animator.setDuration(500).start();
            }
        }

    }


    @Override
    public int getItemCount() {
        return gedanList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cover;
        TextView title,playcount;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.gedan_cover);
            title = itemView.findViewById(R.id.gedan_title);
            playcount = itemView.findViewById(R.id.play_cout);
            cardView = itemView.findViewById(R.id.cardview);
        }
    }

}
