/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.zmdx.enjoyshow.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.zmdx.enjoyshow.ESApplication;

/**
 * Created by zhangyan on 15/12/2.
 */
public class ESPreferences {

    private static final String KEY_ALREADY_LOGIN = "alreadyLogin";
    private static final String KEY_USER_INFO = "userInfo";
    private static SharedPreferences sSp;

    public static final int STATUS_UNLOGIN = 1;
    public static final int STATUS_LOGIN = 2;
    public static final int STATUS_EXPERIENCE = 3;


    private static final String ES_PS_NAME = "es_ps_name";

    static {
        sSp = ESApplication.getInstance().getSharedPreferences(ES_PS_NAME, Context.MODE_PRIVATE);
    }

    public static void saveLoginStatus(int status) {
        sSp.edit().putInt(KEY_ALREADY_LOGIN, status).apply();
    }

    public static int getLoginStatus() {
        return sSp.getInt(KEY_ALREADY_LOGIN, STATUS_UNLOGIN);
    }

    public static void saveUserInfo(String user) {
        sSp.edit().putString(KEY_USER_INFO, user).apply();
    }

    public static String getUserInfo() {
        return sSp.getString(KEY_USER_INFO, null);
    }
}
