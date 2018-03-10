package com.vaiyee.hongmusic.bean;


import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/3/8.
 */

public class Tracks {
    @SerializedName("name")
    public String songname;

    @SerializedName("id")
    public String Id;

    @SerializedName("duration")
    public int duration;

    @SerializedName("artists")
    public List<Artist> artist;

    @SerializedName("album")
    public Abulum abulum;

    public class Artist
    {
        @SerializedName("name")
        public String artistName;
    }
    public class Abulum
    {
        @SerializedName("blurPicUrl")
        public String pic;
        @SerializedName("name")
        public String ablumName;
    }
}
