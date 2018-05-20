package com.vaiyee.hongmusic.util;

import android.app.Activity;
import android.view.View;

import com.vaiyee.hongmusic.R;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Administrator on 2018/4/11.
 */

public class Annotation {
    public static void bind(Activity activity)
    {
        try {
            bindView(activity);    //绑定控件
            bindOnclik(activity);  //绑定控件的点击事件
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private static void bindOnclik(final Activity activity) throws IllegalAccessException {

        Class<? extends Activity> mclass = activity.getClass();
        Method[]methods = mclass.getDeclaredMethods();
        for (final Method method:methods) {
            method.setAccessible(true);
            BindOnclick bindOnclick = method.getAnnotation(BindOnclick.class);
            if (bindOnclick != null)
            {
                int id = bindOnclick.value();
            View view = activity.findViewById(id);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        method.invoke(activity, new Object[]{});   //调用传过来的Activity上的方法,这里可以拓展
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        }
    }

    private static void bindView(Activity activity) throws IllegalAccessException {
        Class<? extends Activity> mclass = activity.getClass(); //获取传过来的Activity的字节码
        Field[] fields =  mclass.getDeclaredFields();  //  获取传过来的Activity上声明的所有字段变量
        for (Field field : fields)
        {
            field.setAccessible(true); //设置字段可以反射
            BindView bindView = field.getAnnotation(BindView.class);  //获取字段变量绑定的注解类型，“BindView.class”参数表示获取@BindView类型的注解
            if (bindView!=null)  //如果该控件有被注解，就根据其Id findviewbyid()
            {
                int viewid = bindView.value();
                View view = activity.findViewById(viewid);
                field.set(activity,view);             //将找到的控件赋值给字段变量
            }
        }
    }
}
