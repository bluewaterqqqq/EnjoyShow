package com.zmdx.enjoyshow.fragment.pic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.entity.ESPhoto;
import com.zmdx.enjoyshow.fragment.detail.ImageDetailActivity;
import com.zmdx.enjoyshow.utils.ImageLoaderManager;
import com.zmdx.enjoyshow.utils.ImageLoaderOptionsUtils;
import com.zmdx.enjoyshow.utils.LogHelper;

import java.util.List;

/**
 * Created by zhangyan on 15/10/26.
 */
public class RecommandAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "RecommandAdapter";

    private List<ESPhoto> mData;

    private LayoutInflater mInflater;

    private DisplayImageOptions mCoverImageOptions;
//    private DisplayImageOptions mHeaderImageOptions;

    public RecommandAdapter(Context context, List<ESPhoto> data) {
        mData = data;
        mInflater = LayoutInflater.from(context);
        mCoverImageOptions = ImageLoaderOptionsUtils.getCoverImageOptions();
//        mHeaderImageOptions = ImageLoaderOptionsUtils.getHeadImageOptions();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PicViewHolder(mInflater.inflate(R.layout.pic_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PicViewHolder pHolder = (PicViewHolder) holder;
        ESPhoto item = mData.get(position);
        ImageLoaderManager.getImageLoader().displayImage(item.getCoverUrl(), pHolder.coverView, mCoverImageOptions);
//        ImageLoaderManager.getImageLoader().displayImage(item.getUser().getHeadPortrait(), pHolder.headView, mHeaderImageOptions);
        pHolder.reportView.setText(item.getPhotoCount() + "");
//        pHolder.userNameView.setText(item.getUser().getUserName());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                ESPhoto esPhoto = mData.get(position);
                ImageDetailActivity.start(v.getContext(), esPhoto.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 追加数据，并刷新(仅对增量的数据刷新)
     *
     * @param newData
     */
    public void appendData(List<ESPhoto> newData) {
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

    public void insertDataAtHeader(List<ESPhoto> data) {
        if (mData.size() <= 0) {//第一次加载数据，直接全部添加，并更新
            mData.addAll(data);
            LogHelper.d(TAG, "更新" + data.size() + "条");
            notifyDataSetChanged();
            return;
        }

        //去重
        String latestId = mData.get(0).getId();
        int pos = -1;
        for (int i = 0; i < data.size(); i++) {
            ESPhoto es = data.get(i);
            if (es.getId().equals(latestId)) {
                pos = i;
                break;
            }
        }
        if (pos == 0) {//全部为重合数据,无需更新，直接返回
            LogHelper.d(TAG, "更新0条");
            return;
        } else if (pos == -1) {//全部为新数据，无需去重，全部添加
            mData.addAll(0, data);
            LogHelper.d(TAG, "更新" + data.size() + "条");
        } else if (pos > 0) {//存在重合数据，只将新数据添加进来
            for (int j = pos - 1; j >= 0; j--) {
                mData.add(0, data.get(j));
            }
            LogHelper.d(TAG, "更新" + pos + "条");
        }

        notifyDataSetChanged();
    }

    public static class PicViewHolder extends RecyclerView.ViewHolder {
        public ImageView coverView;
        public ImageView headView;
        public TextView reportView;
        public TextView userNameView;

        public PicViewHolder(View v) {
            super(v);
            coverView = (ImageView) v.findViewById(R.id.coverImage);
            headView = (ImageView) v.findViewById(R.id.headImage);
            reportView = (TextView) v.findViewById(R.id.picNum);
            userNameView = (TextView) v.findViewById(R.id.userText);
        }
    }
}
