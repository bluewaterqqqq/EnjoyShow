package com.zmdx.enjoyshow.main.detail;

import java.util.List;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.entity.ESComment;
import com.zmdx.enjoyshow.entity.ESUser;
import com.zmdx.enjoyshow.network.ActionConstants;
import com.zmdx.enjoyshow.network.RequestQueueManager;
import com.zmdx.enjoyshow.network.UrlBuilder;
import com.zmdx.enjoyshow.user.ESUserManager;
import com.zmdx.enjoyshow.utils.ESDateFormat;
import com.zmdx.enjoyshow.utils.ImageLoaderManager;
import com.zmdx.enjoyshow.utils.ImageLoaderOptionsUtils;
import com.zmdx.enjoyshow.utils.LogHelper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by zhangyan on 15/11/22.
 */
public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private static final String TAG = "CommentAdapter";

    private List<ESComment> mData;

    private LayoutInflater mInflater;

    private ImageDetailActivity mActivity;

    public CommentAdapter(ImageDetailActivity activity, List<ESComment> data) {
        mActivity = activity;
        mData = data;
        mInflater = LayoutInflater.from(activity);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentHolder(mInflater.inflate(R.layout.comment_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CommentHolder cHolder = (CommentHolder) holder;
        cHolder.itemView.setId(R.id.commentItemView);
        ESComment item = mData.get(position);
        cHolder.mUserName.setText(item.getUser().getUserName());
        ImageLoaderManager.getImageLoader().displayImage(item.getUser().getHeadPortrait(), cHolder.mHeadIcon,
                ImageLoaderOptionsUtils.getHeadImageOptions());
        cHolder.mPostTime.setText(ESDateFormat.format(item.getDate()));
        cHolder.mContent.setText(item.getContent());
        cHolder.mHeadIcon.setOnClickListener(this);
        cHolder.itemView.setOnClickListener(this);
        cHolder.itemView.setTag(position);
        cHolder.mHeadIcon.setTag(position);

        if (isMyComment(position)) {
            cHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final int position = (Integer) v.getTag();
                    SweetAlertDialog dialog = new SweetAlertDialog(mActivity, SweetAlertDialog.WARNING_TYPE);
                    dialog.setTitleText("删除评论");
                    dialog.setContentText("确认删除这条评论吗?");
                    dialog.setConfirmText("确定");
                    dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            deleteComment(position);
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });
                    dialog.setCancelText("取消");
                    dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });
                    dialog.show();
                    return true;
                }
            });
        }
    }

    private boolean isMyComment(int position) {
        final ESComment c = mData.get(position);
        return (c.getUser().getId() + "").equals(ESUserManager.getInstance().getCurrentUserId());
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.commentItemView) {
            int position = (Integer) v.getTag();
            ESComment comment = mData.get(position);
            mActivity.onCommentItemClicked(comment);
        } else if (id == R.id.comment_head) {
            onHeadClicked((Integer) v.getTag());
        }
    }

    private void onHeadClicked(int position) {

    }

    public void insertComment(int position, ESComment comment) {
        mData.add(position, comment);
        notifyItemInserted(position);
    }

    public void deleteComment(int position) {
        String commentId = mData.get(position).getId();
        mData.remove(position);
        notifyItemRemoved(position);
        final String url = UrlBuilder.getUrl(ActionConstants.ACTION_DELETE_COMMENT, "?id=" + commentId);
        LogHelper.d(TAG, "url:" + url);
        final RequestQueue requestQueue = RequestQueueManager.getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogHelper.d(TAG, "response:" + response);
                if (response.optInt("state") == 0) {
                    LogHelper.d(TAG, "评论删除成功");
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

    public void appendData(List<ESComment> newData) {
        mData.addAll(newData);

        // 计算新增数据的start position和end position
        int startIndex = mData.size() - newData.size();
        int endIndex = mData.size() - 1;

        //逐条刷新新增的数据到界面
        for (int pos = startIndex; pos <= endIndex; pos++) {
            notifyItemInserted(pos);
        }

        LogHelper.d(TAG, "增量更新" + newData.size() + "条");

    }

    public static class CommentHolder extends RecyclerView.ViewHolder {

        private ImageView mHeadIcon;
        private TextView mUserName;
        private TextView mContent;
        private TextView mPostTime;

        public CommentHolder(View itemView) {
            super(itemView);
            mHeadIcon = (ImageView) itemView.findViewById(R.id.comment_head);
            mUserName = (TextView) itemView.findViewById(R.id.comment_uName);
            mContent = (TextView) itemView.findViewById(R.id.comment_content);
            mPostTime = (TextView) itemView.findViewById(R.id.comment_date);
        }
    }
}
