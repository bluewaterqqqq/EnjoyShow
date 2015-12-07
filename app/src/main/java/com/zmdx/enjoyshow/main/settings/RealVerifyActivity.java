/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.zmdx.enjoyshow.main.settings;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.zmdx.enjoyshow.ESApplication;
import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.common.BaseAppCompatActivity;
import com.zmdx.enjoyshow.entity.ESUser;
import com.zmdx.enjoyshow.network.ActionConstants;
import com.zmdx.enjoyshow.network.RequestQueueManager;
import com.zmdx.enjoyshow.network.UploadRequest;
import com.zmdx.enjoyshow.network.UrlBuilder;
import com.zmdx.enjoyshow.user.ESUserManager;
import com.zmdx.enjoyshow.utils.BaseInfoHelper;
import com.zmdx.enjoyshow.utils.ImageLoaderManager;
import com.zmdx.enjoyshow.utils.ImageLoaderOptionsUtils;
import com.zmdx.enjoyshow.utils.LogHelper;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

/**
 * Created by zhangyan on 15/12/6.
 */
public class RealVerifyActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RealVerifyActivity";
    private ImageView mRealHeadIv;

    private Button mExampleBtn;

    private View mSaveBtn;

    private File mHeadFile;

    private ProgressDialog mDialog;

    private ESUser mUser;

    private EditText mRealNameEt;

    private EditText mAddrEt;

    private EditText mTeleEt;

    private boolean mHeadChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.real_verify_layout);

        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRealNameEt = (EditText) findViewById(R.id.real_name);

        mAddrEt = (EditText) findViewById(R.id.real_addr);

        mTeleEt = (EditText) findViewById(R.id.real_tel);

        mRealHeadIv = (ImageView) findViewById(R.id.real_head);

        mRealHeadIv.setOnClickListener(this);

        mExampleBtn = (Button) findViewById(R.id.real_example);

        mSaveBtn = findViewById(R.id.real_save);

        mExampleBtn.setOnClickListener(this);

        mSaveBtn.setOnClickListener(this);

        mDialog = new ProgressDialog(this);
        mDialog.setCancelable(false);
        mDialog.setMessage("loading...");
        mUser = ESUserManager.getInstance().getCurrentUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<String> photos = null;
        if (resultCode == RESULT_OK && requestCode == 4) {
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
                if (photos != null && photos.size() > 0) {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(Uri.fromFile(new File(photos.get(0))), "image/*");
                    // crop为true是设置在开启的intent中设置显示的view可以剪裁
                    intent.putExtra("crop", "true");

                    // aspectX aspectY 是宽高的比例
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);

                    int size = BaseInfoHelper.dip2px(this, 100);
                    // outputX,outputY 是剪裁图片的宽高
                    intent.putExtra("outputX", size);
                    intent.putExtra("outputY", size);
                    intent.putExtra("return-data", true);

                    try {
                        startActivityForResult(intent, 3);
                    } catch (Exception e) {
                        String filePath = photos.get(0);
                        mHeadFile = new File(filePath);
                        ImageLoaderManager.getImageLoader().displayImage("file://" + filePath, mRealHeadIv,
                                ImageLoaderOptionsUtils.getHeadImageOptions());
                        pushtoHead2Server(mHeadFile);
                    }
                }
            }
        } else if (resultCode == RESULT_OK && requestCode == 3) { // 裁剪完成
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                Bitmap photo = bundle.getParcelable("data");
                mRealHeadIv.setImageBitmap(photo);
                mHeadFile = saveFile(photo, "head" + System.currentTimeMillis());
                pushtoHead2Server(mHeadFile);
            }
        }
    }

    private void pushtoHead2Server(File file) {
        mDialog.show();
        String url = createPushHeadIconUrl();

        UploadRequest request = new UploadRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogHelper.d(TAG, "response:" + response);
                int state = response.optInt("state");
                mHeadChanged = true;
                mDialog.dismiss();
                LogHelper.d(TAG, "真人头像上传成功");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogHelper.d(TAG, "onErrorResponse:" + error.toString());
                Toast.makeText(RealVerifyActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }
        });

        request.addPart("image", file);
        RequestQueueManager.getRequestQueue().add(request);
    }

    private String createPushHeadIconUrl() {
        return UrlBuilder.getUrl(ActionConstants.ACTION_UPLOAD_REAL, null);
    }

    public File saveFile(Bitmap bm, String fileName) {
        String path = ESApplication.getInstance().getExternalCacheDir() + "/tmp/";
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        LogHelper.d(TAG, "create dir path:" + dirFile.getAbsolutePath());
        File myCaptureFile = null;
        try {
            myCaptureFile = new File(path + fileName);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
        }
        return myCaptureFile;
    }

    @Override
    public void onClick(View v) {
        if (v == mRealHeadIv) {
            PhotoPickerIntent intent = new PhotoPickerIntent(this);
            intent.setPhotoCount(1);
            intent.setShowCamera(true);
            startActivityForResult(intent, 4);
        } else if (v == mExampleBtn) {

        } else if (v == mSaveBtn) {
            pushRealInfo2Server();
        }
    }

    private void pushRealInfo2Server() {
        String realName = mRealNameEt.getText().toString();
        String addr = mAddrEt.getText().toString();
        String tel = mTeleEt.getText().toString();

        if (TextUtils.isEmpty(realName)) {
            Toast.makeText(RealVerifyActivity.this, "请输入真实姓名", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(addr)) {
            Toast.makeText(RealVerifyActivity.this, "请输入可到达的地址", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(tel)) {
            Toast.makeText(RealVerifyActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!mHeadChanged) {
            Toast.makeText(RealVerifyActivity.this, "请上传头像", Toast.LENGTH_SHORT).show();
            return;
        }

        mUser.setRealName(realName);
        mUser.setAddr(addr);
        mUser.setTelephone(tel);
        ESUserManager.getInstance().saveUserInfo(mUser, true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您的信息已经提交成功,审核需要一到两个工作日,请耐心等待,谢谢!");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.create().show();
    }
}
