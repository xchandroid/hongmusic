package com.vaiyee.hongmusic.util;


import com.google.gson.Gson;
import com.vaiyee.hongmusic.SearchActivity;
import com.vaiyee.hongmusic.bean.WangyiBang;
import com.vaiyee.hongmusic.http.HttpCallback;
import com.vaiyee.hongmusic.http.WangyibangBackListener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/11/11.
 */

public class HttpUtil {
    private static final String WANGYI = "http://music.163.com/discover/toplist?id=";  //网易云排行榜
    public static void sendOkhttpRequest(String address, okhttp3.Callback callback)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void getWangyiBang(String type, final WangyibangBackListener listener)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(WANGYI+type).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFail(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String html = response.body().string();
                String json = html.substring(html.indexOf("[{"),html.lastIndexOf("}]")+2); //这里后退2位，不然会报错
                String json2 = "{\"list\":"+json+"}";
                SearchActivity.creatLrc(json2,"网易最终的Json数据");
                WangyiBang wangyiBang =new Gson().fromJson(json2,WangyiBang.class);
                listener.onSuccess(wangyiBang);
            }
        });
    }
}
