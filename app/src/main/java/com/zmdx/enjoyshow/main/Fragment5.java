package com.zmdx.enjoyshow.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.zmdx.enjoyshow.ESApplication;
import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.entity.ESUser;
import com.zmdx.enjoyshow.main.profile.ProfilePagerAdpater;
import com.zmdx.enjoyshow.main.settings.MyProfileActivity;
import com.zmdx.enjoyshow.main.settings.SettingsActivity;
import com.zmdx.enjoyshow.network.ActionConstants;
import com.zmdx.enjoyshow.network.RequestQueueManager;
import com.zmdx.enjoyshow.network.UrlBuilder;
import com.zmdx.enjoyshow.user.ESUserManager;
import com.zmdx.enjoyshow.utils.BlurUtil;
import com.zmdx.enjoyshow.utils.ImageLoaderManager;
import com.zmdx.enjoyshow.utils.ImageLoaderOptionsUtils;
import com.zmdx.enjoyshow.utils.LogHelper;

import org.json.JSONObject;

import java.util.ArrayList;

import me.iwf.photopicker.PhotoPagerActivity;

/**
 * Created by zhangyan on 15/10/26.
 */
public class Fragment5 extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "Fragment5";

    private ViewPager mPager;

    private TabLayout mTab;

    private ProfilePagerAdpater mAdapter;

    private Toolbar mToolbar;

    private View mEntireView;

    private View mSettingsBtn;

    private ImageView mHeadBgIv;

    private ImageView mHeadIconIv;

    private TextView mUsernameTv;

    private TextView mAgeTv;

    private TextView mFollowBtn;

    private ImageView mSexIv;

    private ESUser mUser;

    private boolean mIsMine = true;

    private boolean needShowSettings = false;

    private String mUserId;

    private boolean mShowBack = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mUserId = bundle.getString("userId");
            mShowBack = bundle.getBoolean("back", true);
            mIsMine = mUserId.equals(ESUserManager.getInstance().getCurrentUserId());
        } else { // 首页进入第5个tab,一定是用户本人的个人信息页,所以取登录用户的信息
            mUserId = ESUserManager.getInstance().getCurrentUserId();
            needShowSettings = true;
            mShowBack = false;
            mIsMine = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mEntireView == null) {
            mEntireView = inflater.inflate(R.layout.tab5_layout, container, false);
            initViews(mEntireView);
//            render();
            pullUserDetailInfo();
        }
        ViewGroup parent = (ViewGroup) mEntireView.getParent();
        if (parent != null) {
            parent.removeView(mEntireView);
        }
        return mEntireView;
    }

    private void pullUserDetailInfo() {
        final String url = createUrl();
        LogHelper.d(TAG, "url:" + url);
        final RequestQueue requestQueue = RequestQueueManager.getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogHelper.d(TAG, "response:" + response);
                int state = response.optInt("state");
                if (state == 0) {
                    JSONObject obj = response.optJSONObject("result");
                    String userObj = obj.optString("user");
                    mUser = ESUser.convertByJSON(userObj);
                    render();
                    renderFollowButton();
                    ESUserManager.getInstance().saveUserInfo(mUser, true);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogHelper.e(TAG, "onErrorResponse:" + error.getMessage());
            }
        });

        requestQueue.add(request);
    }

    private void renderFollowButton() {
        if (mIsMine) {
            mFollowBtn.setVisibility(View.GONE);
        } else {
            if (mUser.isAttention()) {
                mFollowBtn.setText("取消关注");
                mFollowBtn.setTextColor(Color.parseColor("#ffffbb33"));
                mFollowBtn.setBackgroundResource(R.drawable.white_btn_bg);
                mFollowBtn.setVisibility(View.VISIBLE);
            } else {
                mFollowBtn.setText("关注");
                mFollowBtn.setBackgroundResource(R.drawable.yellow_btn_bg);
                mFollowBtn.setTextColor(Color.WHITE);
                mFollowBtn.setVisibility(View.VISIBLE);
            }
            mFollowBtn.setOnClickListener(this);
        }
    }

    private String createUrl() {
        String userId = mUser == null ? mUserId : mUser.getId() + "";
        return UrlBuilder.getUrl(ActionConstants.ACTION_USER_INFO, "?userId=" + userId);
    }

    private void render() {
        if (mUser == null) {
            return;
        }
        mHeadIconIv.setOnClickListener(this);
        ImageLoaderManager.getImageLoader().displayImage(mUser.getHeadPortrait(), mHeadIconIv,
                ImageLoaderOptionsUtils.getHeadImageOptions(), new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        Bitmap blurBmp = BlurUtil.fastblur(ESApplication.getInstance(), loadedImage, 15);
                        if (blurBmp != null) {
                            mHeadBgIv.setImageBitmap(blurBmp);
                        } else {
                            mHeadBgIv.setBackgroundColor(Color.BLACK);
                        }
                    }
                });
        mUsernameTv.setText(mUser.getUserName());
        mAgeTv.setText(String.format("年龄: %s", mUser.getAge() + ""));

        // 初始化 设置 入口
        if (needShowSettings) {
            mSettingsBtn.setOnClickListener(this);
        } else {
            mSettingsBtn.setVisibility(View.GONE);
        }

        // 初始化性别
        String sex = mUser.getGender();
        if (sex.equals("0")) {
            mSexIv.setVisibility(View.GONE);
        } else if (sex.equals("1")) {
            mSexIv.setVisibility(View.VISIBLE);
            mSexIv.setImageResource(R.drawable.male_icon);
        } else if (sex.equals("2")) {
            mSexIv.setVisibility(View.VISIBLE);
            mSexIv.setImageResource(R.drawable.female_icon);
        }
    }

    private void initViews(View view) {
        if (mShowBack) {
            Toolbar tb = (Toolbar) view.findViewById(R.id.toolbar);
            final AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.setSupportActionBar(tb);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            tb.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                }
            });
        }

        mHeadBgIv = (ImageView) view.findViewById(R.id.profile_head_bg);
        mHeadIconIv = (ImageView) view.findViewById(R.id.profile_headIcon);
        mUsernameTv = (TextView) view.findViewById(R.id.profile_username);
        mAgeTv = (TextView) view.findViewById(R.id.profile_age);
        mFollowBtn = (TextView) view.findViewById(R.id.profile_follow_btn);
        mSexIv = (ImageView) view.findViewById(R.id.profile_gender);

        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mSettingsBtn = view.findViewById(R.id.profile_settings);

        mPager = (ViewPager) view.findViewById(R.id.profile_pager);
        mAdapter = new ProfilePagerAdpater(getChildFragmentManager(), mUserId);
        mPager.setAdapter(mAdapter);
        mTab = (TabLayout) view.findViewById(R.id.profile_tabLayout);
        mTab.setupWithViewPager(mPager);
    }


    @Override
    protected String getTitle() {
        return "个人主页";
    }

    @Override
    protected Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public void onClick(View v) {
        if (v == mSettingsBtn) {
            getActivity().startActivity(new Intent(getActivity(), SettingsActivity.class));
        } else if (v == mHeadIconIv) {
            if (mUser != null) {
                if (mIsMine) {
                    Intent in = new Intent(getActivity(), MyProfileActivity.class);
                    in.putExtra("user", mUser);
                    getActivity().startActivity(in);
                } else {
                    ArrayList<String> data = new ArrayList<String>();
                    data.add(mUser.getHeadPortrait());
                    Intent intent = new Intent(getActivity(), PhotoPagerActivity.class);
                    intent.putExtra(PhotoPagerActivity.EXTRA_CURRENT_ITEM, 0);
                    intent.putExtra(PhotoPagerActivity.EXTRA_PHOTOS, data);
                    intent.putExtra(PhotoPagerActivity.EXTRA_SHOW_DELETE, false);
                    startActivity(intent);
                }
            }
        } else if (v == mFollowBtn) {
            if (mUser.isAttention()) {
                cancelAttention();
            } else {
                attention();
            }
        }
    }

    private void cancelAttention() {
        mFollowBtn.setText("取消中...");
        mFollowBtn.setEnabled(false);
        String url = UrlBuilder.getUrl(ActionConstants.ACTION_UNFOLLOW, "?userId=" + mUserId);
        final RequestQueue requestQueue = RequestQueueManager.getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogHelper.d(TAG, "response:" + response);
                int state = response.optInt("state");
                if (state == 0) {
                    LogHelper.d(TAG, "取消关注成功");
                    mUser.setIsAttention(false);
                    renderFollowButton();
                }
                mFollowBtn.setEnabled(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogHelper.e(TAG, "onErrorResponse:" + error.getMessage());
                mFollowBtn.setEnabled(true);
                Toast.makeText(ESApplication.getInstance(), "取消关注失败,请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(request);

    }

    private void attention() {
        mFollowBtn.setEnabled(false);
        mFollowBtn.setText("关注中...");
        String url = UrlBuilder.getUrl(ActionConstants.ACTION_FOLLOW, "?userId=" + mUserId);
        final RequestQueue requestQueue = RequestQueueManager.getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogHelper.d(TAG, "response:" + response);
                int state = response.optInt("state");
                if (state == 0) { // 关注成功
                    LogHelper.d(TAG, "关注成功");
                    mUser.setIsAttention(true);
                    renderFollowButton();
                }
                mFollowBtn.setEnabled(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogHelper.e(TAG, "onErrorResponse:" + error.getMessage());
                mFollowBtn.setEnabled(true);
                Toast.makeText(ESApplication.getInstance(), "关注失败,请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(request);
    }
}
