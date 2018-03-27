package com.vaiyee.hongmusic.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/3/26.
 */

public class Gedan {
    @SerializedName("plist")
     public Plist plist;

    public class Plist
    {
        @SerializedName("list")
        public Liebiao liebiao;
    }
    public class Liebiao
    {
        @SerializedName("info")
      public List<Info> infoList;
    }
    public class Info
    {
        @SerializedName("specialname")
        public String specialname;
        @SerializedName("intro")
        public String intro;
        @SerializedName("playcount")
        public String playcount;
        @SerializedName("specialid")
        public String specialid;
        @SerializedName("imgurl")
        public String imgurl;
        @SerializedName("publishtime")
        public String publishtime;

    }
}
