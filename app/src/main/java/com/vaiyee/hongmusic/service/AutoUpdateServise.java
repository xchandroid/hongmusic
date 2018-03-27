package com.vaiyee.hongmusic.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.vaiyee.hongmusic.gson.Weather;
import com.vaiyee.hongmusic.util.HttpUtil;
import com.vaiyee.hongmusic.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateServise extends Service {
    public AutoUpdateServise() {

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();
        AlarmManager alarmManiger =(AlarmManager) getSystemService(ALARM_SERVICE);
        int miniute = 60*60*1000;
        long trggerTime = SystemClock.elapsedRealtime() + miniute;        //设置触发时间为1分钟后
        Intent i = new Intent(this,AutoUpdateServise.class);
        PendingIntent pi = PendingIntent.getService(this,0,i,0);
        alarmManiger.cancel(pi);
        alarmManiger.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,trggerTime,pi);
        Log.d("后台更新天气成功","啦啦啦");
        return super.onStartCommand(intent, flags, startId);
    }

    //更新天气信息
    private void updateWeather()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = sharedPreferences.getString("weather",null);
        if (weatherString!=null)
        {
            Weather weather = Utility.handleWeatherResponse(weatherString);
            String weatherId = weather.basic.weatherId;
            String address = "http://guolin.tech/api/weather?cityid="+weatherId + "&key =c20101953ff8446c99940ba138a96244";
            HttpUtil.sendOkhttpRequest(address, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                 String responseText = response.body().string();
                    Weather weather= Utility.handleWeatherResponse(responseText);
                    if (weather!=null && "ok".equals(weather.status))
                    {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateServise.this).edit();
                        editor.putString("weather",responseText);
                        editor.apply();
                    }
                }
            });
        }
    }

    //更新bing图片
    private void updateBingPic()
    {
      String address = "http://guolin.tech/api/bing_Pic";
        HttpUtil.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String bingPic = response.body().string();
                if (bingPic!=null)
                {
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateServise.this).edit();
                    editor.putString("bing_pic",bingPic);
                    editor.apply();
                }
            }
        });
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
