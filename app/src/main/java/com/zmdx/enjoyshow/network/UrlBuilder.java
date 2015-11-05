package com.zmdx.enjoyshow.network;

import android.text.TextUtils;

import com.zmdx.enjoyshow.common.ESConfig;

/**
 * Created by zhangyan on 15/10/27.
 */
public class UrlBuilder {

    private static final String BASE_URL_TEST = "http://nb.hdlocker.com/";
    private static final String BASE_URL_PROD = "http://nb.hdlocker.com/";

    public static String getUrl(String action, String params) {
        if (TextUtils.isEmpty(action)) {
            throw new IllegalArgumentException();
        }

        if (!TextUtils.isEmpty(params) && !params.startsWith("?")) {
            throw new IllegalArgumentException("params must be start with ?");
        }
        return ESConfig.ONLINE_SERVER ? BASE_URL_PROD + action + params : BASE_URL_TEST + action + params;
    }

}
