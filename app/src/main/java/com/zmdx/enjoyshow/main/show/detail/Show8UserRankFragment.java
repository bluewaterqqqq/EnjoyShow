package com.zmdx.enjoyshow.main.show.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.entity.ESUser;
import com.zmdx.enjoyshow.main.adapter.UserListAdapter;
import com.zmdx.enjoyshow.network.ActionConstants;
import com.zmdx.enjoyshow.network.RequestQueueManager;
import com.zmdx.enjoyshow.network.UrlBuilder;
import com.zmdx.enjoyshow.utils.LogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baidu on 15/11/30.
 */
public class Show8UserRankFragment extends Fragment {

    private static final String TAG = "Show8UserRankFragment";

    private ArrayList<ESUser> mData;

    private String mLastId;

    private String mThemeId;

    private View mEntireView;

    private LinearLayoutManager mLayoutManager;

    private UserListAdapter mAdapter;

    private RecyclerView mRecyclerView;

    private boolean mPulling = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mData = (ArrayList<ESUser>) bundle.getSerializable("data");
        mThemeId = bundle.getString("themeId");
        if (mData != null) {
            if (mData.size() > 0) {
                mLastId = mData.get(mData.size() - 1).getOrderId();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mEntireView == null) {
            mEntireView = inflater.inflate(R.layout.show8_detail_fragment_rank_layout, container, false);
            initViews(mEntireView);
        }
        ViewGroup parent = (ViewGroup) mEntireView.getParent();
        if (parent != null) {
            parent.removeView(mEntireView);
        }
        return mEntireView;
    }

    private void initViews(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.userRankRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new UserListAdapter(view.getContext(), mData);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = mLayoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 2 表示剩下2个item自动加载
                // dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount - 4 && dy > 0) {
                    pullData();
                }
            }
        });
    }

    private void pullData() {
        if (mPulling) {
            return;
        }
        mPulling = true;
        LogHelper.d(TAG, "开始增量拉取");
        final String url = createUrl();
        LogHelper.d(TAG, "url:" + url);
        final RequestQueue requestQueue = RequestQueueManager.getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogHelper.d(TAG, "response:" + response);
                List<ESUser> data = parseResponse2Data(response);
                if (data != null && data.size() > 0) {
                    mAdapter.appendData(data);
                    // 更新lastId
                    mLastId = data.get(data.size() - 1).getOrderId();
                }
                mPulling = false;
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

    private List<ESUser> parseResponse2Data(JSONObject response) {
        List<ESUser> list = null;
        if (response != null) {
            int state = -1;
            try {
                state = response.getInt("state");
                if (state == 0) {
                    JSONObject result = response.getJSONObject("result");
                    JSONArray data = result.optJSONArray("user");
                    if (data != null) {
                        list = new ArrayList<ESUser>();
                        list.addAll(ESUser.convertByJSONArray(data));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private String createUrl() {
        String params = "?themeCycleId=" + mThemeId +
                "&limit=21&lastId=" + mLastId;
        return UrlBuilder.getUrl(ActionConstants.ACTION_THEME_USERRANK, params);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRecyclerView != null) {
            mRecyclerView.clearOnScrollListeners();
        }
    }
}
