package com.zmdx.enjoyshow.utils;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zmdx.enjoyshow.ESApplication;
import com.zmdx.enjoyshow.R;

/**
 * Created by zhangyan on 15/11/15.
 */
public class ImageLoaderOptionsUtils {

    public static DisplayImageOptions getCoverImageOptions() {
        DisplayImageOptions coverImageOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .showImageOnLoading(R.drawable.image_placeholder)
                .showImageOnFail(R.drawable.image_placeholder)
                .showImageForEmptyUri(R.drawable.image_placeholder)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .displayer(new FadeInBitmapDisplayer(100))
                .build();
        return coverImageOptions;
    }

    public static DisplayImageOptions getHeadImageOptions() {
        DisplayImageOptions headerImageOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .showImageOnLoading(R.drawable.image_placeholder)
                .showImageOnFail(R.drawable.image_placeholder)
                .showImageForEmptyUri(R.drawable.image_placeholder)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .displayer(new RoundedBitmapDisplayer(BaseInfoHelper.dip2px(ESApplication.getInstance(), 1000)))
                .build();

        return headerImageOptions;
    }
}
