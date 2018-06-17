package com.vaiyee.hongmusic.http;

import com.vaiyee.hongmusic.bean.WangyiBang;

/**
 * Created by Administrator on 2018/6/16.
 */

public abstract class WangyibangBackListener {
   public abstract void onSuccess(WangyiBang wangyiBang);
   public abstract void onFail(Exception e);
}
