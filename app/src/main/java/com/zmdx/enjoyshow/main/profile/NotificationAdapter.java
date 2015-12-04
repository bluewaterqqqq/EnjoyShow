/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.zmdx.enjoyshow.main.profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.entity.ESNotification;
import com.zmdx.enjoyshow.main.detail.ImageDetailActivity;
import com.zmdx.enjoyshow.utils.ImageLoaderManager;
import com.zmdx.enjoyshow.utils.ImageLoaderOptionsUtils;

import java.util.ArrayList;

/**
 * Created by zhangyan on 15/12/5.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotifyHolder> implements View.OnClickListener {

    private static final String TAG = "NotificationAdapter";

    private LayoutInflater mInflater;

    private Context mContext;

    private ArrayList<ESNotification> mData;

    public NotificationAdapter(Context context, ArrayList<ESNotification> data) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mData = data;
    }

    @Override
    public NotifyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NotificationAdapter.NotifyHolder(mInflater.inflate(R.layout.notify_item, parent, false));
    }

    @Override
    public void onBindViewHolder(NotifyHolder holder, int position) {
        final ESNotification n = mData.get(position);
        holder.userNameTv.setText(n.getUsername());
        ImageLoaderManager.getImageLoader().displayImage(n.getCoverUrl(), holder.coverIv, ImageLoaderOptionsUtils.getCoverImageOptions());
        ImageLoaderManager.getImageLoader().displayImage(n.getHeadPotrait(), holder.headIv, ImageLoaderOptionsUtils.getHeadImageOptions());
        int type = n.getType();
        if (type == 4) { // 评论
            holder.headTypeIv.setImageResource(R.drawable.notify_comment_icon);
            holder.typeStrTv.setText("评论了你");
        } else if (type == 5) { // 回复
            holder.headTypeIv.setImageResource(R.drawable.notify_comment_icon);
            holder.typeStrTv.setText("回复了你");
        } else if (type == 6) { // @
            holder.headTypeIv.setImageResource(R.drawable.notify_comment_icon);
            holder.typeStrTv.setText("@了你");
        } else if (type == 7) { // 赞
            holder.headTypeIv.setImageResource(R.drawable.notify_like_icon);
            holder.typeStrTv.setText("赞了你");
        } else if (type == 8) { // 关注
            holder.headTypeIv.setImageResource(R.drawable.notify_follow_icon);
            holder.typeStrTv.setText("关注了你");
        } else {
            holder.headTypeIv.setVisibility(View.GONE);
        }

        holder.headIv.setTag(n.getUserId());
        holder.headIv.setOnClickListener(this);
        holder.itemView.setTag(n.getPicSetId());
        holder.itemView.setId(R.id.notify_item);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void appendData(ArrayList<ESNotification> newData) {
        mData.addAll(newData);

        // 计算新增数据的start position和end position
        int startIndex = mData.size() - newData.size();
        int endIndex = mData.size() - 1;

        //逐条刷新新增的数据到界面
        for (int pos = startIndex; pos <= endIndex; pos++) {
            notifyItemInserted(pos);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.notify_headIv) {
            String userId = (String) v.getTag();
            UserProfileActivity.start(v.getContext(), userId);
        } else if (id == R.id.notify_item) {
            String picSetId = (String) v.getTag();
            ImageDetailActivity.start(v.getContext(), picSetId);
        }
    }

    public static class NotifyHolder extends RecyclerView.ViewHolder {
        public ImageView headIv;
        public ImageView headTypeIv;
        public ImageView coverIv;
        public TextView userNameTv;
        public TextView typeStrTv;

        public NotifyHolder(View itemView) {
            super(itemView);
            headIv = (ImageView) itemView.findViewById(R.id.notify_headIv);
            headTypeIv = (ImageView) itemView.findViewById(R.id.notify_type);
            coverIv = (ImageView) itemView.findViewById(R.id.notify_cover);
            userNameTv = (TextView) itemView.findViewById(R.id.notify_titleTv);
            typeStrTv = (TextView) itemView.findViewById(R.id.notify_type_str);
        }
    }
}
