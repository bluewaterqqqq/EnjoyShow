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

import com.zmdx.enjoyshow.main.Fragment1;

/**
 * Created by zhangyan on 15/12/11.
 */
public abstract class BasePicFragment extends Fragment {

    public static final String ACTION_DELETE_PICSET = "ac_del_picset";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        IntentFilter filter = new IntentFilter(ACTION_DELETE_PICSET);
        filter.addAction(Fragment1.ACTION_PULL_REFRESH);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, filter);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_DELETE_PICSET)) {
                String picSetId = intent.getStringExtra("picSetId");
                onPicSetDeleted(picSetId);
            } else if (action.equals(Fragment1.ACTION_PULL_REFRESH)) {
                onRefresh();
            }
        }
    };

    protected abstract void onRefresh();

    protected abstract void onPicSetDeleted(String picSetId);
}
