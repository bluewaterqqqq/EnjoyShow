/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.zmdx.enjoyshow.main.settings;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zmdx.enjoyshow.ESApplication;
import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.common.BaseAppCompatActivity;
import com.zmdx.enjoyshow.utils.BaseInfoHelper;
import com.zmdx.enjoyshow.utils.ImageLoaderManager;
import com.zmdx.enjoyshow.utils.ImageLoaderOptionsUtils;
import com.zmdx.enjoyshow.utils.LogHelper;

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

        mRealHeadIv = (ImageView) findViewById(R.id.real_head);

        mRealHeadIv.setOnClickListener(this);

        mExampleBtn = (Button) findViewById(R.id.real_example);

        mSaveBtn = findViewById(R.id.real_save);

        mExampleBtn.setOnClickListener(this);

        mSaveBtn.setOnClickListener(this);
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
//                        pushtoHead2Server(new File(filePath));
                    }
                }
            }
        } else if (resultCode == RESULT_OK && requestCode == 3) { // 裁剪完成
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                Bitmap photo = bundle.getParcelable("data");
                mRealHeadIv.setImageBitmap(photo);

                mHeadFile = saveFile(photo, "head" + System.currentTimeMillis());
            }
        }
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

        }
    }
}
