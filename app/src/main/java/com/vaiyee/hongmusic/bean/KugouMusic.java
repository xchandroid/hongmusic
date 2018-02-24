package com.vaiyee.hongmusic.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/2/20.
 */

public class KugouMusic {
    @SerializedName("data")
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        @SerializedName("hash")
        private String hash;
        @SerializedName("timelength")
        private String timelength;
        @SerializedName("filesize")
        private String filesize;
        @SerializedName("audio_name")
        private String audio_name;
        @SerializedName("have_album")
        private String have_album;
        @SerializedName("album_name")
        private String album_name;
        @SerializedName("album_id")
        private String album_id;
        @SerializedName("img")
        private String img;
        @SerializedName("have_mv")
        private String have_mv;
        @SerializedName("video_id")
        private String video_id;
        @SerializedName("author_name")
        private String author_name;
        @SerializedName("song_name")
        private String song_name;
        @SerializedName("lyrics")
        private String lyrics;
        @SerializedName("author_id")
        private String author_id;
        @SerializedName("play_url")
        private String play_url;
        @SerializedName("bitrate")
        private String bitrate;

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public String getTimelength() {
            return timelength;
        }

        public void setTimelength(String timelength) {
            this.timelength = timelength;
        }

        public String getFilesize() {
            return filesize;
        }

        public void setFilesize(String filesize) {
            this.filesize = filesize;
        }

        public String getAudio_name() {
            return audio_name;
        }

        public void setAudio_name(String audio_name) {
            this.audio_name = audio_name;
        }

        public String getHave_album() {
            return have_album;
        }

        public void setHave_album(String have_album) {
            this.have_album = have_album;
        }

        public String getAlbum_name() {
            return album_name;
        }

        public void setAlbum_name(String album_name) {
            this.album_name = album_name;
        }

        public String getAlbum_id() {
            return album_id;
        }

        public void setAlbum_id(String album_id) {
            this.album_id = album_id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getHave_mv() {
            return have_mv;
        }

        public void setHave_mv(String have_mv) {
            this.have_mv = have_mv;
        }

        public String getVideo_id() {
            return video_id;
        }

        public void setVideo_id(String video_id) {
            this.video_id = video_id;
        }

        public String getAuthor_name() {
            return author_name;
        }

        public void setAuthor_name(String author_name) {
            this.author_name = author_name;
        }

        public String getSong_name() {
            return song_name;
        }

        public void setSong_name(String song_name) {
            this.song_name = song_name;
        }

        public String getLyrics() {
            return lyrics;
        }

        public void setLyrics(String lyrics) {
            this.lyrics = lyrics;
        }

        public String getAuthor_id() {
            return author_id;
        }

        public void setAuthor_id(String author_id) {
            this.author_id = author_id;
        }

        public String getPlay_url() {
            return play_url;
        }

        public void setPlay_url(String play_url) {
            this.play_url = play_url;
        }

        public String getBitrate() {
            return bitrate;
        }

        public void setBitrate(String bitrate) {
            this.bitrate = bitrate;
        }
    }
}
