package com.vaiyee.hongmusic.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/3/25.
 */

public class Banner {
    @SerializedName("banner")
    public List<Fengmian> bannerlist;

   public class Fengmian
    {
        @SerializedName("imgurl")
        public String img ;
        @SerializedName("title")
        public String title;
        @SerializedName("extra")
        public Extra extra;

    }
   public class Extra
    {
        @SerializedName("tourl")
        public String tourl;
    }
}
