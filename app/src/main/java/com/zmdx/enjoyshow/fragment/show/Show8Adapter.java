package com.zmdx.enjoyshow.fragment.show;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.entity.ESTheme;
import com.zmdx.enjoyshow.utils.ImageLoaderManager;
import com.zmdx.enjoyshow.utils.ImageLoaderOptionsUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by baidu on 15/11/27.
 */
public class Show8Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ESTheme> mData;

    private LayoutInflater mInflater;

    private SimpleDateFormat mFormat = new SimpleDateFormat("yyyy.MM.dd");

    public Show8Adapter(Context context, List<ESTheme> data) {
        mData = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Show8Holder(mInflater.inflate(R.layout.show8_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ESTheme theme = mData.get(position);
        Show8Holder sHolder = (Show8Holder) holder;
        sHolder.titleTv.setText(theme.getmTitle());
        sHolder.titleTv.getPaint().setFakeBoldText(true);
        sHolder.durationTv.setText(mFormat.format(new Date(theme.getmStartTime())) + " -- " + mFormat.format(new Date(theme.getmEndTime())));
        ImageLoaderManager.getImageLoader().displayImage(theme.getmBgUrl(), sHolder.bgIv,
                ImageLoaderOptionsUtils.getCoverImageOptions());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class Show8Holder extends RecyclerView.ViewHolder {

        private ImageView bgIv;
        private TextView titleTv;
        private TextView durationTv;

        public Show8Holder(View itemView) {
            super(itemView);
            bgIv = (ImageView) itemView.findViewById(R.id.show8_bgIv);
            titleTv = (TextView) itemView.findViewById(R.id.show8_titleTv);
            durationTv = (TextView) itemView.findViewById(R.id.show8_durationTv);
        }
    }
}
