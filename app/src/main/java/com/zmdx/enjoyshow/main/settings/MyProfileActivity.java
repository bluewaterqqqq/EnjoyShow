/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.zmdx.enjoyshow.main.settings;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zmdx.enjoyshow.ESApplication;
import com.zmdx.enjoyshow.MainActivity;
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

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.drakeet.materialdialog.MaterialDialog;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

/**
 * Created by zhangyan on 15/12/5.
 */
public class MyProfileActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MyProfileActivity";
    private View mHeadLayout;

    private View mNickLayout;

    private View mSexLayout;

    private View mAgeLayout;

    private View mRealVerifyLayout;

    private ImageView mHeadIconIv;

    private TextView mNickNameTv;

    private TextView mSexNameTv;

    private TextView mAgeTv;

    private TextView mRealVerifyStatusTv;

    private ESUser mUser;

    private SweetAlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent in = getIntent();
        mUser = (ESUser) in.getSerializableExtra("user");
        if (mUser == null) {
            return;
        }

        setContentView(R.layout.my_profile);

        initToolbar();

        initViews();

        render();

        mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mDialog.setCancelable(false);
        mDialog.setContentText("更新头像中...");
    }

    private void render() {
        ImageLoaderManager.getImageLoader().displayImage(mUser.getHeadPortrait(), mHeadIconIv,
                ImageLoaderOptionsUtils.getHeadImageOptions());

        mNickNameTv.setText(mUser.getUserName());
        String sexStr = "";
        String sexInt = mUser.getGender();
        if (sexInt.equals("0")) {
            sexStr = "未知";
        } else if (sexInt.equals("1")) {
            sexStr = "男";
        } else if (sexInt.equals("2")) {
            sexStr = "女";
        }
        mSexNameTv.setText(sexStr);
        mAgeTv.setText(mUser.getAge());
        String realStr = "";
        String realStatus = mUser.getIsValidate();
        if (realStatus.equals("0")) {
            realStr = "未验证";
        } else if (realStatus.equals("1")) {
            realStr = "已验证";
        } else if (realStatus.equals("2")) {
            realStr = "验证失败";
        } else if (realStatus.equals("3")) {
            realStr = "审核中";
        }
        mRealVerifyStatusTv.setText(realStr);
    }

    private void initViews() {
        mHeadLayout = findViewById(R.id.my_headLayout);
        mNickLayout = findViewById(R.id.my_nickLayout);
        mSexLayout = findViewById(R.id.my_sexLayout);
        mAgeLayout = findViewById(R.id.my_ageLayout);
        mRealVerifyLayout = findViewById(R.id.my_realVeriyLayout);

        mHeadLayout.setOnClickListener(this);
        mNickLayout.setOnClickListener(this);
        mSexLayout.setOnClickListener(this);
        mAgeLayout.setOnClickListener(this);
        mRealVerifyLayout.setOnClickListener(this);

        mHeadIconIv = (ImageView) findViewById(R.id.my_head);
        mNickNameTv = (TextView) findViewById(R.id.my_nickname);
        mSexNameTv = (TextView) findViewById(R.id.my_sex);
        mAgeTv = (TextView) findViewById(R.id.my_age);
        mRealVerifyStatusTv = (TextView) findViewById(R.id.my_realVerify);


    }

    private void initToolbar() {
        Toolbar tb = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean mInfoChanged = false;

    @Override
    public void onClick(View v) {
        if (v == mHeadLayout) {
            PhotoPickerIntent intent = new PhotoPickerIntent(this);
            intent.setPhotoCount(1);
            intent.setShowCamera(true);
            startActivityForResult(intent, 1);
        } else if (v == mNickLayout) {
            View view = LayoutInflater.from(this).inflate(R.layout.input_dialog_layout, null);
            final EditText input = (EditText) view.findViewById(R.id.modify_nick);
            final MaterialDialog dialog = new MaterialDialog(this);
            dialog.setContentView(view);
            dialog.setPositiveButton("确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str = input.getText().toString();
                    if (TextUtils.isEmpty(str)) {
                        return;
                    }
                    mUser.setUserName(str);
                    dialog.dismiss();
                    mNickNameTv.setText(str);
                    mInfoChanged = true;
                }
            });
            dialog.setNegativeButton("取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else if (v == mSexLayout) {
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_select_age_layout, null);
            final RadioGroup rg = (RadioGroup) view.findViewById(R.id.sexRadioGroup);
            if (mUser.getGender().equals("1")) {
                rg.check(R.id.sex_boy);
            } else if (mUser.getGender().equals("2")) {
                rg.check(R.id.sex_girl);
            }

            final MaterialDialog dialog = new MaterialDialog(this);
            dialog.setContentView(view);
            dialog.setBackground(new ColorDrawable(Color.parseColor("#CFCFCF")));
            dialog.setPositiveButton("确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int checkedId = rg.getCheckedRadioButtonId();
                    if (checkedId == R.id.sex_boy) {
                        mUser.setGender("1");
                        mSexNameTv.setText("男");
                    } else {
                        mUser.setGender("2");
                        mSexNameTv.setText("女");
                    }
                    dialog.dismiss();
                    mInfoChanged = true;
                }
            });
            dialog.setNegativeButton("取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else if (v == mAgeLayout) {
            View view = LayoutInflater.from(this).inflate(R.layout.input_dialog_layout, null);
            final EditText input = (EditText) view.findViewById(R.id.modify_nick);
            input.setHint("输入年龄");
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            final MaterialDialog dialog = new MaterialDialog(this);
            dialog.setContentView(view);

            dialog.setPositiveButton("确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str = input.getText().toString();
                    if (TextUtils.isEmpty(str)) {
                        return;
                    }
                    mUser.setAge(str);
                    dialog.dismiss();
                    mAgeTv.setText(str);
                    mInfoChanged = true;
                }
            });
            dialog.setNegativeButton("取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else if (v == mRealVerifyLayout) {
            Intent in = new Intent(this, RealVerifyActivity.class);
            startActivity(in);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mInfoChanged) {
            ESUserManager.getInstance().saveUserInfo(mUser, true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<String> photos = null;
        if (resultCode == RESULT_OK && requestCode == 1) {
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
                        startActivityForResult(intent, 2);
                    } catch (Exception e) {
                        String filePath = photos.get(0);
                        ImageLoaderManager.getImageLoader().displayImage("file://" + filePath, mHeadIconIv,
                                ImageLoaderOptionsUtils.getHeadImageOptions());
                        pushtoHead2Server(new File(filePath));
                    }
                }
            }
        } else if (resultCode == RESULT_OK && requestCode == 2) { // 裁剪完成
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                Bitmap photo = bundle.getParcelable("data");
                Drawable drawable = new RoundedBitmapDisplayer.RoundedDrawable(photo, 1000, 0);
                mHeadIconIv.setImageDrawable(drawable);

                File file = saveFile(photo, "head" + System.currentTimeMillis());
                if (file != null) {
                    pushtoHead2Server(file);
                }
            }
        }
    }

    /**
     * 保存文件
     *
     * @param bm
     * @param fileName
     * @throws IOException
     */
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

    private String createPushHeadIconUrl() {
        return UrlBuilder.getUrl(ActionConstants.ACTION_UPLOAD_PHOTO, null);
    }

    private boolean mHeadChanged = false;

    private void pushtoHead2Server(File file) {
        mDialog.show();
        String url = createPushHeadIconUrl();

        UploadRequest request = new UploadRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogHelper.d(TAG, "response:" + response);
                int state = response.optInt("state");
                if (state == 0) {
                    String url = response.optJSONObject("result").optString("url");
                    mUser.setHeadPortrait(url);
                    ESUserManager.getInstance().saveUserInfo(mUser, false);
                    Toast.makeText(MyProfileActivity.this, "更改成功", Toast.LENGTH_SHORT).show();
                    mHeadChanged = true;
                } else {
                    Toast.makeText(MyProfileActivity.this, "更改失败" + response.optString("errorMsg"), Toast.LENGTH_SHORT).show();
                }
                mDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogHelper.d(TAG, "onErrorResponse:" + error.toString());
                Toast.makeText(MyProfileActivity.this, "更改失败", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }
        });

        request.addPart("image", file);
        RequestQueueManager.getRequestQueue().add(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHeadChanged || mInfoChanged) {
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(SettingsActivity.ACTION_FINISH_ALL_ACTIVITY));
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
