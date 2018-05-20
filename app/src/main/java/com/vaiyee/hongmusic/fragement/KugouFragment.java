package com.vaiyee.hongmusic.fragement;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.vaiyee.hongmusic.Adapter.KugouAdapter;
import com.vaiyee.hongmusic.R;
import com.vaiyee.hongmusic.bean.KugouBang;
import com.vaiyee.hongmusic.bean.Sheet;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class KugouFragment extends Fragment {

    private RecyclerView recyclerView;
    private KugouAdapter kugouAdapter;
    private List<Sheet> sheetList;
    public KugouFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         View view = inflater.inflate(R.layout.fragment_kugou,container,false);
         recyclerView = view.findViewById(R.id.kukou_list);
         sheetList = new ArrayList<>();
         return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[]title = {"热门榜单","谢昌宏","谢昌宏","谢昌宏","谢昌宏","谢昌宏","全球榜","谢昌宏","谢昌宏","谢昌宏","特色音乐榜","谢昌宏","谢昌宏","谢昌宏","谢昌宏","谢昌宏","谢昌宏","谢昌宏","谢昌宏","谢昌宏","谢昌宏","谢昌宏","谢昌宏","谢昌宏","谢昌宏",};
        String[]type = {"#","6666"," 8888","23784","24971","31308","#","31310","31311"," 31312","#","31313","21101","30972","31777","22603","24306","21335","24307","22096","24574","4680"," 4673"," 22163","4672",};
        for (int i=0;i<type.length;i++)
        {
            Sheet sheet = new Sheet();
            sheet.setType(type[i]);
            sheet.setTitle(title[i]);
            sheetList.add(sheet);
        }
        kugouAdapter = new KugouAdapter(getContext(),R.layout.view_holder_sheet,sheetList);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(kugouAdapter);

    }

    @Override
    public void onDestroy() {
        Log.d("Destroy了","酷狗这个碎片");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d("Detach了","酷狗这个碎片");
        super.onDetach();
    }

    @Override
    public void onPause() {
        Log.d("Pause了","酷狗这个碎片");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d("Stop了","酷狗这个碎片");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d("DestroyView了","酷狗这个碎片");
        super.onDestroyView();
    }
}
