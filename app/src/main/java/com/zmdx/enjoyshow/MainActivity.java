package com.zmdx.enjoyshow;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.zmdx.enjoyshow.common.BaseAppCompatActivity;
import com.zmdx.enjoyshow.fragment.Fragment1;
import com.zmdx.enjoyshow.fragment.Fragment2;
import com.zmdx.enjoyshow.fragment.Fragment4;
import com.zmdx.enjoyshow.fragment.Fragment5;
import com.zmdx.enjoyshow.fragment.publish.PublishActivity;

/**
 * Created by zhangyan on 15/10/26.
 */
public class MainActivity extends BaseAppCompatActivity implements TabHost.OnTabChangeListener {

    private static final String TAB_1_TAG = "tab_1";
    private static final String TAB_2_TAG = "tab_2";
    private static final String TAB_3_TAG = "tab_3";
    private static final String TAB_4_TAG = "tab_4";
    private static final String TAB_5_TAG = "tab_5";

    private FragmentTabHost mTabHost;

    private View[] mIndicators = new View[5];

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        setTitle("照片墙");
    }

    private void initView() {
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        mTabHost.addTab(makeTabSpec(mTabHost, TAB_1_TAG), Fragment1.class, null);
        mTabHost.addTab(makeTabSpec(mTabHost, TAB_2_TAG), Fragment2.class, null);
        mTabHost.addTab(makeTabSpec(mTabHost, TAB_3_TAG), Fragment1.class, null);
        mTabHost.addTab(makeTabSpec(mTabHost, TAB_4_TAG), Fragment4.class, null);
        mTabHost.addTab(makeTabSpec(mTabHost, TAB_5_TAG), Fragment5.class, null);

        mTabHost.setOnTabChangedListener(this);

//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mToolbar.setTitle("照片墙");
//        setSupportActionBar(mToolbar);
    }

    private TabHost.TabSpec makeTabSpec(FragmentTabHost tabHost, @NonNull String tag) {
        TabHost.TabSpec tabSpec = tabHost.newTabSpec(tag);
        ImageView tabImage = null;
        TextView tabText = null;

        if (tag.equals(TAB_1_TAG)) {
            mIndicators[0] = getLayoutInflater().inflate(R.layout.tab_item, null);
            tabImage = (ImageView) mIndicators[0].findViewById(R.id.tab_image);
            tabText = (TextView) mIndicators[0].findViewById(R.id.tab_text);

            tabImage.setImageResource(R.drawable.tab1_item_bg);
            tabText.setText("照片墙");
            tabText.setTextColor(getResources().getColor(R.color.color_main));
            tabSpec.setIndicator(mIndicators[0]);
        } else if (tag.equals(TAB_2_TAG)) {
            mIndicators[1] = getLayoutInflater().inflate(R.layout.tab_item, null);
            tabImage = (ImageView) mIndicators[1].findViewById(R.id.tab_image);
            tabText = (TextView) mIndicators[1].findViewById(R.id.tab_text);

            tabImage.setImageResource(R.drawable.tab2_item_bg);
            tabText.setText("秀吧");
            tabSpec.setIndicator(mIndicators[1]);
        } else if (tag.equals(TAB_3_TAG)) {
            mIndicators[2] = getLayoutInflater().inflate(R.layout.tab_item, null);
            tabImage = (ImageView) mIndicators[2].findViewById(R.id.tab_image);
            tabText = (TextView) mIndicators[2].findViewById(R.id.tab_text);

            tabImage.setImageResource(R.drawable.tab3_item_bg);
            tabText.setText("");
            tabSpec.setIndicator(mIndicators[2]);
            tabImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(MainActivity.this, PublishActivity.class);
                    startActivity(in);
                }
            });
        } else if (tag.equals(TAB_4_TAG)) {
            mIndicators[3] = getLayoutInflater().inflate(R.layout.tab_item, null);
            tabImage = (ImageView) mIndicators[3].findViewById(R.id.tab_image);
            tabText = (TextView) mIndicators[3].findViewById(R.id.tab_text);

            tabImage.setImageResource(R.drawable.tab4_item_bg);
            tabText.setText("发现");
            tabSpec.setIndicator(mIndicators[3]);
        } else if (tag.equals(TAB_5_TAG)) {
            mIndicators[4] = getLayoutInflater().inflate(R.layout.tab_item, null);
            tabImage = (ImageView) mIndicators[4].findViewById(R.id.tab_image);
            tabText = (TextView) mIndicators[4].findViewById(R.id.tab_text);

            tabImage.setImageResource(R.drawable.tab5_item_bg);
            tabText.setText("个人主页");
            tabSpec.setIndicator(mIndicators[4]);
        }
        return tabSpec;
    }

    @Override
    public void onTabChanged(String tag) {
        final Resources res = getResources();
        if (tag.equals(TAB_1_TAG)) {
            for (int i = 0; i < mIndicators.length; i++) {
                if (i == 0) {
                    ((TextView) mIndicators[i].findViewById(R.id.tab_text)).setTextColor(getResources().getColor(R.color.color_main));
                } else {
                    ((TextView) mIndicators[i].findViewById(R.id.tab_text)).setTextColor(Color.WHITE);
                }
            }

            setTitle("照片墙");
        } else if (tag.equals(TAB_2_TAG)) {
            for (int i = 0; i < mIndicators.length; i++) {
                if (i == 1) {
                    ((TextView) mIndicators[i].findViewById(R.id.tab_text)).setTextColor(getResources().getColor(R.color.color_main));
                } else {
                    ((TextView) mIndicators[i].findViewById(R.id.tab_text)).setTextColor(Color.WHITE);
                }
            }
            setTitle("秀吧");
        } else if (tag.equals(TAB_3_TAG)) {
//            for (int i=0;i<mIndicators.length;i++) {
//                if (i == 0) {
//                    ((TextView)mIndicators[i].findViewById(R.id.tab_text)).setTextColor(getResources().getColor(R.color.color_main));
//                } else {
//                    ((TextView)mIndicators[i].findViewById(R.id.tab_text)).setTextColor(Color.WHITE);
//                }
//            }
            setTitle("照片墙");
        } else if (tag.equals(TAB_4_TAG)) {
            for (int i = 0; i < mIndicators.length; i++) {
                if (i == 3) {
                    ((TextView) mIndicators[i].findViewById(R.id.tab_text)).setTextColor(getResources().getColor(R.color.color_main));
                } else {
                    ((TextView) mIndicators[i].findViewById(R.id.tab_text)).setTextColor(Color.WHITE);
                }
            }
            setTitle("发现");
        } else if (tag.equals(TAB_5_TAG)) {
            for (int i = 0; i < mIndicators.length; i++) {
                if (i == 4) {
                    ((TextView) mIndicators[i].findViewById(R.id.tab_text)).setTextColor(getResources().getColor(R.color.color_main));
                } else {
                    ((TextView) mIndicators[i].findViewById(R.id.tab_text)).setTextColor(Color.WHITE);
                }
            }
            setTitle("个人主页");
        }
    }
}
