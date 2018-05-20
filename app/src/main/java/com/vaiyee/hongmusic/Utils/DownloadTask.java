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

import com.vaiyee.hongmusic.DownloadListener;
import com.vaiyee.hongmusic.MyApplication;
import com.vaiyee.hongmusic.PlayMusic;
import com.vaiyee.hongmusic.bean.Song;
import com.vaiyee.hongmusic.fragement.fragement1;
import com.vaiyee.hongmusic.util.ID3TagUtils;
import com.vaiyee.hongmusic.util.ID3Tags;

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

public class DownloadTask extends AsyncTask<String,Integer,Integer> {
    private String songName,geshou,path,ablumName;
    private static int time;
    private static final String ScanIntent = "android.intent.action.MEDIA_SCANNER_SCAN_DIR";
    private DownloadListener listener;
    private int lastprogress=0;
    private long filelength =0;
    private int ID ;
    public DownloadTask(DownloadListener listener)
    {
        this.listener = listener;
    }
    public DownloadTask()
    {
        //这里需要定义一个空的构造方法
    }
    @Override
    protected Integer doInBackground(String... strings) {
        String url  = strings[0];
         songName = strings[1];
         geshou = strings[2];
         ablumName = strings[3];
         time = Integer.parseInt(strings[4]);
         ID = Integer.parseInt(strings[5]);
        path = Environment.getExternalStorageDirectory().getPath()+"/HonchenMusic/download/" + songName+".mp3"; //  Environment.getExternalStorageDirectory().getPath() 获取SD卡的路径
         Response response = null;
         InputStream inputStream = null;      //服务器返回的输入流
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

            /*
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

            */
            inputStream = response.body().byteStream();
            filelength = response.body().contentLength();  //获取返回的文件字节数总长度
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            byte[] bytes = new byte[1024];
            try {
                while ((len = inputStream.read(bytes))!=-1)        //每次读1024个字节，len为每次读取到的实际字节数
                {
                    try {
                        total+= len;
                        out.write(bytes,0,len);             //从字节数组索引0开始，写入每次读到的实际字节数（因为不是每次都一定是读满1024）
                        publishProgress((int)(total*100 / filelength),ID);       //更新实时下载进度,以百分比（整数）显示，所有乘以100再除
                        //Log.d("百分比",String.valueOf(total*100/filelength));
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


        }
        return 0;
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        int ID = values[1];
        if (progress>lastprogress) {
            listener.onProgress(songName, progress,ID);
            lastprogress = progress;
        }
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

                ID3Tags id3Tags = new ID3Tags.Builder()
                        .setTitle(songName)
                        .setArtist(geshou)
                        .setAlbum(ablumName)
                        .build();
                ID3TagUtils.setID3Tags(new File(path), id3Tags, false);          //下载完MP3文件后，设置歌曲详细信息，这里只设置了歌手，歌名和专辑名字

                scanFile(MyApplication.getQuanjuContext(), Environment.getExternalStorageDirectory().getAbsolutePath());
                MediaScannerConnection.scanFile(MyApplication.getQuanjuContext(), new String[]{path}, null, null);  //刷新媒体库
                 /*
                // 刷新媒体库
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(mMusicFile));       //这个方法也可以刷新媒体库
                sendBroadcast(intent);
                */
                Toast.makeText(MyApplication.getQuanjuContext(),songName +"  下载成功",Toast.LENGTH_LONG).show();
                listener.onSuccess(songName,ID);
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
