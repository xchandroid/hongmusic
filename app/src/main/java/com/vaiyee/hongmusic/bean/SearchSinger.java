package com.vaiyee.hongmusic.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/15.
 */

public class SearchSinger {

    /**
     * status : 1
     * error :
     * data : [{"singerid":3047,"singername":"许嵩"}]
     * errcode : 0
     */

    private int status;
    private String error;
    private int errcode;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * singerid : 3047
         * singername : 许嵩
         */

        private int singerid;
        private String singername;

        public int getSingerid() {
            return singerid;
        }

        public void setSingerid(int singerid) {
            this.singerid = singerid;
        }

        public String getSingername() {
            return singername;
        }

        public void setSingername(String singername) {
            this.singername = singername;
        }
    }
}
