package com.vaiyee.hongmusic.Utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.vaiyee.hongmusic.bean.Ablum;
import com.vaiyee.hongmusic.bean.Banner;
import com.vaiyee.hongmusic.bean.DownloadInfo;
import com.vaiyee.hongmusic.bean.Gedan;
import com.vaiyee.hongmusic.bean.GedanList;
import com.vaiyee.hongmusic.bean.Geshou;
import com.vaiyee.hongmusic.bean.GeshouMv;
import com.vaiyee.hongmusic.bean.GeshouMvUrl;
import com.vaiyee.hongmusic.bean.GeshouType;
import com.vaiyee.hongmusic.bean.KugouBang;
import com.vaiyee.hongmusic.bean.MvData;
import com.vaiyee.hongmusic.bean.SearchSinger;
import com.vaiyee.hongmusic.bean.Sheet;
import com.vaiyee.hongmusic.bean.SingerInfo;
import com.vaiyee.hongmusic.bean.WangYiMv;
import com.vaiyee.hongmusic.bean.WangyiLrc;
import com.vaiyee.hongmusic.http.GedanlistJsonCallback;
import com.vaiyee.hongmusic.http.GeshouTypeJson;
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

import io.vov.vitamio.utils.Log;
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
     private static final String BANNER ="http://kuhaoapigz.kugou.com/api/v1/banner/index?api_ver=2&version=8948&plat=0"; //首页banner接口
     private static  final String KUGOUGEDAN="http://mobilecdngz.kugou.com/api/v3/category/special?ugc=1&withsong=1&plat=0&sort=3&pagesize=20&categoryid=0&page=";//酷狗推荐歌单接口
     private static final String KUGOUGEDANLIST ="http://mobilecdngz.kugou.com/api/v3/special/song?version=8948&plat=0&pagesize=-1&area_code=1&page=1&specialid=";
     private static final String WANGYIMV_ZUIXIN ="http://musicapi.leanapp.cn/mv/first?limit=100";  //网易云音乐的MV接口 ，这个是返回最新的MV
     private static final String MVURL = "http://musicapi.leanapp.cn/mv?mvid=";           //  获取网易云音乐MV 播放地址；后面传入MV的相应ID
     private static final String GESHOUTYPE = "http://mobilecdngz.kugou.com/api/v5/singer/list?showtype=2&";  //歌手分类
     private static final String GESHOUADD = "&with_res_tag=1";  //接上歌手分类接口
     private static final String SONGLIST = "http://mobilecdngz.kugou.com/api/v3/singer/song?version=8948&plat=0&area_code=1&page=1&with_res_tag=1";
     private static final String ABLUM = "http://mobilecdngz.kugou.com/api/v3/singer/album?version=8948&plat=0&pagesize=40&page=1&with_res_tag=1";
     private static final String SINGERINFO = "http://mobilecdngz.kugou.com/api/v3/singer/info?with_res_tag=1";
     private static final String GESHOUMV = "http://mobilecdngz.kugou.com/api/v3/singer/mv?pagesize=300&page=1&with_res_tag=1";
     private static final String AblumSongList = "http://mobilecdngz.kugou.com/api/v3/album/song?version=8948&plat=0&pagesize=-1&area_code=1&page=1&with_res_tag=1";
     private static final String SEARCHSINGER = "http://mobilecdngz.kugou.com/api/v3/search/singer?keyword=";
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
            OkHttpUtils.get().url(KUGOUGEDANLIST+id+"&with_res_tag=1")
                    .build()
                    .execute(new GedanlistJsonCallback<GedanList>(GedanList.class) {
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

    public static void getWangyiMV(final HttpCallback<WangYiMv>callback)
    {
        OkHttpUtils.get().url(WANGYIMV_ZUIXIN)
                .build()
                .execute(new JsonCallback<WangYiMv>(WangYiMv.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onResponse(WangYiMv response, int id) {
                              callback.onSuccess(response);
                    }
                });
    }
    public static void getMvUrl(String mvid, final HttpCallback<MvData>callback)
    {
        OkHttpUtils.get()
                .url(MVURL+mvid)
                .build()
                .execute(new JsonCallback<MvData>(MvData.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onResponse(MvData response, int id) {
                        callback.onSuccess(response);
                    }
                });
    }

    public static void getGeshoubang(final HttpCallback<Geshou>callback)     //酷狗歌手飙升榜
    {
            OkHttpUtils.get()
                    .url("http://mobilecdngz.kugou.com/api/v5/singer/list?version=8948&showtype=1&plat=0&sextype=0&sort=2&pagesize=100&type=0&page=1&musician=0")
                    .build()
                    .execute(new JsonCallback<Geshou>(Geshou.class) {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            callback.onFail(e);
                        }

                        @Override
                        public void onResponse(Geshou response, int id) {
                                 callback.onSuccess(response);
                        }
                    });
    }


    public static void getGeshouRemen(final HttpCallback<Geshou>callback)     //酷狗歌手热门榜
    {
        OkHttpUtils.get()
                .url("http://mobilecdngz.kugou.com/api/v5/singer/list?version=8948&showtype=1&plat=0&sextype=0&sort=1&pagesize=100&type=0&page=1&musician=0")
                .build()
                .execute(new JsonCallback<Geshou>(Geshou.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onResponse(Geshou response, int id) {
                        callback.onSuccess(response);
                    }
                });
    }

    public static  void getGeshoutype(String type, final HttpCallback<GeshouType>callback)
    {
        OkHttpUtils.get().url(GESHOUTYPE+type+GESHOUADD)
                .build()
                .execute(new GeshouTypeJson<GeshouType>(GeshouType.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onResponse(GeshouType response, int id) {
                          callback.onSuccess(response);
                    }
                });
    }


    public static void getGeshouSonglist(String singerid, int size, int sortytype, final HttpCallback<GedanList>callback)
    {
        OkHttpUtils.get()
                .url(SONGLIST)
                .addParams("singerid",singerid)
                .addParams("pagesize", String.valueOf(size))
                .addParams("sorttype", String.valueOf(sortytype))
                .build()
                .execute(new GedanlistJsonCallback<GedanList>(GedanList.class) {
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

    public static void getAblumlist(String id, final HttpCallback<Ablum>callback)
    {
              OkHttpUtils.get().url(ABLUM)
                      .addParams("singerid",id)
                      .build()
                      .execute(new GedanlistJsonCallback<Ablum>(Ablum.class) {
                          @Override
                          public void onError(Call call, Exception e, int id) {
                              callback.onFail(e);
                          }

                          @Override
                          public void onResponse(Ablum response, int id) {
                                 callback.onSuccess(response);
                          }
                      });
    }


    public static void getSingerInfo(String id, final HttpCallback<SingerInfo>callback)
    {
              OkHttpUtils.get().url(SINGERINFO)
                      .addParams("singerid",id)
                      .build().execute(new GedanlistJsonCallback<SingerInfo>(SingerInfo.class) {
                  @Override
                  public void onError(Call call, Exception e, int id) {
                      callback.onFail(e);
                  }

                  @Override
                  public void onResponse(SingerInfo response, int id) {
                               callback.onSuccess(response);
                  }
              });
    }

    public static void getSinggerMV(String singerName, String id, final HttpCallback<GeshouMv>callback)
    {
        OkHttpUtils.get().url(GESHOUMV)
                .addParams("singername",singerName)
                .addParams("singerid",id)
                .build()
                .execute(new GedanlistJsonCallback<GeshouMv>(GeshouMv.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onResponse(GeshouMv response, int id) {
                        callback.onSuccess(response);
                    }
                });
    }



    public static  void getGeshouMvUrl(String url, final HttpCallback<GeshouMvUrl>callback)
    {
        OkHttpUtils.get().url(url)
                .build()
                .execute(new JsonCallback<GeshouMvUrl>(GeshouMvUrl.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onResponse(GeshouMvUrl response, int id) {
                         callback.onSuccess(response);
                    }
                });
    }

    public static void getAblumSongList(String ablumid, final HttpCallback<GedanList>callback)
    {
        OkHttpUtils.get().url(AblumSongList)
                .addParams("albumid",ablumid)
                .build()
                .execute(new GedanlistJsonCallback<GedanList>(GedanList.class) {
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


    public static void SearchSinger(String keyword, final HttpCallback<SearchSinger>callback)
    {
        OkHttpUtils.get().url(SEARCHSINGER+keyword)
                .build()
                .execute(new JsonCallback<SearchSinger>(SearchSinger.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onResponse(SearchSinger response, int id) {
                         callback.onSuccess(response);
                    }
                });
    }
}
