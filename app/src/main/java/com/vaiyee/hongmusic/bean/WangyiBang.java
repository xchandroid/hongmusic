package com.vaiyee.hongmusic.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/8.
 */

public class WangyiBang {


    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * score : 0.0
         * copyrightId : 14026
         * mvid : 5917878
         * transNames : null
         * commentThreadId : R_SO_4_573384240
         * publishTime : 1528732800007
         * no : 1
         * fee : 0
         * ftype : 0
         * type : 0
         * duration : 255708
         * status : 0
         * privilege : {"st":0,"flag":0,"subp":1,"fl":320000,"fee":0,"dl":320000,"cp":1,"preSell":false,"cs":false,"toast":false,"maxbr":999000,"id":573384240,"pl":320000,"sp":7,"payed":0}
         * djid : 0
         * album : {"id":39701663,"name":"如约而至","picUrl":"http://p1.music.126.net/MUOEUR7zixJuKUsLSQF9gQ==/109951163353478766.jpg","tns":[],"pic_str":"109951163353478766","pic":109951163353478766}
         * artists : [{"id":5771,"name":"许嵩","tns":[],"alias":[]}]
         * alias : []
         * name : 如约而至
         * id : 573384240
         * lastRank : 4
         */

        private int mvid;
        private Object transNames;
        private long publishTime;
        private int duration;
        private int status;
        private PrivilegeBean privilege;
        private AlbumBean album;
        private String name;
        private int id;
        private int lastRank;
        private List<ArtistsBean> artists;

        public int getMvid() {
            return mvid;
        }

        public void setMvid(int mvid) {
            this.mvid = mvid;
        }

        public Object getTransNames() {
            return transNames;
        }

        public void setTransNames(Object transNames) {
            this.transNames = transNames;
        }

        public long getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(long publishTime) {
            this.publishTime = publishTime;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public PrivilegeBean getPrivilege() {
            return privilege;
        }

        public void setPrivilege(PrivilegeBean privilege) {
            this.privilege = privilege;
        }

        public AlbumBean getAlbum() {
            return album;
        }

        public void setAlbum(AlbumBean album) {
            this.album = album;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLastRank() {
            return lastRank;
        }

        public void setLastRank(int lastRank) {
            this.lastRank = lastRank;
        }

        public List<ArtistsBean> getArtists() {
            return artists;
        }

        public void setArtists(List<ArtistsBean> artists) {
            this.artists = artists;
        }

        public static class PrivilegeBean {
            /**
             * st : 0
             * flag : 0
             * subp : 1
             * fl : 320000
             * fee : 0
             * dl : 320000
             * cp : 1
             * preSell : false
             * cs : false
             * toast : false
             * maxbr : 999000
             * id : 573384240
             * pl : 320000
             * sp : 7
             * payed : 0
             */

            private int id;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }

        public static class AlbumBean {
            /**
             * id : 39701663
             * name : 如约而至
             * picUrl : http://p1.music.126.net/MUOEUR7zixJuKUsLSQF9gQ==/109951163353478766.jpg
             * tns : []
             * pic_str : 109951163353478766
             * pic : 109951163353478766
             */

            private int id;
            private String name;
            private String picUrl;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPicUrl() {
                return picUrl;
            }

            public void setPicUrl(String picUrl) {
                this.picUrl = picUrl;
            }
        }

        public static class ArtistsBean {
            /**
             * id : 5771
             * name : 许嵩
             * tns : []
             * alias : []
             */

            private int id;
            private String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
