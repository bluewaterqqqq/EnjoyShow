package com.zmdx.enjoyshow.fragment.detect;

import java.util.List;

import com.andtinder.view.CardStackAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.entity.ESPhoto;
import com.zmdx.enjoyshow.utils.ImageLoaderManager;
import com.zmdx.enjoyshow.utils.ImageLoaderOptionsUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by zhangyan on 15/11/15.
 */
public class ESCardStackAdapter extends CardStackAdapter<ESPhoto> {

    private LayoutInflater mInflater;

    private List<ESPhoto> mData;

    private DisplayImageOptions mCoverOptions;

    private DisplayImageOptions mHeaderOptions;

    public ESCardStackAdapter(Context context, List<ESPhoto> data) {
        super(context, data);
        mData = data;
        mInflater = LayoutInflater.from(context);
        mCoverOptions = ImageLoaderOptionsUtils.getCoverImageOptions();
        mHeaderOptions = ImageLoaderOptionsUtils.getHeadImageOptions();
    }

    @Override
    protected View getCardView(int position, View convertView, ViewGroup parent) {
        CardViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.detect_card_layout, parent, false);
            holder = new CardViewHolder();
            holder.bgImageView = (ImageView) convertView.findViewById(R.id.bg);
            holder.mHeadIconView = (ImageView) convertView.findViewById(R.id.headIconView);
            holder.mUserNameView = (TextView) convertView.findViewById(R.id.userNameTv);
            holder.mAgeView = (TextView) convertView.findViewById(R.id.ageTv);
            convertView.setTag(holder);
        } else {
            holder = (CardViewHolder) convertView.getTag();
        }

        final ESPhoto item = mData.get(position);
        ImageLoaderManager.getImageLoader()
                .displayImage(item.getCoverUrl(), holder.bgImageView, mCoverOptions);
        ImageLoaderManager.getImageLoader().displayImage(item.getUser().getHeadPortrait(), holder.mHeadIconView,
                mHeaderOptions);
        holder.mUserNameView.setText(item.getUser().getUserName());
        holder.mAgeView.setText(item.getUser().getAge());
        return convertView;
    }

    private static class CardViewHolder {
        private ImageView bgImageView;
        private ImageView mHeadIconView;
        private TextView mUserNameView;
        private TextView mAgeView;
    }
}
