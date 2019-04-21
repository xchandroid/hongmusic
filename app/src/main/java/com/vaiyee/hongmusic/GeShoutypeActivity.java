package com.vaiyee.hongmusic;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vaiyee.hongmusic.Adapter.CategoryAdapter;
import com.vaiyee.hongmusic.Adapter.GeshouAdapter;
import com.vaiyee.hongmusic.Adapter.GeshouTypeAdapter;
import com.vaiyee.hongmusic.Utils.HttpClinet;
import com.vaiyee.hongmusic.bean.GeshouType;
import com.vaiyee.hongmusic.bean.SearchSinger;
import com.vaiyee.hongmusic.bean.SingerInfo;
import com.vaiyee.hongmusic.http.HttpCallback;
import com.vaiyee.hongmusic.util.Annotation;
import com.vaiyee.hongmusic.util.BindOnclick;
import com.vaiyee.hongmusic.util.BindView;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class GeShoutypeActivity extends SwipeBackActivity {
  private ListView listView;
  private TextView title;
  private ImageView back;
  private List<GeshouType.Singer> singerList = new ArrayList<>();
  private List<GeshouType.Singer> singerList1 = new ArrayList<>();
  private List<String> titleList = new ArrayList<>();
  private List<GeshouType.Info> infoList = new ArrayList<>();
  private GeshouType.Singer singer = new GeshouType.Singer();
  @BindView(R.id.search_singer)
  private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ge_shoutype);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //使不自动弹出软键盘
        Annotation.bind(this);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i== EditorInfo.IME_ACTION_SEARCH)
                {
                    SearchSinger(editText.getText().toString(),GeShoutypeActivity.this);
                }
                return false;
            }
        });
        title= (TextView) findViewById(R.id.title);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        listView = (ListView) findViewById(R.id.geshou_type_list);
        final CategoryAdapter adapter = new CategoryAdapter() {
            @Override
            public View getTitleView(String caption, View convertView, ViewGroup parent) {

                 convertView = LayoutInflater.from(GeShoutypeActivity.this).inflate(R.layout.view_holder_sheet_profile, parent, false);
                 convertView.setEnabled(false);
                 convertView.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {

                     }
                 });
               TextView title = convertView.findViewById(R.id.tv_profile);
                title.setText(caption);
                return convertView;
            }
        };
        String type = getIntent().getStringExtra("type");
        String t = getIntent().getStringExtra("title");
        title.setText(t);
        HttpClinet.getGeshoutype(type, new HttpCallback<GeshouType>() {
            @Override
            public void onSuccess(GeshouType geshouType) {
                for (int i = 0;i<geshouType.infoList.size();i++)
                {
                    //singerList.addAll(geshouType.infoList.get(i).singerList);
                   singerList1 = geshouType.infoList.get(i).singerList;
//                    titleList.add(geshouType.infoList.get(i).title);
//                    infoList.addAll(geshouType.infoList);
                    adapter.addCategery(geshouType.infoList.get(i).title,new GeshouTypeAdapter(GeShoutypeActivity.this,singerList1));

                }
                listView.setAdapter(adapter);
            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(GeShoutypeActivity.this, "获取歌手分类榜失败，请检查网络设置", Toast.LENGTH_LONG).show();
            }
        });


    }




    public void SearchSinger(String key, final Context context)
    {
        HttpClinet.SearchSinger(key, new HttpCallback<SearchSinger>() {
            @Override
            public void onSuccess(SearchSinger searchSinger) {
               final String name = searchSinger.getData().get(0).getSingername();
               final String singerid = String.valueOf(searchSinger.getData().get(0).getSingerid());
                HttpClinet.getSingerInfo(singerid, new HttpCallback<SingerInfo>() {
                    @Override
                    public void onSuccess(SingerInfo singerInfo) {
                        String img = singerInfo.getData().getImgurl();
                        Intent intent = new Intent(context,GeshouInfoActivity.class);
                        intent.putExtra("name",name);
                        intent.putExtra("id",Integer.valueOf(singerid));
                        intent.putExtra("img",img);
                        intent.putExtra("fans"," ");
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFail(Exception e) {

                    }
                });
            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }


}
