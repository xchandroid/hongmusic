package com.vaiyee.hongmusic.TransformerLibs;

/**
 * Created by Administrator on 2019/5/4.
 */

import android.view.View;



public class StackTransformer extends ABaseTransformer {



    @Override

    protected void onTransform(View view, float position) {

        view.setTranslationX(position < 0 ? 0f : -view.getWidth() * position);

    }



}