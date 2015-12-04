/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.zmdx.enjoyshow.main.profile;

import com.zmdx.enjoyshow.network.ActionConstants;

/**
 * Created by zhangyan on 15/12/4.
 */
public class UserFunsFragment extends UserFollowedFragment {

    @Override
    protected String getRequestAction() {
        return ActionConstants.ACTION_QUERY_FANS;
    }
}
