/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.zmdx.enjoyshow.main.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.zmdx.enjoyshow.MainActivity;
import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.network.ActionConstants;
import com.zmdx.enjoyshow.network.RequestQueueManager;
import com.zmdx.enjoyshow.network.UrlBuilder;
import com.zmdx.enjoyshow.user.ESUserManager;

import org.json.JSONObject;

/**
 * Created by zhangyan on 15/12/2.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private EditText mPhoneNumEt;

    private EditText mPasswordEt;

    private Button mLoginBtn;

    private TextView mForgetPwdTv;
    private boolean mLogining = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return LayoutInflater.from(getActivity()).inflate(R.layout.login_fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPhoneNumEt = (EditText) view.findViewById(R.id.login_phoneNum);
        mPasswordEt = (EditText) view.findViewById(R.id.login_pwd);
        mLoginBtn = (Button) view.findViewById(R.id.loginBtn);
        mForgetPwdTv = (TextView) view.findViewById(R.id.login_forgetPwd);

        mLoginBtn.setOnClickListener(this);
        mForgetPwdTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mLoginBtn) {
            String phone = mPhoneNumEt.getText().toString();
            String pwd = mPasswordEt.getText().toString();
            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(getActivity(), "手机号不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            if (phone.length() != 11) {
                Toast.makeText(getActivity(), "请正确输入手机号", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(pwd)) {
                Toast.makeText(getActivity(), "密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            login(phone, pwd);
        } else if (v == mForgetPwdTv) {
            // TODO
        }
    }

    private void login(String phoneNum, String pwd) {
        if (mLogining) {
            return;
        }
        mLogining = true;
        String url = createLoginUrl(phoneNum, pwd);
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                int state = response.optInt("state", -1);
                if (state == 0) {
                    Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show();
                    JSONObject obj = response.optJSONObject("result");
                    String user = obj.optString("user");
                    ESUserManager.getInstance().saveUserInfo(user, false);
                    ESUserManager.getInstance().setLoginStatus(ESUserManager.STATUS_LOGIN_BY_ES);
                    startMainActivity();
                    finish();
                } else if (state == 1) {
                    String errorMsg = response.optString("errorMsg");
                    Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                }

                mLogining = false;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "登录失败", Toast.LENGTH_SHORT).show();
                mLogining = false;
            }
        });

        RequestQueueManager.getRequestQueue().add(request);
    }

    private void finish() {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        activity.finish();
    }

    private void startMainActivity() {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        Intent in = new Intent(activity, MainActivity.class);
        activity.startActivity(in);
    }

    private String createLoginUrl(String phoneNum, String pwd) {
        return UrlBuilder.getUrl(ActionConstants.ACTION_LOGIN, "?loginname=" + phoneNum + "&password=" + pwd + "&alias=" + "adsf");
    }
}
