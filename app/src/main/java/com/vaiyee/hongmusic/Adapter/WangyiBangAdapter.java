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
import com.vaiyee.hongmusic.WangyiBangActivity;
import com.vaiyee.hongmusic.bean.DownloadInfo;
import com.vaiyee.hongmusic.bean.KugouMusic;
import com.vaiyee.hongmusic.bean.KugouSearchResult;
import com.vaiyee.hongmusic.bean.OnlineMusic;
import com.vaiyee.hongmusic.bean.Song;
import com.vaiyee.hongmusic.bean.Tracks;
import com.vaiyee.hongmusic.bean.WangyiBang;
import com.vaiyee.hongmusic.bean.WangyiLrc;
import com.vaiyee.hongmusic.http.HttpCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/9.
 */

public class WangyiBangAdapter extends RecyclerView.Adapter<WangyiBangAdapter.ViewHolder> {

    private int resId;
    private List<WangyiBang.ListBean> onlineMusics;
    private Context context;
    private Activity activity;
    private PopupWindow popupWindow;
    private Button title,download;
    private List<Song> songList = new ArrayList<>();;
    private static String url = null;
    private static final String Path = "http://music.163.com/song/media/outer/url?id=";
    public WangyiBangAdapter(Context context, int resId, List<WangyiBang.ListBean> onlineMusics, Activity activity)
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

    */


    private void onItemClick(final int position)
    {
        final WangyiBang.ListBean onlineMusic = onlineMusics.get(position);
        final String songID = String.valueOf(onlineMusic.getId());
        final String geming = onlineMusic.getName();
        final String coverurl = onlineMusic.getAlbum().getPicUrl();
        final String geshou = onlineMusic.getArtists().get(0).getName();
        final int time = onlineMusic.getDuration();
        final PlayMusic playMusic = new PlayMusic();
        switch (NetUtils.getNetType())
        {
            case NET_WIFI:
                playMusic.play(Path+songID,position);
                getLrc(coverurl,geming,geshou,time);
                //加入当前播放列表
                for (int i=0;i<onlineMusics.size();i++)
                {
                    Song song = new Song();
                    song.setTitle(onlineMusics.get(i).getName());
                    song.setSinger(onlineMusics.get(i).getArtists().get(0).getName());
                    song.setFileUrl(String.valueOf(onlineMusics.get(i).getId()));
                    song.setDuration(onlineMusics.get(i).getDuration());
                    songList.add(song);
                }
                PlayMusic.PlayList playList = new PlayMusic.PlayList();
                playList.setPlaylist(songList);
                playList.setBang(3);  //3表示网易音乐列表
                break;
            default:
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(context);
                builder.setMessage("当前正在使用移动网络，是否使用数据流量播放在线音乐？");
                builder.setTitle("提示");
                builder.setIcon(R.drawable.tip);
                builder.setPositiveButton("流量多",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                playMusic.play(Path+songID,position);
                                getLrc(coverurl,geming,geshou,time);
                                //加入当前播放列表
                                for (int i=0;i<onlineMusics.size();i++)
                                {
                                    Song song = new Song();
                                    song.setTitle(onlineMusics.get(i).getName());
                                    song.setSinger(onlineMusics.get(i).getArtists().get(0).getName());
                                    song.setFileUrl(String.valueOf(onlineMusics.get(i).getId()));
                                    song.setDuration(onlineMusics.get(i).getDuration());
                                    songList.add(song);
                                }
                                PlayMusic.PlayList playList = new PlayMusic.PlayList();
                                playList.setPlaylist(songList);
                                playList.setBang(3);  //3表示网易音乐列表
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

        /*
        HttpClinet.WangyiLrc(songID, new HttpCallback<WangyiLrc>() {
            @Override
            public void onSuccess(WangyiLrc wangyiLrc) {
                songList = new ArrayList<>();
                String lrcContent = wangyiLrc.lrc.lyric;
                Log.d("歌词内容是",lrcContent);
                SearchActivity.creatLrc(lrcContent,geming);
                AlertDialog.Builder builder;
                switch (NetUtils.getNetType())
                {
                    case NET_WIFI:

                        String path = Path + songID + ".mp3";
                        PlayMusic playMusic =new PlayMusic();
                        playMusic.play(path,i);
                        String songname = onlineMusic.getName();
                        String geshou = onlineMusic.getArtists().get(0).getName();
                        String coverUrl = onlineMusic.getAlbum().getPicUrl();
                        Log.d("路径是",path);
                        int time = onlineMusic.getDuration();
                        MainActivity mainActivity = new MainActivity();
                        mainActivity.tongbuShow(songname,geshou,coverUrl,time,MainActivity.ONLINE);

                        //加入当前播放列表
                        for (int i=0;i<onlineMusics.size();i++)
                        {
                            Song song = new Song();
                            song.setTitle(onlineMusics.get(i).getName());
                            song.setSinger(onlineMusics.get(i).getArtists().get(0).getName());
                            song.setFileUrl(String.valueOf(onlineMusics.get(i).getId()));
                            song.setDuration(onlineMusics.get(i).getDuration());
                            songList.add(song);
                        }
                        PlayMusic.PlayList playList = new PlayMusic.PlayList();
                        playList.setPlaylist(songList);
                        playList.setBang(3);  //3表示网易音乐列表
                        break;
                    case NET_4G:
                        builder = new AlertDialog.Builder(context);
                        builder.setMessage("当前正在使用4G网络，是否使用数据流量播放在线音乐？");
                        builder.setTitle("提示");
                        builder.setIcon(R.drawable.tip);
                        builder.setPositiveButton("流量多",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        String path = Path + songID + ".mp3";
                                        PlayMusic playMusic =new PlayMusic();
                                        playMusic.play(path,i);
                                        String songname = onlineMusic.getName();
                                        String geshou = onlineMusic.getArtists().get(0).getName();
                                        String coverUrl = onlineMusic.getAlbum().getPicUrl();
                                        Log.d("路径是",path);
                                        int time = onlineMusic.getDuration();
                                        MainActivity mainActivity = new MainActivity();
                                        mainActivity.tongbuShow(songname,geshou,coverUrl,time,MainActivity.ONLINE);
                                        //加入当前播放列表
                                        for (int i=0;i<onlineMusics.size();i++)
                                        {
                                            Song song = new Song();
                                            song.setTitle(onlineMusics.get(i).getName());
                                            song.setSinger(onlineMusics.get(i).getArtists().get(0).getName());
                                            song.setFileUrl(String.valueOf(onlineMusics.get(i).getId()));
                                            song.setDuration(onlineMusics.get(i).getDuration());
                                            songList.add(song);
                                        }
                                        PlayMusic.PlayList playList = new PlayMusic.PlayList();
                                        playList.setPlaylist(songList);
                                        playList.setBang(3);  //3表示网易音乐列表

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
                                        String path = Path + songID + ".mp3";
                                        PlayMusic playMusic =new PlayMusic();
                                        playMusic.play(path,i);
                                        String songname = onlineMusic.getName();
                                        String geshou = onlineMusic.getArtists().get(0).getName();
                                        String coverUrl = onlineMusic.getAlbum().getPicUrl();
                                        Log.d("路径是",path);
                                        int time = onlineMusic.getDuration();
                                        MainActivity mainActivity = new MainActivity();
                                        mainActivity.tongbuShow(songname,geshou,coverUrl,time,MainActivity.ONLINE);

                                        //加入当前播放列表
                                        for (int i=0;i<onlineMusics.size();i++)
                                        {
                                            Song song = new Song();
                                            song.setTitle(onlineMusics.get(i).getName());
                                            song.setSinger(onlineMusics.get(i).getArtists().get(0).getName());
                                            song.setFileUrl(String.valueOf(onlineMusics.get(i).getId()));
                                            song.setDuration(onlineMusics.get(i).getDuration());
                                            songList.add(song);
                                        }
                                        PlayMusic.PlayList playList = new PlayMusic.PlayList();
                                        playList.setPlaylist(songList);
                                        playList.setBang(3);  //3表示网易音乐列表

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

                                        String path = Path + songID + ".mp3";
                                        PlayMusic playMusic =new PlayMusic();
                                        playMusic.play(path,i);
                                        String songname = onlineMusic.getName();
                                        String geshou = onlineMusic.getArtists().get(0).getName();
                                        String coverUrl = onlineMusic.getAlbum().getPicUrl();
                                        Log.d("路径是",path);
                                        int time = onlineMusic.getDuration();
                                        MainActivity mainActivity = new MainActivity();
                                        mainActivity.tongbuShow(songname,geshou,coverUrl,time,MainActivity.ONLINE);
                                        //加入当前播放列表
                                        for (int i=0;i<onlineMusics.size();i++)
                                        {
                                            Song song = new Song();
                                            song.setTitle(onlineMusics.get(i).getName());
                                            song.setSinger(onlineMusics.get(i).getArtists().get(0).getName());
                                            song.setFileUrl(String.valueOf(onlineMusics.get(i).getId()));
                                            song.setDuration(onlineMusics.get(i).getDuration());
                                            songList.add(song);
                                        }
                                        PlayMusic.PlayList playList = new PlayMusic.PlayList();
                                        playList.setPlaylist(songList);
                                        playList.setBang(3);  //3表示网易音乐列表

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

            @Override
            public void onFail(Exception e) {
                Toast.makeText(context,"获取歌词失败了哦",Toast.LENGTH_LONG).show();
            }
        });
        */

    }

    public void getLrc(final String path, final String geming, final String geshou, final int endtime)
    {

        HttpClinet.KugouSearch(geming, 5, new HttpCallback<KugouSearchResult>() {
            @Override
            public void onSuccess(KugouSearchResult kugouSearchResult) {
                if (kugouSearchResult==null)
                {
                    MainActivity mainActivity = new MainActivity();
                    mainActivity.tongbuShow(geming,geshou,path,endtime,MainActivity.LOCAL);
                    Toast.makeText(MyApplication.getQuanjuContext(),"获取歌词失败，请检查网络再试",Toast.LENGTH_LONG).show();
                    return;
                }
                List<KugouSearchResult.lists> lists = kugouSearchResult.getResultList();
                String hash = lists.get(0).getFileHash();
                HttpClinet.KugouUrl(hash, new HttpCallback<KugouMusic>() {
                    @Override
                    public void onSuccess(KugouMusic kugouMusic) {
                        String coverUrl = kugouMusic.getData().getImg();
                        String Lrc = kugouMusic.getData().getLyrics();
                        SearchActivity.creatLrc(Lrc,geming);  //创建歌词文件，为了在Playmusicfragemet中能够定位歌词显示到Lrcview中
                        // play(path,playposition);
                        MainActivity mainActivity = new MainActivity();
                        mainActivity.tongbuShow(geming,geshou,coverUrl,endtime,MainActivity.LOCAL);
                    }

                    @Override
                    public void onFail(Exception e) {
                        MainActivity mainActivity = new MainActivity();
                        mainActivity.tongbuShow(geming,geshou,path,endtime,MainActivity.LOCAL);
                        Toast.makeText(MyApplication.getQuanjuContext(),"获取歌词失败，请检查网络再试",Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onFail(Exception e) {

            }
        });

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
                DownloadTask downloadTask = new DownloadTask(NotiUtil.listener);
                downloadTask.execute(url, songName, geshou, ablumName,time,String.valueOf(NotiUtil.getRandom()));
                Toast.makeText(MyApplication.getQuanjuContext(), "开始下载", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View  view =  LayoutInflater.from(context).inflate(resId,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final WangyiBang.ListBean onlineMusic = onlineMusics.get(position);
        Glide.with(context)
                .load(onlineMusic.getAlbum().getPicUrl())
                .placeholder(R.drawable.music_ic)
                .error(R.drawable.music_ic)
                .into(holder.ablumCover);
        holder.songaName.setText(1+position+"."+onlineMusic.getName());
        holder.singger.setText(onlineMusic.getArtists().get(0).getName());
        holder.ablum.setText("《"+onlineMusic.getAlbum().getName()+"》");
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupwindow(onlineMusic.getName(),onlineMusic.getArtists().get(0).getName(),onlineMusic.getAlbum().getName(), String.valueOf(onlineMusic.getId()),String.valueOf(onlineMusic.getDuration()));
            }
        });
        holder.iview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return onlineMusics.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView ablumCover,more;
        TextView songaName,singger,ablum;
        View iview;

        public ViewHolder(View itemView) {
            super(itemView);
            songaName = itemView.findViewById(R.id.geming);
            more = itemView.findViewById(R.id.more);
            singger = itemView.findViewById(R.id.geshou);
            ablum = itemView.findViewById(R.id.ablum);
            ablumCover = itemView.findViewById(R.id.zj_id);
            iview = itemView;
        }
    }
}
