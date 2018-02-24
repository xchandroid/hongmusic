package com.vaiyee.hongmusic.Adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vaiyee.hongmusic.ColorTrackView;
import com.vaiyee.hongmusic.R;
import com.vaiyee.hongmusic.bean.Song;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Administrator on 2018/1/29.
 */

public class songsAdapter extends ArrayAdapter<Song> {
    private int resourLayout;
    private List<Song> songList;private int start_index, end_index;//当前屏幕显示的起始和结束索引
    private boolean scrollState= false;//Listview是否滑动状态,默认没在滑动

    public songsAdapter(@NonNull Context context, int resource, @NonNull List<Song> objects) {
        super(context, resource, objects);
        resourLayout = resource;
        songList = objects;
    }
    public  void setScrollState(boolean scrollState) {
        this.scrollState = scrollState;
    }

    //获取专辑封面
    public static Bitmap setArtwork(Context context, String url) {
        Uri selectedAudio = Uri.parse(url);
        MediaMetadataRetriever myRetriever = new MediaMetadataRetriever();
        myRetriever.setDataSource(context, selectedAudio); // the URI of audio file
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
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Song song = songList.get(position);
        View view;
        Viewholder viewholder;
        if (convertView==null)
        {
            view = View.inflate(getContext(),resourLayout,null);
            viewholder = new Viewholder();
            viewholder.zjview = view.findViewById(R.id.zj_id);
            viewholder.zjview.setTag(position);
            viewholder.moremenu = view.findViewById(R.id.more);
            viewholder.geming = view.findViewById(R.id.geming);
            viewholder.geshou = view.findViewById(R.id.geshou);
            viewholder.ablum = view.findViewById(R.id.ablum);
            view.setTag(viewholder);
        }
        else
        {
            view = convertView;
            viewholder = (Viewholder)view.getTag();
        }
          String url = song.getFileUrl();
       if(!scrollState) //非滑动状态时加载图片
       {

           viewholder.zjview.setImageBitmap(setArtwork(getContext(), url));
           //设置tag为1表示已加载过数据
           viewholder.zjview.setTag("1");
       }
        else //滑动状态时加载的是本地图片
        {
            //拖动过程显示的图片
            viewholder.zjview.setImageResource(R.drawable.ic_launcher);
            //将数据image_url保存在Tag当中
            viewholder.zjview.setTag(url);


        }
          viewholder.ablum.setText("《"+song.getAlbum()+"》");
          viewholder.geshou.setText(song.getSinger());
          viewholder.geming.setText(song.getTitle());
          viewholder.moremenu.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Toast.makeText(getContext(),"完善中...",Toast.LENGTH_LONG).show();
              }
          });

          return view;
    }
    class Viewholder
    {
        ImageView zjview;
       TextView geming,geshou,ablum;
        ImageView moremenu;
    }

}
