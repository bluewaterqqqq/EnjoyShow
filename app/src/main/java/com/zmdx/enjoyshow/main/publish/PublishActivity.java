package com.zmdx.enjoyshow.main.publish;

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

/**
 * Created by zhangyan on 15/12/1.
 */
public class PublishActivity extends BaseAppCompatActivity {

    private static final String TAG = "PublishActivity";

    private EditText mInputEt;

    private RecyclerView mRecyclerView;

    private SelectedImageAdapter mAdapter;

    private List<String> mData = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_layout);
        initView();
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
    }

    private void publishImages() {
        String url = createUrl();

        List<File> data = new ArrayList<File>();

        UploadRequest request = new UploadRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogHelper.d(TAG, "response:" + response);
                Toast.makeText(PublishActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogHelper.d(TAG, "onErrorResponse:" + error.toString());
                Toast.makeText(PublishActivity.this, "发布失败", Toast.LENGTH_SHORT).show();

            }
        });

        for (int i = 0; i < mData.size(); i++) {
            if (!mAdapter.isAddView(i)) {
                request.addPart("image"+i, new File(mData.get(i)));
            }
        }
        RequestQueueManager.getRequestQueue().add(request);
    }

    private String createUrl() {
        String params = "?type=0"
                + "&descs=" + mInputEt.getText().toString();
        return UrlBuilder.getUrl(ActionConstants.ACTION_UPLOAD_IMAGES, params);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        List<String> photos = null;
        if (resultCode == RESULT_OK && requestCode == SelectedImageAdapter.REQUEST_CODE) {
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            }

            if (photos != null) {
                mAdapter.appendData(photos);
            }
        }
    }
}

