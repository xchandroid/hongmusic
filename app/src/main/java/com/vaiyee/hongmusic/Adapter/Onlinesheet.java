package com.vaiyee.hongmusic.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vaiyee.hongmusic.R;
import com.vaiyee.hongmusic.Utils.HttpClinet;
import com.vaiyee.hongmusic.bean.OnlineMusic;
import com.vaiyee.hongmusic.bean.OnlineMusiclist;
import com.vaiyee.hongmusic.bean.Sheet;
import com.vaiyee.hongmusic.http.HttpCallback;

import java.util.List;

/**
 * Created by Administrator on 2018/2/5.
 */

public class Onlinesheet extends BaseAdapter{

    private List<Sheet> sheetList;
    private static final int TYPE_PROFILE = 0;
    private static final int TYPE_MUSIC_LIST = 1;
    private Context context;
    public Onlinesheet(Context context,  List<Sheet> sheetlist)
    {
        this.context = context;
        this.sheetList = sheetlist;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return sheetList.get(position);
    }

    @Override
    public int getCount() {
        return sheetList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (sheetList.get(position).getType().equals("#"))
        {
            return TYPE_PROFILE;
        }
        else
        {
            return TYPE_MUSIC_LIST;
        }

    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) == TYPE_MUSIC_LIST;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
         MusicviewHolder musicviewHolder = null;
         TitleviewHolder titleviewHolder =null;
         Sheet sheet = sheetList.get(position);

             switch (getItemViewType(position))
             {

                 case TYPE_MUSIC_LIST:
                     if (convertView==null)
                     {
                         musicviewHolder = new MusicviewHolder();
                         convertView = LayoutInflater.from(context).inflate(R.layout.view_holder_sheet, parent, false);
                         musicviewHolder.cover = convertView.findViewById(R.id.iv_cover);
                         musicviewHolder.s1 = convertView.findViewById(R.id.tv_music_1);
                         musicviewHolder.s2 = convertView.findViewById(R.id.tv_music_2);
                         musicviewHolder.s3 = convertView.findViewById(R.id.tv_music_3);
                         musicviewHolder.divider = convertView.findViewById(R.id.v_divider);
                         convertView.setTag(musicviewHolder);
                     }
                     else
                     {
                         musicviewHolder = (MusicviewHolder)convertView.getTag();
                     }
                         getsongInfo(sheet,musicviewHolder);
                         musicviewHolder.divider.setVisibility(isShowDivider(position) ? View.VISIBLE : View.GONE);
                     break;
                 case TYPE_PROFILE:
                     if (convertView==null) {

                         convertView = LayoutInflater.from(context).inflate(R.layout.view_holder_sheet_profile, parent, false);
                         titleviewHolder = new TitleviewHolder();
                         convertView.setTag(titleviewHolder);
                     }
                     else
                     {
                         titleviewHolder = (TitleviewHolder)convertView.getTag();
                     }

                     titleviewHolder.title= convertView.findViewById(R.id.tv_profile);
                     titleviewHolder.title.setText(sheet.getTitle());
                     break;

             }

         return convertView;

    }

    private void getsongInfo(final Sheet sheet, final MusicviewHolder musicviewHolder) {
        if (sheet.getCoverUrl()==null) {
           // musicviewHolder.s1.setTag(sheet.getTitle());
            HttpClinet.getOnlineMusicList(sheet.getType(), 3, 0, new HttpCallback<OnlineMusiclist>() {
                @Override
                public void onSuccess(OnlineMusiclist response) {
                    if (response == null || response.getSong_list() == null) {
                        return;
                    }
                   /* if (!sheet.getTitle().equals(musicviewHolder.s1.getTag()))
                    {
                        return;
                    }
                    */
                    parse(response, sheet);
                    setData(sheet, musicviewHolder);
                }

                @Override
                public void onFail(Exception e) {

                }
            });
        }
        else
        {
            //musicviewHolder.s1.setTag(null);
            setData(sheet,musicviewHolder);
        }
    }
   //将返回的在线音乐列表解析为歌曲
    private void parse(OnlineMusiclist response, Sheet sheet) {
     List<OnlineMusic> onlineMusics = response.getSong_list();
     sheet.setCoverUrl(response.getBillboard().getPic_s260());
        if (onlineMusics.size() >= 1) {
            sheet.setMusic1(onlineMusics.get(0).getTitle() + " - " + onlineMusics.get(0).getArtist_name());
        } else {
            sheet.setMusic1("");
        }
        if (onlineMusics.size() >= 2) {
            sheet.setMusic2(onlineMusics.get(1).getTitle() + " - " + onlineMusics.get(1).getArtist_name());
        } else {
            sheet.setMusic2("");
        }
        if (onlineMusics.size() >= 3) {
            sheet.setMusic3( onlineMusics.get(2).getTitle() + " - " + onlineMusics.get(2).getArtist_name());
        } else {
            sheet.setMusic3("");
        }

    }
    //将解析出来的歌曲设置到控件上显示
    private void setData(Sheet sheet,MusicviewHolder musicviewHolder)
    {
        musicviewHolder.s1.setText(sheet.getMusic1());
        musicviewHolder.s2.setText(sheet.getMusic2());
        musicviewHolder.s3.setText(sheet.getMusic3());
        Glide.with(context)
                .load(sheet.getCoverUrl())
                .placeholder(R.drawable.music_ic)
                .into(musicviewHolder.cover);
    }
    //最后一个位置不显示分割线
    private boolean isShowDivider(int position)
    {
        if (position!=sheetList.size()-1)
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
    class TitleviewHolder
    {
        TextView title;
    }
}
