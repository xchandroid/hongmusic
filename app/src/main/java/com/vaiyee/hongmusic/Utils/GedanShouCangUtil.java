package com.vaiyee.hongmusic.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vaiyee.hongmusic.bean.Gedan;
import com.vaiyee.hongmusic.bean.ShouCangGeDan;
import com.vaiyee.hongmusic.bean.Song;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/20.
 */

public class GedanShouCangUtil {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private List<ShouCangGeDan>gedanList = new ArrayList<>();
    private Context context;
    private Gson gson;
    public GedanShouCangUtil(Context context, ShouCangGeDan shouCangGeDan)
    {
        sharedPreferences = context.getSharedPreferences("gedan",0);
        editor = sharedPreferences.edit();
        gson = new Gson();
        this.context = context;
        gedanList.add(shouCangGeDan);
    }
    public GedanShouCangUtil(Context context)
    {
        sharedPreferences = context.getSharedPreferences("gedan",0);
        gson = new Gson();
        this.context = context;
    }
    public void setGedanList()
    {
        gedanList.addAll(getGedanList());
        String json=  gson.toJson(gedanList);
        editor.putString("gedan",json);
        editor.apply();
    }
    public List<ShouCangGeDan> getGedanList()
    {
        List<ShouCangGeDan> gedans = new ArrayList<>();
        String jsonStr = sharedPreferences.getString("gedan",null);
        if (jsonStr ==null)
        {
            return gedans;
        }
        gedans = gson.fromJson(jsonStr,new TypeToken<List<ShouCangGeDan>>(){}.getType());
        return gedans;
    }
}
