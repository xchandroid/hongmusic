package com.vaiyee.hongmusic.http;


import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/6/16.
 */

public abstract class WangyiBangJsoncallBack<T> extends Callback<T> {
    private Gson gson;
    private Class<T> clazz;

    public WangyiBangJsoncallBack(Class<T> clazz)
    {
        this.clazz =clazz;
        gson = new Gson();
    }
    @Override
    public T parseNetworkResponse(Response response, int id) throws Exception {
        String html = response.body().string();
        String json = html.substring(html.indexOf("[{"),html.lastIndexOf("]}")+1);
        String json2 = "{\"list\":"+json+"}";
        return gson.fromJson(json2,clazz);
    }

}
