/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.zmdx.enjoyshow.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zmdx.enjoyshow.ESApplication;
import com.zmdx.enjoyshow.MainActivity;
import com.zmdx.enjoyshow.common.ESConfig;
import com.zmdx.enjoyshow.common.ESPreferences;
import com.zmdx.enjoyshow.entity.ESUser;
import com.zmdx.enjoyshow.network.ActionConstants;
import com.zmdx.enjoyshow.network.RequestQueueManager;
import com.zmdx.enjoyshow.network.UrlBuilder;
import com.zmdx.enjoyshow.user.ESUserManager;
import com.zmdx.enjoyshow.utils.LogHelper;

import org.json.JSONObject;

/**
 * Created by zhangyan on 15/12/3.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    public static final String ACTION_ACCESS_TOKEN = "action_ac_token";
    public static final String ACTION_LOG_SUCC = "action_log_succ";
    public static final String ACTION_LOG_FAILED = "action_log_failed";
    private static final String AppSecret = "3a4858c543e86591519b0a838810e258";
    private static final String TAG = "WXEntryActivity";
    private static final String REFRESH_TOKEN_BASE_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid="
            + ESApplication.APP_ID + "&grant_type=refresh_token&refresh_token=";
    private static final String ACCESS_TOKEN_BASE_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
            + ESApplication.APP_ID + "&secret=" + AppSecret + "&grant_type=authorization_code&" + "code=";
    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private IWXAPI mWxApi;

    public static void refreshToken() {
        String refreshToken = ESPreferences.getWXRefreshToken();
        String url = REFRESH_TOKEN_BASE_URL + refreshToken;
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogHelper.d(TAG, "获取token成功, result:" + response.toString());
                String accessToken = response.optString("access_token");
                String refreshToken = response.optString("refresh_token");
                String openId = response.optString("openid");
                saveRefreshToken(refreshToken);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogHelper.d(TAG, "获取token失败, result:" + error.toString());
            }
        });

        RequestQueueManager.getRequestQueue().add(request);

    }

    private static void saveRefreshToken(String refreshToken) {
        ESPreferences.saveWXRefreshToken(refreshToken);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 第三个参数为是否做签名检查
        mWxApi = WXAPIFactory.createWXAPI(this, ESApplication.APP_ID, false);
        boolean result = mWxApi.registerApp(ESApplication.APP_ID);
        LogHelper.d(TAG, "registerApp result :" + result);
        mWxApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        ESApplication.getWXAPI().handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        LogHelper.d(TAG, "onReq....");
    }

    @Override
    public void onResp(BaseResp resp) {
        LogHelper.d(TAG, "onResp");
        int type = resp.getType();
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                if (type == 1) {
                    // 登录授权验证
                    onFailure();
                } else if (type == 2) {
                    // 取消分享
                }
                break;
            case BaseResp.ErrCode.ERR_OK:
                if (type == 1) {
                    //拿到了微信返回的code,立马再去请求access_token
                    String code = ((SendAuth.Resp) resp).code;
                    LogHelper.d(TAG, "onResp..... code=" + code);
                    requestToken(code);
                } else if (type == 2) {
                    // 分享成功
                    Toast.makeText(WXEntryActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void onFailure() {
        Toast.makeText(WXEntryActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
        finish();
        LocalBroadcastManager.getInstance(WXEntryActivity.this).sendBroadcast(new Intent(ACTION_LOG_FAILED));
    }

    private void requestToken(String code) {
        String url = ACCESS_TOKEN_BASE_URL + code;
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogHelper.d(TAG, "获取token成功, result:" + response.toString());
                String accessToken = response.optString("access_token");
                String refreshToken = response.optString("refresh_token");
                String openId = response.optString("openid");
                String expiresTime = response.optString("expires_in");
                saveRefreshToken(refreshToken);

                // 将获取的access token广播出去
                Intent in = new Intent(ACTION_ACCESS_TOKEN);
                in.putExtra("accessToken", accessToken);
                LocalBroadcastManager.getInstance(ESApplication.getInstance()).sendBroadcast(in);

                requestUserInfo(accessToken, openId, expiresTime);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogHelper.d(TAG, "获取token失败, result:" + error.toString());
                onFailure();
            }
        });

        RequestQueueManager.getRequestQueue().add(request);
    }

    private void requestUserInfo(final String accessToken, String openId, final String expiresTime) {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId;
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogHelper.d(TAG, "获取用户信息成功, result:" + response.toString());
                String userid = response.optString("openid");

                pushUserInfo2Server(userid, accessToken, expiresTime);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogHelper.d(TAG, "获取用户信息失败, result:" + error.toString());
                onFailure();
            }
        });

        RequestQueueManager.getRequestQueue().add(request);
    }

    private void pushUserInfo2Server(String userid, String token, String expiresTime) {
        String params = "?thirdParty=weixin&userId=" + userid + "&token=" + token + "&alias=123&expiresIn=" + expiresTime;
        String url = UrlBuilder.getUrl(ActionConstants.ACTION_THIRD_LOGIN, params);
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogHelper.d(TAG, "用户信息上传成功, result:" + response.toString());
                int state = response.optInt("state");
                if (state == 0) {
                    String userJSON = response.optJSONObject("result").optString("user");
                    ESUser user = ESUser.convertByJSON(userJSON);
                    ESUserManager.getInstance().saveUserInfo(user, false);
                    ESUserManager.getInstance().setLoginStatus(ESUserManager.STATUS_LOGIN_BY_THRID);

                    Toast.makeText(WXEntryActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    startMainActivity();
                    finish();

                    LocalBroadcastManager.getInstance(WXEntryActivity.this).sendBroadcast(new Intent(ACTION_LOG_SUCC));
                } else {
                    onFailure();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogHelper.d(TAG, "用户信息上传失败, result:" + error.toString());
                onFailure();
            }
        });

        RequestQueueManager.getRequestQueue().add(request);
    }

    private void startMainActivity() {
        Intent in = new Intent(this, MainActivity.class);
        startActivity(in);
    }
}
