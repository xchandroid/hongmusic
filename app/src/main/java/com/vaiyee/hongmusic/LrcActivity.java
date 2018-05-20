package com.vaiyee.hongmusic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.vaiyee.hongmusic.CustomView.MyLrcView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LrcActivity extends AppCompatActivity {
    @BindView(R.id.lrcview)
    MyLrcView lrcview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lrc);
        ButterKnife.bind(this);
        File file = new File("/storage/emulated/0/HonchenMusic/Lrc/" +"我乐意"+".lrc");
        lrcview.LoadLrcFile(file);
    }
}
