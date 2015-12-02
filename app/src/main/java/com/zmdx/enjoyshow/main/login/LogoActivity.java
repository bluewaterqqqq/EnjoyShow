/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.zmdx.enjoyshow.main.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zmdx.enjoyshow.MainActivity;
import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.common.BaseAppCompatActivity;
import com.zmdx.enjoyshow.common.ESPreferences;

/**
 * Created by zhangyan on 15/12/2.
 */
public class LogoActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ESPreferences.getLoginStatus() == ESPreferences.STATUS_LOGIN) {
            startMainActivity();
            finish();
            return;
        }

        setContentView(R.layout.logo_layout);
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
        ESPreferences.saveLoginStatus(ESPreferences.STATUS_EXPERIENCE);
        startMainActivity();
    }

    public void onClickWechat(View view) {
        // TODO
    }
}
