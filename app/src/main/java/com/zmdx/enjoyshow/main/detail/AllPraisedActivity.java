package com.zmdx.enjoyshow.main.detail;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.common.BaseAppCompatActivity;
import com.zmdx.enjoyshow.entity.ESPhoto;
import com.zmdx.enjoyshow.entity.ESUser;
import com.zmdx.enjoyshow.main.adapter.UserListAdapter;
import com.zmdx.enjoyshow.network.ActionConstants;
import com.zmdx.enjoyshow.network.RequestQueueManager;
import com.zmdx.enjoyshow.network.UrlBuilder;
import com.zmdx.enjoyshow.utils.LogHelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by zhangyan on 15/11/22.
 */
public class AllPraisedActivity extends BaseAppCompatActivity {

    private static final String TAG = "AllPraisedActivity";
    private String mPicSetId;

    private RecyclerView mRecyclerView;

    private ArrayList<ESUser> mData = new ArrayList<>();

    private UserListAdapter mAdapter;
    private String mLastId = "0";
    private boolean mPulling = false;
    private LinearLayoutManager mLayoutManager;

    public static void start(Context context, String picSetId) {
        Intent in = new Intent(context, AllPraisedActivity.class);
        in.putExtra("picSetId", picSetId);
        context.startActivity(in);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent in = getIntent();
        if (in != null) {
            mPicSetId = in.getStringExtra("picSetId");
        }
        if (TextUtils.isEmpty(mPicSetId)) {
            return;
        }

        setContentView(R.layout.all_praised);
        initToolbar();

        mRecyclerView = (RecyclerView) findViewById(R.id.allPraisedRecyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new UserListAdapter(this, mData);
        mRecyclerView.setAdapter(mAdapter);

        pullData();
    }

    private String createUrl() {
        String params = "?pictureSetId=" + mPicSetId + "&limit=20&lastId=" + mLastId;
        return UrlBuilder.getUrl(ActionConstants.ACTION_LOAD_ALLPRAISE, params);
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

    @Override
    public void onPause() {
        super.onPause();
        if (mRecyclerView != null) {
            mRecyclerView.clearOnScrollListeners();
        }
    }

    private void pullData() {
        if (mPulling) {
            return;
        }
        mPulling = true;
        LogHelper.d(TAG, "开始下拉刷新");
        final String url = createUrl();
        LogHelper.d(TAG, "url:" + url);
        final RequestQueue requestQueue = RequestQueueManager.getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mPulling = false;
                LogHelper.d(TAG, "response:" + response);
                List<ESUser> data = parseResponse2Data(response);
                if (data != null && data.size() > 0) {
                    mAdapter.appendData(data);
                    mLastId = data.get(data.size() - 1).getOrderId();
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

    private List<ESUser> parseResponse2Data(JSONObject response) {
        ArrayList<ESUser> result = new ArrayList<>();
        int state = response.optInt("state");
        if (state == 0) {
            JSONObject obj = response.optJSONObject("result");
            JSONArray list = obj.optJSONArray("praiseUsers");
            result.addAll(ESUser.convertByJSONArray(list));
        }
        return result;
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("点赞列表");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static class PraiseHolder extends RecyclerView.ViewHolder {

        public ImageView headerIv;
        public TextView userNameTv;
        public TextView ageTv;
        public View genderView;

        public PraiseHolder(View itemView) {
            super(itemView);
            headerIv = (ImageView) itemView.findViewById(R.id.headIv);
            userNameTv = (TextView) itemView.findViewById(R.id.titleTv);
            ageTv = (TextView) itemView.findViewById(R.id.ageTv);
            genderView = itemView.findViewById(R.id.genderView);
        }
    }
}
