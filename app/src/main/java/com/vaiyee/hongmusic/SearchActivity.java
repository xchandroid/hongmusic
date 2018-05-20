package com.vaiyee.hongmusic;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vaiyee.hongmusic.Adapter.SearchAdapter;
import com.vaiyee.hongmusic.Utils.HttpClinet;
import com.vaiyee.hongmusic.Utils.NetUtils;
import com.vaiyee.hongmusic.Utils.OnLoadSearchFinishListener;
import com.vaiyee.hongmusic.Utils.SearchUtils;
import com.vaiyee.hongmusic.bean.DownloadInfo;
import com.vaiyee.hongmusic.bean.KugouMusic;
import com.vaiyee.hongmusic.bean.KugouSearchResult;
import com.vaiyee.hongmusic.bean.Music;
import com.vaiyee.hongmusic.bean.OnlineMusic;
import com.vaiyee.hongmusic.bean.SearchMusic;
import com.vaiyee.hongmusic.http.HttpCallback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class SearchActivity extends SwipeBackActivity {

    private ImageView back,delete;
    private Button search;
    private EditText editText;
    public static ListView listView;
    private  SearchAdapter adapter;
    private TextView textView;
    private String geshou,coverUrl,lrc;
    public static String geming;
    private static int endtime,i=-1;
    private static ProgressDialog progressDialog;
    private static List<KugouSearchResult.lists> songs = new ArrayList<>();
    private static List<KugouSearchResult.lists> resultList = new ArrayList<>();
    private int mfirstvisibleItem = 0,size = 0;
    private static String content = null;
    private static View footer;
    private boolean isLoding = false,isShowlishi = false;
    private String[] lishi = new String[10];
    private SharedPreferences.Editor editor;
    private LinearLayout linearLayout;
    private FloatingActionButton geshou_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);  //使不自动弹出软键盘
        footer = LayoutInflater.from(SearchActivity.this).inflate(R.layout.loading_footer,null);
        initView();
        editor = getSharedPreferences("L",0).edit();
    }

    @Override
    protected void onResume() {
        DuquSousuoLishi();
        ShowHistory();
        super.onResume();
    }

    private void initView()
    {
        adapter = new SearchAdapter(SearchActivity.this,this,R.layout.search_listitem,songs);
        back = (ImageView) findViewById(R.id.fanhui);
        textView = (TextView)findViewById(R.id.Null);
        linearLayout = (LinearLayout) findViewById(R.id.lishi_layout);
        geshou_button = (FloatingActionButton)findViewById(R.id.open_geshou);
        geshou_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this,GeshouActivity.class);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        editText = (EditText) findViewById(R.id.sousuokuang);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String txt = editText.getText().toString();
                if (txt.length()>0) {
                    delete.setVisibility(View.VISIBLE);
                }
                else
                {
                    delete.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i==EditorInfo.IME_ACTION_SEARCH)
                {
                    huicheSousuo();
                }
                return false;
            }
        });


        search = (Button) findViewById(R.id.search_online_music);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
              huicheSousuo();
            }
        });
        listView = (ListView) findViewById(R.id.searched_online_music);
         footer = LayoutInflater.from(SearchActivity.this).inflate(R.layout.loading_footer,null);
        listView.addFooterView(footer);
        listView.removeFooterView(footer);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {  // i 是第一个可见item，第二个是可见的总item, 第三个参数是总的item
                boolean isPulldonw = i>mfirstvisibleItem;   //当前的第一个可见item的索引大于上一次第一个可见item的索引时，listview为上拉
                int lastvisibleItem = i+i1;  //最后一个可见的item
                if (isPulldonw && !isLoding)  //判断listview是否滑动到底部（isLoading为限制开关,防止滑动到底部一直调用loadMore方法造成listview反应不过来导致程序崩溃）
                {
                    linearLayout.setVisibility(View.GONE);
                    isShowlishi = false;         //这个是否显示历史记录的标志位，防止下拉过程视图闪屏
                    if (lastvisibleItem==i2-1)  //滑动到最后一个Item调用loadMore加载更多，此时isLoading为true，也就是正在加载
                    {
                        loadMore();
                        isLoding = true;
                        listView.addFooterView(footer);
                    }
                }
                if (i+2==mfirstvisibleItem&&!isShowlishi)   //这里表示当下拉超过2个item时（实测是快速下拉就显示，慢慢下拉则不显示，这里有效防止下拉过程闪屏问题）显示历史记录
                {
                    linearLayout.setVisibility(View.VISIBLE);
                    isShowlishi = true;
                }
                mfirstvisibleItem = i;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              KugouSearchResult.lists  song = songs.get(i);
                String hash = song.getFileHash();
                geming = song.getSongName();
                geshou = song.getSingerName();
                endtime = song.getDuration()*1000;
                getSongUrl(hash);
            }
        });


        delete = (ImageView) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               editText.setText("");
            }
        });
    }
   //滑动到底部自动加载更多
    private void loadMore() {
        switch (NetUtils.getNetType())
        {
            case NET_NONE:
                Toast.makeText(SearchActivity.this,"当前无网络，请检查网络设置再试",Toast.LENGTH_LONG).show();
                CloseProgress();
                return;
        }
        HttpClinet.KugouSearch(content,size, new HttpCallback<KugouSearchResult>() {
            @Override
            public void onSuccess(KugouSearchResult kugouSearchResult) {
                songs.clear();
                if (kugouSearchResult!=null) {
                    resultList = kugouSearchResult.getResultList();
                }
                if (resultList!=null)
                {
                    songs.addAll(resultList);
                    adapter.notifyDataSetChanged();
                    CloseProgress();
                    listView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    listView.removeFooterView(footer);
                    size+=20;
                    isLoding = false;
                }
                else
                {
                    listView.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                }

                      hintKeyboard();
            }

            @Override
            public void onFail(Exception e) {
                CloseProgress();
                Toast.makeText(SearchActivity.this,"获取数据失败，请检查网络设置重试",Toast.LENGTH_LONG).show();
            }
        });
    }


    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
           switch (msg.what)
           {
               case 1:
                   if (songs!=null) {
                       adapter.notifyDataSetChanged();
                       listView.setVisibility(View.VISIBLE);
                       textView.setVisibility(View.GONE);
                   }
                   else
                   {
                       listView.setVisibility(View.GONE);
                       textView.setVisibility(View.VISIBLE);
                   }
                   break;

           }
        }
    };

    private void getSongUrl(final String hash)
    {
        AlertDialog.Builder builder;
        switch (NetUtils.getNetType())
        {
            case NET_WIFI:
                HttpClinet.KugouUrl(hash, new HttpCallback<KugouMusic>() {
                    @Override
                    public void onSuccess(KugouMusic kugouMusic) {
                        String path = kugouMusic.getData().getPlay_url();
                        if (path!=null) {
                            Log.d("歌曲地址是", path);
                            PlayMusic playMusic = new PlayMusic();
                            playMusic.play(path, PlayMusic.playposition);
                            coverUrl = kugouMusic.getData().getImg();
                            lrc = kugouMusic.getData().getLyrics();
                            creatLrc(lrc,geming);
                            MainActivity mainActivity = new MainActivity();
                            mainActivity.tongbuShow(geming,geshou,coverUrl,endtime,MainActivity.ONLINE);
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        CloseProgress();
                        Toast.makeText(SearchActivity.this,"获取在线音乐失败！请检查网络设置",Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case NET_4G:
                builder = new AlertDialog.Builder(SearchActivity.this);
                builder.setMessage("当前正在使用4G网络，是否使用数据流量播放在线音乐？");
                builder.setTitle("提示");
                builder.setIcon(R.drawable.tip);
                builder.setPositiveButton("流量多",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                HttpClinet.KugouUrl(hash, new HttpCallback<KugouMusic>() {
                                    @Override
                                    public void onSuccess(KugouMusic kugouMusic) {
                                        String path = kugouMusic.getData().getPlay_url();
                                        if (path!=null) {
                                            Log.d("歌曲地址是", path);
                                            PlayMusic playMusic = new PlayMusic();
                                            playMusic.play(path, PlayMusic.playposition);
                                            coverUrl = kugouMusic.getData().getImg();
                                            lrc = kugouMusic.getData().getLyrics();
                                            creatLrc(lrc,geming);
                                            MainActivity mainActivity = new MainActivity();
                                            mainActivity.tongbuShow(geming,geshou,coverUrl,endtime,MainActivity.ONLINE);
                                        }
                                    }

                                    @Override
                                    public void onFail(Exception e) {
                                        CloseProgress();
                                        Toast.makeText(SearchActivity.this,"获取在线音乐失败！请检查网络设置",Toast.LENGTH_LONG).show();
                                    }
                                });
                                dialog.dismiss();
                            }
                        });

                builder.setNegativeButton("伤不起",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
                break;
            case NET_3G:
                builder = new AlertDialog.Builder(SearchActivity.this);
                builder.setMessage("当前正在使用3G网络，是否使用数据流量播放在线音乐？");
                builder.setTitle("提示");
                builder.setIcon(R.drawable.tip);
                builder.setPositiveButton("流量多",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                HttpClinet.KugouUrl(hash, new HttpCallback<KugouMusic>() {
                                    @Override
                                    public void onSuccess(KugouMusic kugouMusic) {
                                        String path = kugouMusic.getData().getPlay_url();
                                        if (path!=null) {
                                            Log.d("歌曲地址是", path);
                                            PlayMusic playMusic = new PlayMusic();
                                            playMusic.play(path,PlayMusic.playposition);
                                            coverUrl = kugouMusic.getData().getImg();
                                            lrc = kugouMusic.getData().getLyrics();
                                            creatLrc(lrc,geming);
                                            MainActivity mainActivity = new MainActivity();
                                            mainActivity.tongbuShow(geming,geshou,coverUrl,endtime,MainActivity.ONLINE);
                                        }
                                    }

                                    @Override
                                    public void onFail(Exception e) {
                                        CloseProgress();
                                        Toast.makeText(SearchActivity.this,"获取在线音乐失败！请检查网络设置",Toast.LENGTH_LONG).show();
                                    }
                                });
                                dialog.dismiss();
                            }
                        });

                builder.setNegativeButton("伤不起",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
                break;
            case NET_2G:
                builder = new AlertDialog.Builder(SearchActivity.this);
                builder.setMessage("当前正在使用2G网络，缓冲较慢，确定是否播放在线音乐？");
                builder.setTitle("提示");
                builder.setIcon(R.drawable.tip);
                builder.setPositiveButton("流量多",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                HttpClinet.KugouUrl(hash, new HttpCallback<KugouMusic>() {
                                    @Override
                                    public void onSuccess(KugouMusic kugouMusic) {
                                        String path = kugouMusic.getData().getPlay_url();
                                        if (path!=null) {
                                            Log.d("歌曲地址是", path);
                                            PlayMusic playMusic = new PlayMusic();
                                            playMusic.play(path,PlayMusic.playposition);
                                            coverUrl = kugouMusic.getData().getImg();
                                            lrc = kugouMusic.getData().getLyrics();
                                            creatLrc(lrc,geming);
                                            MainActivity mainActivity = new MainActivity();
                                            mainActivity.tongbuShow(geming,geshou,coverUrl,endtime,MainActivity.ONLINE);
                                        }
                                    }

                                    @Override
                                    public void onFail(Exception e) {
                                        CloseProgress();
                                        Toast.makeText(SearchActivity.this,"获取在线音乐失败！请检查网络设置",Toast.LENGTH_LONG).show();
                                    }
                                });
                                dialog.dismiss();
                            }
                        });

                builder.setNegativeButton("伤不起",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
                break;


        }


        /*
        HttpClinet.getMusicUrl(songId, new HttpCallback<DownloadInfo>() {
            @Override
            public void onSuccess(DownloadInfo downloadInfo) {
                if(downloadInfo==null||downloadInfo.getBitrate()==null)
                {
                    onFail(null);
                    return;
                }
                String path = downloadInfo.getBitrate().getFile_link();
                int endtime = downloadInfo.getBitrate().getFile_duration()*1000;
                PlayMusic playMusic = new PlayMusic();
                playMusic.play(path,0);
                MainActivity mainActivity = new MainActivity();
                mainActivity.tongbuShow(geming,geshou,coverUrl,endtime,MainActivity.ONLINE);
            }

            @Override
            public void onFail(Exception e) {

            }
        });
        */
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

   public static void creatLrc(String lrc, String geming)         //创建歌词文件，为了在Playmusicfragemet中能够定位歌词显示到Lrcview中
    {
        String name = String.valueOf(Html.fromHtml(geming));
        String filePath = null;
        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);//是否有外置SD卡
        if (hasSDCard) {
            filePath =Environment.getExternalStorageDirectory().getPath()+"/HonchenMusic/Lrc/"+name+".lrc";
        } else
            filePath =Environment.getDownloadCacheDirectory().toString() + File.separator +name+".lrc";

        try {
            File file = new File(filePath);
            if (!file.exists()) {   //如果文件不存在
                File dir = new File(file.getParent());  //获取file文件在内置或外置SD卡  getParent()与getParentFile()的区别：getParentFile()的返回值是File型的。而getParent() 的返回值是String型的。mkdirs是File类里面的方法，所以当然得用f.getParentFile().mkdirs();getParentFile()的返回值是File型的。而getParent() 的返回值是String型的。mkdirs是File类里面的方法，所以当然得用f.getParentFile().mkdirs();
                dir.mkdirs();  //先创建文件夹
                file.createNewFile();//创建文件
            }

            FileOutputStream outStream = new FileOutputStream(file);//创建文件字节输出流对象，以字节形式写入所创建的文件中
            outStream.write(lrc.getBytes());//开始写入文件（也就是把文件写入内存卡中）
           // Log.d("歌词路径",filePath);
          //  Log.d("歌词内容是",lrc);
            outStream.close();//关闭文件字节输出流
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //读取保存的历史搜索记录
    private void DuquSousuoLishi()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("L",0);
        i = sharedPreferences.getInt("s",-1);   //读取已保存的长度,这里默认值为-1，是因为在搜索按钮点击事件中保存数据索引要从0开始
        if (sharedPreferences.getInt("s",0)==-1)
        {
            return;
        }
        for (int i=0;i<=sharedPreferences.getInt("s",0);i++)    //保存是历史记录索引从0开始，所以这里的条件是<=
        {
            lishi[i] = sharedPreferences.getString("h"+i,null);
        }
    }

    //显示历史搜索记录
    private void ShowHistory()
    {
        linearLayout.removeAllViews();
        for (int k=0;k<=i;k++)
        {
            View jilu = LayoutInflater.from(this).inflate(R.layout.search_history,linearLayout,false);
            TextView textView = jilu.findViewById(R.id.lishi);
            if (lishi[k]!=null)
            {
                textView.setText(lishi[k]);
                final int finalK = k;
                jilu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ShowProgress();
                        content = lishi[finalK];
                        size = 20;
                        loadMore();
                    }
                });
                linearLayout.addView(jilu);
            }
        }
    }


    //显示正在加载数据对话框
    private void ShowProgress()
    {
            progressDialog = new ProgressDialog(SearchActivity.this);
            progressDialog.setMessage("正在加载数据");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
    }
    //关闭正在加载对话框
    private  void CloseProgress()
    {
        if (progressDialog!=null)
        {
            progressDialog.dismiss();
        }
    }

    private void hintKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null)
        {
            if (getCurrentFocus().getWindowToken()!=null)
        {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); }
        }
    }

    private void huicheSousuo()
    {
        ShowProgress();
        content = editText.getText().toString();
        if (TextUtils.isEmpty(content))
        {
            Toast toast= Toast.makeText(SearchActivity.this,"请输入搜索内容",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP , 0, 200);
            toast.show();
            CloseProgress();
            return;
        }
        i=i+1;
        if(i<10)
        {
            editor.putString("h" + i, content);      //这里表示仅保存10条历史搜索记录,i是从0开始的
            editor.putInt("s", i);
            editor.apply();
        }
        else
        {
            i = 0;                                       //当大于9的时候，重置i的值为0
            editor.putString("h" + i, content);      //else这段代码仅执行一次
            editor.putInt("s", 9);
            editor.apply();
        }
        size = 20;
        loadMore();
    }



}
