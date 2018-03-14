package com.vaiyee.hongmusic.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/3/14.
 */

public class KugouBangList {
    @SerializedName("filename")
    public String filename;
    @SerializedName("hash")
    public String hash;
    @SerializedName("duration")
    public int duration;
    @SerializedName("filesize")
    public String filesize;
    @SerializedName("sqhash")
    public String sqhash;
    @SerializedName("sqfilesize")
    public String sqfilesize;
    @SerializedName("320hash")
    public String hqhash;
    @SerializedName("320filesize")
    public String hqfilesize;
}
