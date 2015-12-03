package com.zmdx.enjoyshow.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.entity.ESUser;
import com.zmdx.enjoyshow.main.profile.ProfilePagerAdpater;
import com.zmdx.enjoyshow.user.ESUserManager;
import com.zmdx.enjoyshow.utils.ImageLoaderManager;
import com.zmdx.enjoyshow.utils.ImageLoaderOptionsUtils;

/**
 * Created by zhangyan on 15/10/26.
 */
public class Fragment5 extends BaseFragment {

    private static final String TAG = "Fragment5";

    private ViewPager mPager;

    private TabLayout mTab;

    private ProfilePagerAdpater mAdapter;

    private Toolbar mToolbar;

    private View mEntireView;

    private View mSettingsBtn;

    private ImageView mHeadBgIv;

    private ImageView mHeadIconIv;

    private TextView mUsernameTv;

    private TextView mAgeTv;

    private boolean isMine = true;

    private ESUser mUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            isMine = bundle.getBoolean("mine", true);
            if (isMine) {
                mUser = ESUserManager.getInstance().getCurrentUser();
            } else {
                mUser = (ESUser) bundle.getSerializable("user");
            }
        } else {
            mUser = ESUserManager.getInstance().getCurrentUser();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mEntireView == null) {
            mEntireView = inflater.inflate(R.layout.tab5_layout, container, false);
            initViews(mEntireView);
            render();
        }
        ViewGroup parent = (ViewGroup) mEntireView.getParent();
        if (parent != null) {
            parent.removeView(mEntireView);
        }
        return mEntireView;
    }

    private void render() {
        mHeadBgIv.setBackgroundColor(Color.BLACK);
        ImageLoaderManager.getImageLoader().displayImage(mUser.getHeadPortrait(), mHeadIconIv,
                ImageLoaderOptionsUtils.getHeadImageOptions());
        mUsernameTv.setText(mUser.getUserName());
        mAgeTv.setText(String.format("年龄: %s", mUser.getAge()+ ""));
    }

    private void initViews(View view) {
        mHeadBgIv = (ImageView) view.findViewById(R.id.profile_head_bg);
        mHeadIconIv = (ImageView) view.findViewById(R.id.profile_headIcon);
        mUsernameTv = (TextView) view.findViewById(R.id.profile_username);
        mAgeTv = (TextView) view.findViewById(R.id.profile_age);

        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mSettingsBtn = view.findViewById(R.id.profile_settings);
        mPager = (ViewPager) view.findViewById(R.id.profile_pager);
        mAdapter = new ProfilePagerAdpater(getChildFragmentManager(), isMine);
        mPager.setAdapter(mAdapter);
        mTab = (TabLayout) view.findViewById(R.id.profile_tabLayout);
        mTab.setupWithViewPager(mPager);
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
