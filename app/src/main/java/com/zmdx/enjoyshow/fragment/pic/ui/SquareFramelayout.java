package com.zmdx.enjoyshow.fragment.pic.ui;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by zhangyan on 15/10/27.
 */
public class SquareFramelayout extends CardView {
    public SquareFramelayout(Context context) {
        super(context);
    }

    public SquareFramelayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareFramelayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
