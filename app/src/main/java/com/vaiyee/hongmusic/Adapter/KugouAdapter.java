package com.vaiyee.hongmusic.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vaiyee.hongmusic.KugoubangActivity;
import com.vaiyee.hongmusic.R;
import com.vaiyee.hongmusic.Utils.HttpClinet;
import com.vaiyee.hongmusic.bean.KugouBang;
import com.vaiyee.hongmusic.bean.Sheet;
import com.vaiyee.hongmusic.http.HttpCallback;

import java.util.List;

/**
 * Created by Administrator on 2018/3/14.
 */

public class KugouAdapter extends RecyclerView.Adapter<KugouAdapter.ViewHolder> {
    private int resId;
    private List<Sheet> list;
    private Context context;
    private int page =1;
    private KugouBang kugouBang;
    public KugouAdapter(Context context, int resId, List<Sheet>list)
    {
             this.resId = resId;
             this.list = list;
             this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).getType().equals("#"))
        {
            return 1;    //标题
        }
        else
        {
            return 0;    //内容
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType)
        {
            case 0 :
                view = LayoutInflater.from(context).inflate(R.layout.view_holder_sheet,parent,false);
                break;
            case 1:
                view = LayoutInflater.from(context).inflate(R.layout.view_holder_sheet_profile,parent,false);
                break;
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Sheet sheet = list.get(position);
        if (sheet.getType().equals("#"))
        {
            holder.title.setText(sheet.getTitle());
        }
        if (!sheet.getType().equals("#")) {
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, KugoubangActivity.class);
                    intent.putExtra("Kugou",sheet);
                    context.startActivity(intent);
                }
            });
            holder.divider.setVisibility(isShowDivider(position)?View.VISIBLE:View.GONE);       //如果条件为true选第一个，否则选第二个
        }
        getSongInfo(sheet,holder);
    }

    private void getSongInfo(final Sheet sheet, final ViewHolder holder) {
        if (sheet.getCoverUrl() == null)
        {
            HttpClinet.getKugoubang(sheet.getType(), page, new HttpCallback<KugouBang>() {
                @Override
                public void onSuccess(KugouBang mkugouBang) {

                    if (mkugouBang == null) {
                        return;
                    }
                    kugouBang = mkugouBang;
                    if(kugouBang.song==null)
                    {
                        return;
                    }
                    if (kugouBang.song.kugouBangList.size()==0)
                    {
                        return;
                    }
                    String coverUrl = kugouBang.info.imgurl.replace("{size}","150");
                    sheet.setCoverUrl(coverUrl);
                    if (kugouBang.song.kugouBangList.size()>=1)
                    {
                        sheet.setMusic1(kugouBang.song.kugouBangList.get(0).filename);
                    }
                    if (kugouBang.song.kugouBangList.size()>=2)
                    {
                        sheet.setMusic2(kugouBang.song.kugouBangList.get(1).filename);
                    }
                    if (kugouBang.song.kugouBangList.size()>=3)
                    {
                        sheet.setMusic3(kugouBang.song.kugouBangList.get(2).filename);
                    }
                    showData(sheet, holder);
                }

                @Override
                public void onFail(Exception e) {
                    Toast.makeText(context,"获取酷狗榜单失败",Toast.LENGTH_LONG).show();
                }
            });
    }
        else
        {
            showData(sheet,holder);
        }
    }

    private void showData(Sheet sheet,ViewHolder holder)
    {
        holder.s1.setText(sheet.getMusic1());
        holder.s2.setText(sheet.getMusic2());
        holder.s3.setText(sheet.getMusic3());
        Glide.with(context)
                .load(sheet.getCoverUrl())
                .placeholder(R.drawable.default_cover)
                .into(holder.cover);
    }


    private boolean isShowDivider(int position)
    {
        if (position!=list.size()-1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView s1,s2,s3,title;
        ImageView cover;
        View divider,view;
        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            divider = itemView.findViewById(R.id.v_divider);
            s1 = itemView.findViewById(R.id.tv_music_1);
            s2 = itemView.findViewById(R.id.tv_music_2);
            s3 = itemView.findViewById(R.id.tv_music_3);
            cover = itemView.findViewById(R.id.iv_cover);
            title = itemView.findViewById(R.id.tv_profile);
        }
    }

}
