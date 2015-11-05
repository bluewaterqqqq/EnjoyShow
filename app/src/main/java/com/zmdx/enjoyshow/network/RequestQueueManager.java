package com.zmdx.enjoyshow.network;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.zmdx.enjoyshow.BuildConfig;

/**
 * Created by zhangyan on 15/10/27.
 */
public class RequestQueueManager {

    private static RequestQueue sRequestQueue;

    private RequestQueueManager() {

    }

    /**
     * init RequestQueue
     *
     * @param ctx
     */
    public static void init(Context ctx) {
        VolleyLog.DEBUG = BuildConfig.DEBUG;
        sRequestQueue = Volley.newRequestQueue(ctx);
    }

    public static RequestQueue getRequestQueue() {
        if (null != sRequestQueue) {
            return sRequestQueue;
        } else {
            throw new IllegalArgumentException("Not initialized");
        }
    }

}
