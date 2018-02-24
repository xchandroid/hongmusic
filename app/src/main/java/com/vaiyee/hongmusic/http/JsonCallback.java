package com.vaiyee.hongmusic.http;

import android.os.Environment;
import android.telecom.Call;
import android.util.Log;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
import java.io.FileOutputStream;

import okhttp3.Response;

/**
 * Created by Administrator on 2018/2/5.
 * 把返回的Json数据解析成实体类
 */

public abstract class JsonCallback<T> extends Callback<T> {
    private Class<T> clazz;
    private Gson gson;
    public JsonCallback(Class<T> clazz)
    {
        this.clazz = clazz;
        gson = new Gson();
    }

    @Override
    public T parseNetworkResponse(Response response, int id) throws Exception {
        try
        {
            String Jsonresponse = response.body().string();
            return gson.fromJson(Jsonresponse,clazz);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
