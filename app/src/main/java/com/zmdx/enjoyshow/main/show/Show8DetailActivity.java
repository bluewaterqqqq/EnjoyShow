package com.zmdx.enjoyshow.main.show;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.common.BaseAppCompatActivity;
import com.zmdx.enjoyshow.entity.ESBullet;
import com.zmdx.enjoyshow.entity.ESTheme;
import com.zmdx.enjoyshow.entity.ESThemeDetailInfo;
import com.zmdx.enjoyshow.main.show.detail.Show8DetailPagerAdpater;
import com.zmdx.enjoyshow.network.ActionConstants;
import com.zmdx.enjoyshow.network.RequestQueueManager;
import com.zmdx.enjoyshow.network.UrlBuilder;
import com.zmdx.enjoyshow.protocol.VShowParser;
import com.zmdx.enjoyshow.utils.ImageLoaderManager;
import com.zmdx.enjoyshow.utils.ImageLoaderOptionsUtils;
import com.zmdx.enjoyshow.utils.LogHelper;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhangyan on 15/11/29.
 */
public class Show8DetailActivity extends BaseAppCompatActivity {

    private static final String TAG = "Show8DetailActivity";

    private ESThemeDetailInfo mDetailData;

    private ViewPager mPager;

    private Show8DetailPagerAdpater mAdapter;

    private String mThemeId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show8_detail);
        Intent from = getIntent();
        if (from != null) {
//            ESBullet bullet = (ESBullet) from.getSerializableExtra("themeData");
//            mThemeId = VShowParser.parseThemeIdBy(bullet.getUrl());
            mThemeId = from.getStringExtra("themeId");
            if (mThemeId == null) {
                finish();
                return;
            }
        } else {
            finish();
            return;
        }

        // 拉取3个tab页的数据
        pullData();
    }

    private void pullData() {
        String url = createUrl();
        LogHelper.d(TAG, "url:" + url);
        final RequestQueue requestQueue = RequestQueueManager.getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogHelper.d(TAG, "response:" + response);
                mDetailData = parseResponse2Data(response);
                if (mDetailData == null) {
                    return;
                }
                // 初始化tab之上部分的界面
                initBaseViews(mDetailData.getmTheme());
                initContent();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogHelper.e(TAG, "onErrorResponse:" + error.getMessage());
            }
        });

        requestQueue.add(request);
    }

    private ESThemeDetailInfo parseResponse2Data(JSONObject response) {
        int state = response.optInt("state");
        if (state == 0) {
            return ESThemeDetailInfo.convertByJSON(response.optJSONObject("result"));
        }
        return null;
    }

    // 初始化页面下方的3个tab
    private void initContent() {
        // init tab views
        TabLayout tab = (TabLayout) findViewById(R.id.themeDetail_tabLayout);
        mPager = (ViewPager) findViewById(R.id.themeDetail_pager);
        mAdapter = new Show8DetailPagerAdpater(getSupportFragmentManager(), this, mDetailData);
        mPager.setAdapter(mAdapter);
        tab.setupWithViewPager(mPager);
    }

    private String createUrl() {
        String params = "?themeCycleId=" + mThemeId +
                "&limit=15";
        return UrlBuilder.getUrl(ActionConstants.ACTION_THEME_DETAIL, params);
    }

    private void initBaseViews(final ESTheme data) {
        // init toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(data.getmTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // init cover image
        ImageView coverView = (ImageView) findViewById(R.id.show8_detail_cover);
        ImageLoaderManager.getImageLoader().displayImage(data.getInsideBgUrl(), coverView, ImageLoaderOptionsUtils.getCoverImageOptions());

        TextView contentTv = (TextView) findViewById(R.id.show8_detail_content);
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        contentTv.setText(format.format(new Date(data.getmStartTime())) + " -- " + format.format(new Date(data.getmEndTime())));

        TextView descTv = (TextView) findViewById(R.id.show8_detail_desc);
        descTv.setText(data.getmDesc());

        final TextView extendBtn = (TextView) findViewById(R.id.show8_detail_extendBtn);
        extendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = LayoutInflater.from(Show8DetailActivity.this).inflate(R.layout.show8_detail_expand_desc_layout, null);
                TextView descTv = (TextView) dialogView.findViewById(R.id.show8_detail_desc);
                descTv.setText(data.getmDesc());

                TextView ruleTv = (TextView) dialogView.findViewById(R.id.show8_detail_rule);
                ruleTv.setText(data.getRule());

                TextView durationTv = (TextView) dialogView.findViewById(R.id.show8_detail_duration);
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy.MM.dd HH:mm");
                durationTv.setText(format1.format(new Date(data.getmStartTime())) + " -- " + format1.format(new Date(data.getmEndTime())));

                TextView awardTv = (TextView) dialogView.findViewById(R.id.show8_detail_award);
                awardTv.setText(data.getAwardSetting());
                new AlertDialog.Builder(Show8DetailActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert).setView(dialogView).setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });
    }

    public static void start(Context context, String themeId) {
        Intent in = new Intent(context, Show8DetailActivity.class);
        in.putExtra("themeId", themeId);
        context.startActivity(in);
    }
}
