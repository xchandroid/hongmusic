package com.vaiyee.hongmusic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vaiyee.hongmusic.gson.Forecast;
import com.vaiyee.hongmusic.gson.Weather;
import com.vaiyee.hongmusic.service.AutoUpdateServise;
import com.vaiyee.hongmusic.util.HttpUtil;
import com.vaiyee.hongmusic.util.Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

      private ScrollView weatherLayout;
    private TextView titleCity,titleUpdateTime,degreeText,weatherInfoText,aqiText,pm25Text,comfortText,carWashText,sportText;
    private LinearLayout forecastLayout;
    private ImageView bingPicImg;
    public SwipeRefreshLayout swipeRefreshLayout;
    public DrawerLayout drawerlayout;
    private ImageView navButton;
    private MainActivity mainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT>=21)
        {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        //初始化各控件
       weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        bingPicImg = (ImageView)findViewById(R.id.bing_pic_img);
        navButton = findViewById(R.id.nav_button);
        drawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerlayout.openDrawer(Gravity.LEFT);
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(Color.RED);
        final String weatherId ;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = sharedPreferences.getString("weather",null);
        String bingPic = sharedPreferences.getString("bing_pic",null);
        if (bingPic!=null)
        {
            Glide.with(this).load(bingPic).into(bingPicImg);
        }
        else
        {
            loadBingPic();
        }
        if (weatherString!= null)
        {
            //有缓存时直接解析并显示天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            weatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        }
        else
        {
            weatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requesWeather(weatherId);
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requesWeather(weatherId);
            }
        });
    }

   //加载必应每日一图
    private void loadBingPic() {
      String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkhttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
               final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).bitmapTransform(new BlurTransformation(MyApplication.getQuanjuContext(),25,4)).into(bingPicImg);  // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。.into(bingPicImg);

                    }
                });
            }
        });
    }

    //请求天气数据
    public void requesWeather(String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=c20101953ff8446c99940ba138a96244";
        HttpUtil.sendOkhttpRequest(weatherUrl, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"天气数据获取失败",Toast.LENGTH_LONG).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                 final String responseText = response.body().string();
                Log.d("返回的天气数据是",responseText);
                createjson(responseText);
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather!=null&&"ok".equals(weather.status))
                        {
                            SharedPreferences.Editor editer = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editer.putString("weather",responseText);
                            editer.apply();
                            showWeatherInfo(weather);
                        }
                        else
                        {
                            Toast.makeText(WeatherActivity.this,"天气数据获取失败",Toast.LENGTH_LONG).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(WeatherActivity.this,"天气信息更新于" + weather.basic.update.updateTime,Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        loadBingPic();
    }

    //处理并展示实体类Weather中的天气数据
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime =weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature+"℃";
        String weatherInfo = weather.now.more.info;
        int code = weather.now.more.code;    //用来显示对应的天气图标
        String fengX = weather.now.dir;   //风向
        String fengL = weather.now.sc;     //风力
        String fengS = weather.now.spd;     //风速
        if (mainActivity ==null)
        {
            mainActivity = new MainActivity();
        }
        mainActivity.setWeatherInfo(code,cityName,fengX,fengL,fengS,degree,weatherInfo);
        titleCity.setText(cityName);
        titleUpdateTime.setText("更新于"+updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for (Forecast f :weather.forecastList)
        {
             View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dateText = view.findViewById(R.id.date_text);
            TextView infoText = view.findViewById(R.id.info_text);
            TextView maxText = view.findViewById(R.id.max_text);
            TextView minText = view.findViewById(R.id.min_text);
            dateText.setText(f.date);
            infoText.setText(f.more.info);
            maxText.setText(f.temperature.max);
            minText.setText(f.temperature.min);
            forecastLayout.addView(view);
        }
        if (weather.aqi!=null)
        {
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度:"+"\n"+weather.suggestion.comfort.info;
        String carwash = "洗车指数:"+"\n"+weather.suggestion.carwash.info;
        String sport = "运动建议:"+"\n" + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carwash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        loadBingPic();
        super.onResume();
    }

    private void createjson(String lrcContent)
    {
        String filePath = null;
        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);//是否有外置SD卡
        if (hasSDCard) {
            filePath =Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator+"HonchenWeather"+File.separator+"weatherjson.txt";
        } else
            filePath =Environment.getDownloadCacheDirectory().toString() + File.separator +"weatherjson.txt";

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
