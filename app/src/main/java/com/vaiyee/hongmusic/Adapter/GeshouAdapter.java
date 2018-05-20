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
import com.vaiyee.hongmusic.bean.Geshou;

import java.util.List;

import io.vov.vitamio.utils.Log;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Administrator on 2018/4/5.
 */

public class GeshouAdapter extends BaseAdapter {
    private Context context;
    private List<Geshou.DataBean.InfoBean> geshoulist;

    public GeshouAdapter(Context context, List<Geshou.DataBean.InfoBean> geshoulist) {
        this.context = context;
        this.geshoulist = geshoulist;
    }

    @Override
    public int getCount() {
        return geshoulist.size();
    }

    @Override
    public Object getItem(int i) {
        return geshoulist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View containview, ViewGroup viewGroup) {
        View view = null;
        ViewHolder holder = null;
        Geshou.DataBean.InfoBean singger = geshoulist.get(i);
        if (containview==null)
        {
            view = LayoutInflater.from(context).inflate(R.layout.geshou_item,viewGroup,false);
            holder = new ViewHolder();
            holder.touxiang = view.findViewById(R.id.geshou_touxiang);
            holder.top = view.findViewById(R.id.top);
            holder.name = view.findViewById(R.id.name);
            holder.redu = view.findViewById(R.id.redu);
            holder.rank = view.findViewById(R.id.rank);
            view.setTag(holder);
        }
        else
        {
            view = containview;
            holder = (ViewHolder) view.getTag();
        }

        switch (i)
        {
            case 0:

                holder.top.setImageResource(R.drawable.one);
                Log.d("当前的位置"+String.valueOf(i));
                break;

            case 1:

                holder.top.setImageResource(R.drawable.two);
                Log.d("当前的位置"+String.valueOf(i));
               break;
            case 2:
                holder.top.setImageResource(R.drawable.three);
                Log.d("当前的位置"+String.valueOf(i));
            break;
            default:
                holder.top.setImageResource(0);   //除了0,1,2 以外的item要把图片设置为空，不然会其他的item也会显示图片出来
        }



            if (i>2) {
                holder.rank.setText(String.valueOf(i + 1));
            }
            else
            {
                holder.rank.setText("");        //这里非常重要，0,1,2不设置为空的话，会显示其他的数值，如10,11,12
            }
        holder.name.setText(singger.getSingername());
        holder.redu.setText("热度:"+singger.getHeat()+", "+"  粉丝数:"+singger.getFanscount());
        Glide.with(context)
                .load(singger.getImgurl().replace("{size}","150"))
                .placeholder(R.drawable.default_cover)
                .bitmapTransform(new CropCircleTransformation(context))
                .into(holder.touxiang);

        return view;
    }

    class ViewHolder
    {
        ImageView touxiang,top;
        TextView name,redu,rank;
    }
}
