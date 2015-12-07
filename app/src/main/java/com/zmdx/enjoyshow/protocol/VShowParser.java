/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.zmdx.enjoyshow.protocol;

import android.text.TextUtils;

/**
 * Created by zhangyan on 15/12/7.
 */
public class VShowParser {

    private static final String PROTOCOL = "vshow://";

    private static final String HOST = "vshow.com/";

    private static final String ACTION_THEME = "theme";

    public static String parseThemeIdBy(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        if (!url.startsWith("vshow://")) {
            throw new IllegalArgumentException("错误的协议");
        }

        String[] strs = url.split("[?]");
        if (strs != null && strs.length >= 2) {
            String[] params = strs[1].split("=");
            if (params.length > 1) {
                return params[1];
            }
        }

        return null;
    }
}
