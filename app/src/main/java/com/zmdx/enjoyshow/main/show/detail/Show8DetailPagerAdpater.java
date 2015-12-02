package com.zmdx.enjoyshow.main.show.detail;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zmdx.enjoyshow.entity.ESPhoto;
import com.zmdx.enjoyshow.entity.ESThemeDetailInfo;
import com.zmdx.enjoyshow.entity.ESUser;

import java.util.ArrayList;

/**
 * Created by zhangyan on 15/11/30.
 */
public class Show8DetailPagerAdpater extends FragmentPagerAdapter {

    private Context mContext;

    private ESThemeDetailInfo mDetailData;

    public Show8DetailPagerAdpater(FragmentManager fragmentManager, Context context, ESThemeDetailInfo detailData) {
        super(fragmentManager);
        mContext = context;
        mDetailData = detailData;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        if (position == 0) {
            frag = new Show8HotestFragment();
            Bundle bundle = new Bundle();
            ArrayList<ESPhoto> hotestData = (ArrayList) mDetailData.getHotestPhotoList();
            bundle.putSerializable("data", hotestData);
            frag.setArguments(bundle);
        } else if (position == 1) {
            frag = new Show8NewestFragment();
            Bundle bundle = new Bundle();
            ArrayList<ESPhoto> newestData = (ArrayList) mDetailData.getNewestPhotoList();
            bundle.putSerializable("data", newestData);
            frag.setArguments(bundle);
        } else if (position == 2) {
            frag = new Show8UserRankFragment();
            Bundle bundle = new Bundle();
            ArrayList<ESUser> userData =(ArrayList) mDetailData.getUserRankList();
            bundle.putSerializable("data", userData);
            bundle.putString("themeId", mDetailData.getmTheme().getmId());
            frag.setArguments(bundle);
        }
        return frag;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "最热";
        } else if (position == 1) {
            return "最新";
        } else if (position == 2) {
            return "排名";
        }
        return super.getPageTitle(position);
    }
}
