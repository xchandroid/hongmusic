package com.vaiyee.hongmusic.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import com.vaiyee.hongmusic.Utils.DownloadTask;
import com.vaiyee.hongmusic.Utils.HttpClinet;
import com.vaiyee.hongmusic.bean.KugouBang;
import com.vaiyee.hongmusic.bean.KugouBangList;
import com.vaiyee.hongmusic.bean.KugouMusic;
import com.vaiyee.hongmusic.http.HttpCallback;

import java.util.List;

/**
 * Created by Administrator on 2018/3/14.
 */

public class KugouBangAdapter extends BaseAdapter {
    private List<KugouBangList> kugouBangList;
    private int resId;
    private Context context;
    private Button title,download;
    private Activity activity;
    private PopupWindow popupWindow;

    public KugouBangAdapter(Context context, int resId, List<KugouBangList>kugouBangList, Activity activity)
    {
        this.activity = activity;
        this.context = context;
        this.resId = resId;
        this.kugouBangList = kugouBangList;
        popupWindow = new PopupWindow(context);
    }
    @Override
    public int getCount() {
        return kugouBangList.size();
    }

    @Override
    public Object getItem(int i) {
        return kugouBangList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View contentview, ViewGroup viewGroup) {
        View view = null;
        ViewHolder viewHolder = null;
        final KugouBangList song = kugouBangList.get(i);
        final String[]s=song.filename.split("-");
        if (contentview ==null)
        {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(resId,null);
            viewHolder.songaName = view.findViewById(R.id.geming);
            viewHolder.singger = view.findViewById(R.id.geshou);
            viewHolder.ablum = view.findViewById(R.id.ablum);
            viewHolder.more = view.findViewById(R.id.more);
            view.setTag(viewHolder);
        }
        else
        {
            view = contentview;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopu(s[1],s[0],"未知",song.hash,String.valueOf(song.duration*1000));
            }
        });
        viewHolder.songaName.setText(1+i+"."+s[1]);
        viewHolder.singger.setText(s[0]);
        return view;
    }

    private void showPopu(String songName,String geshou,String ablumName,String songId,final String duration) {
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

    private void getdownLoadUrl(String hash, final String songName, final String geshou, final String ablumName, final String duration) {
        HttpClinet.KugouUrl(hash, new HttpCallback<KugouMusic>() {
            @Override
            public void onSuccess(KugouMusic kugouMusic) {
                String url = kugouMusic.getData().getPlay_url();
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url,songName,geshou,ablumName,duration);
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
