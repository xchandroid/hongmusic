package com.vaiyee.hongmusic.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/3/27.
 */

public class GedanList {
    @SerializedName("data")
    public Data data;
    public class Data
    {
        @SerializedName("info")
        public List<Info> infoList;
        @SerializedName("total")
        public int total;
    }
    public class Info
    {
        @SerializedName("filesize")
        public int filesize;
        @SerializedName("filename")
        public String filename;
        @SerializedName("hash")
        public String hash;
        @SerializedName("duration")
        public int duration;
        @SerializedName("320hash")
        public String hqhash;
        @SerializedName("sqhash")
        public String sqhash;
    }
}
