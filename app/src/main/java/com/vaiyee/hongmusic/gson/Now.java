package com.vaiyee.hongmusic.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/11/12.
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;
    @SerializedName("cond")
    public More more;

    public class More{
        @SerializedName("txt")        //天气状态
        public String info;
        @SerializedName("code")     //天气状态代码
        public int code;
    }
      @SerializedName("wind_dir")      //风向
        public String dir;
      @SerializedName("wind_sc")       //风力
        public String sc;
      @SerializedName("wind_spd")      //风速
        public String spd;

}
