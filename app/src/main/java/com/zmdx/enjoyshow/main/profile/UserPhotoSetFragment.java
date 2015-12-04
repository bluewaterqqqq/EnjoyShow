/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.zmdx.enjoyshow.main.profile;

import android.os.Bundle;

import com.zmdx.enjoyshow.main.pic.PicRecommendFragment;
import com.zmdx.enjoyshow.network.ActionConstants;
import com.zmdx.enjoyshow.user.ESUserManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangyan on 15/12/4.
 */
public class UserPhotoSetFragment extends PicRecommendFragment {

    private String mUserId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mUserId = bundle.getString("userId");
        }
    }

    @Override
    protected String getRequestAction() {
        return ActionConstants.ACTION_QUERY_PERSONAL_PHOTOS;
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("userId", mUserId);
        return params;
    }
}
