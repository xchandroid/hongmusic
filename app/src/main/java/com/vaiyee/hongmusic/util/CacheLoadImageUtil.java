package com.vaiyee.hongmusic.util;


import android.graphics.Bitmap;
import android.util.LruCache;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2019/4/21.
 */

public class CacheLoadImageUtil extends LruCache<String,Bitmap> {
    public Map<String,SoftReference<Bitmap>> softReferenceMap = new HashMap<>();
    public CacheLoadImageUtil(int maxSize) {
        super(maxSize);
    }

    @Override
    protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
        softReferenceMap.put(key,new SoftReference<Bitmap>(oldValue)); //当图片被从LruCache中移除时，存放到软引用中
    }
}
