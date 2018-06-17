package com.vaiyee.hongmusic.Adapter;

import android.app.Activity;
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
import com.vaiyee.hongmusic.http.WangyibangBackListener;
import com.vaiyee.hongmusic.util.HttpUtil;

import java.util.List;

/**
 * Created by Administrator on 2018/3/7.
 */

public class WangYiAdapter extends BaseAdapter {
    private Context context;
    private List<Sheet> typeList; //明天测试传过来的数据有没有问题
    private int Id;
    private   List<Tracks> tracksList = null;
    private Activity activity;

    public WangYiAdapter(Context context,int Id, List<Sheet> sheetList,Activity activity) {
        this.context = context;
        this.Id = Id;
        this.typeList = sheetList;
        this.activity = activity;
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
            case "19723756":
                holder.cover.setImageResource(R.drawable.biaosheng);
                break;
            case "3779629":
                holder.cover.setImageResource(R.drawable.xinge);
                break;
            case "2884035":
                holder.cover.setImageResource(R.drawable.yuanchuang);
                break;
            case "3778678":
                holder.cover.setImageResource(R.drawable.rege);
                break;
            case "1978921795":
            holder.cover.setImageResource(R.drawable.dianyin);
                break;
            case "991319590":
                holder.cover.setImageResource(R.drawable.xiha);
                break;
            case "2250011882":
                holder.cover.setImageResource(R.drawable.acg);
                break;
            case "60131":
                holder.cover.setImageResource(R.drawable.oricon);
                break;
            case "10520166":
                holder.cover.setImageResource(R.drawable.xindianli);
                break;
            case "60198":
                holder.cover.setImageResource(R.drawable.meiguo);
                break;
            case "11641012":
                holder.cover.setImageResource(R.drawable.itunes);
                break;
            case "71384707":
                holder.cover.setImageResource(R.drawable.gudian);
                break;
            case "21845217":
                holder.cover.setImageResource(R.drawable.ktv);
                break;
            case "112463":
                holder.cover.setImageResource(R.drawable.hito);
                break;
            case "112504":
                holder.cover.setImageResource(R.drawable.gangtai);
                break;
            case "64016":
                holder.cover.setImageResource(R.drawable.neidi);
                break;
            case "1899724":
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
            HttpUtil.getWangyiBang(sheet.getType(), new WangyibangBackListener() {
                @Override
                public void onSuccess(WangyiBang wangyiBang) {
                    sheet.setMusic1(wangyiBang.getList().get(0).getName()+" - "+wangyiBang.getList().get(0).getArtists().get(0).getName());        //把歌名存到sheet中,为了不每次都访问网络获取数据浪费流量
                    sheet.setMusic2(wangyiBang.getList().get(1).getName()+" - "+wangyiBang.getList().get(1).getArtists().get(0).getName());
                    sheet.setMusic3(wangyiBang.getList().get(2).getName()+" - "+wangyiBang.getList().get(2).getArtists().get(0).getName());
                    sheet.setCoverUrl("标志位，防止重复访问网络获取数据");
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showData(sheet,holder);
                        }
                    });
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
