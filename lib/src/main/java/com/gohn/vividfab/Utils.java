package com.gohn.vividfab;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

class Utils {
    static double getLength(float x, float y) {
        return Math.sqrt(Math.pow(x, 2f) + Math.pow(y, 2f));
    }

    static int dp2px(Context context, int dp) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return (int) px;
    }
    static int dp2px(Context context, float dp) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return (int) px;
    }
}
