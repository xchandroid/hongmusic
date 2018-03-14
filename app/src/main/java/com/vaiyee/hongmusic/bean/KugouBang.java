package com.vaiyee.hongmusic.bean;

import android.content.Intent;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/3/14.
 */

public class KugouBang {

    @SerializedName("info")
    public Info info;
    @SerializedName("songs")
    public Song song;

    public class Info
    {
        @SerializedName("intro")
        public String intro;
        @SerializedName("rankname")
        public String rankname;
        @SerializedName("imgurl")
        public String imgurl;
    }

    public class Song
    {
        @SerializedName("list")
       public List <KugouBangList> kugouBangList;
    }
}
