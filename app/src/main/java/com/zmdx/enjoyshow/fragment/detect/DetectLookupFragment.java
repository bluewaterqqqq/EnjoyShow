package com.zmdx.enjoyshow.fragment.detect;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.andtinder.model.Orientations;
import com.andtinder.view.CardContainer;
import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.entity.ESPhoto;
import com.zmdx.enjoyshow.network.ActionConstants;
import com.zmdx.enjoyshow.network.RequestQueueManager;
import com.zmdx.enjoyshow.network.UrlBuilder;
import com.zmdx.enjoyshow.user.ESUserManager;
import com.zmdx.enjoyshow.utils.LogHelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by zhangyan on 15/11/15.
 */
public class DetectLookupFragment extends Fragment {

    private static final String TAG = "DetectLookupFragment";
    private static final int LIMIT = 15;
    private static final String PIC_WIDTH = "280";

    private LinearLayout mEntireView;

    private ESCardStackAdapter mCardAdapter;

    private List<ESPhoto> mPics = new ArrayList<ESPhoto>();

    private boolean mPulling = false;

    private String mLastId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mEntireView == null) {
            mEntireView = (LinearLayout) inflater.inflate(R.layout.detect_lookup_layout, container, false);
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

    private void initViews(LinearLayout entireView) {
        CardContainer cardView = (CardContainer) entireView.findViewById(R.id.cardLayout);
        cardView.setOrientation(Orientations.Orientation.Ordered);
        mCardAdapter = new ESCardStackAdapter(getContext(), mPics);
        cardView.setAdapter(mCardAdapter);
        cardView.setOnCar
        cardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        LogHelper.d(TAG, "down");
                        break;
                    case MotionEvent.ACTION_UP:
                        LogHelper.d(TAG, "up");

                        break;
                    case MotionEvent.ACTION_MOVE:
                        LogHelper.d(TAG, "move");
                    default:
                }
                return false;
            }
        });
        //        cardView.setO
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
