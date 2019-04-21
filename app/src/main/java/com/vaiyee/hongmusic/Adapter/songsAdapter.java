package com.vaiyee.hongmusic.Adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vaiyee.hongmusic.ColorTrackView;
import com.vaiyee.hongmusic.MainActivity;
import com.vaiyee.hongmusic.MyApplication;
import com.vaiyee.hongmusic.PlayMusic;
import com.vaiyee.hongmusic.R;
import com.vaiyee.hongmusic.Utils.DownloadTask;
import com.vaiyee.hongmusic.bean.Song;
import com.vaiyee.hongmusic.fragement.fragement1;
import com.vaiyee.hongmusic.util.CacheLoadImageUtil;

import org.w3c.dom.Text;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2018/1/29.
 */

public class songsAdapter extends RecyclerView.Adapter<songsAdapter.Viewholder> {
    private int resourLayout;
    private List<Song> songList;
    private int start_index, end_index;//当前屏幕显示的起始和结束索引
    private boolean scrollState = false;//Listview是否滑动状态,默认没在滑动
    private Activity activity;
    private Context context;

    public songsAdapter(@NonNull Context context, int resource, @NonNull List<Song> objects,Activity activity) {
        resourLayout = resource;
        songList = objects;
        this.activity = activity;
        this.context = context;
        LoadImg(); //预加载所有歌曲封面
    }
   private static CacheLoadImageUtil cacheLoadImageUtil = new CacheLoadImageUtil((int) (Runtime.getRuntime().maxMemory()/8));//缓存区域的最大内存为本进程运行内存的1/8
    private void LoadImg() {
        for (Song song:songList)
        {
            String url = song.getFileUrl();
            byte [] imgByte = getArtwork(context,url);
            if (imgByte!=null) {  //有专辑封面才存入
                cacheLoadImageUtil.put(url, BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
            }
        }
    }
    private Bitmap getfengmian(String url) //从Lru集合或软引用集合中获取歌曲封面的Bitmap
    {
        Bitmap bitmap = cacheLoadImageUtil.get(url);
        if (bitmap!=null) //如果强引用中有就返回
        {
            return bitmap;
        }
        SoftReference<Bitmap> softReference = cacheLoadImageUtil.softReferenceMap.get(url);
        if (softReference!=null) //如果软引用中有返回
        {
            cacheLoadImageUtil.put(url,softReference.get());//将Bitmap重新放回运行内存中（强引用）
            return softReference.get();
        }
        return null; //没有封面就返回null
    }

    public void setScrollState(boolean scrollState) {
        this.scrollState = scrollState;
    }

    //获取专辑封面,返回图片的字节数组用Glide加载
    public static byte[] getArtwork(Context context, String url) {
        Uri selectedAudio = Uri.parse(url);
        MediaMetadataRetriever myRetriever = new MediaMetadataRetriever();
        try {
            myRetriever.setDataSource(context, selectedAudio); // the URI of audio file
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        byte[] artwork;

        artwork = myRetriever.getEmbeddedPicture();
        if (artwork!=null) {
            return artwork;
        }
        else
        {
            return null;
        }
    }

/*
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Song song = songList.get(position);
        View view;
        Viewholder viewholder;
        if (convertView == null) {
            view = View.inflate(getContext(), resourLayout, null);
            viewholder = new Viewholder();
            viewholder.zjview = view.findViewById(R.id.zj_id);
            viewholder.zjview.setTag(position);
            viewholder.moremenu = view.findViewById(R.id.more);
            viewholder.geming = view.findViewById(R.id.geming);
            viewholder.geshou = view.findViewById(R.id.geshou);
            viewholder.ablum = view.findViewById(R.id.ablum);
            view.setTag(viewholder);
        } else {
            view = convertView;
            viewholder = (Viewholder) view.getTag();
        }
        String url = song.getFileUrl();
        if (!scrollState) //非滑动状态时加载图片
        {

            viewholder.zjview.setImageBitmap(setArtwork(getContext(), url));
            //设置tag为1表示已加载过数据
            viewholder.zjview.setTag("1");
        } else //滑动状态时加载的是本地图片
        {
            //拖动过程显示的图片
            viewholder.zjview.setImageResource(R.drawable.music_ic);
            //将数据image_url保存在Tag当中
            viewholder.zjview.setTag(url);


        }
        viewholder.ablum.setText("《" + song.getAlbum() + "》");
        viewholder.geshou.setText(song.getSinger());
        viewholder.geming.setText(1 + position + "." + song.getTitle());
        viewholder.moremenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Showpopwindow(song.getTitle(), song.getFileUrl(),position);
            }
        });

        return view;
    }
*/
    private void Showpopwindow(String genming, String path,int i) {
        View contentview = LayoutInflater.from(context).inflate(R.layout.popuwindow, null);
        PopupWindow popupWindow = new PopupWindow(contentview, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        initContentview(popupWindow,contentview, genming, path,i);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.popuAnim);
        popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER,0,0);
        final WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = 0.6f;
        activity.getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                activity.getWindow().setAttributes(params);
            }
        });

    }

    private void initContentview(final PopupWindow popupWindow, View contentview, final String geming, final String path,final int i) {
        Button title = contentview.findViewById(R.id.pop_xq);
        title.setText(geming);
        Button delete = contentview.findViewById(R.id.pop_xg);
        delete.setText("删除");
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
               showDiolog(geming,path,i);

            }
        });
        Button desmiss = contentview.findViewById(R.id.pop_lol);
        desmiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }

    private void showDiolog(final String geming, final String path,final int i)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("确定要删除<"+geming+">吗？");
        builder.setTitle("提示");
        builder.setIcon(R.drawable.tip);
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        File file = new File(path);
                        if (file.exists()) {
                            file.delete();
                            fragement1.songs.remove(i);
                            notifyDataSetChanged();
                            DownloadTask.scanFile(context,"/storage/emulated/0/HonchenMusic/download");
                            MediaScannerConnection.scanFile(context, new String[]{path}, null, null);
                            Toast.makeText(context, "删除了" + geming, Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    }
                });

        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resourLayout,parent,false);
       final Viewholder viewholder = new Viewholder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Song song = songList.get(viewholder.getAdapterPosition());
                String geming = song.getTitle().toString();
                String geshou = song.getSinger().toString();
                PlayMusic playMusic = new PlayMusic();
                final String path = song.getFileUrl();
                playMusic.play(path,viewholder.getAdapterPosition());
                fragement1.getLrc(geming,song,viewholder.getAdapterPosition());
                PlayMusic.PlayList playList = new PlayMusic.PlayList();
                playList.setPlaylist(songList);
                playList.setBang(0);
            }
        });
        return viewholder;
    }

    @Override
    public void onBindViewHolder(Viewholder holder, final int position) {
        final Song song = songList.get(position);
        String url = song.getFileUrl();
        holder.ablum.setText("《" + song.getAlbum() + "》");
        holder.geshou.setText(song.getSinger());
        holder.geming.setText(1 + position + "." + song.getTitle());
        holder.moremenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Showpopwindow(song.getTitle(), song.getFileUrl(), position);
            }
        });
        Bitmap bitmap = getfengmian(url);
           if (bitmap!=null)
           {
               holder.zjview.setImageBitmap(bitmap);
           }
           else
           {
               holder.zjview.setImageResource(R.drawable.music_ic);
           }
        Animator animator = ObjectAnimator.ofFloat(holder.itemView,"alpha",0f,0.5f,1.0f);
        animator.setDuration(500).start();

    }


    @Override
    public int getItemCount() {
        return songList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder
    {
        ImageView zjview;
        TextView geming,geshou,ablum;
        ImageView moremenu;

        public Viewholder(View itemView) {
            super(itemView);
            zjview = itemView.findViewById(R.id.zj_id);
            //zjview.setTag(position);
            moremenu = itemView.findViewById(R.id.more);
            geming = itemView.findViewById(R.id.geming);
            geshou = itemView.findViewById(R.id.geshou);
            ablum = itemView.findViewById(R.id.ablum);
        }
    }

}
