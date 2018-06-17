package com.vaiyee.hongmusic.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/6/15.
 */

public class KugouMv {
   @SerializedName("data")
    public   Data data;
    public class Data
    {
        @SerializedName("info")
        public List<Info> infoList;
    }
    public class Info
    {
        @SerializedName("description")
        public  String description;
        @SerializedName("publish")
        public String publishtime;
         @SerializedName("mvhash")
        public  String mvhash;
        @SerializedName("comment")
        public String comment;
        @SerializedName("videoname")
        public  String videoname;
        @SerializedName("img")
        public String img;
        @SerializedName("playcount")
        public String playcount;
    }
}
