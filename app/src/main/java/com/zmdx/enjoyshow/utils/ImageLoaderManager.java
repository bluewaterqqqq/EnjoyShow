
package com.zmdx.enjoyshow.utils;

import android.os.Environment;
import android.os.StatFs;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zmdx.enjoyshow.ESApplication;

import java.io.File;

public class ImageLoaderManager {
    private static ImageLoader sImageLoader;

    private static final int IMAGE_DISC_CACHE_SIZE = 50 * 1014 * 1024;// 最小的SD卡应该也有2G

    public synchronized static ImageLoader getImageLoader() {
        if (sImageLoader == null) {
            initImageLoader();
        }
        return sImageLoader;
    }

    private ImageLoaderManager() {
    }

    private static void initImageLoader() {
        sImageLoader = ImageLoader.getInstance();
        // 默认的内存缓存大小为可用内存的15%
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                ESApplication.getInstance())
                .memoryCacheSizePercentage(15)// 设置内存缓存为当前应用最大内存的15%;
                .writeDebugLogs()
                .build();

        if (sImageLoader.isInited()) {
            sImageLoader.destroy();
        }
        sImageLoader.init(config);
    }

    static long calculateDiskCacheSize() {
        File dir = Environment.getExternalStorageDirectory();
        long size = IMAGE_DISC_CACHE_SIZE;
        try {
            StatFs statFs = new StatFs(dir.getAbsolutePath());
            long available = ((long) statFs.getBlockCount()) * statFs.getBlockSize();
            // Target 2% of the total space.
            size = available / 50;
        } catch (IllegalArgumentException ignored) {
        }
        // Bound inside min/max size for disk cache.
        return Math.min(size, IMAGE_DISC_CACHE_SIZE);
    }

}
