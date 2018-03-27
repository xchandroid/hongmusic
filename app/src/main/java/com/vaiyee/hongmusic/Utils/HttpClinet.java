package com.vaiyee.hongmusic.Utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.vaiyee.hongmusic.bean.Banner;
import com.vaiyee.hongmusic.bean.DownloadInfo;
import com.vaiyee.hongmusic.bean.Gedan;
import com.vaiyee.hongmusic.bean.GedanList;
import com.vaiyee.hongmusic.bean.KugouBang;
import com.vaiyee.hongmusic.bean.Sheet;
import com.vaiyee.hongmusic.bean.WangyiLrc;
import com.vaiyee.hongmusic.http.JsoncallbackKugou;
import com.vaiyee.hongmusic.bean.KugouMusic;
import com.vaiyee.hongmusic.bean.KugouSearchResult;
import com.vaiyee.hongmusic.bean.OnlineLrc;
import com.vaiyee.hongmusic.bean.OnlineMusiclist;
import com.vaiyee.hongmusic.bean.SearchMusic;
import com.vaiyee.hongmusic.bean.WangyiBang;
import com.vaiyee.hongmusic.http.HttpCallback;
import com.vaiyee.hongmusic.http.HttpInterceptor;
import com.vaiyee.hongmusic.http.JsonCallback;
import com.vaiyee.hongmusic.http.WangyiJsoncallback;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;


/**
 * Created by Administrator on 2018/2/5.
 */

public class HttpClinet {
 private static final String SPLASH_URL = "http://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";
 private static final String BASE_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting";
 private static final String METHOD_GET_MUSIC_LIST = "baidu.ting.billboard.billList";
 private static final String METHOD_DOWNLOAD_MUSIC = "baidu.ting.song.play";
 private static final String METHOD_ARTIST_INFO = "baidu.ting.artist.getInfo";
 private static final String METHOD_SEARCH_MUSIC = "baidu.ting.search.catalogSug";
 private static final String METHOD_LRC = "baidu.ting.song.lry";
 private static final String PARAM_METHOD = "method";
 private static final String PARAM_TYPE = "type";
 private static final String PARAM_SIZE = "size";
 private static final String PARAM_OFFSET = "offset";
 private static final String PARAM_SONG_ID = "songid";
 private static final String PARAM_TING_UID = "tinguid";
 private static final String PARAM_QUERY = "query";    //以上是百度音乐的接口
 private static final String KUGOU = "http://songsearch.kugou.com/song_search_v2?callback=jQuery191034642999175022426_1489023388639&keyword=";  //酷狗搜索音乐接口
 private static final String KUGOUURL = "&page=1&pagesize=";
 private static final String KUGOUEND = "&userid=-1&clientver=&platform=WebFilter&tag=em&filter=2&iscorrection=1&privilege_filter=0&_=1489023388641";
 private static final String  KUGO = "http://www.kugou.com/yy/index.php?r=play/getdata&hash="; //酷狗单曲详情详情
 private static final String WANGYI = "http://musicapi.leanapp.cn/top/list?idx=";  //网易云排行榜
 private static final String WYLRC = "http://musicapi.leanapp.cn/lyric?id=";   //网易云歌词接口
 private static final String KUGOUBANG = "http://m.kugou.com/rank/info/?rankid=";
 private static final String BANNER ="http://m.kugou.com/?json=true"; //首页banner接口
 private static final String KUGOUGEDAN="http://m.kugou.com/plist/index&json=true&page="; //酷狗推荐歌单接口
    private static final String KUGOUGEDANLIST = "http://m.kugou.com/plist/list/";//酷狗推荐歌单的歌曲列表接口，在后面加上歌单相应的ID，如125032?json=true

 static {
  OkHttpClient okHttpClient = new OkHttpClient.Builder()
          .connectTimeout(10, TimeUnit.SECONDS)
          .readTimeout(10, TimeUnit.SECONDS)
          .writeTimeout(10, TimeUnit.SECONDS)
          .addInterceptor(new HttpInterceptor())   //添加拦截器伪造头为电脑网页访问
          .build();
  OkHttpUtils.initClient(okHttpClient);    //初始化OkHttpClinet
 }
 //获取相应Type的在线音乐列表,这是百度在线列表
 public static void getOnlineMusicList(String type,int size,int offset,@NonNull final HttpCallback<OnlineMusiclist>callback)
 {
         OkHttpUtils.get().url(BASE_URL)
                 .addParams(PARAM_METHOD,METHOD_GET_MUSIC_LIST)
                 .addParams(PARAM_TYPE,type)
                 .addParams(PARAM_SIZE,String.valueOf(size))
                 .addParams(PARAM_OFFSET,String.valueOf(offset))
                 .build()
                 .execute(new JsonCallback<OnlineMusiclist>(OnlineMusiclist.class) {
                  @Override
                  public void onError(Call call, Exception e, int id) {
                   callback.onFail(e);
                  }

                  @Override
                  public void onAfter(int id) {
                   callback.onFinish();
                  }

                  @Override
                  public void onResponse(OnlineMusiclist response, int id) {
                   callback.onSuccess(response);
                  }
                 });
 }

 //根据歌曲ID获取在线音乐的下载（播放）地址以及歌曲的相关信息
    public static  void getMusicUrl(String id,@NonNull final HttpCallback<DownloadInfo>callback)
    {
        OkHttpUtils.get().url(BASE_URL)
                .addParams(PARAM_METHOD,METHOD_DOWNLOAD_MUSIC)
                .addParams(PARAM_SONG_ID,id)
                .build()
                .execute(new JsonCallback<DownloadInfo>(DownloadInfo.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onResponse(DownloadInfo response, int id) {
                        callback.onSuccess(response);
                    }

                    @Override
                    public void onAfter(int id) {
                        callback.onFinish();
                    }
                });
    }
    public static void SearchMusic(String keyword, final HttpCallback<SearchMusic> callback)
    {
        OkHttpUtils.get().url(BASE_URL)
                .addParams(PARAM_METHOD,METHOD_SEARCH_MUSIC)
                .addParams(PARAM_QUERY,keyword)
                .build()
                .execute(new JsonCallback<SearchMusic>(SearchMusic.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(SearchMusic response, int id) {
                        callback.onSuccess(response);
                    }
                });
    }

    public static void getLrc(String id, final HttpCallback<OnlineLrc>callback)
    {
        OkHttpUtils.get().url(BASE_URL)
                .addParams(PARAM_METHOD,METHOD_LRC)
                .addParams(PARAM_SONG_ID,id)
                .build()
                .execute(new JsonCallback<OnlineLrc>(OnlineLrc.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(OnlineLrc response, int id) {
                          callback.onSuccess(response);
                    }
                });
    }

   //调用酷狗的api进行音乐搜索
    public static void KugouSearch(String key,int size,final HttpCallback<KugouSearchResult> callback)
    {
        OkHttpUtils.get().url(KUGOU+key+KUGOUURL+size+KUGOUEND)
                .build()
                .execute(new JsoncallbackKugou<KugouSearchResult>(KugouSearchResult.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(KugouSearchResult response, int id) {
                        callback.onSuccess(response);
                    }
                });
    }
    //获取酷狗的单曲信息
    public static void KugouUrl(String hash, final HttpCallback<KugouMusic>callback)
    {
        OkHttpUtils.get().url(KUGO+hash)
                .build()
                .execute(new JsonCallback<KugouMusic>(KugouMusic.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(KugouMusic response, int id) {
                          callback.onSuccess(response);
                    }
                });
    }


    public static void WangyiBang(String type, final HttpCallback<WangyiBang> callback)
    {
            OkHttpUtils.get().url(WANGYI+type)
                    .build()
                   .execute(new JsonCallback<WangyiBang>(WangyiBang.class) {
                       @Override
                       public void onError(Call call, Exception e, int id) {
                           callback.onFail(e);
                       }

                       @Override
                       public void onResponse(WangyiBang response, int id) {
                               callback.onSuccess(response);
                       }
                   });
    }

    public static void WangyiLrc(String id, final HttpCallback<WangyiLrc> callback)
    {
        OkHttpUtils.get().url(WYLRC+id)
                .build()
                .execute(new JsonCallback<WangyiLrc>(WangyiLrc.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onResponse(WangyiLrc response, int id) {
                            callback.onSuccess(response);
                    }
                });
    }

    public static void getKugoubang(String rankid,int page, final HttpCallback<KugouBang> callback)
    {
        OkHttpUtils.get().url(KUGOUBANG+rankid+"&page="+page+"&json=true")
                .build()
                .execute(new JsonCallback<KugouBang>(KugouBang.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onResponse(KugouBang response, int id) {

                        callback.onSuccess(response);
                    }
                });
    }
    public static void getBanner(final HttpCallback<Banner> callback)
    {
        OkHttpUtils.get().url(BANNER)
                .build().execute(new JsonCallback<Banner>(Banner.class) {
            @Override
            public void onError(Call call, Exception e, int id) {
                callback.onFail(e);
            }

            @Override
            public void onResponse(Banner response, int id) {
                    callback.onSuccess(response);
            }
        });
    }
    public static void getKugouGedan(int page, final HttpCallback<Gedan>callback)
    {
        OkHttpUtils.get().url(KUGOUGEDAN+page)
                .build()
                .execute(new JsonCallback<Gedan>(Gedan.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onResponse(Gedan response, int id) {
                         callback.onSuccess(response);
                    }
                });
    }
    public static void getKugouGedanList(String id, final HttpCallback<GedanList>callback)
    {
            OkHttpUtils.get().url(KUGOUGEDANLIST+id+"?json=true")
                    .build()
                    .execute(new JsonCallback<GedanList>(GedanList.class) {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            callback.onFail(e);
                        }

                        @Override
                        public void onResponse(GedanList response, int id) {
                                 callback.onSuccess(response);
                        }
                    });
    }
}
