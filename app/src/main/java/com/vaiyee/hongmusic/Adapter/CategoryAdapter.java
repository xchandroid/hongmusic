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
        int i=0;
        System.out.println("当前position的值是"+position);
       for(Category category:categories)  //显示到第n个类别,for循环就循环n次
       {
           i++;
           System.out.println("for循环运行了"+i+"次");
           switch (getItemViewType(position))
           {
               case 0:
                   System.out.println("返回了标题view");
                   return getTitleView(category.getmTitle(),convertView,parent);
               case 1:
                   int size = category.getmAdapter().getCount()+1;//获取当前要显示的类别加上标题总共长度，因为在所有为0的位置返回了标题view
                   if (size>=2) //判断当前类别下至少有一个项目才显示
                   {
                       if (position<size) //position==size时的索引位置就是下一个类别的标题，因为索引是从0开始的，当position==9时在列表中其实已经是第十个位置了
                       {
                           return category.getmAdapter().getView(position-1,convertView,parent);
                       }
                       else
                       {
                           position = position-size; //当position==size时，减去上一个类别的数量，显示下一个类别的条目
                           System.out.println("里面position的值是"+position);
                       }
                   }
           }
       }
        return null;
    }

    //获取分类数据视图的方法,OOP的思想,留给子类实现该方法，
    public abstract View getTitleView(String caption, View convertView, ViewGroup parent);

}
