package com.vaiyee.hongmusic.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/3/25.
 */

public class Banner {

    @SerializedName("data")
    public DataBean bean;
    public static class DataBean {
        /**
         * timestamp : 1522822791
         * info : [{"title":"四月即将上映的四部大戏，最后一部从万众期待到深受抵制？","id":1181,"type":3,"img":"http://imge.kugou.com/mobilebanner/20180404/20180404094615156681.jpg","extra":{"url":"http://m.kugou.com/kuHao/article.html?changeFrame=1&id=29687"}},{"title":"厉害了！小哥哥自弹自唱，改编串烧Eason的十首名曲","id":1179,"type":2,"img":"http://imge.kugou.com/mobilebanner/20180403/20180403095850955524.png","extra":{"video_hash":"AB61A29A3C8493FF9AECA7EC4F2D34D1","user_name":"乐癌患者","avatar":"http://imge.kugou.com/kugouicon/165/20180323/20180323090057778359.jpg","user_id":605251573}},{"title":"周杰伦谢霆锋李健成导师 新一季《中国新歌声》三位才子一台戏","id":1177,"type":3,"img":"http://imge.kugou.com/mobilebanner/20180402/20180402094029462766.jpg","extra":{"url":"http://m.kugou.com/kuHao/article.html?changeFrame=1&id=28165"}},{"title":"庄心妍2018全新单曲《多么舍不得》","id":1175,"type":4,"img":"http://imge.kugou.com/mobilebanner/20180401/20180401224004440071.jpg","extra":{"url":"http://zhuanjistatic.kugou.com/html/mobile_commonchargeV3/index_118380.html?is_go=1&hreffrom=20"}},{"title":"同学们，娱乐圈秀恩爱套路了解一下！","id":1171,"type":3,"img":"http://imge.kugou.com/mobilebanner/20180401/20180401100235963710.jpg","extra":{"url":"http://m.kugou.com/kuHao/article.html?changeFrame=1&id=28027"}},{"title":"《头号玩家》上映3天稳居票房榜第一，这五大看点不容错过。","id":1173,"type":3,"img":"http://imge.kugou.com/mobilebanner/20180401/20180401155257908593.jpg","extra":{"url":"http://m.kugou.com/kuHao/article.html?changeFrame=1&id=29229"}},{"title":"《歌手》第十一期：Jessie J夺五冠破纪录，张韶涵的回忆杀很强大","id":1169,"type":3,"img":"http://imge.kugou.com/mobilebanner/20180331/20180331124612321564.jpg","extra":{"url":"http://m.kugou.com/kuHao/article.html?changeFrame=1&id=28795"}},{"title":"救救童年，大佬们别再翻拍啦！放过那些童年经典吧！","id":1167,"type":3,"img":"http://imge.kugou.com/mobilebanner/20180331/20180331103540763475.jpg","extra":{"url":"http://m.kugou.com/kuHao/article.html?changeFrame=1&id=27323"}}]
         */

        private List<InfoBean> info;

        public List<InfoBean> getInfo() {
            return info;
        }

        public void setInfo(List<InfoBean> info) {
            this.info = info;
        }

        public static class InfoBean {
            /**
             * title : 四月即将上映的四部大戏，最后一部从万众期待到深受抵制？
             * id : 1181
             * type : 3
             * img : http://imge.kugou.com/mobilebanner/20180404/20180404094615156681.jpg
             * extra : {"url":"http://m.kugou.com/kuHao/article.html?changeFrame=1&id=29687"}
             */

            private String title;
            private int id;
            private String img;
            private ExtraBean extra;
            private int type;

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public ExtraBean getExtra() {
                return extra;
            }

            public void setExtra(ExtraBean extra) {
                this.extra = extra;
            }

            public static class ExtraBean {
                /**
                 * url : http://m.kugou.com/kuHao/article.html?changeFrame=1&id=29687
                 */

                private String url;
                private String video_hash;

                public String getVideo_hash() {
                    return video_hash;
                }

                public void setVideo_hash(String video_hash) {
                    this.video_hash = video_hash;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }
        }
    }
}
