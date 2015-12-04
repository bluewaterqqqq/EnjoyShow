package com.zmdx.enjoyshow.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.entity.ESUser;
import com.zmdx.enjoyshow.main.detail.AllPraisedActivity;
import com.zmdx.enjoyshow.main.profile.UserProfileActivity;
import com.zmdx.enjoyshow.utils.ImageLoaderManager;
import com.zmdx.enjoyshow.utils.ImageLoaderOptionsUtils;
import com.zmdx.enjoyshow.utils.LogHelper;

import java.util.List;

/**
 * Created by zhangyan on 15/12/1.
 */
public class UserListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private static final String TAG = "AllPraisedAdapter";

    private LayoutInflater mInflater;

    private List<ESUser> mData;

    private Context mContext;

    public UserListAdapter(Context context, List<ESUser> data) {
        mInflater = LayoutInflater.from(context);
        mData = data;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AllPraisedActivity.PraiseHolder pHolder = new AllPraisedActivity.PraiseHolder(mInflater.inflate(R.layout.praise_item, parent, false));
        return pHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AllPraisedActivity.PraiseHolder pHolder = (AllPraisedActivity.PraiseHolder) holder;
        ESUser pi = mData.get(position);
        pHolder.userNameTv.setText(pi.getUserName());
        pHolder.ageTv.setText(pi.getAge());
        int gender = Integer.valueOf(pi.getGender());
        if (gender == 0) {
            pHolder.genderView.setVisibility(View.GONE);
        } else if (gender == 1) {
            //男
            pHolder.genderView.setBackgroundResource(R.drawable.male_icon);
        } else if (gender == 2) {
            pHolder.genderView.setBackgroundResource(R.drawable.female_icon);
        }

        ImageLoaderManager.getImageLoader().displayImage(pi.getHeadPortrait(), pHolder.headerIv,
                ImageLoaderOptionsUtils.getHeadImageOptions());

        pHolder.itemView.setTag(pi);
        pHolder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void appendData(List<ESUser> newData) {
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

    @Override
    public void onClick(View v) {
        ESUser user = (ESUser) v.getTag();
        UserProfileActivity.start(mContext, user);
    }
}
