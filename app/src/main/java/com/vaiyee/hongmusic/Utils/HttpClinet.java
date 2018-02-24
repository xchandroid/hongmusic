package com.vaiyee.hongmusic.Utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.vaiyee.hongmusic.bean.DownloadInfo;
import com.vaiyee.hongmusic.bean.JsoncallbackKugou;
import com.vaiyee.hongmusic.bean.KugouMusic;
import com.vaiyee.hongmusic.bean.KugouSearchResult;
import com.vaiyee.hongmusic.bean.OnlineLrc;
import com.vaiyee.hongmusic.bean.OnlineMusiclist;
import com.vaiyee.hongmusic.bean.SearchMusic;
import com.vaiyee.hongmusic.http.HttpCallback;
import com.vaiyee.hongmusic.http.HttpInterceptor;
import com.vaiyee.hongmusic.http.JsonCallback;
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
 private static final String PARAM_QUERY = "query";
 private static final String KUGOU = "http://songsearch.kugou.com/song_search_v2?callback=jQuery191034642999175022426_1489023388639&keyword=";
 private static final String KUGOUURL = "&page=1&pagesize=";
 private static final String KUGOUEND = "&userid=-1&clientver=&platform=WebFilter&tag=em&filter=2&iscorrection=1&privilege_filter=0&_=1489023388641";
private static final String  KUGO = "http://www.kugou.com/yy/index.php?r=play/getdata&hash=";

 static {
  OkHttpClient okHttpClient = new OkHttpClient.Builder()
          .connectTimeout(10, TimeUnit.SECONDS)
          .readTimeout(10, TimeUnit.SECONDS)
          .writeTimeout(10, TimeUnit.SECONDS)
          .addInterceptor(new HttpInterceptor())
          .build();
  OkHttpUtils.initClient(okHttpClient);
 }
 //获取相应Type的在线音乐列表
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
}
