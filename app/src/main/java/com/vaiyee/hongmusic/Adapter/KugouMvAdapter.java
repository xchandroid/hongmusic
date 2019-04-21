package com.vaiyee.hongmusic.Adapter;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vaiyee.hongmusic.PlayMvActivity;
import com.vaiyee.hongmusic.R;
import com.vaiyee.hongmusic.bean.KugouMv;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

/**
 * Created by Administrator on 2018/6/15.
 */

public class KugouMvAdapter extends RecyclerView.Adapter<KugouMvAdapter.ViewHolder> {
    private Context context;
    private List<KugouMv.Info> infoList;
    private Activity activity;
    public static boolean isAnimation = true; //控制滑动到底部时加载更多时，重复播放动画的bug

    public KugouMvAdapter(Context context,List<KugouMv.Info> infoList,Activity activity)
    {
        this.context = context;
        this.infoList = infoList;
        this.activity = activity;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mv_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final KugouMv.Info info = infoList.get(position);
        holder.decription.setText(info.description);
        Glide.with(context).load(info.img.replace("{size}","400")).placeholder(R.drawable.default_cover).into(holder.img);
       final String Bighash = info.mvhash.toUpperCase();
       final String key = getMD5(Bighash+"kugoumvcloud");
        holder.playbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PlayMvActivity.class);
                intent.putExtra("mvid","http://trackermv.kugou.com/interface/index/cmd=100&hash="+Bighash+"&key="+key+"&pid=6&ext=mp4&ismp3=0");
                intent.putExtra("type","kg");
                intent.putExtra("title",info.videoname);
                context.startActivity(intent);
            }
        });
        if (isAnimation) {
            AnimatorSet animatorSet = new AnimatorSet();
            Animator animator = ObjectAnimator.ofFloat(holder.itemView, "translationX", 1000, 500, -200, 0);
            Animator animator1 = ObjectAnimator.ofFloat(holder.itemView, "scaleX", 0f, 0.5f, 1f);
            Animator animator2 = ObjectAnimator.ofFloat(holder.itemView, "scaleY", 0f, 0.5f, 1f);
            animatorSet.playTogether(animator, animator1);
            animatorSet.setDuration(500).start();
        }
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    class ViewHolder extends  RecyclerView.ViewHolder
    {
        TextView decription,time,playcout;
        ImageView img,playbt;
        public ViewHolder(View itemView) {
            super(itemView);
            decription = itemView.findViewById(R.id.mv_title);
            img = itemView.findViewById(R.id.mv_cover);
            playbt = itemView.findViewById(R.id.mv_play_btn);
        }
    }


    //加密MV的hash值作为网址参数访问服务器获取MV的播放地址
    public static String getMD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
