package com.vaiyee.hongmusic.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;

import com.vaiyee.hongmusic.GeShoutypeActivity;
import com.vaiyee.hongmusic.GeshouInfoActivity;
import com.vaiyee.hongmusic.MyApplication;
import com.vaiyee.hongmusic.bean.Category;
import com.vaiyee.hongmusic.bean.GeshouType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/6.
 */

public abstract class CategoryAdapter extends BaseAdapter {
    //用于存储分类和类型数据的集合,这里每一个对象为一个分类和其相对应的数据。
    private List<Category> categories=new ArrayList();
    private static final int TYPE_PROFILE = 0;
    private static final int TYPE_SINGGER_LIST = 1;
    public void addCategery(String title, GeshouTypeAdapter adapter){
        categories.add(new Category(adapter,title));

    }

    @Override
    public int getCount() {
        int total=0;
        for (Category category:categories){
            total=total+category.getmAdapter().getCount()+1; //标题站了一个位置，所以要加一
        }
        return total;
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0)
        {
            return TYPE_PROFILE;
        }
        else
        {
            return TYPE_SINGGER_LIST;
        }
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position)==TYPE_SINGGER_LIST;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        for (Category category:categories) {
            switch (getItemViewType(position)) {
                case 0:
                //如果是第一个，则显示分类的名称

                    if (category.getmAdapter().getCount() + 1 >= 2) {       //如果当前分类下面没有内容则不显示标题
                        return getTitleView(category.getmTitle(), convertView, parent);
                    }
                    break;
                case 1:
                //否则显示该分类下的数据
                //每个分类加上其子类的数量
                int size = category.getmAdapter().getCount()+1;
                if (size >= 2) {                                          //当前分类下面有内容才显示
                    if (position < size) {
                        return category.getmAdapter().getView(position - 1, convertView, parent);
                    }
                    //当position为0时，说明一个分类的数据已经展示完毕，紧接着展示下一个分类的数据
                    position = position - size;          //重置position的值为0，加载下一个分类的内容

                }
                break;
            }

        }
        return null;
    }

    //获取分类数据视图的方法,OOP的思想,留给子类实现该方法，
    public abstract View getTitleView(String caption, View convertView, ViewGroup parent);

}
