package com.vaiyee.hongmusic.bean;

/**
 * Created by Administrator on 2018/4/13.
 */

public class GeshouMvUrl {

    /**
     * status : 1
     * singer : 周杰伦
     * songname : 等你下课 (with 杨瑞代)
     * track : 3
     * type : 2
     * mvdata : {"hd":{"hash":"c5c97396f4002554c6f4dd09fc59dc1d","filesize":38356811,"timelength":269969,"bitrate":1136519,"downurl":"http://fs.mv.web.kugou.com/201804130009/61bade777496b35895b926d2bcd406ae/G131/M05/1A/07/I4cBAFqNgr6AXTS7AklHS3CSCYQ251.mp4"},"sd":{"hash":"9d69246fef60ffa64015d5556b8a84b1","filesize":21489832,"timelength":269969,"bitrate":636748,"downurl":"http://fs.mv.web.kugou.com/201804130009/a5b28850fa4ce91bb48d44028d0cb272/G124/M09/06/03/XJQEAFqNgriAfM7_AUfoqEhViW0925.mp4"},"sq":{"hash":"8b68758c386009626ce268dc2ad31af1","filesize":71886480,"timelength":269969,"bitrate":2130009,"downurl":"http://fs.mv.web.kugou.com/201804130009/d6c870c118546a288a6669373278ad89/G126/M0B/09/08/XpQEAFqNgsGAUddyBEjmkHejHPw966.mp4"},"rq":{"hash":"13a1c4cb3c63ef64d02f9608025bc510","filesize":139112695,"timelength":269969,"bitrate":4121934,"downurl":"http://fs.mv.web.kugou.com/201804130009/7900a79508dc4936bf97120373b70aff/G123/M00/03/04/W5QEAFqNgsuAEsyvCEqw921JHnI630.mp4"}}
     */

    private int status;
    private String singer;
    private String songname;
    private int track;
    private int type;
    private MvdataBean mvdata;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public int getTrack() {
        return track;
    }

    public void setTrack(int track) {
        this.track = track;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public MvdataBean getMvdata() {
        return mvdata;
    }

    public void setMvdata(MvdataBean mvdata) {
        this.mvdata = mvdata;
    }

    public static class MvdataBean {
        /**
         * hd : {"hash":"c5c97396f4002554c6f4dd09fc59dc1d","filesize":38356811,"timelength":269969,"bitrate":1136519,"downurl":"http://fs.mv.web.kugou.com/201804130009/61bade777496b35895b926d2bcd406ae/G131/M05/1A/07/I4cBAFqNgr6AXTS7AklHS3CSCYQ251.mp4"}
         * sd : {"hash":"9d69246fef60ffa64015d5556b8a84b1","filesize":21489832,"timelength":269969,"bitrate":636748,"downurl":"http://fs.mv.web.kugou.com/201804130009/a5b28850fa4ce91bb48d44028d0cb272/G124/M09/06/03/XJQEAFqNgriAfM7_AUfoqEhViW0925.mp4"}
         * sq : {"hash":"8b68758c386009626ce268dc2ad31af1","filesize":71886480,"timelength":269969,"bitrate":2130009,"downurl":"http://fs.mv.web.kugou.com/201804130009/d6c870c118546a288a6669373278ad89/G126/M0B/09/08/XpQEAFqNgsGAUddyBEjmkHejHPw966.mp4"}
         * rq : {"hash":"13a1c4cb3c63ef64d02f9608025bc510","filesize":139112695,"timelength":269969,"bitrate":4121934,"downurl":"http://fs.mv.web.kugou.com/201804130009/7900a79508dc4936bf97120373b70aff/G123/M00/03/04/W5QEAFqNgsuAEsyvCEqw921JHnI630.mp4"}
         */

        private HdBean hd;
        private SdBean sd;
        private SqBean sq;
        private RqBean rq;

        public HdBean getHd() {
            return hd;
        }

        public void setHd(HdBean hd) {
            this.hd = hd;
        }

        public SdBean getSd() {
            return sd;
        }

        public void setSd(SdBean sd) {
            this.sd = sd;
        }

        public SqBean getSq() {
            return sq;
        }

        public void setSq(SqBean sq) {
            this.sq = sq;
        }

        public RqBean getRq() {
            return rq;
        }

        public void setRq(RqBean rq) {
            this.rq = rq;
        }

        public static class HdBean {
            /**
             * hash : c5c97396f4002554c6f4dd09fc59dc1d
             * filesize : 38356811
             * timelength : 269969
             * bitrate : 1136519
             * downurl : http://fs.mv.web.kugou.com/201804130009/61bade777496b35895b926d2bcd406ae/G131/M05/1A/07/I4cBAFqNgr6AXTS7AklHS3CSCYQ251.mp4
             */

            private String hash;
            private int filesize;
            private int timelength;
            private int bitrate;
            private String downurl;

            public String getHash() {
                return hash;
            }

            public void setHash(String hash) {
                this.hash = hash;
            }

            public int getFilesize() {
                return filesize;
            }

            public void setFilesize(int filesize) {
                this.filesize = filesize;
            }

            public int getTimelength() {
                return timelength;
            }

            public void setTimelength(int timelength) {
                this.timelength = timelength;
            }

            public int getBitrate() {
                return bitrate;
            }

            public void setBitrate(int bitrate) {
                this.bitrate = bitrate;
            }

            public String getDownurl() {
                return downurl;
            }

            public void setDownurl(String downurl) {
                this.downurl = downurl;
            }
        }

        public static class SdBean {
            /**
             * hash : 9d69246fef60ffa64015d5556b8a84b1
             * filesize : 21489832
             * timelength : 269969
             * bitrate : 636748
             * downurl : http://fs.mv.web.kugou.com/201804130009/a5b28850fa4ce91bb48d44028d0cb272/G124/M09/06/03/XJQEAFqNgriAfM7_AUfoqEhViW0925.mp4
             */

            private String hash;
            private int filesize;
            private int timelength;
            private int bitrate;
            private String downurl;

            public String getHash() {
                return hash;
            }

            public void setHash(String hash) {
                this.hash = hash;
            }

            public int getFilesize() {
                return filesize;
            }

            public void setFilesize(int filesize) {
                this.filesize = filesize;
            }

            public int getTimelength() {
                return timelength;
            }

            public void setTimelength(int timelength) {
                this.timelength = timelength;
            }

            public int getBitrate() {
                return bitrate;
            }

            public void setBitrate(int bitrate) {
                this.bitrate = bitrate;
            }

            public String getDownurl() {
                return downurl;
            }

            public void setDownurl(String downurl) {
                this.downurl = downurl;
            }
        }

        public static class SqBean {
            /**
             * hash : 8b68758c386009626ce268dc2ad31af1
             * filesize : 71886480
             * timelength : 269969
             * bitrate : 2130009
             * downurl : http://fs.mv.web.kugou.com/201804130009/d6c870c118546a288a6669373278ad89/G126/M0B/09/08/XpQEAFqNgsGAUddyBEjmkHejHPw966.mp4
             */

            private String hash;
            private int filesize;
            private int timelength;
            private int bitrate;
            private String downurl;

            public String getHash() {
                return hash;
            }

            public void setHash(String hash) {
                this.hash = hash;
            }

            public int getFilesize() {
                return filesize;
            }

            public void setFilesize(int filesize) {
                this.filesize = filesize;
            }

            public int getTimelength() {
                return timelength;
            }

            public void setTimelength(int timelength) {
                this.timelength = timelength;
            }

            public int getBitrate() {
                return bitrate;
            }

            public void setBitrate(int bitrate) {
                this.bitrate = bitrate;
            }

            public String getDownurl() {
                return downurl;
            }

            public void setDownurl(String downurl) {
                this.downurl = downurl;
            }
        }

        public static class RqBean {
            /**
             * hash : 13a1c4cb3c63ef64d02f9608025bc510
             * filesize : 139112695
             * timelength : 269969
             * bitrate : 4121934
             * downurl : http://fs.mv.web.kugou.com/201804130009/7900a79508dc4936bf97120373b70aff/G123/M00/03/04/W5QEAFqNgsuAEsyvCEqw921JHnI630.mp4
             */

            private String hash;
            private int filesize;
            private int timelength;
            private int bitrate;
            private String downurl;

            public String getHash() {
                return hash;
            }

            public void setHash(String hash) {
                this.hash = hash;
            }

            public int getFilesize() {
                return filesize;
            }

            public void setFilesize(int filesize) {
                this.filesize = filesize;
            }

            public int getTimelength() {
                return timelength;
            }

            public void setTimelength(int timelength) {
                this.timelength = timelength;
            }

            public int getBitrate() {
                return bitrate;
            }

            public void setBitrate(int bitrate) {
                this.bitrate = bitrate;
            }

            public String getDownurl() {
                return downurl;
            }

            public void setDownurl(String downurl) {
                this.downurl = downurl;
            }
        }
    }
}
