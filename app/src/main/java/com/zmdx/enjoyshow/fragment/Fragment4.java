package com.zmdx.enjoyshow.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.fragment.detect.DetectPagerAdpater;
import com.zmdx.enjoyshow.ui.ESViewPager;

/**
 * Created by zhangyan on 15/10/26.
 */
public class Fragment4 extends BaseFragment implements ViewPager.OnPageChangeListener {

    private Toolbar mToolbar;

    private ESViewPager mPager;

    private DetectPagerAdpater mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab4_layout, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
//        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        TabLayout tab = (TabLayout) view.findViewById(R.id.tabLayout);
        mPager = (ESViewPager) view.findViewById(R.id.viewPager);
        mPager.setDisableScroll(true);
        mPager.addOnPageChangeListener(this);
        mAdapter = new DetectPagerAdpater(getChildFragmentManager(), getContext());
        mPager.setAdapter(mAdapter);
        tab.setupWithViewPager(mPager);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected String getTitle() {
        return "发现";
    }

    @Override
    protected Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
