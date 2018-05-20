package com.vaiyee.hongmusic.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vaiyee.hongmusic.GeShoutypeActivity;
import com.vaiyee.hongmusic.GeshouInfoActivity;
import com.vaiyee.hongmusic.R;
import com.vaiyee.hongmusic.bean.Geshou;
import com.vaiyee.hongmusic.bean.GeshouType;

import java.util.ArrayList;
import java.util.List;

import io.vov.vitamio.utils.Log;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Administrator on 2018/4/5.
 */

public class GeshouTypeAdapter extends BaseAdapter {
    private Context context;
    private List<GeshouType.Singer> singerList;
    private static int index = 0;
    public GeshouTypeAdapter(Context context,List<GeshouType.Singer> singerList)
    {
        this.context = context;
        this.singerList = singerList;

    }
    @Override
    public int getCount() {
        return singerList.size();
    }

    @Override
    public Object getItem(int i) {
        return singerList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(final int i, View containview, ViewGroup viewGroup) {

        ViewHolder holder = new ViewHolder();
        final GeshouType.Singer singger = singerList.get(i);
        containview = LayoutInflater.from(context).inflate(R.layout.geshou_item, viewGroup, false);
                    holder.name = containview.findViewById(R.id.name);
                    holder.redu = containview.findViewById(R.id.redu);
                    holder.rank = containview.findViewById(R.id.rank);
                    holder.touxiang = containview.findViewById(R.id.geshou_touxiang);
                    holder.name.setText(singger.singername);
                    holder.rank.setText(String.valueOf(i+1));
                    holder.redu.setText("热度:" + singger.heat + ", " + "  粉丝数:" + singger.fanscount);
                    Glide.with(context)
                            .load(singger.imgurl.replace("{size}", "150"))
                            .placeholder(R.drawable.default_cover)
                            .bitmapTransform(new CropCircleTransformation(context))
                            .into(holder.touxiang);
        containview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,GeshouInfoActivity.class);
                intent.putExtra("id",singger.singerid);
                intent.putExtra("name",singger.singername);
                intent.putExtra("img",singger.imgurl);
                intent.putExtra("fans",singger.fanscount);
                context.startActivity(intent);
            }
        });
        return containview;
    }

    class ViewHolder
    {
        ImageView touxiang,top;
        TextView name,redu,rank,title;
    }
}
