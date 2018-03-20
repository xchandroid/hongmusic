package com.vaiyee.hongmusic.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vaiyee.hongmusic.bean.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/17.
 *
 * 此工具类用Sharepreference来保存列表list<>数据
 * */

public class ListDataSave {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public ListDataSave(Context mContext, String preferenceName) {
        preferences = mContext.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    /**
     * 保存List
     * @param tag
     * @param datalist
     */
    public void setDataList(String tag, List<Song> datalist) {
        if (null == datalist || datalist.size() <= 0)
            return;

        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.clear();
        editor.putString(tag, strJson);
        editor.commit();

    }

    /**
     * 获取List
     * @param tag
     * @return
     */
    public  List<Song> getDataList(String tag) {
        List<Song> datalist=new ArrayList<>();
        String strJson = preferences.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<Song>>() {
        }.getType());
        return datalist;

    }

}
