package com.zmdx.enjoyshow;

import android.app.Application;
import android.content.Context;

import com.zmdx.enjoyshow.network.RequestQueueManager;
import com.zmdx.enjoyshow.utils.threadpool.ThreadPool;

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
    }

    public static Context getInstance() {
        return sContext;
    }
}
