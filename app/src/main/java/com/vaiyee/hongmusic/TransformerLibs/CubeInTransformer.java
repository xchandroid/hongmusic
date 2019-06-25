package com.vaiyee.hongmusic.TransformerLibs;

/**
 * Created by Administrator on 2019/5/4.
 */

import android.view.View;



public class CubeInTransformer extends ABaseTransformer {



    @Override

    protected void onTransform(View view, float position) {

        // Rotate the fragment on the left or right edge

        view.setPivotX(position > 0 ? 0 : view.getWidth());

        view.setPivotY(0);

        view.setRotationY(-90f * position);

    }



    @Override

    public boolean isPagingEnabled() {

        return true;

    }



}
