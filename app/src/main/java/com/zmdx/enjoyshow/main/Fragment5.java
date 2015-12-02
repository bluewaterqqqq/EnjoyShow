package com.zmdx.enjoyshow.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zmdx.enjoyshow.R;

/**
 * Created by zhangyan on 15/10/26.
 */
public class Fragment5 extends BaseFragment {

    private Toolbar mToolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab5_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        TextView tv = (TextView) view.findViewById(R.id.text);
        tv.setText("什么啊");
        tv.setTextColor(Color.WHITE);
        tv.setBackgroundColor(Color.BLACK);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected String getTitle() {
        return "个人主页";
    }

    @Override
    protected Toolbar getToolbar() {
        return mToolbar;
    }
}
