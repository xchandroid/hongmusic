package com.vaiyee.hongmusic.http;

/**
 * Created by Administrator on 2018/2/20.
 */

import android.os.Environment;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
import java.io.FileOutputStream;

import okhttp3.Response;

/**
 * Created by Administrator on 2018/2/5.
 * 把返回的Json数据解析成实体类
 */

public abstract class JsoncallbackKugou<T> extends Callback<T> {
    private Class<T> clazz;
    private Gson gson;
    public JsoncallbackKugou(Class<T> clazz)
    {
        this.clazz = clazz;
        gson = new Gson();
    }

    @Override
    public T parseNetworkResponse(Response response, int id) throws Exception {
        try
        {
            String Jsonresponse = response.body().string();
            String jsonSub = Jsonresponse.substring(Jsonresponse.indexOf("{"),Jsonresponse.lastIndexOf(")"));
            String jsonSub2 = jsonSub.substring(jsonSub.indexOf("[{"),jsonSub.lastIndexOf("}]"));
            String jsonSub3 = jsonSub2.substring(jsonSub2.indexOf("[{"),jsonSub2.lastIndexOf("}]")+2);
            String jsonSub4 = "{"+"\"lists\""+":"+jsonSub3+"}";
            createjson(jsonSub4);
            return gson.fromJson(jsonSub4,clazz);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    private void createjson(String lrcContent)
    {
        String filePath = null;
        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);//是否有外置SD卡
        if (hasSDCard) {
            filePath =Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator+"HonchenMusic"+File.separator+"json.txt";
        } else
            filePath =Environment.getDownloadCacheDirectory().toString() + File.separator +"json.txt";

        try {
            File file = new File(filePath);
            if (!file.exists()) {   //如果文件不存在
                File dir = new File(file.getParent());  //获取file文件在内置或外置SD卡  getParent()与getParentFile()的区别：getParentFile()的返回值是File型的。而getParent() 的返回值是String型的。mkdirs是File类里面的方法，所以当然得用f.getParentFile().mkdirs();getParentFile()的返回值是File型的。而getParent() 的返回值是String型的。mkdirs是File类里面的方法，所以当然得用f.getParentFile().mkdirs();
                dir.mkdirs();  //先创建文件夹
                file.createNewFile();//创建文件
            }
            FileOutputStream outStream = new FileOutputStream(file);//创建文件字节输出流对象，以字节形式写入所创建的文件中
            outStream.write(lrcContent.getBytes());//开始写入文件（也就是把文件写入内存卡中）
            outStream.close();//关闭文件字节输出流
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
