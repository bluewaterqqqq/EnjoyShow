/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.zmdx.enjoyshow.main.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.common.BaseAppCompatActivity;
import com.zmdx.enjoyshow.main.login.LogoActivity;
import com.zmdx.enjoyshow.user.ESUserManager;

/**
 * Created by zhangyan on 15/12/5.
 */
public class SettingsActivity extends BaseAppCompatActivity implements View.OnClickListener {

    public static final String ACTION_FINISH_ALL_ACTIVITY = "ac_finish_acti";
    private static final String TAG = "SettingsActivity";
    private View mLogoutBtn;

    private View mAboutus;

    private View mFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Toolbar tb = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mLogoutBtn = findViewById(R.id.logoutBtn);
        mAboutus = findViewById(R.id.aboutus);
        mFeedback = findViewById(R.id.feedback);
        mLogoutBtn.setOnClickListener(this);
        mAboutus.setOnClickListener(this);
        mFeedback.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == mLogoutBtn) {
            ESUserManager.getInstance().logout();
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ACTION_FINISH_ALL_ACTIVITY));
            startActivity(new Intent(this, LogoActivity.class));
            finish();
        } else if (v == mAboutus) {

        } else if (v == mFeedback) {

        }
    }
}
