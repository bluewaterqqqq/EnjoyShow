/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.zmdx.enjoyshow.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.zmdx.enjoyshow.MainActivity;
import com.zmdx.enjoyshow.common.ESConfig;
import com.zmdx.enjoyshow.main.detail.ImageDetailActivity;
import com.zmdx.enjoyshow.main.show.Show8DetailActivity;
import com.zmdx.enjoyshow.protocol.VShowUri;
import com.zmdx.enjoyshow.utils.LogHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by zhangyan on 15/12/7.
 */
public class MyPushReceiver extends BroadcastReceiver {

    private static final String TAG = "MyPushReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        LogHelper.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            LogHelper.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            LogHelper.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            LogHelper.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            LogHelper.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            LogHelper.d(TAG, "[MyReceiver] 用户点击打开了通知");
            if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                LogHelper.i(TAG, "This message has no Extra data");
                return;
            }

            String uri = null;
            try {
                JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                uri = json.optString("scheme");
                LogHelper.d(TAG, "scheme:" + uri);
            } catch (JSONException e) {
                LogHelper.e(TAG, "Get message extra JSON error!");
            }

            if (uri != null) {
                VShowUri vsu = null;
                try {
                    vsu = new VShowUri().parse(uri);
                    openTarget(context, vsu);
                    LogHelper.d(TAG, vsu.toString());
                } catch (VShowUri.VShowParserException e) {
                    if (ESConfig.DEBUG) {
                        e.printStackTrace();
                    }
                }
            } else {
                LogHelper.w(TAG, "uri is null");
            }


        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            LogHelper.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            LogHelper.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            LogHelper.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    private void openTarget(Context context, VShowUri vsu) {
        String action = vsu.getmAction();
        if (!TextUtils.isEmpty(action)) {
            if (action.equals(VShowUri.ACTION_NOTI)) { // 关注,点赞, 跳转到app首页
                //打开自定义的Activity
                Intent i = new Intent(context, MainActivity.class);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            } else if (action.equals(VShowUri.ACTION_THEME)) { // 新主题提醒,跳转到主题详情页
                String themeId = vsu.getmValue();
                if (!TextUtils.isEmpty(themeId)) {
                    Intent in = new Intent(context, Show8DetailActivity.class);
                    in.putExtra("themeId", themeId);
                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(in);
                }
            } else if (action.equals(VShowUri.ACTION_PICDETAIL)) { // 评论, 回复,跳转到图集详情页
                String picSetId = vsu.getmValue();
                if (!TextUtils.isEmpty(picSetId)) {
                    Intent in = new Intent(context, ImageDetailActivity.class);
                    in.putExtra("picSetId", picSetId);
                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(in);
                }
            } else if (action.equals(VShowUri.ACTION_UPDATE)) { // 跳转到升级页面
                String url = vsu.getmValue();
                if (!TextUtils.isEmpty(url)) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Uri uri = Uri.parse(url);
                    intent.setData(uri);
                    context.startActivity(intent);
                }
            } else if (action.equals(VShowUri.ACTION_WEBVIEW)) {
                String url = vsu.getmValue();
                if (!TextUtils.isEmpty(url)) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Uri uri = Uri.parse(url);
                    intent.setData(uri);
                    context.startActivity(intent);
                }
            }
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    LogHelper.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    LogHelper.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
//        if (MainActivity.isForeground) {
//            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//            if (!TextUtils.isEmpty(extras)) {
//                try {
//                    JSONObject extraJson = new JSONObject(extras);
//                    if (null != extraJson && extraJson.length() > 0) {
//                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//                    }
//                } catch (JSONException e) {
//
//                }
//
//            }
//            context.sendBroadcast(msgIntent);
//        }
    }
}
