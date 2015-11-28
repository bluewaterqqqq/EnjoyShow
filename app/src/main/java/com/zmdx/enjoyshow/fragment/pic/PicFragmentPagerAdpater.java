package com.zmdx.enjoyshow.fragment.pic;

import android.content.Context;
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
        if (position == 0) {
            return new PicRecommendFragment();
        } else if (position == 1) {
            return new FollowFragment();
        }
        return null;
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
