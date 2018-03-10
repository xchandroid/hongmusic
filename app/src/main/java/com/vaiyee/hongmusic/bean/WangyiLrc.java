package com.vaiyee.hongmusic.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/3/9.
 */

public class WangyiLrc {
    @SerializedName("lrc")
    public Lrc lrc;

    public class Lrc
    {
        @SerializedName("lyric")
        public String lyric;
    }
}
