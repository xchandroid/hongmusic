package com.vaiyee.hongmusic.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
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
import com.vaiyee.hongmusic.MainActivity;
import com.vaiyee.hongmusic.MyApplication;
import com.vaiyee.hongmusic.PlayMusic;
import com.vaiyee.hongmusic.R;
import com.vaiyee.hongmusic.SearchActivity;
import com.vaiyee.hongmusic.Utils.DownloadTask;
import com.vaiyee.hongmusic.Utils.HttpClinet;
import com.vaiyee.hongmusic.Utils.NetUtils;
import com.vaiyee.hongmusic.Utils.NotiUtil;
import com.vaiyee.hongmusic.bean.DownloadInfo;
import com.vaiyee.hongmusic.bean.OnlineLrc;
import com.vaiyee.hongmusic.bean.OnlineMusic;
import com.vaiyee.hongmusic.bean.Song;
import com.vaiyee.hongmusic.http.HttpCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/2/6.
 */

public class OnlineMusicAdapter extends RecyclerView.Adapter<OnlineMusicAdapter.ViewHolder> {
    private int resId;
    private List<OnlineMusic> onlineMusics;
    private Context context;
    private Activity activity;
    private  PopupWindow popupWindow;
    private Button title,download;
    private static String url = null;
    private static String Lrccontent = null;
    private List<Song> songList;
    public OnlineMusicAdapter(Context context, int resId, List<OnlineMusic> onlineMusics,Activity activity)
    {
        this.context=context;
        this.resId = resId;
        this.onlineMusics = onlineMusics;
        this.activity = activity;
        popupWindow = new PopupWindow(context);
    }

/*
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

    */

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
                DownloadTask downloadTask = new DownloadTask(NotiUtil.listener);
                downloadTask.execute(url, songName, geshou, ablumName,time,String.valueOf(NotiUtil.getRandom()));
                Toast.makeText(MyApplication.getQuanjuContext(), "开始下载", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(MyApplication.getQuanjuContext(), "下载失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view =  LayoutInflater.from(context).inflate(resId,parent,false);
       ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final OnlineMusic onlineMusic = onlineMusics.get(position);
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupwindow(onlineMusic.getTitle(),onlineMusic.getArtist_name(),onlineMusic.getAlbum_title(),onlineMusic.getSong_id());
            }
        });
        Glide.with(context)
                .load(onlineMusic.getPic_big())
                .placeholder(R.drawable.music_ic)
                .error(R.drawable.music_ic)
                .into(holder.ablumCover);
        holder.songaName.setText(1+position+"."+onlineMusic.getTitle());
        holder.singger.setText(onlineMusic.getArtist_name());
        holder.ablum.setText("《"+onlineMusic.getAlbum_title()+"》");
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemclick(position);
            }
        });

    }

    private void onItemclick(final int i)
    {
        final OnlineMusic onlineMusic = onlineMusics.get(i);
        final String songID = onlineMusic.getSong_id();
        final String geming = onlineMusic.getTitle();
        HttpClinet.getLrc(songID, new HttpCallback<OnlineLrc>() {
            @Override
            public void onSuccess(OnlineLrc onlineLrc) {
                Lrccontent = onlineLrc.getLrcContent();
                SearchActivity.creatLrc(Lrccontent,geming);

            }

            @Override
            public void onFail(Exception e) {

            }
        });

        AlertDialog.Builder builder;
        switch (NetUtils.getNetType())
        {
            case NET_WIFI:
                HttpClinet.getMusicUrl(songID, new HttpCallback<DownloadInfo>() {
                    @Override
                    public void onSuccess(DownloadInfo downloadInfo) {
                        if (downloadInfo == null || downloadInfo.getBitrate() == null) {
                            onFail(null);
                            return;
                        }
                        String path = downloadInfo.getBitrate().getFile_link();
                        PlayMusic playMusic =new PlayMusic();
                        playMusic.play(path,i);
                        String songname = onlineMusic.getTitle().toString();
                        String geshou = onlineMusic.getArtist_name().toString();
                        String coverUrl = onlineMusic.getPic_big().toString();
                        Log.d("路径是",path);
                        int time = downloadInfo.getBitrate().getFile_duration()*1000;
                        MainActivity mainActivity = new MainActivity();
                        mainActivity.tongbuShow(songname,geshou,coverUrl,time,MainActivity.ONLINE);
                        addToNowPlaylist();

                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onFail(Exception e) {

                    }

                });
                break;
            case NET_4G:
                builder = new AlertDialog.Builder(context);
                builder.setMessage("当前正在使用4G网络，是否使用数据流量播放在线音乐？");
                builder.setTitle("提示");
                builder.setIcon(R.drawable.tip);
                builder.setPositiveButton("流量多",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                HttpClinet.getMusicUrl(songID, new HttpCallback<DownloadInfo>() {
                                    @Override
                                    public void onSuccess(DownloadInfo downloadInfo) {
                                        if (downloadInfo == null || downloadInfo.getBitrate() == null) {
                                            onFail(null);
                                            return;
                                        }
                                        String path = downloadInfo.getBitrate().getFile_link();
                                        PlayMusic playMusic =new PlayMusic();
                                        playMusic.play(path,i);
                                        String songname = onlineMusic.getTitle().toString();
                                        String geshou = onlineMusic.getArtist_name().toString();
                                        String coverUrl = onlineMusic.getPic_big().toString();
                                        Log.d("路径是",path);
                                        int time = downloadInfo.getBitrate().getFile_duration()*1000;
                                        MainActivity mainActivity = new MainActivity();
                                        mainActivity.tongbuShow(songname,geshou,coverUrl,time,MainActivity.ONLINE);
                                        addToNowPlaylist();
                                    }

                                    @Override
                                    public void onFinish() {

                                    }

                                    @Override
                                    public void onFail(Exception e) {

                                    }

                                });
                                dialog.dismiss();
                            }
                        });

                builder.setNegativeButton("伤不起",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();

                break;
            case NET_3G:
                builder = new AlertDialog.Builder(context);
                builder.setMessage("当前正在使用3G网络，是否使用数据流量播放在线音乐？");
                builder.setTitle("提示");
                builder.setIcon(R.drawable.tip);
                builder.setPositiveButton("流量多",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                HttpClinet.getMusicUrl(songID, new HttpCallback<DownloadInfo>() {
                                    @Override
                                    public void onSuccess(DownloadInfo downloadInfo) {
                                        if (downloadInfo == null || downloadInfo.getBitrate() == null) {
                                            onFail(null);
                                            return;
                                        }
                                        String path = downloadInfo.getBitrate().getFile_link();
                                        PlayMusic playMusic =new PlayMusic();
                                        playMusic.play(path,i);
                                        String songname = onlineMusic.getTitle().toString();
                                        String geshou = onlineMusic.getArtist_name().toString();
                                        String coverUrl = onlineMusic.getPic_big().toString();
                                        Log.d("路径是",path);
                                        int time = downloadInfo.getBitrate().getFile_duration()*1000;
                                        MainActivity mainActivity = new MainActivity();
                                        mainActivity.tongbuShow(songname,geshou,coverUrl,time,MainActivity.ONLINE);
                                        addToNowPlaylist();
                                    }

                                    @Override
                                    public void onFinish() {

                                    }

                                    @Override
                                    public void onFail(Exception e) {

                                    }

                                });
                                dialog.dismiss();
                            }
                        });

                builder.setNegativeButton("伤不起",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();

                break;
            case NET_2G:
                builder = new AlertDialog.Builder(context);
                builder.setMessage("当前正在使用2G网络，播放在线音乐会卡顿哟");
                builder.setTitle("提示");
                builder.setIcon(R.drawable.tip);
                builder.setPositiveButton("流量多",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                HttpClinet.getMusicUrl(songID, new HttpCallback<DownloadInfo>() {
                                    @Override
                                    public void onSuccess(DownloadInfo downloadInfo) {
                                        if (downloadInfo == null || downloadInfo.getBitrate() == null) {
                                            onFail(null);
                                            return;
                                        }
                                        String path = downloadInfo.getBitrate().getFile_link();
                                        PlayMusic playMusic =new PlayMusic();
                                        playMusic.play(path,i);
                                        String songname = onlineMusic.getTitle().toString();
                                        String geshou = onlineMusic.getArtist_name().toString();
                                        String coverUrl = onlineMusic.getPic_big().toString();
                                        Log.d("路径是",path);
                                        int time = downloadInfo.getBitrate().getFile_duration()*1000;
                                        MainActivity mainActivity = new MainActivity();
                                        mainActivity.tongbuShow(songname,geshou,coverUrl,time,MainActivity.ONLINE);
                                        addToNowPlaylist();
                                    }

                                    @Override
                                    public void onFinish() {

                                    }

                                    @Override
                                    public void onFail(Exception e) {

                                    }

                                });
                                dialog.dismiss();
                            }
                        });

                builder.setNegativeButton("伤不起",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();

                break;
            default:
                break;
        }

    }


    private void addToNowPlaylist()
    {
        songList = new ArrayList<>();
        //将当前列表添加到正在播放列表
        for (int k=0;k<onlineMusics.size();k++)
        {
            Song song = new Song();
            song.setTitle(onlineMusics.get(k).getTitle());
            song.setSinger(onlineMusics.get(k).getArtist_name());
            song.setDuration(360000);
            song.setFileUrl(onlineMusics.get(k).getSong_id());
            songList.add(song);
        }
        PlayMusic.PlayList playList = new PlayMusic.PlayList();
        playList.setPlaylist(songList);
        playList.setBang(1);  //表示百度音乐榜
    }

    @Override
    public int getItemCount() {
        return onlineMusics.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView ablumCover,more;
        TextView songaName,singger,ablum;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            ablumCover = itemView.findViewById(R.id.zj_id);
            more = itemView.findViewById(R.id.more);
            songaName = itemView.findViewById(R.id.geming);
            singger = itemView.findViewById(R.id.geshou);
            ablum = itemView.findViewById(R.id.ablum);
            view = itemView;
        }
    }
}
