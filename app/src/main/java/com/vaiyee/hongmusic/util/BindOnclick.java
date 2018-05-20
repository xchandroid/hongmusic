package com.vaiyee.hongmusic.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2018/4/11.
 */

@Target(ElementType.METHOD)             //设置当前注解的使用范围是注解方法
@Retention(RetentionPolicy.RUNTIME)
public @interface BindOnclick {
    int value();      //表示根据控件的id进行设置点击事件
}
