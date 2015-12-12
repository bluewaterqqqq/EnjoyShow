package com.zmdx.enjoyshow;

import android.app.Application;
import android.content.Context;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zmdx.enjoyshow.common.ESConfig;
import com.zmdx.enjoyshow.network.RequestQueueManager;
import com.zmdx.enjoyshow.utils.LogHelper;
import com.zmdx.enjoyshow.utils.threadpool.ThreadPool;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by zhangyan on 15/10/27.
 */
public class ESApplication extends Application {

    private static final String TAG = "ESApplication";
    private static Context sContext;
    public static IWXAPI sApi;
    public static final String APP_ID = "wx81aaaad92e07a7fd";

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        ThreadPool.startup();
        RequestQueueManager.init(this);

        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush

        sApi = WXAPIFactory.createWXAPI(this, APP_ID, false);
        boolean result = sApi.registerApp(APP_ID);
        LogHelper.d(TAG, "registerApp result :" + result);


    }

    public static IWXAPI getWXAPI() {
        return sApi;
    }

    public static Context getInstance() {
        return sContext;
    }
}
