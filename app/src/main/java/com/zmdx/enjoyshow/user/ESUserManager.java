package com.zmdx.enjoyshow.user;

import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.zmdx.enjoyshow.common.ESPreferences;
import com.zmdx.enjoyshow.entity.ESUser;
import com.zmdx.enjoyshow.network.ActionConstants;
import com.zmdx.enjoyshow.network.RequestQueueManager;
import com.zmdx.enjoyshow.network.UrlBuilder;
import com.zmdx.enjoyshow.utils.LogHelper;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by zhangyan on 15/10/27.
 */
public class ESUserManager {

    public static final int STATUS_UNLOGIN = 1;
    public static final int STATUS_LOGIN_BY_ES = 2;
    public static final int STATUS_LOGIN_BY_THRID = 4;
    public static final int STATUS_EXPERIENCE = 3;
    private static final String TAG = "ESUserManager";
    private static ESUserManager INSTANCE;
    private ESUser mUser;

    private ESUserManager() {

    }

    public synchronized static ESUserManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ESUserManager();
        }
        return INSTANCE;
    }

    public String getCurrentUserId() {
        mUser = getCurrentUser();
        if (mUser != null) {
            return mUser.getId() + "";
        }
        return null;
    }

    public void setLoginStatus(int status) {
        ESPreferences.saveLoginStatus(status);
    }

    public void saveUserInfo(String user, boolean syncServer) {
        ESPreferences.saveUserInfo(user);
        mUser = ESUser.convertByJSON(user);
        LogHelper.d(TAG, "保存登录用户信息, user:" + user);
        if (syncServer) {
            pushUserInfo2Server(mUser);
        }
    }

    private void pushUserInfo2Server(ESUser user) {
        StringBuffer sb = new StringBuffer();
        sb.append("?username=");
        try {
            String userName = URLEncoder.encode(user.getUserName(), "utf-8");
            String newName = URLEncoder.encode(userName, "utf-8");
            sb.append(newName);
        } catch (UnsupportedEncodingException e) {
            // ignore
        }
        if (!TextUtils.isEmpty(user.getAddr())) {
            sb.append("&address=");
            try {
                String addr = URLEncoder.encode(user.getAddr(), "utf-8");
                String newaddr = URLEncoder.encode(addr, "utf-8");
                sb.append(newaddr);
            } catch (UnsupportedEncodingException e) {
                // ignore
            }
        }
        if (!TextUtils.isEmpty(user.getTelephone())) {
            sb.append("&telephone=");
            sb.append(user.getTelephone());
        }
        if (!TextUtils.isEmpty(user.getRealName())) {
            sb.append("&name=");
            try {
                String realName = URLEncoder.encode(user.getRealName(), "utf-8");
                String newName = URLEncoder.encode(realName, "utf-8");
                sb.append(newName);
            } catch (UnsupportedEncodingException e) {
                // ignore
            }
        }
        if (!TextUtils.isEmpty(user.getAge())) {
            sb.append("&age=");
            sb.append(user.getAge());
        }
        if (!TextUtils.isEmpty(user.getGender())) {
            sb.append("&gender=");
            sb.append(user.getGender());
        }
        if (!TextUtils.isEmpty(user.getIntrodution())) {
            sb.append("&introduction=");
            try {
                String indro = URLEncoder.encode(user.getIntrodution(), "utf-8");
                String newIndro = URLEncoder.encode(indro, "utf-8");
                sb.append(newIndro);
            } catch (UnsupportedEncodingException e) {
                // ignore
            }
            sb.append(user.getIntrodution());
        }

        String url = UrlBuilder.getUrl(ActionConstants.ACTION_UPLOAD_INFO, sb.toString());

        final RequestQueue requestQueue = RequestQueueManager.getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogHelper.d(TAG, "response:" + response);
                int state = response.optInt("state");
                if (state == 0) {
                    JSONObject obj = response.optJSONObject("result").optJSONObject("user");
                    mUser = ESUser.convertByJSON(obj.toString());
                    saveUserInfo(mUser, false);
                    LogHelper.d(TAG, "个人信息更新成功,并同步至服务器");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogHelper.e(TAG, "同步个人信息失败,onErrorResponse:" + error.getMessage());
            }
        });

        requestQueue.add(request);
    }

    public void saveUserInfo(ESUser user, boolean syncServer) {
        String userStr = ESUser.convert2JSON(user).toString();
        saveUserInfo(userStr, syncServer);
    }

    public void logout() {
        mUser = null;
        ESPreferences.saveLoginStatus(ESUserManager.STATUS_UNLOGIN);
    }

    public ESUser getCurrentUser() {
        if (mUser == null) {
            String userInfo = ESPreferences.getUserInfo();
            if (userInfo != null) {
                mUser = ESUser.convertByJSON(userInfo);
            }
        }
        return mUser;
    }
}
