package com.vaiyee.hongmusic;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import java.util.List;

/**
 * Created by Administrator on 2018/1/29.
 */

public class fragmentAdapter extends FragmentPagerAdapter {
    List<Fragment> list;
    String[] titles;
    public fragmentAdapter(FragmentManager fm, List<Fragment> list)
    {
        super(fm);
        this.list=list;
        this.titles = titles;
    }
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }


    // 重写该方法，取消调用父类该方法
   // 可以避免在viewpager切换，fragment不可见时执行到onDestroyView销毁视图，可见时又从onCreateView重新加载视图造成的卡顿问题
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
    }
}
