package com.vaiyee.hongmusic.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vaiyee.hongmusic.MyApplication;
import com.vaiyee.hongmusic.R;
import com.vaiyee.hongmusic.SearchActivity;
import com.vaiyee.hongmusic.Utils.DownloadTask;
import com.vaiyee.hongmusic.Utils.HttpClinet;
import com.vaiyee.hongmusic.bean.DownloadInfo;
import com.vaiyee.hongmusic.bean.OnlineMusic;
import com.vaiyee.hongmusic.http.HttpCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/2/6.
 */

public class OnlineMusicAdapter extends ArrayAdapter {
    private int resId;
    private List<OnlineMusic> onlineMusics;
    private Context context;
    private Activity activity;
    private  PopupWindow popupWindow;
    private Button title,download;
    private static String url = null;
    public OnlineMusicAdapter(Context context, int resId, List<OnlineMusic> onlineMusics,Activity activity)
    {
        super(context,resId,onlineMusics);
        this.context=context;
        this.resId = resId;
        this.onlineMusics = onlineMusics;
        this.activity = activity;
        popupWindow = new PopupWindow(context);
    }

    @Override
    public int getCount() {
        return onlineMusics.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return onlineMusics.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        final OnlineMusic onlineMusic = onlineMusics.get(position);
        View view =null;
        ViewHolder viewHolder = null;
        if (convertView == null)
        {
            view =  LayoutInflater.from(context).inflate(resId,parent,false);
            viewHolder = new ViewHolder();
            view.setTag(viewHolder);
        }
        else
        {
            view = convertView;
            viewHolder =(ViewHolder) view.getTag();
        }
        viewHolder.ablumCover = view.findViewById(R.id.zj_id);
        viewHolder.more = view.findViewById(R.id.more);
        viewHolder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupwindow(onlineMusic.getTitle(),onlineMusic.getArtist_name(),onlineMusic.getAlbum_title(),onlineMusic.getSong_id());
            }
        });
        viewHolder.songaName = view.findViewById(R.id.geming);
        viewHolder.singger = view.findViewById(R.id.geshou);
        viewHolder.ablum = view.findViewById(R.id.ablum);
        Glide.with(context)
                .load(onlineMusic.getPic_big())
                .placeholder(R.drawable.music_ic)
                .error(R.drawable.music_ic)
                .into(viewHolder.ablumCover);
        viewHolder.songaName.setText(1+position+"."+onlineMusic.getTitle());
        viewHolder.singger.setText(onlineMusic.getArtist_name());
        viewHolder.ablum.setText("《"+onlineMusic.getAlbum_title()+"》");
        return view;
    }

    private void showPopupwindow(String songName,String geshou,String ablumName,String songId) {
        View contentview = LayoutInflater.from(MyApplication.getQuanjuContext()).inflate(R.layout.popuwindow,null);
        initContentview(contentview,songName,geshou,ablumName,songId);
        popupWindow.setContentView(contentview);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        Drawable drawable = MyApplication.getQuanjuContext().getResources().getDrawable(R.drawable.popubg);
        popupWindow.setBackgroundDrawable(drawable);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.popuAnim);
        popupWindow.setFocusable(true);
        final WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha=0.3f;
        activity.getWindow().setAttributes(params);
        popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER,0,0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                activity.getWindow().setAttributes(params);
            }
        });
    }
    private void initContentview(View view, final String songName,final String geshou,final String ablumName,final String sonId)
    {
        title = view.findViewById(R.id.pop_xq);
        download = view.findViewById(R.id.pop_xg);
        title.setText(String.valueOf(Html.fromHtml(songName)));
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getdownLoadUrl(sonId,songName,geshou,ablumName);
            }
        });
        Button desmiss = view.findViewById(R.id.pop_lol);
        desmiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }

    private void getdownLoadUrl(String songId, final String songName, final String geshou, final String ablumName) {
        HttpClinet.getMusicUrl(songId, new HttpCallback<DownloadInfo>() {
            @Override
            public void onSuccess(DownloadInfo downloadInfo) {
                if (downloadInfo == null || downloadInfo.getBitrate() == null) {
                    onFail(null);
                    return;
                }

                String time = String.valueOf(downloadInfo.getBitrate().getFile_duration() * 1000);
                url = downloadInfo.getBitrate().getFile_link();
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url, songName, geshou, ablumName, time);
                Toast.makeText(MyApplication.getQuanjuContext(), "开始下载", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(MyApplication.getQuanjuContext(), "下载失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class ViewHolder
    {
        ImageView ablumCover,more;
        TextView songaName,singger,ablum;
    }
}
