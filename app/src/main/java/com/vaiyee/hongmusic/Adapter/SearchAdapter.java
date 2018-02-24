package com.vaiyee.hongmusic.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.vaiyee.hongmusic.MyApplication;
import com.vaiyee.hongmusic.R;
import com.vaiyee.hongmusic.SearchActivity;
import com.vaiyee.hongmusic.bean.KugouMusic;
import com.vaiyee.hongmusic.bean.KugouSearchResult;
import com.vaiyee.hongmusic.bean.Music;
import com.vaiyee.hongmusic.bean.SearchMusic;

import org.jsoup.helper.StringUtil;

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

    public SearchAdapter(Context context, int resId, List<KugouSearchResult.lists> songList) {
        this.songList = songList;
        this.context = context;
        this.resId = resId;
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
        KugouSearchResult.lists song = songList.get(i);
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
              View contentview = LayoutInflater.from(MyApplication.getQuanjuContext()).inflate(R.layout.popuwindow,null);
                PopupWindow popupWindow =new  PopupWindow(context);
                popupWindow.setContentView(contentview);
                popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setAnimationStyle(R.anim.popup_enter);
                popupWindow.showAtLocation(SearchActivity.listView, Gravity.CENTER,0,0);
            }
        });
        String name = song.getSingerName();
       String name1 = name.replace("<em>","");
       String name2 = name1.replace("</em>","");
       String songname = song.getSongName();
       String songname1 = songname.replace("<em>","");
       String songname2 = songname1.replace("</em>","");
        viewHolder.geming.setText(songname2);
        viewHolder.geshou.setText(name2);
        return view;
    }

    class ViewHolder {
        TextView geming, geshou;
        ImageView more;
    }

}
