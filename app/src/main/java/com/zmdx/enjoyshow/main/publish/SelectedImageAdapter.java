/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.zmdx.enjoyshow.main.publish;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.utils.ImageLoaderManager;
import com.zmdx.enjoyshow.utils.ImageLoaderOptionsUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPagerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

/**
 * Created by zhangyan on 15/12/1.
 */
public class SelectedImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    public final static int REQUEST_CODE = 2;
    private static final int TYPE_IMAGE = 1;
    private static final int TYPE_ADD = 2;
    private static final String ADD_BTN = "addIcon";
    private static final String TAG = "SelectedImageAdapter";
    private Context mContext;
    private List<String> mData;

    private LayoutInflater mInflater;

    public SelectedImageAdapter(PublishActivity activity, List<String> data) {
        mContext = activity;
        data.add(Math.max(0, data.size() - 1), ADD_BTN);
        mData = data;
        mInflater = LayoutInflater.from(activity);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageHolder(mInflater.inflate(R.layout.publish_selected_images_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        ImageHolder iHolder = (ImageHolder) holder;
        if (type == TYPE_ADD) {
            iHolder.imageView.setImageResource(R.drawable.add_img_button);
        } else if (type == TYPE_IMAGE) {
            ImageLoaderManager.getImageLoader().displayImage("file://" + mData.get(position),
                    iHolder.imageView, ImageLoaderOptionsUtils.getCoverImageOptions());
        }

        iHolder.imageView.setTag(position);
        iHolder.imageView.setOnClickListener(this);
    }

    public void appendData(List<String> newData) {
        mData.addAll(Math.max(0, mData.size() - 1), newData);
        if (mData.size() > 9) {
            mData.remove(9);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == Math.max(0, mData.size() - 1) && mData.get(position).equals(ADD_BTN)) {
            return TYPE_ADD;
        } else {
            return TYPE_IMAGE;
        }
    }

    public boolean isAddView(int position) {
        if (position <= 0 || position >= mData.size()) {
            return false;
        }
        return mData.get(position).equals(ADD_BTN);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        int type = getItemViewType(position);
        if (type == TYPE_ADD) {
            PhotoPickerIntent intent = new PhotoPickerIntent(mContext);
            intent.setPhotoCount(9 - Math.max(0, mData.size() - 1));
            ((PublishActivity) mContext).startActivityForResult(intent, REQUEST_CODE);
        } else if (type == TYPE_IMAGE) {
            Intent intent = new Intent(mContext, PhotoPagerActivity.class);
            intent.putExtra(PhotoPagerActivity.EXTRA_CURRENT_ITEM, position);
            ArrayList<String> data = new ArrayList<>();
            data.addAll(mData);
            // 移除最后的加号数据
            String lastStr = data.get(mData.size() - 1);
            if (lastStr.equals(ADD_BTN)) {
                data.remove(data.size() - 1);
            }
            intent.putExtra(PhotoPagerActivity.EXTRA_PHOTOS, data);
            intent.putExtra(PhotoPagerActivity.EXTRA_SHOW_DELETE, true);
            if (mContext instanceof PublishActivity) {
                mContext.startActivity(intent);
            }
        }
    }

    public static class ImageHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public ImageHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.publish_selected_image);
        }
    }
}
