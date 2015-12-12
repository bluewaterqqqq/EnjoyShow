package com.zmdx.enjoyshow.main.pic;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by zhangyan on 15/10/26.
 */
public class PicFragmentPagerAdpater extends FragmentPagerAdapter {


    public PicFragmentPagerAdpater(FragmentManager manager, Context context) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        if (position == 0) {
            frag = new PicRecommendFragment();
        } else if (position == 1) {
            frag = new FollowFragment();
        }
        return frag;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "推荐";
        } else if (position == 1) {
            return "关注";
        }
        return super.getPageTitle(position);
    }
}
