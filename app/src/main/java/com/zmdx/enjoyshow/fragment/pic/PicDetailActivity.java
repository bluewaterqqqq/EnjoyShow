package com.zmdx.enjoyshow.fragment.pic;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.common.BaseAppCompatActivity;
import com.zmdx.enjoyshow.network.ActionConstants;
import com.zmdx.enjoyshow.network.UrlBuilder;

/**
 * Created by zhangyan on 15/10/28.
 */
public class PicDetailActivity extends BaseAppCompatActivity {

    private static final String PIC_WIDTH = "280";
    private String mPicSetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent in = getIntent();
        mPicSetId = in.getStringExtra("picSetId");
        if (TextUtils.isEmpty(mPicSetId)) {
            Toast.makeText(this, "打开失败", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setContentView(R.layout.pic_detail_layout);
        initViews();
    }

    private void initViews() {
    }

    private String createUrl() {
        String params = "?pictureSetId=" + mPicSetId + "&w=" + PIC_WIDTH;
        return UrlBuilder.getUrl(ActionConstants.ACTION_VIEW_PICSET, params);
    }
}
