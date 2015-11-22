package com.zmdx.enjoyshow.fragment.detail;

import java.util.List;

import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.common.BaseAppCompatActivity;
import com.zmdx.enjoyshow.entity.PraiseInfo;
import com.zmdx.enjoyshow.utils.ImageLoaderManager;
import com.zmdx.enjoyshow.utils.ImageLoaderOptionsUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by zhangyan on 15/11/22.
 */
public class AllPraisedActivity extends BaseAppCompatActivity {

    private static List<PraiseInfo> sData;

    private RecyclerView mRecyclerView;

    private AllPraisedAdapter mAdapter;

    private List<PraiseInfo> mData;

    public static void start(Context context, List<PraiseInfo> praises) {
        sData = praises;
        Intent in = new Intent(context, AllPraisedActivity.class);
        context.startActivity(in);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_praised);
        initToolbar();

        mRecyclerView = (RecyclerView) findViewById(R.id.allPraisedRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new AllPraisedAdapter(this, sData);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("点赞列表");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static class PraiseHolder extends RecyclerView.ViewHolder {

        private ImageView headerIv;
        private TextView userNameTv;
        private TextView ageTv;
        private View genderView;

        public PraiseHolder(View itemView) {
            super(itemView);
            headerIv = (ImageView) itemView.findViewById(R.id.headIv);
            userNameTv = (TextView) itemView.findViewById(R.id.titleTv);
            ageTv = (TextView) itemView.findViewById(R.id.ageTv);
            genderView = itemView.findViewById(R.id.genderView);

        }
    }

    private class AllPraisedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private LayoutInflater mInflater;

        private List<PraiseInfo> mData;

        public AllPraisedAdapter(Context context, List<PraiseInfo> data) {
            mInflater = LayoutInflater.from(context);
            mData = data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            PraiseHolder pHolder = new PraiseHolder(mInflater.inflate(R.layout.praise_item, parent, false));
            return pHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            PraiseHolder pHolder = (PraiseHolder) holder;
            PraiseInfo pi = mData.get(position);
            pHolder.userNameTv.setText(pi.getUsername());
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
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }
}
