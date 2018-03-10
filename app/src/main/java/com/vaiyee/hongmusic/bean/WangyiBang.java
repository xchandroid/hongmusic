package com.vaiyee.hongmusic.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/3/8.
 */

public class WangyiBang {


    @SerializedName("result")
    public Result result;

    public class Result
    {


        @SerializedName("name")
        public String bangName;

        @SerializedName("trackUpdateTime")
        public String updatatime;

        @SerializedName("description")
        public String description;

        @SerializedName("playCount")
        public String  playCount;

        @SerializedName("tracks")
        public List<Tracks> tracksList;
    }
}
