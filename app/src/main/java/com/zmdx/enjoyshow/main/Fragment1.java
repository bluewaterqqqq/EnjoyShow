package com.zmdx.enjoyshow.main;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.zmdx.enjoyshow.ESApplication;
import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.common.ESPreferences;
import com.zmdx.enjoyshow.entity.ESBullet;
import com.zmdx.enjoyshow.main.login.LogoActivity;
import com.zmdx.enjoyshow.main.pic.PicFragmentPagerAdpater;
import com.zmdx.enjoyshow.main.show.Show8DetailActivity;
import com.zmdx.enjoyshow.network.ActionConstants;
import com.zmdx.enjoyshow.network.RequestQueueManager;
import com.zmdx.enjoyshow.network.UrlBuilder;
import com.zmdx.enjoyshow.protocol.VShowUri;
import com.zmdx.enjoyshow.user.ESUserManager;
import com.zmdx.enjoyshow.utils.ImageLoaderManager;
import com.zmdx.enjoyshow.utils.ImageLoaderOptionsUtils;
import com.zmdx.enjoyshow.utils.LogHelper;

import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by zhangyan on 15/10/26.
 */
public class Fragment1 extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, AppBarLayout.OnOffsetChangedListener {

    private static final String TAG = "Fragment1";

    public static final String ACTION_PULL_REFRESH = "ac_pull_refresh";

    public static final int MODE_DEFAULT = 1;

    public static final int MODE_EXPRIENCE = 2;

    private TabLayout mTab;

    private ViewPager mPager;

    private PicFragmentPagerAdpater mAdapter;

    private Toolbar mToolbar;

    private TopPagerAdapter mTopAdapter;

    private ArrayList<ESBullet> mTopList = new ArrayList<ESBullet>();

    private ViewPager mTopPager;

    private View mEntireView;

    private boolean mPulling = false;

    private LinearLayout mDotContainer;

    private SwipeRefreshLayout mRefreshView;
    private AppBarLayout mAppBarLayout;

    public Fragment1() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mEntireView == null) {
            mEntireView = inflater.inflate(R.layout.tab1_layout, container, false);
            initViews(mEntireView);
        }
        ViewGroup parent = (ViewGroup) mEntireView.getParent();
        if (parent != null) {
            parent.removeView(mEntireView);
        }
        return mEntireView;
    }

    private void initViews(View view) {
        mTab = (TabLayout) view.findViewById(R.id.tabLayout);
        mPager = (ViewPager) view.findViewById(R.id.viewPager);
        mAdapter = new PicFragmentPagerAdpater(getChildFragmentManager(), getContext());
        mPager.setAdapter(mAdapter);
        mTab.setupWithViewPager(mPager);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        initTopPager(view);
        mRefreshView = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        mRefreshView.setOnRefreshListener(this);
        mAppBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);

        pullThemeData();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        //The Refresh must be only active when the offset is zero :
        mRefreshView.setEnabled(i == 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAppBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mAppBarLayout.removeOnOffsetChangedListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void pullThemeData() {
        if (mPulling) {
            return;
        }
        mRefreshView.setRefreshing(true);
        mPulling = true;
        final String url = createUrl();
        LogHelper.d(TAG, "开始增量拉取,url:" + url);
        final RequestQueue requestQueue = RequestQueueManager.getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mPulling = false;
                LogHelper.d(TAG, "response:" + response);
                int state = response.optInt("state");
                if (state == 0) {
                    ArrayList<ESBullet> data = ESBullet.convertByJson(response.optJSONObject("result").optJSONArray("bulletinList"));
                    if (data != null && data.size() > 0) {
                        mTopList.clear();
                        mTopList.addAll(data);
                        initDotContainer();
                        mTopAdapter.notifyDataSetChanged();
                        mTopPager.setCurrentItem(0);
                    }
                }
                mRefreshView.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPulling = false;
                mRefreshView.setRefreshing(false);
                LogHelper.e(TAG, "onErrorResponse:" + error.getMessage());
            }
        });

        requestQueue.add(request);
    }

    private void initDotContainer() {
        Activity activity = getActivity();
        if (activity != null) {
            int length = mTopList.size();
            LayoutInflater inflater = LayoutInflater.from(activity);
            mDotContainer.removeAllViews();
            for (int i = 0; i < length; i++) {
                ImageView dotView = (ImageView) inflater.inflate(R.layout.pager_dot,
                        mDotContainer, false);
                mDotContainer.addView(dotView);
            }
        }

        updateDot(0);
    }

    private String createUrl() {
        return UrlBuilder.getUrl(ActionConstants.ACTION_QUERY_PHOTO_WALL, "?limit=1&category=1");

    }

    private void initTopPager(View view) {
        mTopPager = (ViewPager) view.findViewById(R.id.topViewPager);
        mDotContainer = (LinearLayout) view.findViewById(R.id.dotContainer);
        mTopAdapter = new TopPagerAdapter();
        mTopPager.setAdapter(mTopAdapter);
        mTopPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateDot(position);
            }
        });
    }

    private void updateDot(int position) {
        for (int i = 0, childCount = mDotContainer.getChildCount(); i < childCount; i++) {
            View childView = mDotContainer.getChildAt(i);
            childView.setSelected(i == position);
        }
    }

    @Override
    protected String getTitle() {
        return "照片墙";
    }

    @Override
    protected Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public void onRefresh() {
        pullThemeData();
        LocalBroadcastManager.getInstance(ESApplication.getInstance()).sendBroadcast(new Intent(ACTION_PULL_REFRESH));

    }

    private class TopPagerAdapter extends PagerAdapter {

        private LayoutInflater mInflater;

        public TopPagerAdapter() {
            mInflater = LayoutInflater.from(getActivity());
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = (ImageView) mInflater.inflate(R.layout.top_pager_item, container, false);
            ImageLoaderManager.getImageLoader().displayImage(mTopList.get(position).getImageUrl(), view, ImageLoaderOptionsUtils.getCoverImageOptions());
            container.addView(view);
            view.setTag(position);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ESPreferences.getLoginStatus() == ESUserManager.STATUS_EXPERIENCE) {
                        showExprienceDialog();
                        return;
                    }

                    int position = (Integer) v.getTag();
                        ESBullet esb = mTopList.get(position);
                        String themeId = "";
                        try {
                            VShowUri uri = new VShowUri().parse(esb.getUrl());
                            themeId = uri.getmValue();
                        } catch (VShowUri.VShowParserException e) {
                            return;
                        }

                        if (!TextUtils.isEmpty(themeId)) {
                            Show8DetailActivity.start(v.getContext(), themeId);
                        }

                }
            });
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return mTopList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private void showExprienceDialog() {
        SweetAlertDialog dialog = new SweetAlertDialog(getActivity());
        dialog.setTitleText("提示");
        dialog.setContentText("您还没有登录,登录后能体验更多功能哦!");
        dialog.setConfirmText("确定");
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
            }
        });
        dialog.show();
    }
}
