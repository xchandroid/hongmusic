package com.vaiyee.hongmusic.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/2/20.
 */

public class KugouSearchResult {
    @SerializedName("lists")
    private List<lists> resultList;

    public List<lists> getResultList() {
        return resultList;
    }

    public void setResultList(List<lists> resultList) {
        this.resultList = resultList;
    }

    public static class lists {
        @SerializedName("SongName")
        private String SongName;
        @SerializedName("SingerName")
        private String SingerName;
        @SerializedName("Duration")
        private int Duration;
        @SerializedName("FileHash")
        private String FileHash;
        @SerializedName("AlbumName")
        private String AlbumName;

        public String getAlbumName() {
            return AlbumName;
        }

        public void setAlbumName(String albumName) {
            AlbumName = albumName;
        }

        public String getFileHash() {
            return FileHash;
        }

        public void setFileHash(String fileHash) {
            FileHash = fileHash;
        }

        public String getSongName() {
            return SongName;
        }

        public void setSongName(String songName) {
            SongName = songName;
        }

        public String getSingerName() {
            return SingerName;
        }

        public void setSingerName(String singerName) {
            SingerName = singerName;
        }

        public int getDuration() {
            return Duration;
        }

        public void setDuration(int duration) {
            Duration = duration;
        }
    }
}
