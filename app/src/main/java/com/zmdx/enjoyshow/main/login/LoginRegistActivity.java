/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.zmdx.enjoyshow.main.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.common.BaseAppCompatActivity;

/**
 * Created by zhangyan on 15/12/2.
 */
public class LoginRegistActivity extends BaseAppCompatActivity implements View.OnClickListener {

    public static final int PAGE_LOGIN = 1;
    public static final int PAGE_REGIST = 2;
    private TextView mToolbarBtn;
    private FrameLayout mContent;
    private int mCurrentPage = PAGE_LOGIN;

    private Fragment mLoginFragment;

    private Fragment mRegistFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Intent in = getIntent();
        if (in != null) {
            int action = in.getIntExtra("action", PAGE_LOGIN);
            if (action == PAGE_LOGIN) {
                mCurrentPage = PAGE_LOGIN;
            } else if (action == PAGE_REGIST) {
                mCurrentPage = PAGE_REGIST;
            }
        }

        initToolbar();

        fillContent(false);
    }

    private void fillContent(boolean addToStack) {
        if (mCurrentPage == PAGE_LOGIN) {
            switchToLoginFragment(addToStack);
        } else if (mCurrentPage == PAGE_REGIST) {
            switchToRegistFragment(addToStack);
        }
    }

    public void switchToLoginFragment(boolean addToStack) {
        if (mLoginFragment == null) {
            mLoginFragment = new LoginFragment();
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.login_content, mLoginFragment);
        if (addToStack) {
            ft.addToBackStack("login");
        }
        ft.commit();
        updateTitle();
        mCurrentPage = PAGE_REGIST;
    }

    private void switchToRegistFragment(boolean addToStack) {
        if (mRegistFragment == null) {
            mRegistFragment = new RegistFragment();
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.login_content, mRegistFragment);
        if (addToStack) {
            ft.addToBackStack("regist");
        }
        ft.commit();
        updateTitle();
        mCurrentPage = PAGE_LOGIN;
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbarBtn = (TextView) toolbar.findViewById(R.id.login_toolbar_btn);
        mToolbarBtn.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        updateTitle();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startLogoActivity();
            }
        });

    }

    private void updateTitle() {
        if (mCurrentPage == PAGE_LOGIN) {
            getSupportActionBar().setTitle("登录");
            mToolbarBtn.setText("注册");
        } else if (mCurrentPage == PAGE_REGIST) {
            getSupportActionBar().setTitle("注册");
            mToolbarBtn.setText("登录");
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mToolbarBtn) {
            fillContent(true);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startLogoActivity();
    }

    private void startLogoActivity() {
        Intent in = new Intent(this, LogoActivity.class);
        startActivity(in);
    }
}
