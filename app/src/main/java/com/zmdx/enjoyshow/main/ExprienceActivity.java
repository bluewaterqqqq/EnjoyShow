/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.zmdx.enjoyshow.main;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.common.BaseAppCompatActivity;

/**
 * Created by zhangyan on 15/12/12.
 */
public class ExprienceActivity extends BaseAppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exprience_layout);
        getSupportFragmentManager().beginTransaction().replace(R.id.exprience_container, new Fragment1()).commit();
    }
}
