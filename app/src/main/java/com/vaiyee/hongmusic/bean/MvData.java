package com.vaiyee.hongmusic.bean;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/3/31.
 */

public class MvData {
    @SerializedName("data")
    public Data data;

    public class Data
    {
        @SerializedName("name")
        public String videoName;
        @SerializedName("artistName")
        public String artistName;
        @SerializedName("desc")
        public String description;
        @SerializedName("brs")
        public Url url;
        @SerializedName("playCount")
        public String playCount;
        @SerializedName("publishTime")
        public String publishTime;
        @SerializedName("duration")
        public String duration;
    }
    public class Url
    {
        @SerializedName("480")
        public String vga;
        @SerializedName("720")
        public String hd;
        @SerializedName("1080")
        public String fhd;
    }
}
