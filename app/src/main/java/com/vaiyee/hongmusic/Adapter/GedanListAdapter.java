package com.vaiyee.hongmusic.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.vaiyee.hongmusic.DownloadListener;
import com.vaiyee.hongmusic.GedanActivity;
import com.vaiyee.hongmusic.KugoubangActivity;
import com.vaiyee.hongmusic.MainActivity;
import com.vaiyee.hongmusic.MyApplication;
import com.vaiyee.hongmusic.PlayMusic;
import com.vaiyee.hongmusic.R;
import com.vaiyee.hongmusic.SearchActivity;
import com.vaiyee.hongmusic.Utils.DownloadTask;
import com.vaiyee.hongmusic.Utils.HttpClinet;
import com.vaiyee.hongmusic.Utils.NetUtils;
import com.vaiyee.hongmusic.Utils.NotiUtil;
import com.vaiyee.hongmusic.bean.GedanList;
import com.vaiyee.hongmusic.bean.KugouBangList;
import com.vaiyee.hongmusic.bean.KugouMusic;
import com.vaiyee.hongmusic.bean.Song;
import com.vaiyee.hongmusic.http.HttpCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/28.
 */

public class GedanListAdapter extends RecyclerView.Adapter<GedanListAdapter.ViewHolder> {
    private List<GedanList.Info> songlist;
    private Context context;
    private List<Song> songList;
    private Activity activity;
    private static String geshou,geming,coverUrl,lrc;
    private ProgressDialog progressDialog;
    private static  int duration;
    private PopupWindow popupWindow;
    private Button title,download;
    private ShowBottonPal showBottonPal;
    public static boolean duoxuan =false; // 长按进入多选的标志位
    private static SparseBooleanArray selectedposition = new SparseBooleanArray();  //默认每个条目为false
    public GedanListAdapter(Context context, List<GedanList.Info> songlist,Activity activity) {

        this.context = context;
        this.songlist = songlist;
        this.activity =activity;
        popupWindow = new PopupWindow(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.localmusi_listitem,parent,false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final GedanList.Info song = songlist.get(position);
        holder.geming.setText(1+position+"."+song.filename.split(" - ")[1]);
        holder.geshou.setText(song.filename.split(" - ")[0]);
        holder.cover.setVisibility(View.GONE);
        final String[] s = song.filename.split(" - ");
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                songList = new ArrayList<>();
                String hash = song.hash;
                geming = s[1];
                geshou = s[0];
                duration = song.duration*1000;
                getSongUrl(hash,position);
            }
        });
        if (duoxuan) //根据标志位来显示|隐藏 checkbox
        {
            holder.more.setVisibility(View.GONE);
            holder.checkBox.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.more.setVisibility(View.VISIBLE);
            holder.checkBox.setVisibility(View.GONE);
        }

        //长按多选，批量下载
        if (!duoxuan)
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                duoxuan =true; //标志长按进入多选模式
                NotiDataChange();//刷新列表重新显示
                showBottonPal.show();
                return true;//表示只触发长按事件，屏蔽单击事件（防止单击和长按同时触发）
            }
        });
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedposition.get(position)) // 根据位置判断条目是否选中
                {
                    selectedposition.put(position,false); //如果选中就相反
                }
                else
                {
                    selectedposition.put(position,true);
                }
            }
        });
        holder.checkBox.setChecked(selectedposition.get(position));  //根据position保存的是否选中状态设置checkbox的状态
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopu(s[1],s[0],"未知",song.hash,String.valueOf(song.duration*1000));
            }
        });

    }

    //批量下载方法
    public void startDownload()
    {
        for (int j = 0;j<songlist.size();j++)
        {
            if (selectedposition.get(j)==true)
            {
                GedanList.Info song = songlist.get(j);
                final String hash = song.hash;
                final String id =String.valueOf(j);
                final String songname = String.valueOf(Html.fromHtml((song.filename.split(" - ")[1])));
                final String singger = String.valueOf(Html.fromHtml(song.filename.split(" - ")[0]));
                final int duration = song.duration*1000;
                HttpClinet.KugouUrl(hash, new HttpCallback<KugouMusic>() {
                            @Override
                            public void onSuccess(KugouMusic kugouMusic) {
                                String url = kugouMusic.getData().getPlay_url();
                                DownloadTask downloadTask = new DownloadTask(listener);
                                downloadTask.execute(url,songname,singger,"未知",String.valueOf(duration),id);
                            }

                            @Override
                            public void onFail(Exception e) {

                            }
                        });
                Log.d("选中了",String.valueOf(j));
            }
        }
    }

    DownloadListener listener = new DownloadListener() {
        @Override
        public void onProgress(String geming, int progress,int notiID) {
            getManager().notify(notiID,getNotification(geming,progress));
        }


        @Override
        public void onSuccess(String geming,int notiID) {
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(notiID);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentTitle(geming);
            builder.setContentText("下载完成！");
            builder.setSmallIcon(R.drawable.xiazai1);
            builder.setContentIntent(pendingIntent);
            builder.setAutoCancel(true);//点击后清除通知
            manager.notify(notiID,builder.build());
        }

        @Override
        public void onFail() {

        }
    };
    private NotificationManager getManager()
    {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }
    private Notification getNotification(String geming, int progress)
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentText("《"+geming+"》"+"  正在下载："+progress+"%");
        builder.setContentTitle("歌曲");
        builder.setSmallIcon(R.drawable.xiazai1);
        builder.setProgress(100, progress, false);
        return builder.build();
    }



    private void NotiDataChange()
    {
        notifyDataSetChanged();
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
                DownloadTask downloadTask = new DownloadTask(listener);
                downloadTask.execute(url,songName,geshou,ablumName,duration,String.valueOf(NotiUtil.getRandom()));
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
    public int getItemCount() {
        return songlist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView geming,geshou;
        View view;
        ImageView more,cover;
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            geming = itemView.findViewById(R.id.geming);
            geshou = itemView.findViewById(R.id.geshou);
            more = itemView.findViewById(R.id.more);
            cover = itemView.findViewById(R.id.zj_id);
            checkBox = itemView.findViewById(R.id.checkbox);
            view = itemView;
        }
    }


    private void getSongUrl(final String hash,final int i) {
        AlertDialog.Builder builder;
        switch (NetUtils.getNetType()) {
            case NET_WIFI:
                HttpClinet.KugouUrl(hash, new HttpCallback<KugouMusic>() {
                    @Override
                    public void onSuccess(KugouMusic kugouMusic) {
                        String path = kugouMusic.getData().getPlay_url();
                        if (path != null) {
                            Log.d("歌曲地址是", path);
                            PlayMusic playMusic = new PlayMusic();
                            playMusic.play(path, i);
                            coverUrl = kugouMusic.getData().getImg();
                            lrc = kugouMusic.getData().getLyrics();
                            SearchActivity.creatLrc(lrc, geming);
                            MainActivity mainActivity = new MainActivity();
                            mainActivity.tongbuShow(geming, geshou, coverUrl, duration, MainActivity.ONLINE);
                            // 将播放列表加入当前播放列表
                            for (int i=0;i<songlist.size();i++)
                            {
                                String[] s = songlist.get(i).filename.split(" - ");
                                Song song = new Song();
                                song.setTitle(s[1]);
                                song.setSinger(s[0]);
                                song.setDuration(songlist.get(i).duration*1000);
                                song.setFileUrl(songlist.get(i).hash);
                                songList.add(song);
                            }
                            PlayMusic.PlayList playList = new PlayMusic.PlayList();
                            playList.setPlaylist(songList);
                            playList.setBang(2);  //2表示酷狗列表

                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        CloseProgress();
                        Toast.makeText(context, "获取音乐歌单失败！请检查网络设置", Toast.LENGTH_LONG).show();
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
                                HttpClinet.KugouUrl(hash, new HttpCallback<KugouMusic>() {
                                    @Override
                                    public void onSuccess(KugouMusic kugouMusic) {
                                        String path = kugouMusic.getData().getPlay_url();
                                        if (path != null) {
                                            Log.d("歌曲地址是", path);
                                            PlayMusic playMusic = new PlayMusic();
                                            playMusic.play(path, i);
                                            coverUrl = kugouMusic.getData().getImg();
                                            lrc = kugouMusic.getData().getLyrics();
                                            SearchActivity.creatLrc(lrc, geming);
                                            MainActivity mainActivity = new MainActivity();
                                            mainActivity.tongbuShow(geming, geshou, coverUrl, duration, MainActivity.ONLINE);

                                            // 将播放列表加入当前播放列表
                                            for (int i=0;i<songlist.size();i++)
                                            {
                                                String[] s = songlist.get(i).filename.split(" - ");
                                                Song song = new Song();
                                                song.setTitle(s[1]);
                                                song.setSinger(s[0]);
                                                song.setDuration(songlist.get(i).duration*1000);
                                                song.setFileUrl(songlist.get(i).hash);
                                                songList.add(song);
                                            }
                                            PlayMusic.PlayList playList = new PlayMusic.PlayList();
                                            playList.setPlaylist(songList);
                                            playList.setBang(2);  //2表示酷狗列表

                                        }
                                    }

                                    @Override
                                    public void onFail(Exception e) {
                                        CloseProgress();
                                        Toast.makeText(context, "获取音乐歌单失败！请检查网络设置", Toast.LENGTH_LONG).show();
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
                                HttpClinet.KugouUrl(hash, new HttpCallback<KugouMusic>() {
                                    @Override
                                    public void onSuccess(KugouMusic kugouMusic) {
                                        String path = kugouMusic.getData().getPlay_url();
                                        if (path != null) {
                                            Log.d("歌曲地址是", path);
                                            PlayMusic playMusic = new PlayMusic();
                                            playMusic.play(path, i);
                                            coverUrl = kugouMusic.getData().getImg();
                                            lrc = kugouMusic.getData().getLyrics();
                                            SearchActivity.creatLrc(lrc, geming);
                                            MainActivity mainActivity = new MainActivity();
                                            mainActivity.tongbuShow(geming, geshou, coverUrl, duration, MainActivity.ONLINE);

                                            // 将播放列表加入当前播放列表
                                            for (int i=0;i<songlist.size();i++)
                                            {
                                                String[] s = songlist.get(i).filename.split(" - ");
                                                Song song = new Song();
                                                song.setTitle(s[1]);
                                                song.setSinger(s[0]);
                                                song.setDuration(songlist.get(i).duration*1000);
                                                song.setFileUrl(songlist.get(i).hash);
                                                songList.add(song);
                                            }
                                            PlayMusic.PlayList playList = new PlayMusic.PlayList();
                                            playList.setPlaylist(songList);
                                            playList.setBang(2);  //2表示酷狗列表

                                        }
                                    }

                                    @Override
                                    public void onFail(Exception e) {
                                        CloseProgress();
                                        Toast.makeText(context, "获取音乐歌单失败！请检查网络设置", Toast.LENGTH_LONG).show();
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
                builder.setMessage("当前正在使用2G网络，缓冲较慢，确定是否播放在线音乐？");
                builder.setTitle("提示");
                builder.setIcon(R.drawable.tip);
                builder.setPositiveButton("流量多",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                HttpClinet.KugouUrl(hash, new HttpCallback<KugouMusic>() {
                                    @Override
                                    public void onSuccess(KugouMusic kugouMusic) {
                                        String path = kugouMusic.getData().getPlay_url();
                                        if (path != null) {
                                            Log.d("歌曲地址是", path);
                                            PlayMusic playMusic = new PlayMusic();
                                            playMusic.play(path, i);
                                            coverUrl = kugouMusic.getData().getImg();
                                            lrc = kugouMusic.getData().getLyrics();
                                            SearchActivity.creatLrc(lrc, geming);
                                            MainActivity mainActivity = new MainActivity();
                                            mainActivity.tongbuShow(geming, geshou, coverUrl, duration, MainActivity.ONLINE);

                                            // 将播放列表加入当前播放列表
                                            for (int i=0;i<songlist.size();i++)
                                            {
                                                String[] s = songlist.get(i).filename.split(" - ");
                                                Song song = new Song();
                                                song.setTitle(s[1]);
                                                song.setSinger(s[0]);
                                                song.setDuration(songlist.get(i).duration*1000);
                                                song.setFileUrl(songlist.get(i).hash);
                                                songList.add(song);
                                            }
                                            PlayMusic.PlayList playList = new PlayMusic.PlayList();
                                            playList.setPlaylist(songList);
                                            playList.setBang(2);  //2表示酷狗列表

                                        }
                                    }

                                    @Override
                                    public void onFail(Exception e) {
                                        CloseProgress();
                                        Toast.makeText(context, "获取音乐歌单失败！请检查网络设置", Toast.LENGTH_LONG).show();
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

        }
    }

   public interface ShowBottonPal
   {
       void show();
   }

   public void setListener(ShowBottonPal showBottonPal)
   {
       this.showBottonPal = showBottonPal;
   }

    //显示正在加载数据对话框
    private void ShowProgress()
    {
        if (progressDialog==null)
        {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("正在加载数据");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
    }
    //关闭正在加载对话框
    private  void CloseProgress()
    {
        if (progressDialog!=null)
        {
            progressDialog.dismiss();
        }
    }
}