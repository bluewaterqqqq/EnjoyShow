/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.zmdx.enjoyshow.main.login;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.zmdx.enjoyshow.entity.ESUser;
import com.zmdx.enjoyshow.network.ActionConstants;
import com.zmdx.enjoyshow.network.RequestQueueManager;
import com.zmdx.enjoyshow.network.UrlBuilder;
import com.zmdx.enjoyshow.user.ESUserManager;
import com.zmdx.enjoyshow.utils.LogHelper;

import org.json.JSONObject;

/**
 * Created by baidu on 15/12/9.
 */
public class UpdateUserInfoHelper {

    private static final String TAG = "UpdateUserInfoHelper";

    public static void pullUserDetailInfo(String userId) {
        String url = UrlBuilder.getUrl(ActionConstants.ACTION_USER_INFO, "?userId=" + userId);
        final RequestQueue requestQueue = RequestQueueManager.getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogHelper.d(TAG, "response:" + response);
                int state = response.optInt("state");
                if (state == 0) {
                    JSONObject obj = response.optJSONObject("result");
                    String userObj = obj.optString("user");
                    ESUser user = ESUser.convertByJSON(userObj);
                    ESUserManager.getInstance().saveUserInfo(user, true);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogHelper.e(TAG, "onErrorResponse:" + error.getMessage());
            }
        });

        requestQueue.add(request);
    }
}
