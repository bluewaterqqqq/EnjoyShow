package com.zmdx.enjoyshow.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.main.detect.DetectLookupFragment;
import com.zmdx.enjoyshow.main.detect.DetectNewestFragment;
import com.zmdx.enjoyshow.main.detect.DetectPagerAdpater;
import com.zmdx.enjoyshow.ui.ESViewPager;

/**
 * Created by zhangyan on 15/10/26.
 */
public class Fragment4 extends BaseFragment implements ViewPager.OnPageChangeListener {

    private Toolbar mToolbar;

    private ESViewPager mPager;

    private DetectPagerAdpater mAdapter;

    private Fragment mLatestFragment;

    private Fragment mLookupFragment;

    private FrameLayout mContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab4_layout, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
//        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mContainer = (FrameLayout) view.findViewById(R.id.container);
        TabLayout tab = (TabLayout) view.findViewById(R.id.tabLayout);
        tab.addTab(tab.newTab().setText("最新").setTag("1"));
        tab.addTab(tab.newTab().setText("随便看看").setTag("2"));

        tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tag = (String) tab.getTag();
                if (TextUtils.isEmpty(tag)) {
                    return;
                }
                if (tag.equals("1")) {
                    if (mLatestFragment == null) {
                        mLatestFragment = new DetectNewestFragment();
                    }
                    getChildFragmentManager().beginTransaction().replace(R.id.container, mLatestFragment).commit();

                } else if (tag.equals("2")) {
                    if (mLookupFragment == null) {
                        mLookupFragment = new DetectLookupFragment();
                    }
                    getChildFragmentManager().beginTransaction().replace(R.id.container, mLookupFragment).commit();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if (mLatestFragment == null) {
            mLatestFragment = new DetectNewestFragment();
        }
        getChildFragmentManager().beginTransaction().replace(R.id.container, mLatestFragment).commit();
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
