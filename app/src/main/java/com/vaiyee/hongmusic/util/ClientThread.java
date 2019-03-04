package com.vaiyee.hongmusic.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


/**
 * Created by Administrator on 2018/6/12.
 */

public class ClientThread extends Thread {
    private OutputStream out =null;
    private InputStream in = null;
    private Handler handler;
    private Socket socket=null;
    private Activity activity;
    public ClientThread(Activity activity,Handler handler)
    {
        this.activity = activity;
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            SocketAddress address = new InetSocketAddress("129.204.59.119", 6666);
            socket = new Socket();
            socket.connect(address,5000);
            //socket = new Socket("123.207.99.189",6666);
            while (true)
            {
                in = socket.getInputStream();
                byte[]bytes = new byte[1024];
                int len = in.read(bytes);
                String s = new String(bytes,0,len);
                String[] ss = s.split(":");
                if (ss[1].equals("关闭"))
                {
                    Intent intent = new Intent("com.vaiyee.hongmusic.offline");
                    intent.setComponent(new ComponentName(activity.getPackageName(),"com.vaiyee.hongmusic.brocastReciver.ForceOffline"));
                    activity.sendBroadcast(intent);
                    Log.d("远程关闭","...");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Socket异常",e.getMessage());
            Intent intent = new Intent("com.vaiyee.hongmusic.offline");
            intent.setComponent(new ComponentName(activity.getPackageName(),"com.vaiyee.hongmusic.brocastReciver.ForceOffline"));
            activity.sendBroadcast(intent);
        }
    }

    public void sendMessage(final String s)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    out = socket.getOutputStream();
                    out.write(s.getBytes());
                    if (s.equals("退出群聊"))
                    {
                        try {
                            in.close();
                            socket.close();
                            handler.sendEmptyMessage(2);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void close()
    {
        Log.d("执行了","这句话");
        String ss = "退出群聊";
        try {
            out = socket.getOutputStream();
            out.write(ss.getBytes());
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
