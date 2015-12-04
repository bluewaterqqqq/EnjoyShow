/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.zmdx.enjoyshow.main.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zmdx.enjoyshow.user.ESUserManager;

/**
 * Created by zhangyan on 15/12/4.
 */
public class ProfilePagerAdpater extends FragmentPagerAdapter {

    private String[] mTitle = new String[]{"1", "2"};

    private boolean isMine;

    private String mUserId;

    public ProfilePagerAdpater(FragmentManager fm, String userId) {
        super(fm);
        isMine = ESUserManager.getInstance().getCurrentUserId().equals(userId);
        if (isMine) {
            mTitle = new String[]{"我的享秀", "通知", "我关注的", "我的粉丝"};
        } else {
            mTitle = new String[]{"TA的图集", "TA关注的", "TA的粉丝"};
        }

        mUserId = userId;
    }

    @Override
    public Fragment getItem(int position) {
        return isMine ? getItemForMine(position) : getItemForTA(position);
    }

    private Fragment getItemForTA(int position) {
        Fragment frag = null;
        Bundle bundle = new Bundle();
        bundle.putString("userId", mUserId);
        if (position == 0) {
            frag = new UserPhotoSetFragment();
        } else if (position == 1) {
            frag = new UserFollowedFragment();
        } else if (position == 2) {
            frag = new UserFunsFragment();
        } else {
            // 不会执行到
            frag = new UserPhotoSetFragment();
        }
        frag.setArguments(bundle);

        return frag;
    }

    private Fragment getItemForMine(int position) {
        Fragment frag = null;
        Bundle bundle = new Bundle();
        bundle.putString("userId", mUserId);
        if (position == 0) {
            frag = new UserPhotoSetFragment();
        } else if (position == 1) {
            frag = new MsgCenterFragment();
        } else if (position == 2) {
            frag = new UserFollowedFragment();
        } else if (position == 3) {
            frag = new UserFunsFragment();
        }
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public int getCount() {
        return mTitle.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle[position];
    }
}
