package com.vaiyee.hongmusic.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/3/31.
 */

public class WangYiMv {

    @SerializedName("data")
    public List<Data> mvlist;
    public class Data
    {
        @SerializedName("id")
        public String mvid;
        @SerializedName("cover")
        public String coverurl;
        @SerializedName("briefDesc")
        public String briefDesc;
        @SerializedName("playCoun")
        public String playCoun;

    }
}
