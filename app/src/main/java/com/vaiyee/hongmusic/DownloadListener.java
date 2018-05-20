package com.vaiyee.hongmusic;

/**
 * Created by Administrator on 2018/4/26.
 */

public interface DownloadListener {
    void onProgress(String geming,int progress,int notiID);
    void onSuccess(String geming,int notiID);
    void onFail();

}
