package com.vaiyee.hongmusic.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vaiyee.hongmusic.R;
import com.vaiyee.hongmusic.Utils.HttpClinet;
import com.vaiyee.hongmusic.bean.Sheet;
import com.vaiyee.hongmusic.bean.Tracks;
import com.vaiyee.hongmusic.bean.WangyiBang;
import com.vaiyee.hongmusic.http.HttpCallback;

import java.util.List;

/**
 * Created by Administrator on 2018/3/7.
 */

public class WangYiAdapter extends BaseAdapter {
    private Context context;
    private List<Sheet> typeList; //明天测试传过来的数据有没有问题
    private int Id;
    private   List<Tracks> tracksList = null;

    public WangYiAdapter(Context context,int Id, List<Sheet> sheetList) {
        this.context = context;
        this.Id = Id;
        this.typeList = sheetList;
    }

    @Override
    public int getCount() {
        return typeList.size();
    }

    @Override
    public Object getItem(int i) {
        return typeList.get(1);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View contentview, ViewGroup viewGroup) {
        View view = null;
        MusicviewHolder holder = null;
        Sheet sheet =typeList.get(position);
        if (contentview==null)
        {
            view = LayoutInflater.from(context).inflate(Id,viewGroup,false);
            holder = new MusicviewHolder();
            holder.cover = view.findViewById(R.id.iv_cover);
            holder.s1 = view.findViewById(R.id.tv_music_1);
            holder.s2 = view.findViewById(R.id.tv_music_2);
            holder.s3 = view.findViewById(R.id.tv_music_3);
            holder.divider = view.findViewById(R.id.v_divider);
            view.setTag(holder);
        }
        else
        {
            view = contentview;
            holder = (MusicviewHolder) view.getTag();
        }
        holder.divider.setVisibility(isShowDivider(position) ? View.VISIBLE : View.GONE);
        getsongInfo(sheet,holder);
        switch (sheet.getType())
        {
            case "3":
                holder.cover.setImageResource(R.drawable.biaosheng);
                break;
            case "0":
                holder.cover.setImageResource(R.drawable.xinge);
                break;
            case "2":
                holder.cover.setImageResource(R.drawable.yuanchuang);
                break;
            case "1":
                holder.cover.setImageResource(R.drawable.rege);
                break;
            case "4":
            holder.cover.setImageResource(R.drawable.dianyin);
                break;
            case "23":
                holder.cover.setImageResource(R.drawable.xiha);
                break;
            case "22":
                holder.cover.setImageResource(R.drawable.acg);
                break;
            case "10":
                holder.cover.setImageResource(R.drawable.oricon);
                break;
            case "17":
                holder.cover.setImageResource(R.drawable.gudian);
                break;
            case "6":
                holder.cover.setImageResource(R.drawable.meiguo);
                break;
            case "8":
                holder.cover.setImageResource(R.drawable.itunes);
                break;
            case "11":
                holder.cover.setImageResource(R.drawable.nrj);
                break;
            case "7":
                holder.cover.setImageResource(R.drawable.ktv);
                break;
            case "20":
                holder.cover.setImageResource(R.drawable.hito);
                break;
            case "14":
                holder.cover.setImageResource(R.drawable.gangtai);
                break;
            case "15":
                holder.cover.setImageResource(R.drawable.neidi);
                break;
            case "18":
                holder.cover.setImageResource(R.drawable.chinaxiha);
                break;
                default:
                    break;
        }
        return view;
    }

    private void getsongInfo(final Sheet sheet, final MusicviewHolder holder)
    {
        if (sheet.getCoverUrl()==null) {
            HttpClinet.WangyiBang(sheet.getType(), new HttpCallback<WangyiBang>() {
                @Override
                public void onSuccess(WangyiBang wangyiBang) {
                    if (wangyiBang.result.tracksList != null) {
                        tracksList = wangyiBang.result.tracksList;
                    } else {
                        return;
                    }

                   sheet.setMusic1(tracksList.get(0).songname+" - "+tracksList.get(0).artist.get(0).artistName);        //把歌名存到sheet中,为了不每次都访问网络获取数据浪费流量
                   sheet.setMusic2(tracksList.get(1).songname+" - "+tracksList.get(1).artist.get(0).artistName);
                   sheet.setMusic3(tracksList.get(2).songname+" - "+tracksList.get(2).artist.get(0).artistName);
                    sheet.setCoverUrl("标志位，防止重复访问网络获取数据");
                    showData(sheet,holder);
                }

                @Override
                public void onFail(Exception e) {

                }
            });
        }
        else
        {
          showData(sheet,holder);        //已经获取过数据，直接从sheet中取出数据显示到TextView
        }
    }

    private void showData(Sheet sheet, MusicviewHolder holder) {
        holder.s1.setText(sheet.getMusic1());
        holder.s2.setText(sheet.getMusic2());
        holder.s3.setText(sheet.getMusic3());
    }

    //最后一个位置不显示分割线
    private boolean isShowDivider(int position)
    {
        if (position!=typeList.size()-1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    class MusicviewHolder
    {
        ImageView cover;
        TextView s1,s2,s3;
        View divider;
    }

}
