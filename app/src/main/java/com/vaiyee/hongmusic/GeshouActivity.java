package com.vaiyee.hongmusic;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.Resource;
import com.vaiyee.hongmusic.Adapter.GeshouAdapter;
import com.vaiyee.hongmusic.Utils.HttpClinet;
import com.vaiyee.hongmusic.bean.Geshou;
import com.vaiyee.hongmusic.http.HttpCallback;
import com.vaiyee.hongmusic.util.Annotation;
import com.vaiyee.hongmusic.util.BindOnclick;
import com.vaiyee.hongmusic.util.BindView;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class GeshouActivity extends SwipeBackActivity implements View.OnClickListener{

    private ListView listView;
    private ImageView back;
    private TextView biaosheng,remen;
    @BindView(R.id.loading)
    private LinearLayout linearLayout;
    private View Headerview;
    private Button geshouType;
    private GeshouAdapter adapter;
    private boolean biao = false,re = true,isShow = false;  //防止用户多次点击访问网络
    private List<Geshou.DataBean.InfoBean> beanList = new ArrayList<>();
    private static PopupWindow popupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geshou);
        Annotation.bind(this);
        listView = (ListView) findViewById(R.id.geshou_paihangbang);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        Headerview = LayoutInflater.from(this).inflate(R.layout.geshou_hearder,null);
        biaosheng = Headerview.findViewById(R.id.biaosheng);
        remen = Headerview.findViewById(R.id.remen);
        biaosheng.setOnClickListener(this);
        remen.setOnClickListener(this);
        geshouType = Headerview.findViewById(R.id.geshou_type);
        geshouType.setOnClickListener(this);
        listView.addHeaderView(Headerview);
        adapter = new GeshouAdapter(GeshouActivity.this,beanList);
        listView.setAdapter(adapter);
        HttpClinet.getGeshoubang(new HttpCallback<Geshou>() {
            @Override
            public void onSuccess(Geshou geshou) {
                beanList.clear();
                beanList.addAll(geshou.getData().getInfo());
                adapter.notifyDataSetChanged();
                linearLayout.setVisibility(View.GONE);
            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(GeshouActivity.this,"获取歌手榜失败，请检查网络设置",Toast.LENGTH_LONG).show();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Geshou.DataBean.InfoBean singer = beanList.get(i-1);
                String name = singer.getSingername();
                Intent intent = new Intent(GeshouActivity.this,GeshouInfoActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("id",singer.getSingerid());
                intent.putExtra("img",singer.getImgurl());
                intent.putExtra("fans",String.valueOf(singer.getFanscount()));
                startActivity(intent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(GeshouActivity.this,GeShoutypeActivity.class);
        switch (view.getId())
        {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.biaosheng:
                if (biao) {
                    biao = false;
                    re = true;
                    HttpClinet.getGeshoubang(new HttpCallback<Geshou>() {
                        @Override
                        public void onSuccess(Geshou geshou) {
                            beanList.clear();
                            beanList.addAll(geshou.getData().getInfo());
                            adapter.notifyDataSetChanged();
                            biaosheng.setTextColor(Color.parseColor("#00F5FF"));
                            remen.setTextColor(Color.parseColor("#9C9C9C"));
                        }

                        @Override
                        public void onFail(Exception e) {
                            Toast.makeText(GeshouActivity.this, "获取歌手榜失败，请检查网络设置", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                break;
            case R.id.remen:
                if (re) {
                    re = false;
                    biao = true;
                    HttpClinet.getGeshouRemen(new HttpCallback<Geshou>() {
                        @Override
                        public void onSuccess(Geshou geshou) {
                            beanList.clear();
                            beanList.addAll(geshou.getData().getInfo());
                            adapter.notifyDataSetChanged();
                            remen.setTextColor(Color.parseColor("#00F5FF"));
                            biaosheng.setTextColor(Color.parseColor("#9C9C9C"));
                        }

                        @Override
                        public void onFail(Exception e) {
                            Toast.makeText(GeshouActivity.this, "获取歌手榜失败，请检查网络设置", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                    break;
            case R.id.geshou_type:
                View contentview = LayoutInflater.from(this).inflate(R.layout.geshou_type,null);
                LinearLayout linearLayout = contentview.findViewById(R.id.alpha);
                initcontentview(contentview);
                if (popupWindow==null)
                {
                    popupWindow = new PopupWindow(contentview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                popupWindow.setOutsideTouchable(true);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                       // listView.setForeground(null);
                    }
                });
                if (!isShow) {
                    popupWindow.setAnimationStyle(R.style.popmenu_animation);
                    popupWindow.showAsDropDown(geshouType, 0, 0);
                        // Resources res = getResources();
                    //Drawable drawable = res.getDrawable(R.drawable.alpha);
                    isShow = true;
                }
                else
                {
                    popupWindow.dismiss();
                    isShow = false;
                }
                break;
            case R.id.b1:
                 intent.putExtra("type","sextype=1&type=1&musician=0");
                 intent.putExtra("title","华语男歌手");
                 startActivity(intent);
                 break;
            case R.id.b2:
                intent.putExtra("type","sextype=2&type=1&musician=0");
                intent.putExtra("title","华语女歌手");
                startActivity(intent);
                break;
            case R.id.b3:
                intent.putExtra("type","sextype=3&type=1&musician=0");
                intent.putExtra("title","华语组合");
                startActivity(intent);
                break;
            case R.id.b4:
                intent.putExtra("type","sextype=1&type=2&musician=0");
                intent.putExtra("title","欧美男歌手");
                startActivity(intent);
                break;
            case R.id.b5:
                intent.putExtra("type","sextype=2&type=2&musician=0");
                intent.putExtra("title","欧美女歌手");
                startActivity(intent);
                break;
            case R.id.b6:
                intent.putExtra("type","sextype=3&type=2&musician=0");
                intent.putExtra("title","欧美组合");
                startActivity(intent);
                break;
            case R.id.b7:
                intent.putExtra("type","sextype=1&type=6&musician=0");
                intent.putExtra("title","韩国男歌手");
                startActivity(intent);
                break;
            case R.id.b8:
                intent.putExtra("type","sextype=2&type=6&musician=0");
                intent.putExtra("title","华语女歌手");
                startActivity(intent);
                break;
            case R.id.b9:
                intent.putExtra("type","sextype=3&type=6&musician=0");
                intent.putExtra("title","韩国组合");
                startActivity(intent);
                break;
            case R.id.b10:
                intent.putExtra("type","sextype=1&type=5&musician=0");
                intent.putExtra("title","日本男歌手");
                startActivity(intent);
                break;
            case R.id.b11:
                intent.putExtra("type","sextype=2&type=5&musician=0");
                intent.putExtra("title","日本女歌手");
                startActivity(intent);
                break;
            case R.id.b12:
                intent.putExtra("type","sextype=3&type=5&musician=0");
                intent.putExtra("title","日本组合");
                startActivity(intent);
                break;
            case R.id.b13:
                intent.putExtra("type","sextype=0&type=4&musician=0");
                intent.putExtra("title","其他歌手");
                startActivity(intent);
                break;
            case R.id.b14:
                intent.putExtra("type","sextype=0&type=0&musician=3");
                intent.putExtra("title","原创音乐人");
                startActivity(intent);
                break;



        }
    }

    private void initcontentview(View contentview) {
        Button b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,b11,b12,b13,b14;
        b1 = contentview .findViewById(R.id.b1);
        b2 = contentview .findViewById(R.id.b2);
        b3 = contentview .findViewById(R.id.b3);
        b4 = contentview .findViewById(R.id.b4);
        b5 = contentview .findViewById(R.id.b5);
        b6 = contentview .findViewById(R.id.b6);
        b7 = contentview .findViewById(R.id.b7);
        b8 = contentview .findViewById(R.id.b8);
        b9 = contentview .findViewById(R.id.b9);
        b10= contentview .findViewById(R.id.b10);
        b11= contentview .findViewById(R.id.b11);
        b12 = contentview .findViewById(R.id.b12);
        b13 = contentview .findViewById(R.id.b13);
        b14 = contentview .findViewById(R.id.b14);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);
        b7.setOnClickListener(this);
        b8.setOnClickListener(this);
        b9.setOnClickListener(this);
        b10.setOnClickListener(this);
        b11.setOnClickListener(this);
        b12.setOnClickListener(this);
        b13.setOnClickListener(this);
        b14.setOnClickListener(this);
    }


    @BindOnclick(R.id.search)
    private void OpenSearchActivity()
    {
        Intent intent = new Intent(this,SearchActivity.class);
        startActivity(intent);
    }
}
