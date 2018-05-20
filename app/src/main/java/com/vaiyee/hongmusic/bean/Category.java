package com.vaiyee.hongmusic.bean;

import android.widget.Adapter;

import com.vaiyee.hongmusic.Adapter.GeshouTypeAdapter;

/**
 * Created by Administrator on 2018/4/6.
 */

    public class Category {
        private GeshouTypeAdapter mAdapter;
        private String mTitle;

        public Category() {

        }

        public Category(GeshouTypeAdapter mAdapter, String mTitle) {
            this.mAdapter = mAdapter;
            this.mTitle = mTitle;
        }

        public Adapter getmAdapter() {
            return mAdapter;
        }

        public void setmAdapter(GeshouTypeAdapter mAdapter) {
            this.mAdapter = mAdapter;
        }

        public String getmTitle() {
            return mTitle;
        }

        public void setmTitle(String mTitle) {
            this.mTitle = mTitle;
        }


}
