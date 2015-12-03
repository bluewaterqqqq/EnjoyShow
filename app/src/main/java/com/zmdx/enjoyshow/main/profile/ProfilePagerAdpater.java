/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.zmdx.enjoyshow.main.profile;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zmdx.enjoyshow.main.detect.DetectNewestFragment;

/**
 * Created by zhangyan on 15/12/4.
 */
public class ProfilePagerAdpater extends FragmentPagerAdapter {

    private String[] mTitle = new String[]{"1", "2"};

    private boolean isMine;

    public ProfilePagerAdpater(FragmentManager fm, boolean me) {
        super(fm);
        if (me) {
            mTitle = new String[]{"我的享秀", "通知", "我关注的", "我的粉丝"};
        } else {
            mTitle = new String[]{"TA的图集", "TA关注的", "TA的粉丝"};
        }

        isMine = me;
    }

    @Override
    public Fragment getItem(int position) {
        return isMine ? getItemForMine(position) : getItemForTA(position);
    }

    private Fragment getItemForTA(int position) {

        return new DetectNewestFragment();
    }

    private Fragment getItemForMine(int position) {
        return new DetectNewestFragment();
    }

    @Override
    public int getCount() {
        return isMine ? 4 : 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle[position];
    }
}
