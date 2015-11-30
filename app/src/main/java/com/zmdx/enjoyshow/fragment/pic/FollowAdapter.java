package com.zmdx.enjoyshow.fragment.pic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.entity.ESPhoto;
import com.zmdx.enjoyshow.fragment.detail.ImageDetailActivity;
import com.zmdx.enjoyshow.fragment.profile.UserProfileActivity;
import com.zmdx.enjoyshow.network.ActionConstants;
import com.zmdx.enjoyshow.network.RequestQueueManager;
import com.zmdx.enjoyshow.network.UrlBuilder;
import com.zmdx.enjoyshow.utils.ESDateFormat;
import com.zmdx.enjoyshow.utils.ImageLoaderManager;
import com.zmdx.enjoyshow.utils.ImageLoaderOptionsUtils;
import com.zmdx.enjoyshow.utils.LogHelper;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by zhangyan on 15/11/28.
 */
public class FollowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private static final String TAG = "FollowAdapter";

    private List<ESPhoto> mData;

    private LayoutInflater mInflater;

    private DisplayImageOptions mCoverImageOptions;

    private DisplayImageOptions mHeaderImageOptions;

    private Context mContext;

    public FollowAdapter(Context context, List<ESPhoto> data) {
        mData = data;
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mCoverImageOptions = ImageLoaderOptionsUtils.getCoverImageOptions();
        mHeaderImageOptions = ImageLoaderOptionsUtils.getHeadImageOptions();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FollowViewHolder(mInflater.inflate(R.layout.follow_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FollowViewHolder pHolder = (FollowViewHolder) holder;
        ESPhoto item = mData.get(position);
        ImageLoaderManager.getImageLoader().displayImage(item.getCoverUrl(), pHolder.coverView, mCoverImageOptions);
        ImageLoaderManager.getImageLoader().displayImage(item.getUser().getHeadPortrait(), pHolder.headView, mHeaderImageOptions);
        pHolder.userNameView.setText(item.getUser().getUserName());
        pHolder.picNum.setText(item.getPhotoCount() + "");
        pHolder.commentCount.setText(item.getComments());
        pHolder.dateTv.setText(ESDateFormat.format(item.getUploadDate()));
        pHolder.desc.setText(item.getDescs());
        pHolder.praiseCount.setText(item.getPraise() + "");
        pHolder.praiseIcon.setBackgroundResource(item.isUserPraised() ? R.drawable.liked_icon : R.drawable.llike_icon);

        pHolder.coverView.setOnClickListener(this);
        pHolder.coverView.setTag(item.getId());
        pHolder.headView.setOnClickListener(this);
        pHolder.headView.setTag(item.getUserId());
        pHolder.commentLayout.setOnClickListener(this);
        pHolder.commentLayout.setTag(item.getId());
        pHolder.praiseLayout.setOnClickListener(this);
        pHolder.praiseLayout.setTag(item);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.follow_cover) {
            String picSetId = (String) v.getTag();
            ImageDetailActivity.start(mContext, picSetId);
        } else if (id == R.id.follow_commentLayout) {
            String picSetId = (String) v.getTag();
            ImageDetailActivity.start(mContext, picSetId);
        } else if (id == R.id.follow_headIcon) {
            String userId = (String) v.getTag();
            UserProfileActivity.start(mContext, userId);
        } else if (id == R.id.follow_praiseLayout) {
            ESPhoto item = (ESPhoto) v.getTag();
            handlePraiseText(v, item);
        }
    }

    private void handlePraiseText(final View tv, ESPhoto item) {
        tv.setEnabled(false);
        ImageView praiseIv = (ImageView) tv.findViewById(R.id.follow_praisedIcon);
        TextView praiseNum = (TextView) tv.findViewById(R.id.follow_praiseNum);
        if (item.isUserPraised()) {
            praiseIv.setBackgroundResource(R.drawable.llike_icon);
            praiseNum.setText(item.getPraise() + "");
            item.setUserPraised(false);
            item.setPraise(Math.max(0, item.getPraise() - 1));
        } else {
            praiseIv.setBackgroundResource(R.drawable.liked_icon);
            item.setUserPraised(true);
            item.setPraise(item.getPraise() + 1);
            praiseNum.setText(item.getPraise() + "");
        }

        final String action =
                item.isUserPraised() ? ActionConstants.ACTION_PRESS_PRAISE : ActionConstants.ACTION_CANCEL_PRAISE;
        final String url = createPraiseUrl(action, item.getId());
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogHelper.d(TAG, "赞状态同步成功");
                if (tv != null) {
                    tv.setEnabled(true);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogHelper.d(TAG, "赞状态同步失败");
                if (tv != null) {
                    tv.setEnabled(true);
                }
            }
        });
        RequestQueueManager.getRequestQueue().add(request);
    }

    private String createPraiseUrl(String action, String picSetId) {
        String params = "?pictureSetId=" + picSetId;
        return UrlBuilder.getUrl(action, params);
    }

    public static class FollowViewHolder extends RecyclerView.ViewHolder {
        public ImageView coverView;
        public ImageView headView;
        public TextView userNameView;
        public TextView picNum;
        public TextView dateTv;
        public TextView desc;
        public TextView commentCount;
        public TextView praiseCount;
        public ImageView praiseIcon;
        public ViewGroup commentLayout;
        public ViewGroup praiseLayout;

        public FollowViewHolder(View v) {
            super(v);
            coverView = (ImageView) v.findViewById(R.id.follow_cover);
            headView = (ImageView) v.findViewById(R.id.follow_headIcon);
            userNameView = (TextView) v.findViewById(R.id.follow_userName);
            picNum = (TextView) v.findViewById(R.id.follow_picNum);
            dateTv = (TextView) v.findViewById(R.id.follow_date);
            desc = (TextView) v.findViewById(R.id.follow_desc);
            commentCount = (TextView) v.findViewById(R.id.follow_commentNum);
            praiseCount = (TextView) v.findViewById(R.id.follow_praiseNum);
            praiseIcon = (ImageView) v.findViewById(R.id.follow_praisedIcon);
            commentLayout = (ViewGroup) v.findViewById(R.id.follow_commentLayout);
            praiseLayout = (ViewGroup) v.findViewById(R.id.follow_praiseLayout);
        }
    }
}
