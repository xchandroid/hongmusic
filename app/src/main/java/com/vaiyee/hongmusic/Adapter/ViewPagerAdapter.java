package com.vaiyee.hongmusic.Adapter;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2018/2/13.
 */

public class ViewPagerAdapter extends PagerAdapter {
    private List<View> mViewList;

  public  ViewPagerAdapter(List<View> viewList) {
        mViewList = viewList;
    }

    @Override
    public int getCount() {

        return mViewList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == mViewList.get((int)Integer.parseInt(object.toString()));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mViewList.get(position);
        container.addView(view);
        return position;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewList.get(position));
    }
}
