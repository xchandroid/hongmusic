package com.vaiyee.hongmusic.Adapter;

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

import com.bumptech.glide.Glide;
import com.vaiyee.hongmusic.MainActivity;
import com.vaiyee.hongmusic.PlayMvActivity;
import com.vaiyee.hongmusic.R;
import com.vaiyee.hongmusic.Utils.NetUtils;
import com.vaiyee.hongmusic.bean.GeshouMv;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

import io.vov.vitamio.utils.Log;

/**
 * Created by Administrator on 2018/4/10.
 */

public class GeshouMvAdapter extends RecyclerView.Adapter<GeshouMvAdapter.ViewHolder> {
    private List<GeshouMv.DataBean.InfoBean> mvliist;
    private Context context;

    public GeshouMvAdapter(Context context, List<GeshouMv.DataBean.InfoBean> mvliist)
    {
        this.context = context;
        this.mvliist = mvliist;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.geshou_mv_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GeshouMv.DataBean.InfoBean mv = mvliist.get(position);
         holder.title.setText(mv.getFilename());
         holder.singer.setText(mv.getSingername());
        Glide.with(context).load(mv.getImgurl()).placeholder(R.drawable.default_cover).into(holder.cover);
        final String Bighash = mv.getHash().toUpperCase();
        final String key =  getMD5(Bighash+"kugoumvcloud");
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (NetUtils.getNetType())
                {
                    case NET_WIFI:
                        Intent intent = new Intent(context, PlayMvActivity.class);
                        intent.putExtra("title",mv.getFilename());
                        intent.putExtra("type","kg");
                        intent.putExtra("mvid","http://trackermv.kugou.com/interface/index/cmd=100&hash="+Bighash+"&key="+key+"&pid=6&ext=mp4&ismp3=0");
                        Log.d("获取mv的播放地址的URl是"+"http://trackermv.kugou.com/interface/index/cmd=100&hash="+Bighash+"&key="+key+"&pid=6&ext=mp4&ismp3=0");
                        context.startActivity(intent);
                        break;
                        default:
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("当前处于移动网络，播放视频将消耗数据流量，确定使用流量播放吗？");
                            builder.setTitle("提示");
                            builder.setIcon(R.drawable.tip);
                            builder.setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            Intent intent = new Intent(context, PlayMvActivity.class);
                                            intent.putExtra("title",mv.getFilename());
                                            intent.putExtra("type","kg");
                                            intent.putExtra("mvid","http://trackermv.kugou.com/interface/index/cmd=100&hash="+Bighash+"&key="+key+"&pid=6&ext=mp4&ismp3=0");
                                            Log.d("获取mv的播放地址的URl是"+"http://trackermv.kugou.com/interface/index/cmd=100&hash="+Bighash+"&key="+key+"&pid=6&ext=mp4&ismp3=0");
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
        return mvliist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView cover;
        TextView title,singer;
        View view;
        public ViewHolder(View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.mv_cover);
            title = itemView.findViewById(R.id.title);
            singer = itemView.findViewById(R.id.singer);
            view = itemView;
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
