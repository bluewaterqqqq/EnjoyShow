/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.zmdx.enjoyshow.main.login;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
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
import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.network.ActionConstants;
import com.zmdx.enjoyshow.network.RequestQueueManager;
import com.zmdx.enjoyshow.network.UrlBuilder;

import org.json.JSONObject;

/**
 * Created by zhangyan on 15/12/2.
 */
public class RegistFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private EditText mPhoneNumEt;

    private EditText mPasswordEt;

    private EditText mCodeEt;

    private Button mRegistBtn;

    private TextView mGetCodeBtn;

    private boolean mRegisting = false;

    private boolean mGetCoding = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return LayoutInflater.from(getActivity()).inflate(R.layout.register_fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPhoneNumEt = (EditText) view.findViewById(R.id.regist_phoneNum);
        mPasswordEt = (EditText) view.findViewById(R.id.regist_pwd);
        mCodeEt = (EditText) view.findViewById(R.id.regist_code);
        mRegistBtn = (Button) view.findViewById(R.id.registBtn);
        mGetCodeBtn = (TextView) view.findViewById(R.id.getIdentifyCodeBtn);

        mRegistBtn.setOnClickListener(this);
        mGetCodeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mRegistBtn) {
            String phoneNum = mPhoneNumEt.getText().toString();
            String pwd = mPasswordEt.getText().toString();
            String code = mCodeEt.getText().toString();
            if (TextUtils.isEmpty(phoneNum) || phoneNum.length() != 11) {
                Toast.makeText(getActivity(), "请正确输入手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(pwd)) {
                Toast.makeText(getActivity(), "请输入密码", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(code)) {
                Toast.makeText(getActivity(), "请输入验证码", Toast.LENGTH_SHORT).show();
                return;
            }

            register(phoneNum, pwd, code);
        } else if (v == mGetCodeBtn) {
            obtainCode();
        }
    }

    private void obtainCode() {
        if (mGetCoding) {
            return;
        }
        String phoneNum = mPhoneNumEt.getText().toString();
        if (TextUtils.isEmpty(phoneNum) || phoneNum.length() != 11) {
            Toast.makeText(getActivity(), "请正确输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        mGetCoding = true;
        mGetCodeBtn.setText("获取中...");

        String url = createGetCodeUrl(phoneNum);
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                int state = response.optInt("state", -1);
                if (state == 0) {
                    Toast.makeText(getActivity(), "获取验证码成功", Toast.LENGTH_SHORT).show();
                    mGetCodeInterval = 60; // 60s;
                    mHandler.sendEmptyMessageDelayed(1, 1000);
                } else if (state == 1) {
                    String errorMsg = response.optString("errorMsg");
                    Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "获取验证码失败", Toast.LENGTH_SHORT).show();
                }
                mGetCoding = false;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "获取验证码失败", Toast.LENGTH_SHORT).show();
                mGetCoding = false;
            }
        });

        RequestQueueManager.getRequestQueue().add(request);
    }

    private int mGetCodeInterval = 60; // 60s

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                mGetCodeInterval = mGetCodeInterval - 1;
                int value = Math.max(0, mGetCodeInterval);
                if (value == 0) {
                    mGetCodeBtn.setEnabled(true);
//                    mGetCodeBtn.setBackgroundResource(R.drawable.gray_oval_btn_bg);
                    mGetCodeBtn.setText("获取验证码");
                } else {
                    mGetCodeBtn.setEnabled(false);
                    mGetCodeBtn.setText(mGetCodeInterval + "");
                    sendEmptyMessageDelayed(1, 1000);
                }
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHandler.removeMessages(1);
    }

    private void register(String phoneNum, String pwd, String code) {
        if (mRegisting) {
            return;
        }
        mRegisting = true;
        String url = createRegisterUrl(phoneNum, pwd, code);
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                int state = response.optInt("state", -1);
                if (state == 0) {
                    Toast.makeText(getActivity(), "注册成功", Toast.LENGTH_SHORT).show();
                    Activity ac = getActivity();
                    if (ac != null) {
                        LoginRegistActivity lra = (LoginRegistActivity) ac;
                        lra.switchToLoginFragment(false);
                    }
                } else if (state == 1) {
                    String errorMsg = response.optString("errorMsg");
                    Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                }

                mRegisting = false;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "注册失败", Toast.LENGTH_SHORT).show();
                mRegisting = false;
            }
        });

        RequestQueueManager.getRequestQueue().add(request);
    }

    private String createGetCodeUrl(String phoneNum) {
        return UrlBuilder.getUrl(ActionConstants.ACTION_GET_CODE, "?phoneNumber=" + phoneNum);
    }

    private String createRegisterUrl(String phoneNum, String pwd, String code) {
        String params = "?loginname=" + phoneNum + "&password=" + pwd + "&code=" + code;
        return UrlBuilder.getUrl(ActionConstants.ACTION_REGISTER, params);
    }
}
