package com.zmdx.enjoyshow.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by zhangyan on 15/10/27.
 */
public class UIUtils {
    private static float sDensity = 0f;

    private static int sScreenWidth = 0;

    private static int sScreenHeight = 0;

    public static float getDensity(Context context) {
        if (sDensity == 0f) {
            DisplayMetrics dm = new DisplayMetrics();
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay().getMetrics(dm);
            sDensity = dm.density;
        }
        return sDensity;
    }
    public static int dipToPx(Context context, int dip) {
        return (int) (dip * getDensity(context) + 0.5f);
    }
}
