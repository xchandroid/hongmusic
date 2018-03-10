package com.vaiyee.hongmusic.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.vaiyee.hongmusic.MyApplication;
import com.vaiyee.hongmusic.R;
import com.vaiyee.hongmusic.SearchActivity;
import com.vaiyee.hongmusic.Utils.DownloadTask;
import com.vaiyee.hongmusic.Utils.HttpClinet;
import com.vaiyee.hongmusic.bean.KugouMusic;
import com.vaiyee.hongmusic.bean.KugouSearchResult;
import com.vaiyee.hongmusic.bean.Music;
import com.vaiyee.hongmusic.bean.SearchMusic;
import com.vaiyee.hongmusic.http.HttpCallback;

import org.jsoup.helper.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by Administrator on 2018/2/9.
 */

public class SearchAdapter extends BaseAdapter {
    private List<KugouSearchResult.lists> songList;
    private Context context;
    private int resId;
    private Activity activity;
    private  PopupWindow popupWindow;
    private Button title,download;
    private static String url = null;

    public SearchAdapter(Context context, Activity activity, int resId, List<KugouSearchResult.lists> songList) {
        this.songList = songList;
        this.context = context;
        this.resId = resId;
        this.activity = activity;
        popupWindow = new PopupWindow(context);
    }

    @Override
    public int getCount() {
        return songList.size();
    }

    @Override
    public Object getItem(int i) {
        return songList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        final KugouSearchResult.lists song = songList.get(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(resId, null);
            viewHolder = new ViewHolder();
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.geming = view.findViewById(R.id.searched_songname);
        viewHolder.geshou = view.findViewById(R.id.searched_geshou);
        viewHolder.more = view.findViewById(R.id.search_list_more);
        viewHolder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            showPopuwindow(song.getSongName(),song.getSingerName(),song.getAlbumName(),String.valueOf(song.getDuration()*1000),song.getFileHash());
            }
        });
        String name = song.getSingerName();
       String name1 = name.replace("<em>","");
       String name2 = name1.replace("</em>","");
       String songname = song.getSongName();
       String songname1 = songname.replace("<em>","");
       String songname2 = songname1.replace("</em>","");
        viewHolder.geming.setText(1+i+"."+songname2);
        viewHolder.geshou.setText(name2);
        return view;
    }

    class ViewHolder {
        TextView geming, geshou;
        ImageView more;
    }

    private void showPopuwindow(String songName,String geshou,String ablumName,String time,String hash)
    {
        View contentview = LayoutInflater.from(MyApplication.getQuanjuContext()).inflate(R.layout.popuwindow,null);
        initContentview(contentview,songName,geshou,ablumName,time,hash);
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
        popupWindow.showAtLocation(SearchActivity.listView, Gravity.CENTER,0,0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
               params.alpha = 1.0f;
               activity.getWindow().setAttributes(params);
            }
        });
    }


    private void initContentview(View view, final String songName,final String geshou,final String ablumName,final String time, final String hash)
    {
        title = view.findViewById(R.id.pop_xq);
        download = view.findViewById(R.id.pop_xg);
        title.setText(String.valueOf(Html.fromHtml(songName)));
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             getdownLoadUrl(hash,songName,geshou,ablumName,time);
            }
        });
    }
    private void getdownLoadUrl(String hash, final String geming,final String geshou,final String ablumName,final String time)
    {
        final String subname = String.valueOf(Html.fromHtml(geming));
        final String subgeshou = String.valueOf(Html.fromHtml(geshou));
        final String subablumName = String.valueOf(Html.fromHtml(ablumName));
        HttpClinet.KugouUrl(hash, new HttpCallback<KugouMusic>() {
            @Override
            public void onSuccess(KugouMusic kugouMusic) {
               String path = "/storage/emulated/0/HonchenMusic/download/" + subname+".mp3";
                File file = new File(path);
                if (file.exists())
                {
                    Toast.makeText(MyApplication.getQuanjuContext(),"该歌曲已下载过啦",Toast.LENGTH_LONG).show();
                    popupWindow.dismiss();
                    return;
                }
                url = kugouMusic.getData().getPlay_url();
                DownloadTask task = new DownloadTask();
                task.execute(url,subname,subgeshou,subablumName,time);
                Toast.makeText(MyApplication.getQuanjuContext(),"开始下载",Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }

}
