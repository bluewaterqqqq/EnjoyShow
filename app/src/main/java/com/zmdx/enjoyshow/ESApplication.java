package com.zmdx.enjoyshow;

import android.app.Application;
import android.content.Context;

import com.zmdx.enjoyshow.network.RequestQueueManager;
import com.zmdx.enjoyshow.utils.LogHelper;
import com.zmdx.enjoyshow.utils.threadpool.ThreadPool;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by zhangyan on 15/10/27.
 */
public class ESApplication extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        ThreadPool.startup();
        RequestQueueManager.init(this);

        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
    }

    public static Context getInstance() {
        return sContext;
    }
}
