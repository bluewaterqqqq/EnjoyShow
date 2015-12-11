/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.zmdx.enjoyshow.main.pic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by zhangyan on 15/12/11.
 */
public abstract class BasePicFragment extends Fragment {

    public static final String ACTION_DELETE_PICSET = "ac_del_picset";

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);

    }

    @Override
    public void onPause() {
        super.onPause();
        IntentFilter filter = new IntentFilter(ACTION_DELETE_PICSET);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, filter);
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String picSetId = intent.getStringExtra("picSetId");
            onPicSetDeleted(picSetId);
        }
    };

    protected abstract void onPicSetDeleted(String picSetId);
}
