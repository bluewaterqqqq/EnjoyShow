package com.zmdx.enjoyshow.main;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.entity.ESTheme;
import com.zmdx.enjoyshow.main.pic.PicFragmentPagerAdpater;
import com.zmdx.enjoyshow.main.show.Show8DetailActivity;
import com.zmdx.enjoyshow.network.ActionConstants;
import com.zmdx.enjoyshow.network.RequestQueueManager;
import com.zmdx.enjoyshow.network.UrlBuilder;
import com.zmdx.enjoyshow.utils.ImageLoaderManager;
import com.zmdx.enjoyshow.utils.ImageLoaderOptionsUtils;
import com.zmdx.enjoyshow.utils.LogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyan on 15/10/26.
 */
public class Fragment1 extends BaseFragment {

    private static final String TAG = "Fragment1";
    private TabLayout mTab;

    private ViewPager mPager;

    private PagerAdapter mAdapter;

    private Toolbar mToolbar;

    private TopPagerAdapter mTopAdapter;

    private ArrayList<ESTheme> mTopList = new ArrayList<ESTheme>();

    private ViewPager mTopPager;

    private View mEntireView;

    private boolean mPulling = false;

    private LinearLayout mDotContainer;

    public Fragment1() {
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
        pullThemeData();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void pullThemeData() {
        if (mPulling) {
            return;
        }
        mPulling = true;
        final String url = createUrl();
        LogHelper.d(TAG, "开始增量拉取,url:" + url);
        final RequestQueue requestQueue = RequestQueueManager.getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mPulling = false;
                LogHelper.d(TAG, "response:" + response);
                List<ESTheme> data = parseResponse2Data(response);
                if (data != null && data.size() > 0) {
                    mTopList.addAll(data);
                    initDotContainer();
                    mTopAdapter.notifyDataSetChanged();
                    mTopPager.setCurrentItem(0);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPulling = false;
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

            for (int i = 0; i < length; i++) {
                ImageView dotView = (ImageView) inflater.inflate(R.layout.pager_dot,
                        mDotContainer, false);
                mDotContainer.addView(dotView);
            }
        }

        updateDot(0);
    }

    private List<ESTheme> parseResponse2Data(JSONObject response) {
        List<ESTheme> list = null;
        if (response != null) {
            int state = -1;
            try {
                state = response.getInt("state");
                if (state == 0) {
                    JSONObject result = response.getJSONObject("result");
                    JSONArray data = result.optJSONArray("themeCycle");
                    if (data != null) {
                        list = new ArrayList<ESTheme>();
                        list.addAll(ESTheme.convertListByJSON(data));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return list;
    }

    private String createUrl() {
        return UrlBuilder.getUrl(ActionConstants.ACTION_QUERY_THEME, "?limit=4");

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

    private class TopPagerAdapter extends PagerAdapter {

        private LayoutInflater mInflater;

        public TopPagerAdapter() {
            mInflater = LayoutInflater.from(getActivity());
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = (ImageView) mInflater.inflate(R.layout.top_pager_item, container, false);
            ImageLoaderManager.getImageLoader().displayImage(mTopList.get(position).getmBgUrl(), view, ImageLoaderOptionsUtils.getCoverImageOptions());
            container.addView(view);
            view.setTag(position);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (Integer) v.getTag();
                    Show8DetailActivity.start(v.getContext(), mTopList.get(position));
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
}
