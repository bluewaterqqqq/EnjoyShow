package com.zmdx.enjoyshow.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.fragment.pic.PicFragmentPagerAdpater;

/**
 * Created by zhangyan on 15/10/26.
 */
public class Fragment1 extends BaseFragment {

    private TabLayout mTab;

    private ViewPager mPager;

    private PagerAdapter mAdapter;

    private Toolbar mToolbar;

    public Fragment1() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab1_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mTab = (TabLayout) view.findViewById(R.id.tabLayout);
        mPager = (ViewPager) view.findViewById(R.id.viewPager);
        mAdapter = new PicFragmentPagerAdpater(getChildFragmentManager(), getContext());
        mPager.setAdapter(mAdapter);
        mTab.setupWithViewPager(mPager);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected String getTitle() {
        return "照片墙";
    }

    @Override
    protected Toolbar getToolbar() {
        return mToolbar;
    }
}
