package com.vaiyee.hongmusic.http;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/3/8.
 */

public abstract class WangyiJsoncallback<T> extends Callback<T> {
    private Class<T> clazz;
    private Gson gson;
    public WangyiJsoncallback(Class<T> clazz)
    {
          this.clazz = clazz;
          this.gson = new Gson();
    }
    @Override
    public T parseNetworkResponse(Response response, int id) throws Exception {
        try {
            String json = response.body().string();
            String json2 =json.substring(json.indexOf("[{"),json.lastIndexOf("}]"+1));
            String json3 = "{"+"\"tracks\""+":"+json2+"}";
            return gson.fromJson(json3,clazz);
        }catch (Exception ee)
        {
            ee.printStackTrace();
        }
        return null;
    }

}
