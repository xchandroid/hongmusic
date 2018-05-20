package com.vaiyee.hongmusic.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * JavaBean
 * Created by xch on 2018/2/13.  、、这个是百度音乐搜索接口的实体类，已弃用
 */
public class SearchMusic {
    @SerializedName("song")
    private List<Song> song;

    public List<Song> getSong() {
        return song;
    }

    public void setSong(List<Song> song) {
        this.song = song;
    }

    public static class Song {
        @SerializedName("songname")
        private String songname;
        @SerializedName("artistname")
        private String artistname;
        @SerializedName("songid")
        private String songid;


        public String getSongname() {
            return songname;
        }

        public void setSongname(String songname) {
            this.songname = songname;
        }

        public String getArtistname() {
            return artistname;
        }

        public void setArtistname(String artistname) {
            this.artistname = artistname;
        }

        public String getSongid() {
            return songid;
        }

        public void setSongid(String songid) {
            this.songid = songid;
        }
    }
}
