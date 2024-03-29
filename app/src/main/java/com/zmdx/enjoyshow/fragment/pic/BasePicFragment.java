package com.zmdx.enjoyshow.fragment.pic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.entity.ESPhoto;
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
public abstract class BasePicFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = BasePicFragment.class.getSimpleName();

    private static final int LIMIT = 20;

    private static final int PIC_WIDTH = 280;

    private SwipeRefreshLayout mRefreshView;

    private RecyclerView mRecyclerView;

    private GridLayoutManager mLayoutManager;

    private PicAdapter mAdapter;

    private List<ESPhoto> mPics = new ArrayList<ESPhoto>();

    private boolean mPulling = false;

    /**
     * 增量更新时，url中的key为lastId,如果下拉刷新数据，传0，如果增量更新，传数据中的最后一条orderId的值
     */
    private String mLastId = "0";

    private View mEntireView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mEntireView == null) {
            mEntireView = inflater.inflate(R.layout.latest_pic_layout, container, false);
            initViews(mEntireView);
            mPics.clear();
            pullData(false);
        }
        ViewGroup parent = (ViewGroup) mEntireView.getParent();
        if (parent != null) {
            parent.removeView(mEntireView);
        }
        return mEntireView;
    }

    private void initViews(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRefreshView = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 6);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {
                if (position <= 3) {
                    return 3;
                } else {
                    return 2;
                }
            }
        });
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new PicAdapter(getContext(), mPics);
        mRecyclerView.setAdapter(mAdapter);

        mRefreshView.setOnRefreshListener(this);
    }

    private void setSuitableSpanSizeLookup(int count) {
        if (count <= 0) {
            return;
        }
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 4;
                } else{
                    return 2;
                }
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = mLayoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载
                // dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount - 6 && dy > 0) {
                    pullData(true);
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRecyclerView != null) {
            mRecyclerView.clearOnScrollListeners();
        }
    }

    private void pullData(final boolean order) {
        if (mPulling) {
            return;
        }
        mPulling = true;
        if (order) {
            LogHelper.d(TAG, "开始增量拉取");
        } else {
            LogHelper.d(TAG, "开始下拉刷新");
        }
        final String url = createUrl(order);
        LogHelper.d(TAG, "url:" + url);
        final RequestQueue requestQueue = RequestQueueManager.getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mPulling = false;
                if (mRefreshView.isRefreshing()) {
                    mRefreshView.setRefreshing(false);
                }
                LogHelper.d(TAG, "response:" + response);
                List<ESPhoto> data = parseResponse2Data(response);
                if (data != null && data.size() > 0) {
                    if (order) {
                        mAdapter.appendData(data);
                        // 更新lastId
                        mLastId = data.get(data.size() - 1).getOrderId();
                    } else {
                        mAdapter.insertDataAtHeader(data);
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPulling = false;
                if (mRefreshView.isRefreshing()) {
                    mRefreshView.setRefreshing(false);
                }
                LogHelper.e(TAG, "onErrorResponse:" + error.getMessage());
            }
        });

        requestQueue.add(request);
    }

    private List<ESPhoto> parseResponse2Data(JSONObject response) {
        List<ESPhoto> list = null;
        if (response != null) {
            int state = -1;
            try {
                state = response.getInt("state");
                if (state == 0) {
                    JSONObject result = response.getJSONObject("result");
                    JSONArray data = result.optJSONArray("photoSet");
                    if (data != null) {
                        list = new ArrayList<ESPhoto>();
                        list.addAll(ESPhoto.convertListByJSON(data));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return list;
    }

    private String createUrl(boolean order) {
        String lastId = order ? mLastId : "0";
        String params = "?category=" + getCategory()
                + "&currentUserId=" + ESUserManager.getInstance().getCurrentUserId()
                + "&lastId=" + lastId
                + "&limit=" + LIMIT
                + "&w=" + PIC_WIDTH;
        return UrlBuilder.getUrl(ActionConstants.ACTION_QUERY_PHOTO_WALL, params);
    }

    @Override
    public void onRefresh() {
        pullData(false);
    }

    protected abstract String getCategory();
}
