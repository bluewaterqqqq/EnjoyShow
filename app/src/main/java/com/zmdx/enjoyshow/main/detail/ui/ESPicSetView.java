package com.zmdx.enjoyshow.main.detail.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.entity.ESPicInfo;
import com.zmdx.enjoyshow.utils.ImageLoaderManager;
import com.zmdx.enjoyshow.utils.ImageLoaderOptionsUtils;
import com.zmdx.enjoyshow.utils.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyan on 15/11/28.
 */
public class ESPicSetView extends LinearLayout implements View.OnClickListener {

    private static final String TAG = "ESPicSetView";

    private List<ESPicInfo> mData;

    private List<ImageView> mViews;

    private Context mContext;

    private OnItemClickListener mItemClickListener;

    public ESPicSetView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ESPicSetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public ESPicSetView(Context context, List<ESPicInfo> pics) {
        super(context);
        mContext = context;
        mData = pics;
        initInnterView(mData.size());
    }

    private void initInnterView(int size) {
        int layout = 0;
        if (size <= 0 || size > 9) {
            LogHelper.d(TAG, "图片集数量超出范围[1,9],size=" + size);
            return;
        } else if (size == 1) {
            layout = R.layout.pic_set_view_one;
        } else if (size == 2) {
            layout = R.layout.pic_set_view_two;
        } else if (size == 3) {
            layout = R.layout.pic_set_view_three;
        } else if (size == 4) {
            layout = R.layout.pic_set_view_four;
        } else if (size == 5) {
            layout = R.layout.pic_set_view_five;
        } else if (size == 6) {
            layout = R.layout.pic_set_view_six;
        } else if (size == 7) {
            layout = R.layout.pic_set_view_seven;
        } else if (size == 8) {
            layout = R.layout.pic_set_view_eight;
        } else if (size == 9) {
            layout = R.layout.pic_set_view_nine;
        }
        inflate(mContext, layout, this);
        mViews = new ArrayList<ImageView>(size);
        loadImageViews(this);
        render(mViews, mData);
    }

    private void render(List<ImageView> views, List<ESPicInfo> data) {
        LogHelper.d(TAG, "views.size:" + views.size() + ", data.size:" + data.size());
        for (int i = 0; i < views.size(); i++) {
            ImageView view = views.get(i);
            view.setOnClickListener(this);
            ESPicInfo item = data.get(i);
            ImageLoaderManager.getImageLoader().displayImage(item.getUrl(), view,
                    ImageLoaderOptionsUtils.getCoverImageOptions());
            view.setTag(item);
        }
    }

    private void loadImageViews(ViewGroup vg) {
        int childCount = vg.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = vg.getChildAt(i);
            if (view instanceof ImageView) {
                mViews.add((ImageView) view);
            } else if (view instanceof ViewGroup) {
                loadImageViews((ViewGroup) view);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, ESPicInfo data);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null) {
            ESPicInfo data = (ESPicInfo) v.getTag();
            mItemClickListener.onItemClick(v, data);
        }
    }
}
