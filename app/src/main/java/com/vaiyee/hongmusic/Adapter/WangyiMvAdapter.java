package com.vaiyee.hongmusic.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.vaiyee.hongmusic.MainActivity;
import com.vaiyee.hongmusic.PlayMvActivity;
import com.vaiyee.hongmusic.R;
import com.vaiyee.hongmusic.Utils.NetUtils;
import com.vaiyee.hongmusic.bean.WangYiMv;

import java.util.List;

import io.vov.vitamio.utils.Log;

/**
 * Created by Administrator on 2018/3/31.
 */

public class WangyiMvAdapter extends RecyclerView.Adapter<WangyiMvAdapter.ViewHolder> {
    private List<WangYiMv.Data> mvlist;
    private Context context;
    private  Activity activity;

    public WangyiMvAdapter(Context context, List<WangYiMv.Data>mvlist,Activity activity)
    {
        this.context = context;
        this.mvlist = mvlist;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mv_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final WangYiMv.Data mv = mvlist.get(position);
        Glide.with(context)
                .load(mv.coverurl)
                .placeholder(R.drawable.default_cover)
                .into(holder.cover);
        holder.title.setText(mv.briefDesc);
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (NetUtils.getNetType())
                {
                    case NET_WIFI:
                        Intent intent = new Intent(context, PlayMvActivity.class);
                        intent.putExtra("mvid",mv.mvid);
                        intent.putExtra("type","wy");
                        intent.putExtra("title",mv.briefDesc);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                        default:
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setMessage("当前处于移动网络，播放视频将消耗数据流量，确定使用流量播放吗？");
                            builder.setTitle("提示");
                            builder.setIcon(R.drawable.tip);
                            builder.setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            Intent intent = new Intent(context, PlayMvActivity.class);
                                            intent.putExtra("title",mv.briefDesc);
                                            intent.putExtra("type","wy");
                                            intent.putExtra("mvid",mv.mvid);
                                            context.startActivity(intent);

                                        }
                                    });

                            builder.setNegativeButton("取消",
                                    new android.content.DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

                            builder.create().show();
                            break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mvlist.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView cover,play;
        TextView title;
        public ViewHolder(View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.mv_cover);
            title = itemView.findViewById(R.id.mv_title);
            play = itemView.findViewById(R.id.mv_play_btn);
        }
    }
}
