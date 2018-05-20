package com.vaiyee.hongmusic.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2018/4/11.
 */

@Target(ElementType.FIELD)    //   设当前注解的使用范围为注解字段
@Retention(RetentionPolicy.RUNTIME)   //设置当前直接的生命周期（注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在）
public @interface BindView {

    int value();  // 传进来的是控件的ID，所以为int类型
}
