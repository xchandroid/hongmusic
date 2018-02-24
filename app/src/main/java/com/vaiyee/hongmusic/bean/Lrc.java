package com.vaiyee.hongmusic.bean;

import android.util.Log;

import java.util.List;

/**
 * Created by Administrator on 2018/2/13.
 */

public class Lrc {

   private String title;
   private String artist;
   private String ablum;
   private Long offset;
   private List<LineInfo> lineInfoList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAblum() {
        return ablum;
    }

    public void setAblum(String ablum) {
        this.ablum = ablum;
    }

    public Long getOffset() {
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    public List<LineInfo> getLineInfoList() {
        return lineInfoList;
    }

    public void setLineInfoList(List<LineInfo> lineInfoList) {
        this.lineInfoList = lineInfoList;
    }

    class LineInfo
    {
        private String lrc;
        private long startTime;

        public String getLrc() {
        return lrc;
    }

        public void setLrc(String lrc) {
        this.lrc = lrc;
    }

        public long getStartTime() {
        return startTime;
    }

        public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    }

}
