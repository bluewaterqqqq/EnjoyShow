package com.zmdx.enjoyshow.ui;

import com.zmdx.enjoyshow.utils.LogHelper;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by zhangyan on 15/11/15.
 */
public class ESViewPager extends ViewPager {

    private boolean mDisableSroll = false;

    public ESViewPager(Context context) {
        this(context, null);
    }

    public ESViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setDisableScroll(boolean disable) {
        mDisableSroll = disable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mDisableSroll) {
            LogHelper.d("DetectLookupFragment", "onInterceptTouchEvent");
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mDisableSroll) {
            LogHelper.d("DetectLookupFragment", "onTouchEvent");
            return false;
        }
        return super.onTouchEvent(ev);
    }
}
