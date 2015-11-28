package com.zmdx.enjoyshow.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.entity.ESPhoto;
import com.zmdx.enjoyshow.entity.ESTheme;
import com.zmdx.enjoyshow.fragment.show.Show8Adapter;
import com.zmdx.enjoyshow.network.ActionConstants;
import com.zmdx.enjoyshow.network.RequestQueueManager;
import com.zmdx.enjoyshow.network.UrlBuilder;
import com.zmdx.enjoyshow.user.ESUserManager;
import com.zmdx.enjoyshow.utils.LogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyan on 15/10/26.
 */
public class Fragment2 extends BaseFragment {

    private static final String TAG = "Fragment2";

    private Toolbar mToolbar;

    private RecyclerView mRecyclerView;

    private Show8Adapter mAdapter;

    private List<ESTheme> mData = new ArrayList<ESTheme>();

    private boolean mPulling = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab2_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.show8_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new Show8Adapter(getContext(), mData);
        mRecyclerView.setAdapter(mAdapter);

        pullData();
        super.onViewCreated(view, savedInstanceState);
    }

    private void pullData() {
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
                    mData.addAll(data);
                    mAdapter.notifyDataSetChanged();
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
        return UrlBuilder.getUrl(ActionConstants.ACTION_QUERY_THEME, null);
    }

    @Override
    protected String getTitle() {
        return "秀吧";
    }

    @Override
    protected Toolbar getToolbar() {
        return mToolbar;
    }
}
