package com.vaiyee.hongmusic;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.vaiyee.hongmusic.CustomView.SimpleVideoView;
import com.vaiyee.hongmusic.Utils.HttpClinet;
import com.vaiyee.hongmusic.bean.GeshouMvUrl;
import com.vaiyee.hongmusic.bean.MvData;
import com.vaiyee.hongmusic.http.HttpCallback;
import com.vaiyee.hongmusic.util.Annotation;
import com.vaiyee.hongmusic.util.BindOnclick;
import com.vaiyee.hongmusic.util.BindView;
import com.zhy.http.okhttp.OkHttpUtils;


import io.vov.vitamio.provider.MediaStore;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class PlayMvActivity extends SwipeBackActivity {

     private static SimpleVideoView videoView;
     private static String mvpath = null;
     private View docorview;
     private PlayMusic playMusic = new PlayMusic();
     public static String title;
     @BindView(R.id.title)
     private TextView tit;
     @BindView(R.id.toolbar)
     public static LinearLayout linearLayout;
     public static int q = 0;
     public static String[]paths = new String[4];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        if (Build.VERSION.SDK_INT>=21) {
            docorview = getWindow().getDecorView();
            docorview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        */
        q=0;  //初始化q的值为0，不然会发生超出索引范围的错误
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        setContentView(R.layout.activity_play_mv);
        Annotation.bind(this);
        if(PlayMusic.mediaPlayer.isPlaying())
        {
            playMusic.pause();
        }
        tit.setText(title);
        videoView = (SimpleVideoView) findViewById(R.id.simple_video_view);
        switch (intent.getStringExtra("type"))
        {
            case "wy":
                final String mvid = intent.getStringExtra("mvid");
                HttpClinet.getMvUrl(mvid, new HttpCallback<MvData>() {
                    @Override
                    public void onSuccess(MvData mvData) {

                        if (mvData.data.url.vga!=null) {
                            paths[0] = mvData.data.url.vga;
                            q+=1;
                        }
                        if (mvData.data.url.hd!=null)
                        {
                            paths[1] = mvData.data.url.hd;
                            q+=1;
                        }
                        if (mvData.data.url.fhd!=null)
                        {
                            paths[2] = mvData.data.url.fhd;
                            q+=1;
                        }
                        io.vov.vitamio.utils.Log.d("视频路径"+mvData.data.url.vga);
                        videoView.setVideoUri(Uri.parse(mvData.data.url.vga));
                    }

                    @Override
                    public void onFail(Exception e) {
                        Toast.makeText(PlayMvActivity.this,"播放MV失败，可能是该MV不存在",Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case "kg":
                String path = getIntent().getStringExtra("mvid");
                HttpClinet.getGeshouMvUrl(path, new HttpCallback<GeshouMvUrl>() {
                    @Override
                    public void onSuccess(GeshouMvUrl geshouMvUrl) {
                        if (geshouMvUrl.getMvdata() != null) {
                            if (!TextUtils.isEmpty(geshouMvUrl.getMvdata().getHd().getDownurl()))
                            {
                                paths[0] = geshouMvUrl.getMvdata().getHd().getDownurl();
                                q += 1;
                            }
                            if (!TextUtils.isEmpty(geshouMvUrl.getMvdata().getSd().getDownurl())) {
                                paths[1] = geshouMvUrl.getMvdata().getSd().getDownurl();
                                q += 1;
                            }
                            if (!TextUtils.isEmpty(geshouMvUrl.getMvdata().getSq().getDownurl())) {
                                paths[2] = geshouMvUrl.getMvdata().getSq().getDownurl();
                                q += 1;
                            }
                            if (!TextUtils.isEmpty(geshouMvUrl.getMvdata().getRq().getDownurl())) {
                                paths[3] = geshouMvUrl.getMvdata().getRq().getDownurl();
                                q += 1;
                            }


                            if (!TextUtils.isEmpty(geshouMvUrl.getMvdata().getHd().getDownurl())) {
                                videoView.setVideoUri(Uri.parse(geshouMvUrl.getMvdata().getHd().getDownurl()));
                            }
                            else
                            {
                                videoView.setVideoUri(Uri.parse(geshouMvUrl.getMvdata().getSd().getDownurl()));
                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        Toast.makeText(PlayMvActivity.this,"播放MV失败，可能是该MV不存在",Toast.LENGTH_LONG).show();
                    }
                });
                break;

        }

    }
    @BindOnclick(R.id.back)
    private void back()
    {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.suspend();
    }

    @Override
    public void onBackPressed() {
        if(videoView.isFullScreen())
        {
            videoView.setNoFullScreen();
        }
        else
        {
           super.onBackPressed();
        }
    }



}
