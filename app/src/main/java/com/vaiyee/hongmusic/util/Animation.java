package com.vaiyee.hongmusic.util;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by Administrator on 2019/4/21.
 */

public class Animation {
    public static Animator[] getAnimators(View view)
    {
        return new Animator[]{ObjectAnimator.ofFloat(view,"scaleX",0f,0.5f,1.0f),
        ObjectAnimator.ofFloat(view,"scaleY",0f,0.5f,1.0f)
        };
    }
}
