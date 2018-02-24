package com.vaiyee.hongmusic;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

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
}
