package com.vaiyee.hongmusic.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/2/14.
 */

public class OnlineLrc {
    @SerializedName("lrcContent")
    private String lrcContent;

    public String getLrcContent() {
        return lrcContent;
    }

    public void setLrcContent(String lrcContent) {
        this.lrcContent = lrcContent;
    }
}
