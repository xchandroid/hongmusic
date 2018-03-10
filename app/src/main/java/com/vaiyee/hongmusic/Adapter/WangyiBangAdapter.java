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
import com.vaiyee.hongmusic.Utils.DownloadTask;
import com.vaiyee.hongmusic.Utils.HttpClinet;
import com.vaiyee.hongmusic.bean.DownloadInfo;
import com.vaiyee.hongmusic.bean.OnlineMusic;
import com.vaiyee.hongmusic.bean.Tracks;
import com.vaiyee.hongmusic.http.HttpCallback;

import java.util.List;

/**
 * Created by Administrator on 2018/3/9.
 */

public class WangyiBangAdapter extends ArrayAdapter {

    private int resId;
    private List<Tracks> onlineMusics;
    private Context context;
    private Activity activity;
    private PopupWindow popupWindow;
    private Button title,download;
    private static String url = null;
    private static final String Path = "http://music.163.com/song/media/outer/url?id=";
    public WangyiBangAdapter(Context context, int resId, List<Tracks> onlineMusics,Activity activity)
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
        final Tracks onlineMusic = onlineMusics.get(position);
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
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.ablumCover = view.findViewById(R.id.zj_id);
        viewHolder.more = view.findViewById(R.id.more);
        viewHolder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupwindow(onlineMusic.songname,onlineMusic.artist.get(0).artistName,onlineMusic.abulum.ablumName,onlineMusic.Id,String.valueOf(onlineMusic.duration));
            }
        });
        viewHolder.songaName = view.findViewById(R.id.geming);
        viewHolder.singger = view.findViewById(R.id.geshou);
        viewHolder.ablum = view.findViewById(R.id.ablum);
        Glide.with(context)
                .load(onlineMusic.abulum.pic)
                .placeholder(R.drawable.music_ic)
                .error(R.drawable.music_ic)
                .into(viewHolder.ablumCover);
        viewHolder.songaName.setText(1+position+"."+onlineMusic.songname);
        viewHolder.singger.setText(onlineMusic.artist.get(0).artistName);
        viewHolder.ablum.setText("《"+onlineMusic.abulum.ablumName+"》");
        return view;
    }

    private void showPopupwindow(String songName,String geshou,String ablumName,String songId,final String duration) {
        View contentview = LayoutInflater.from(MyApplication.getQuanjuContext()).inflate(R.layout.popuwindow,null);
        initContentview(contentview,songName,geshou,ablumName,songId,duration);
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
    private void initContentview(View view, final String songName,final String geshou,final String ablumName,final String sonId,final String duration)
    {
        title = view.findViewById(R.id.pop_xq);
        download = view.findViewById(R.id.pop_xg);
        title.setText(String.valueOf(Html.fromHtml(songName)));
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getdownLoadUrl(sonId,songName,geshou,ablumName,duration);
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

    private void getdownLoadUrl(String songId, final String songName, final String geshou, final String ablumName,final String duration) {


                url = Path + songId + ".mp3";
                String time = String.valueOf(duration);
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url, songName, geshou, ablumName, time);
                Toast.makeText(MyApplication.getQuanjuContext(), "开始下载", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();

    }

    class ViewHolder
    {
        ImageView ablumCover,more;
        TextView songaName,singger,ablum;
    }
}
