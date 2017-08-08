package com.yxj.spiderview;

import android.content.Context;

/**
 * Created by yxj on 17/8/3.
 */

public class UIUtils {

    public static float dp2px(Context context,float dp){
        return context.getResources().getDisplayMetrics().density*dp;
    }

}
