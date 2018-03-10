package com.vaiyee.hongmusic.Adapter;

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

import com.vaiyee.hongmusic.ColorTrackView;
import com.vaiyee.hongmusic.MainActivity;
import com.vaiyee.hongmusic.MyApplication;
import com.vaiyee.hongmusic.R;
import com.vaiyee.hongmusic.Utils.DownloadTask;
import com.vaiyee.hongmusic.bean.Song;
import com.vaiyee.hongmusic.fragement.fragement1;

import org.w3c.dom.Text;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2018/1/29.
 */

public class songsAdapter extends ArrayAdapter<Song> {
    private int resourLayout;
    private List<Song> songList;
    private int start_index, end_index;//当前屏幕显示的起始和结束索引
    private boolean scrollState = false;//Listview是否滑动状态,默认没在滑动
    private Activity activity;

    public songsAdapter(@NonNull Context context, int resource, @NonNull List<Song> objects,Activity activity) {
        super(context, resource, objects);
        resourLayout = resource;
        songList = objects;
        this.activity = activity;

    }

    public void setScrollState(boolean scrollState) {
        this.scrollState = scrollState;
    }

    //获取专辑封面
    public static Bitmap setArtwork(Context context, String url) {
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

        if (artwork != null) {
            Bitmap bMap = BitmapFactory.decodeByteArray(artwork, 0, artwork.length);
            return bMap;
        } else {

            return BitmapFactory.decodeResource(context.getResources(), R.drawable.music_ic);
        }
    }

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
            viewholder.zjview.setImageResource(R.drawable.ic_launcher);
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

    private void Showpopwindow(String genming, String path,int i) {
        View contentview = LayoutInflater.from(getContext()).inflate(R.layout.popuwindow, null);
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
                            DownloadTask.scanFile(getContext(),"/storage/emulated/0/HonchenMusic/download");
                            MediaScannerConnection.scanFile(getContext(), new String[]{path}, null, null);
                            Toast.makeText(getContext(), "删除了" + geming, Toast.LENGTH_LONG).show();
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

    class Viewholder
    {
        ImageView zjview;
       TextView geming,geshou,ablum;
        ImageView moremenu;
    }

}
