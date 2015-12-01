package com.zmdx.enjoyshow.fragment.detect;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.fragment.detail.ImageDetailActivity;
import com.zmdx.enjoyshow.entity.ESPhoto;
import com.zmdx.enjoyshow.network.ActionConstants;
import com.zmdx.enjoyshow.network.RequestQueueManager;
import com.zmdx.enjoyshow.network.UrlBuilder;
import com.zmdx.enjoyshow.ui.swipecard.SwipeFlingAdapterView;
import com.zmdx.enjoyshow.user.ESUserManager;
import com.zmdx.enjoyshow.utils.LogHelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by zhangyan on 15/11/15.
 */
public class DetectLookupFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "DetectLookupFragment";
    private static final int LIMIT = 15;
    private static final String PIC_WIDTH = "280";

    private FrameLayout mEntireView;

    private ESCardStackAdapter mCardAdapter;

    private List<ESPhoto> mPics = new ArrayList<ESPhoto>();

    private boolean mPulling = false;

    private String mLastId;

    private ImageView mLikeBtn;

    private ImageView mDislikeBtn;

    private SwipeFlingAdapterView mSwipeView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mEntireView == null) {
            mEntireView = (FrameLayout) inflater.inflate(R.layout.detect_lookup_layout, container, false);
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

    private void pullData(boolean older) {
        if (mPulling) {
            return;
        }
        mPulling = true;
        if (older) {
            LogHelper.d(TAG, "开始增量拉取");
        } else {
            LogHelper.d(TAG, "开始下拉刷新");
        }
        final String url = createUrl(older);
        LogHelper.d(TAG, "url:" + url);
        final RequestQueue requestQueue = RequestQueueManager.getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mPulling = false;
                LogHelper.d(TAG, "response:" + response);
                List<ESPhoto> data = parseResponse2Data(response);
                if (data != null && data.size() > 0) {
                    mPics.addAll(data);
                                        mCardAdapter.notifyDataSetChanged();
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
                        mLastId = list.get(list.size() - 1).getOrderId();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return list;
    }

    private String createUrl(boolean older) {
        String lastId = older ? mLastId : "0";
        String params = "?currentUserId=" + ESUserManager.getInstance().getCurrentUserId()
                + "&lastId=" + lastId
                + "&limit=" + LIMIT
                + "&w=" + PIC_WIDTH;
        return UrlBuilder.getUrl(ActionConstants.ACTION_DETECT_LOOKUP, params);
    }

    private void initViews(FrameLayout entireView) {
        mSwipeView = (SwipeFlingAdapterView) entireView.findViewById(R.id.swipeCardView);
        mSwipeView.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                if (dataObject instanceof ESPhoto) {
                    ESPhoto item = (ESPhoto) dataObject;
                    ImageDetailActivity.start(getContext(), item.getId());

                }
            }
        });
        mCardAdapter = new ESCardStackAdapter(getContext(), mPics);
        mSwipeView.setAdapter(mCardAdapter);
        mSwipeView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {

            @Override
            public void removeFirstObjectInAdapter() {
                LogHelper.d(TAG, "removeFirstObjectInAdapter");
                if (mPics.size() > 0) {
                    mPics.remove(0);
                    mCardAdapter.notifyDataSetChanged();
                    if (mPics.size() < 3) {
                        LogHelper.d(TAG, "还剩" + mPics.size() + "卡片,开始增量拉取");
                        pullData(true);
                    }
                }
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                LogHelper.d(TAG, "onLeftCardExit");

            }

            @Override
            public void onRightCardExit(Object dataObject) {
                LogHelper.d(TAG, "onRightCardExit");

            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                LogHelper.d(TAG, "onAdapterAboutToEmpty");

            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });

        mLikeBtn = (ImageView) mEntireView.findViewById(R.id.like);
        mDislikeBtn = (ImageView) mEntireView.findViewById(R.id.dislike);
        mLikeBtn.setOnClickListener(this);
        mDislikeBtn.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        if (v == mLikeBtn) {
            mSwipeView.swipeRight();
        } else if (v == mDislikeBtn) {
            mSwipeView.swipeLeft();
        }
    }
}
