package com.vaiyee.hongmusic.bean;


import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/4/5.
 */

public class GeshouType {
    @SerializedName("info")
    public List<Info> infoList;
    public class Info
    {
        @SerializedName("title")
        public String title;
        @SerializedName("singer")
        public List<Singer> singerList;

    }
    public static class Singer
    {
        @SerializedName("singername")
        public String singername;
        @SerializedName("imgurl")
        public String imgurl;
        @SerializedName("singerid")
        public int singerid;
        @SerializedName("fanscount")
        public int fanscount;
        @SerializedName("heat")
        public String heat;
    }
}

