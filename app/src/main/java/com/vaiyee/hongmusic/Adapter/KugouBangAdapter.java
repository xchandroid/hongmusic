package com.vaiyee.hongmusic.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
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

import com.vaiyee.hongmusic.KugoubangActivity;
import com.vaiyee.hongmusic.MainActivity;
import com.vaiyee.hongmusic.MyApplication;
import com.vaiyee.hongmusic.PlayMusic;
import com.vaiyee.hongmusic.R;
import com.vaiyee.hongmusic.SearchActivity;
import com.vaiyee.hongmusic.Utils.DownloadTask;
import com.vaiyee.hongmusic.Utils.HttpClinet;
import com.vaiyee.hongmusic.Utils.NetUtils;
import com.vaiyee.hongmusic.bean.KugouBang;
import com.vaiyee.hongmusic.bean.KugouBangList;
import com.vaiyee.hongmusic.bean.KugouMusic;
import com.vaiyee.hongmusic.bean.Song;
import com.vaiyee.hongmusic.http.HttpCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/14.
 */

public class KugouBangAdapter extends RecyclerView.Adapter<KugouBangAdapter.ViewHolder> {
    private List<KugouBangList> kugouBangList;
    private int resId;
    private Context context;
    private Button title,download;
    private Activity activity;
    private List<Song> songList;
    private PopupWindow popupWindow;
    private static String geshou,geming,coverUrl,lrc;
    private static int duration;

    public KugouBangAdapter(Context context, int resId, List<KugouBangList>kugouBangList, Activity activity)
    {
        this.activity = activity;
        this.context = context;
        this.resId = resId;
        this.kugouBangList = kugouBangList;
        popupWindow = new PopupWindow(context);
    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resId,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final KugouBangList song = kugouBangList.get(position);
        final String[]s=song.filename.split(" - ");
        holder.songaName.setText(1+position+"."+s[1]);
        holder.singger.setText(s[0]);
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopu(s[1],s[0],"未知",song.hash,String.valueOf(song.duration*1000));
            }
        });
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                songList = new ArrayList<>();
                String[] s = song.filename.split("-");
                String hash = song.hash;
                geming = s[1];
                geshou = s[0];
                duration = song.duration*1000;
                getSongUrl(hash,position);
            }
        });
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return kugouBangList.size();
    }

    /*
    @Override
    public View getView(int i, View contentview, ViewGroup viewGroup) {
        View view = null;
        ViewHolder viewHolder = null;
        final KugouBangList song = kugouBangList.get(i);
        final String[]s=song.filename.split(" - ");
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
    */

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
                            for (int i=0;i<kugouBangList.size();i++)
                            {
                                String[] s = kugouBangList.get(i).filename.split(" - ");
                                Song song = new Song();
                                song.setTitle(s[1]);
                                song.setSinger(s[0]);
                                song.setDuration(kugouBangList.get(i).duration*1000);
                                song.setFileUrl(kugouBangList.get(i).hash);
                                songList.add(song);
                            }
                            PlayMusic.PlayList playList = new PlayMusic.PlayList();
                            playList.setPlaylist(songList);
                            playList.setBang(2);  //2表示酷狗列表

                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        Toast.makeText(context, "获取在线音乐失败！请检查网络设置", Toast.LENGTH_LONG).show();
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
                                            for (int i=0;i<kugouBangList.size();i++)
                                            {
                                                String[] s = kugouBangList.get(i).filename.split(" - ");
                                                Song song = new Song();
                                                song.setTitle(s[1]);
                                                song.setSinger(s[0]);
                                                song.setDuration(kugouBangList.get(i).duration*1000);
                                                song.setFileUrl(kugouBangList.get(i).hash);
                                                songList.add(song);
                                            }
                                            PlayMusic.PlayList playList = new PlayMusic.PlayList();
                                            playList.setPlaylist(songList);
                                            playList.setBang(2);  //2表示酷狗列表

                                        }
                                    }

                                    @Override
                                    public void onFail(Exception e) {
                                        Toast.makeText(context, "获取在线音乐失败！请检查网络设置", Toast.LENGTH_LONG).show();
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
                                            for (int i=0;i<kugouBangList.size();i++)
                                            {
                                                String[] s = kugouBangList.get(i).filename.split(" - ");
                                                Song song = new Song();
                                                song.setTitle(s[1]);
                                                song.setSinger(s[0]);
                                                song.setDuration(kugouBangList.get(i).duration*1000);
                                                song.setFileUrl(kugouBangList.get(i).hash);
                                                songList.add(song);
                                            }
                                            PlayMusic.PlayList playList = new PlayMusic.PlayList();
                                            playList.setPlaylist(songList);
                                            playList.setBang(2);  //2表示酷狗列表

                                        }
                                    }

                                    @Override
                                    public void onFail(Exception e) {
                                        Toast.makeText(context, "获取在线音乐失败！请检查网络设置", Toast.LENGTH_LONG).show();
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
                                            for (int i=0;i<kugouBangList.size();i++)
                                            {
                                                String[] s = kugouBangList.get(i).filename.split(" - ");
                                                Song song = new Song();
                                                song.setTitle(s[1]);
                                                song.setSinger(s[0]);
                                                song.setDuration(kugouBangList.get(i).duration*1000);
                                                song.setFileUrl(kugouBangList.get(i).hash);
                                                songList.add(song);
                                            }
                                            PlayMusic.PlayList playList = new PlayMusic.PlayList();
                                            playList.setPlaylist(songList);
                                            playList.setBang(2);  //2表示酷狗列表

                                        }
                                    }

                                    @Override
                                    public void onFail(Exception e) {
                                        Toast.makeText(context, "获取在线音乐失败！请检查网络设置", Toast.LENGTH_LONG).show();
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


    class ViewHolder extends  RecyclerView.ViewHolder
    {
        ImageView ablumCover,more;
        TextView songaName,singger,ablum;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            songaName = itemView.findViewById(R.id.geming);
            singger = itemView.findViewById(R.id.geshou);
            ablum = itemView.findViewById(R.id.ablum);
            more = itemView.findViewById(R.id.more);
            view = itemView;
        }
    }
}
