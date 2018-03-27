package com.vaiyee.hongmusic.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/3/27.
 */

public class GedanList {
    @SerializedName("list")
    public List1 list1;

    public class List1
    {
      @SerializedName("list")
        public List2 list2;
    }
    public class List2
    {
        @SerializedName("info")
        public List<Info> gedanlist;
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
    }
}
