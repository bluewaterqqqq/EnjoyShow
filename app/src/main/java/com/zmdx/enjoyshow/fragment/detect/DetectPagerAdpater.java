package com.zmdx.enjoyshow.fragment.detect;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by zhangyan on 15/11/15.
 */
public class DetectPagerAdpater extends FragmentPagerAdapter {
    public DetectPagerAdpater(FragmentManager childFragmentManager, Context context) {
        super(childFragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new DetectNewestFragment();
        } else if (position == 1) {
            return new DetectLookupFragment();
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
            return "最新";
        } else if (position == 1) {
            return "随便看看";
        }
        return super.getPageTitle(position);
    }
}
