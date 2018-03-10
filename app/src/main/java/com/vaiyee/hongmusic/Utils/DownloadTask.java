package com.vaiyee.hongmusic.Utils;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.vaiyee.hongmusic.MyApplication;
import com.vaiyee.hongmusic.bean.Song;
import com.vaiyee.hongmusic.fragement.fragement1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/2/25.
 */

public class DownloadTask extends AsyncTask<String,Void,Integer> {
    private static String songName,geshou,path,ablumName;
    private static int time;
    private static final String ScanIntent = "android.intent.action.MEDIA_SCANNER_SCAN_DIR";

    @Override
    protected Integer doInBackground(String... strings) {
        String url  = strings[0];
         songName = strings[1];
         geshou = strings[2];
         ablumName = strings[3];
         time = Integer.parseInt(strings[4]);
        path = "/storage/emulated/0/HonchenMusic/download/" + songName+".mp3";
         Response response = null;
        File file = new File(path);
        if (!file.exists())
        {
            File dir = new File(file.getParent());  //获取file文件在内置或外置SD卡  getParent()与getParentFile()的区别：getParentFile()的返回值是File型的。而getParent() 的返回值是String型的。mkdirs是File类里面的方法，所以当然得用f.getParentFile().mkdirs();getParentFile()的返回值是File型的。而getParent() 的返回值是String型的。mkdirs是File类里面的方法，所以当然得用f.getParentFile().mkdirs();
            dir.mkdirs();  //先创建文件夹
            try {
                file.createNewFile();//创建文件

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            return 1;
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
             response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int len=0;
        int total=0;
        if (response!=null)
        {
            try {
                byte[] b = response.body().bytes();

                PrintStream printStream = new PrintStream(file);  //用打印流而不用FileoutputStream流是因为后者会导致mp3文件失真
                printStream.write(b);
                printStream.close();



            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {

                response.close();
            }

            /*
            inputStream = response.body().byteStream();
            byte[] bytes = new byte[4096];
            int len=0;
            int total=0;
            try {
                while ((len = inputStream.read(bytes))!=-1)
                {
                    try {

                        out.write(bytes);
                        total+= len;
                        Log.d("文件总长度是",String.valueOf(total));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                out.close();
                response.body().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            */

        }


        return 0;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        switch (integer)
        {
            case 0:
                Song song = new Song();
                song.setTitle(songName);
                song.setSinger(geshou);
                song.setFileUrl(path);
                song.setAlbum(ablumName);
                song.setDuration(time);
                fragement1.songs.add(0,song);
                fragement1.adapter.notifyDataSetChanged();
                scanFile(MyApplication.getQuanjuContext(), Environment.getExternalStorageDirectory().getAbsolutePath());
                MediaScannerConnection.scanFile(MyApplication.getQuanjuContext(), new String[]{path}, null, null);
                Toast.makeText(MyApplication.getQuanjuContext(),"下载成功",Toast.LENGTH_LONG).show();
                break;
            case 1:
                Toast.makeText(MyApplication.getQuanjuContext(),"歌曲已下载过啦",Toast.LENGTH_LONG).show();
                break;
        }
    }

    //更新媒体库
    /**
     * 通知媒体库更新文件
     * @param context
     * @param filePath 文件全路径
     *
     * */
    public static void scanFile(Context context, String filePath) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(Uri.fromFile(new File(filePath)));
        context.sendBroadcast(scanIntent);
    }
}
