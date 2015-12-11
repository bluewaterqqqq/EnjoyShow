package com.zmdx.enjoyshow.main.publish;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.common.BaseAppCompatActivity;
import com.zmdx.enjoyshow.entity.ESTheme;
import com.zmdx.enjoyshow.network.ActionConstants;
import com.zmdx.enjoyshow.network.RequestQueueManager;
import com.zmdx.enjoyshow.network.UploadRequest;
import com.zmdx.enjoyshow.network.UrlBuilder;
import com.zmdx.enjoyshow.utils.LogHelper;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

/**
 * Created by zhangyan on 15/12/1.
 */
public class PublishActivity extends BaseAppCompatActivity {

    private static final String TAG = "PublishActivity";

    private EditText mInputEt;

    private RecyclerView mRecyclerView;

    private SelectedImageAdapter mAdapter;

    private List<String> mData = new ArrayList<String>();

    private ProgressDialog mDialog;

    /**
     * 0个人(默认) 1秀场
     */
    private int mType = 0;

    private ESTheme mTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent in = getIntent();
        if (in != null) {
            mType = in.getIntExtra("type", 0);
            mTheme = (ESTheme) in.getSerializableExtra("theme");
        }

        setContentView(R.layout.publish_layout);
        initView();

        PhotoPickerIntent intent = new PhotoPickerIntent(this);
        intent.setPhotoCount(9 - Math.max(0, mData.size() - 1));
        startActivityForResult(intent, SelectedImageAdapter.REQUEST_CODE);
    }

    private void initView() {
        Toolbar tool = (Toolbar) findViewById(R.id.public_toolbar);
        tool.setTitle("选择照片");
        setSupportActionBar(tool);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mInputEt = (EditText) findViewById(R.id.publish_inputDesc);
        findViewById(R.id.publishBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogHelper.d(TAG, "clicked publish button");
                if (TextUtils.isEmpty(mInputEt.getText().toString())) {
                    Toast.makeText(PublishActivity.this, "发送内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                publishImages();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.publish_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new SelectedImageAdapter(this, mData);
        mRecyclerView.setAdapter(mAdapter);

        mDialog = new ProgressDialog(this);
        mDialog.setCancelable(false);
        mDialog.setMessage("发布中...");
    }

    private void dismissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    private void publishImages() {
        mDialog.show();
        String url = createUrl();

        UploadRequest request = new UploadRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogHelper.d(TAG, "response:" + response);
                int state = response.optInt("state");
                if (state == 0) {
                    Toast.makeText(PublishActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                    finish();
                    setResult(RESULT_OK);
                } else {
                    Toast.makeText(PublishActivity.this, "发布失败" + response.optString("errorMsg"), Toast.LENGTH_SHORT).show();
                }
                dismissDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogHelper.d(TAG, "onErrorResponse:" + error.toString());
                Toast.makeText(PublishActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                dismissDialog();
            }
        });

        for (int i = 0; i < mData.size(); i++) {
            if (!mAdapter.isAddView(i)) {
                request.addPart("image", new File(mData.get(i)));
            }
        }
        request.addPart("descs", mInputEt.getText().toString());
        if (mType == 0) {
            request.addPart("type", "0");
        } else if (mType == 1) {
            request.addPart("type", "1");
            request.addPart("themeCycleId", mTheme.getmId());
            request.addPart("themeTitle", mTheme.getmTitle());
        }
        RequestQueueManager.getRequestQueue().add(request);
    }

    private String createUrl() {
        return UrlBuilder.getUrl(ActionConstants.ACTION_UPLOAD_IMAGES, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        List<String> photos = null;
        if (resultCode == RESULT_OK && requestCode == SelectedImageAdapter.REQUEST_CODE) {
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            }

            if (photos != null && photos.size() > 0) {
                mAdapter.appendData(photos);
            }
        }
    }
}

