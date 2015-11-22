package com.zmdx.enjoyshow.fragment.detail;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.common.BaseAppCompatActivity;
import com.zmdx.enjoyshow.entity.ESComment;
import com.zmdx.enjoyshow.entity.ESPhotoSet;
import com.zmdx.enjoyshow.entity.ESUser;
import com.zmdx.enjoyshow.entity.PraiseInfo;
import com.zmdx.enjoyshow.network.ActionConstants;
import com.zmdx.enjoyshow.network.RequestQueueManager;
import com.zmdx.enjoyshow.network.UrlBuilder;
import com.zmdx.enjoyshow.user.ESUserManager;
import com.zmdx.enjoyshow.utils.ESDateFormat;
import com.zmdx.enjoyshow.utils.ImageLoaderManager;
import com.zmdx.enjoyshow.utils.ImageLoaderOptionsUtils;
import com.zmdx.enjoyshow.utils.LogHelper;
import com.zmdx.enjoyshow.utils.UIUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by zhangyan on 15/11/21.
 */
public class ImageDetailActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private static final int PIC_WIDTH = 650;
    private static String TAG = "ImageDetailActivity";
    private boolean mPulling = false;

    private FrameLayout mPicSetLayout;

    private String mPicSetId;
    private ViewGroup mPraiseLayout;
    private ImageView mPraiseImage;
    private TextView mPraiseNumTv;
    private ImageView mUserHeader;
    private TextView mUserName;
    private TextView mPostTime;
    private TextView mDescTv;
    private RecyclerView mCommentRecyclerView;
    private ESPhotoSet mData;
    private EditText mCommentEt;
    private Button mSendCommentBtn;
    private int mCommentTargetId = -1;//回复评论,对方的userId
    private CommentAdapter mCommentAdapter;

    public static void start(Context context, String pictureSetId) {
        Intent in = new Intent(context, ImageDetailActivity.class);
        in.putExtra("picSetId", pictureSetId);
        context.startActivity(in);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent in = getIntent();
        mPicSetId = in.getStringExtra("picSetId");
        if (TextUtils.isEmpty(mPicSetId)) {
            Toast.makeText(ImageDetailActivity.this, "打开失败", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setContentView(R.layout.image_detail);

        initToolbar();

        pullData();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("图片详情");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void pullData() {
        if (mPulling) {
            return;
        }
        mPulling = true;
        final String url = createDetailUrl();
        LogHelper.d(TAG, "url:" + url);
        final RequestQueue requestQueue = RequestQueueManager.getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mPulling = false;
                LogHelper.d(TAG, "response:" + response);
                mData = parseResponse2Data(response);
                if (mData != null) {
                    render(mData);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPulling = false;
                LogHelper.e(TAG, "onErrorResponse:" + error.getMessage());
                Toast.makeText(ImageDetailActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        requestQueue.add(request);
    }

    private void render(ESPhotoSet data) {
        initPicsView(data);
        initUserInfoView(data);
        initPraiseListView(data);
        initCommentView(data);
    }

    private ESPhotoSet parseResponse2Data(JSONObject response) {
        ESPhotoSet data = null;
        if (response != null) {
            try {
                int state = response.getInt("state");
                if (state == 0) {
                    JSONObject result = response.optJSONObject("result");
                    data = ESPhotoSet.convertByJSON(result);
                }
            } catch (JSONException e) {
                return data;
            }
        }
        return data;
    }

    private String createDetailUrl() {
        String params = "?pictureSetId=" + mPicSetId
                + "&w=" + PIC_WIDTH;
        return UrlBuilder.getUrl(ActionConstants.ACTION_IMAGE_DETAIL, params);
    }

    private String createSendCommentUrl(int targetUserId, String content) {
        String encodeContent = "";
        try {
            encodeContent = URLEncoder.encode(content, "utf-8");
        } catch (UnsupportedEncodingException e) {
            // ignore
        }
        String params = "?pictureSetId=" + mPicSetId
                + "&currentUserId=" + ESUserManager.getInstance().getCurrentUserId()
                + "&content=" + encodeContent;
        if (targetUserId != -1 && content.startsWith("回复")) {
            params += "&userId=" + targetUserId;
        }
        return UrlBuilder.getUrl(ActionConstants.ACTION_SEND_COMMENT, params);
    }

    private String createPraiseUrl(String action) {
        String params = "?pictureSetId=" + mPicSetId
                + "&currentUserId=" + ESUserManager.getInstance().getCurrentUserId();
        return UrlBuilder.getUrl(action, params);
    }

    private void clickedPraisedImg() {
        if (mData != null) {
            if (mData.isUserPraised()) {
                mData.setIsUserPraised(false);
                mData.setPraiseNum(Math.max(0, mData.getPraiseNum() - 1));
            } else {
                mData.setIsUserPraised(true);
                mData.setPraiseNum(mData.getPraiseNum() + 1);
            }
            mData.setPraiseStatusChanged(true);// 标记点赞状态已发生变化, 然后在退出也面试通知服务器更新状态
            updatePraisedStatus(mData);
        }
    }

    @Override
    protected void onDestroy() {
        pushPraisedStatusToServerIfNeeded();
        super.onDestroy();
    }

    private void pushPraisedStatusToServerIfNeeded() {
        if (mData.isPraiseStatusChanged()) {
            final String action =
                    mData.isUserPraised() ? ActionConstants.ACTION_PRESS_PRAISE : ActionConstants.ACTION_CANCEL_PRAISE;
            final String url = createPraiseUrl(action);
            JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    LogHelper.d(TAG, "赞状态同步成功");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    LogHelper.d(TAG, "赞状态同步失败");
                }
            });
            RequestQueueManager.getRequestQueue().add(request);
        }
    }

    private void updatePraisedStatus(ESPhotoSet data) {
        mPraiseImage.setImageResource(data.isUserPraised() ? R.drawable.liked_icon : R.drawable.llike_icon);
        mPraiseNumTv.setText("" + data.getPraiseNum());
    }

    private void initPraiseListView(ESPhotoSet data) {
        mPraiseLayout = (ViewGroup) findViewById(R.id.praiseLayout);
        mPraiseImage = (ImageView) findViewById(R.id.praiseImage);
        mPraiseNumTv = (TextView) findViewById(R.id.praiseNumTv);
        updatePraisedStatus(data);
        List<PraiseInfo> praises = data.getPraiseList();
        Context context = getApplicationContext();
        int headSize = UIUtils.dipToPx(context, 23);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(headSize, headSize);
        lp.leftMargin = UIUtils.dipToPx(context, 4);
        if (praises != null && praises.size() > 0) {
            int limit = Math.min(7, praises.size());
            for (int i = 0; i < limit; i++) {
                PraiseInfo pi = praises.get(i);
                ImageView view = new ImageView(context);
                mPraiseLayout.addView(view, lp);
                ImageLoaderManager.getImageLoader().displayImage(pi.getHeadPortrait(), view,
                        ImageLoaderOptionsUtils.getHeadImageOptions());
            }
            TextView moreView = new TextView(context);
            moreView.setText("...");
            moreView.setGravity(Gravity.CENTER_VERTICAL);
            moreView.setTextColor(Color.parseColor("#30000000"));
            mPraiseLayout.addView(moreView, lp);
        }

        // 点赞事件
        findViewById(R.id.praiseNumLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedPraisedImg();
            }
        });

        // 点击赞列表跳转到所有赞的列表页
        mPraiseLayout.setOnClickListener(this);
    }

    private void initUserInfoView(ESPhotoSet data) {
        mUserHeader = (ImageView) findViewById(R.id.headIconView);
        mUserName = (TextView) findViewById(R.id.userNameTv);
        mPostTime = (TextView) findViewById(R.id.postTimeTv);
        mDescTv = (TextView) findViewById(R.id.userDescTv);

        ImageLoaderManager.getImageLoader().displayImage(data.getUser().getHeadPortrait(), mUserHeader,
                ImageLoaderOptionsUtils.getHeadImageOptions());
        mUserName.setText(data.getUser().getUserName());
        mPostTime.setText(ESDateFormat.format(data.getUploadDate()));
        mDescTv.setText(data.getDescStr());
    }

    private void initPicsView(ESPhotoSet data) {
        mPicSetLayout = (FrameLayout) findViewById(R.id.picSetLayout);
    }

    private void initCommentView(ESPhotoSet data) {
        if (data.getComments() == null || data.getComments().size() <= 0) {
            LogHelper.d(TAG, "还没有评论");
            return;
        }
        LogHelper.d(TAG, "评论条数:" + data.getComments().size());
        mCommentRecyclerView = (RecyclerView) findViewById(R.id.commentRecyclerView);
        mCommentAdapter = new CommentAdapter(this, data.getComments());
        mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCommentRecyclerView.setAdapter(mCommentAdapter);

        mCommentEt = (EditText) findViewById(R.id.commentEt);
        mSendCommentBtn = (Button) findViewById(R.id.commentBtn);
        mSendCommentBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mSendCommentBtn) {
            if (mCommentEt.getText().toString().trim().length() <= 0) {
                LogHelper.d(TAG, "评论内容为空");
                return;
            }

            sendComment(mCommentTargetId, mCommentEt.getText().toString());
            mCommentTargetId = -1; // 恢复mCommentTargetId的值
        } else if (v == mPraiseLayout) {
            // TODO 跳转到所有赞的列表
        }
    }

    /**
     * 发送评论
     *
     * @param targetUserId 回复人的id（如果值为-1则无回复人,只是回复图集）
     * @param content      评论内容
     */
    private void sendComment(final int targetUserId, final String content) {
        mSendCommentBtn.setEnabled(false);
        String url = createSendCommentUrl(targetUserId, content);
        LogHelper.d(TAG, "send comment url:" + url);
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mSendCommentBtn.setEnabled(true);
                if (response != null) {
                    int state = response.optInt("state");
                    if (state == 0) {
                        JSONObject obj = response.optJSONObject("result");
                        if (obj != null) {
                            String newCommentId = obj.optString("commentId");
                            if (!TextUtils.isEmpty(newCommentId)) {
                                Toast.makeText(ImageDetailActivity.this, "发表成功", Toast.LENGTH_SHORT).show();
                                // 根据返回的commentId,构造ESComment对象,更新到评论列表中
                                ESComment comment = new ESComment();
                                ESUser user = ESUserManager.getInstance().getCurrentUser();
                                comment.setUser(user);
                                comment.setContent(content);
                                comment.setId(newCommentId);
                                comment.setParentUserId(String.valueOf(targetUserId));
                                comment.setDate(String.valueOf(System.currentTimeMillis()));
                                if (mCommentAdapter != null) {
                                    mCommentAdapter.insertComment(0, comment);
                                    mCommentRecyclerView.smoothScrollToPosition(0);
                                }
                            }
                        }

                    }
                }
                mCommentEt.setText("");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                mSendCommentBtn.setEnabled(true);
                Toast.makeText(ImageDetailActivity.this, "评论失败", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueueManager.getRequestQueue().add(request);
    }

    public void onCommentItemClicked(ESComment comment) {
        mCommentTargetId = comment.getUser().getId();
        mCommentEt.setText("回复 " + comment.getUser().getUserName() + ":");
        Editable etext = mCommentEt.getText();
        Selection.setSelection(etext, etext.length());
        mCommentEt.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }
}