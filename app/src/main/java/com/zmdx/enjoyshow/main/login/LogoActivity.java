/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.zmdx.enjoyshow.main.login;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zmdx.enjoyshow.MainActivity;
import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.common.BaseAppCompatActivity;
import com.zmdx.enjoyshow.common.ESConfig;
import com.zmdx.enjoyshow.common.ESPreferences;
import com.zmdx.enjoyshow.user.ESUserManager;
import com.zmdx.enjoyshow.utils.LogHelper;
import com.zmdx.enjoyshow.wxapi.WXEntryActivity;


/**
 * Created by zhangyan on 15/12/2.
 */
public class LogoActivity extends BaseAppCompatActivity {

    private static final String APP_ID = "wx81aaaad92e07a7fd";
    private static final String WX_APP_SCOPE = "snsapi_userinfo";
    private static final String WX_APP_STATE = "enjoyshow_wxlogin";
    private static final String TAG = "LogoActivity";

    private IWXAPI mApi;

    private ProgressDialog mDialog;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (!TextUtils.isEmpty(action)) {
                if (action.equals(WXEntryActivity.ACTION_ACCESS_TOKEN)) {
                    LogHelper.d(TAG, "收到ACTION_ACCESS_TOKEN");
                } else if (action.equals(WXEntryActivity.ACTION_LOG_SUCC)) {
                    LogHelper.d(TAG, "收到ACTION_LOG_SUCC");
                    dismissLoadingDialog();
                    finish();
                } else if (action.equals(WXEntryActivity.ACTION_LOG_FAILED)) {
                    LogHelper.d(TAG, "收到ACTION_LOG_FAILED");
                    dismissLoadingDialog();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int logStatus = ESPreferences.getLoginStatus();
        if (logStatus == ESUserManager.STATUS_LOGIN_BY_ES) {
            startMainActivity();
            finish();
            return;
        } else if (logStatus == ESUserManager.STATUS_LOGIN_BY_THRID) {
//            WXEntryActivity.refreshToken();
            startMainActivity();
            finish();
            return;
        }

        setContentView(R.layout.logo_layout);

        // 第三个参数为是否做签名检查
        mApi = WXAPIFactory.createWXAPI(this, APP_ID, !ESConfig.DEBUG);

        IntentFilter filter = new IntentFilter();
        filter.addAction(WXEntryActivity.ACTION_ACCESS_TOKEN);
        filter.addAction(WXEntryActivity.ACTION_LOG_SUCC);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    private void startMainActivity() {
        Intent in = new Intent(this, MainActivity.class);
        startActivity(in);
    }

    public void onClickRegist(View view) {
        Intent in = new Intent(this, LoginRegistActivity.class);
        in.putExtra("action", LoginRegistActivity.PAGE_REGIST);
        startActivity(in);
        finish();
    }

    public void onClickLogin(View view) {
        Intent in = new Intent(this, LoginRegistActivity.class);
        in.putExtra("action", LoginRegistActivity.PAGE_LOGIN);
        startActivity(in);
        finish();
    }

    public void onClickTry(View view) {
        ESPreferences.saveLoginStatus(ESUserManager.STATUS_EXPERIENCE);
        startMainActivity();
    }

    private void showLoadingDialog() {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("登录中");
        mDialog.setCancelable(false);
        mDialog.show();

    }

    private void dismissLoadingDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public void onClickWechat(View view) {
        LogHelper.d(TAG, "onCLickWeChat");
        showLoadingDialog();
        if (!mApi.isWXAppInstalled()) {
            Toast.makeText(this, "请先安装微信应用", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!mApi.isWXAppSupportAPI()) {
            Toast.makeText(this, "请先更新微信应用", Toast.LENGTH_SHORT).show();
            return;
        }
        mApi.registerApp(APP_ID);
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = WX_APP_SCOPE;
        req.state = WX_APP_STATE;
        mApi.sendReq(req);
    }
}
