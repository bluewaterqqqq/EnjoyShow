package com.zmdx.enjoyshow.main.detail;

import java.util.List;

import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.entity.ESComment;
import com.zmdx.enjoyshow.utils.ESDateFormat;
import com.zmdx.enjoyshow.utils.ImageLoaderManager;
import com.zmdx.enjoyshow.utils.ImageLoaderOptionsUtils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by zhangyan on 15/11/22.
 */
public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

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
        // TODO
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
