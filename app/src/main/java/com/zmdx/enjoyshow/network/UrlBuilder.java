package com.zmdx.enjoyshow.network;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.zmdx.enjoyshow.ESApplication;
import com.zmdx.enjoyshow.common.ESConfig;
import com.zmdx.enjoyshow.common.ESConstants;
import com.zmdx.enjoyshow.user.ESUserManager;
import com.zmdx.enjoyshow.utils.BaseInfoHelper;
import com.zmdx.enjoyshow.utils.HashUtils;
import com.zmdx.enjoyshow.utils.LogHelper;

/**
 * Created by zhangyan on 15/10/27.
 */
public class UrlBuilder {

    private static final String BASE_URL_TEST = "http://nb.hdlocker.com/";

    private static final String BASE_URL_PROD = "http://nb.hdlocker.com/";

    private static final String BASE_URL = ESConfig.ONLINE_SERVER ? BASE_URL_PROD : BASE_URL_TEST;
    private static final String TAG = "UrlBuilder";

    public static String getUrl(String action, String params) {
        if (TextUtils.isEmpty(action)) {
            throw new IllegalArgumentException();
        }

        if (!TextUtils.isEmpty(params) && !params.startsWith("?")) {
            throw new IllegalArgumentException("params must be start with ?");
        }
        StringBuffer sb = new StringBuffer(BASE_URL);
        sb.append(action);
        if (!TextUtils.isEmpty(params)) {
            sb.append(params);
        }
        String str = sb.toString();
        if (!str.contains("?")) {
            sb.append("?");
        } else {
            sb.append("&");
        }
        sb.append(getBaseInfo());
        String finalStr = sb.toString();
        LogHelper.d(TAG, "request url:" + finalStr);
        return finalStr;
    }

    public static String getBaseInfo() {
        Context context = ESApplication.getInstance();
        StringBuffer sb = new StringBuffer();

        sb.append("did=");
        String did = BaseInfoHelper.getIMEI(context);
        sb.append(did);
        sb.append("&pf=Android");
        sb.append("&appversion=");
        String vCode = BaseInfoHelper.getPkgVersionName(context) + "";
        sb.append(vCode);
        sb.append("&version=");
        sb.append(BaseInfoHelper.getAndroidVersion(context));
        sb.append("&channel=");
        sb.append(getChannel());
        String userId = "";
        if (ESUserManager.getInstance().getCurrentUserId() != null) {
            sb.append("&currentUserId=");
            userId = ESUserManager.getInstance().getCurrentUserId();
            sb.append(userId);
        }
        sb.append("&t=");
        long currentTime = System.currentTimeMillis();
        sb.append(currentTime);
        sb.append("&w=");
        sb.append(ESConstants.LARGE_IMAGE_WIDTH);
        sb.append("&sw=");
        sb.append(ESConstants.SMALL_IMAGE_WIDTH);
        sb.append("&s=");
        sb.append(getS(did, userId, "Android", currentTime, vCode));
        return sb.toString();
    }

    private static String getS(String did, String userId, String android, long currentTime, String vCode) {
        String a = "AtQym[didR%s`currentUserIde%s`pfF%s`tI%s`appVersionc%s]AtQym}/~&^;'";
        String str = String.format(a, did, userId, android, currentTime, vCode);
        String sha1Str = HashUtils.getStringSHA1(str);
        return sha1Str;
    }

    public static String getChannel() {
        if (ESConfig.DEBUG) {
            return "default";
        } else {
            throw new IllegalStateException("必须包含正确的渠道号");
        }
    }
}
