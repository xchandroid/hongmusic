package com.vaiyee.hongmusic.fragement;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.vaiyee.hongmusic.Adapter.WangyiMvAdapter;
import com.vaiyee.hongmusic.MyApplication;
import com.vaiyee.hongmusic.R;
import com.vaiyee.hongmusic.Utils.HttpClinet;
import com.vaiyee.hongmusic.bean.WangYiMv;
import com.vaiyee.hongmusic.http.HttpCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MVFragment extends Fragment {

    private RecyclerView mvlistview;
    private List<WangYiMv.Data> mvlist = new ArrayList<>();
    private LinearLayout lodaing;
    private boolean isSuccess =false;
    private WangyiMvAdapter adapter;


    public MVFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new WangyiMvAdapter(MyApplication.getQuanjuContext(),mvlist,getActivity());
        HttpClinet.getWangyiMV(new HttpCallback<WangYiMv>() {
            @Override
            public void onSuccess(WangYiMv wangYiMv) {
                mvlist.addAll(wangYiMv.mvlist);
                mvlistview.setAdapter(adapter);
                isSuccess = true;
                lodaing.setVisibility(View.GONE);
            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(getContext(),"获取MV数据失败，请检查网络设置",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mv,container,false);
        mvlistview = view.findViewById(R.id.mv_recycleview);
        lodaing = view.findViewById(R.id.loading);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mvlistview.setLayoutManager(manager);
        mvlistview.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isSuccess)
        {
            lodaing.setVisibility(View.GONE);
        }                        
    }
}
