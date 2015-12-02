package com.zmdx.enjoyshow.main.detail;

import java.util.List;

import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.common.BaseAppCompatActivity;
import com.zmdx.enjoyshow.entity.ESUser;
import com.zmdx.enjoyshow.main.adapter.UserListAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by zhangyan on 15/11/22.
 */
public class AllPraisedActivity extends BaseAppCompatActivity {

    private static List<ESUser> sData;

    private RecyclerView mRecyclerView;

    private UserListAdapter mAdapter;

    public static void start(Context context, List<ESUser> praises) {
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
        mAdapter = new UserListAdapter(this, sData);
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

        public ImageView headerIv;
        public TextView userNameTv;
        public TextView ageTv;
        public View genderView;

        public PraiseHolder(View itemView) {
            super(itemView);
            headerIv = (ImageView) itemView.findViewById(R.id.headIv);
            userNameTv = (TextView) itemView.findViewById(R.id.titleTv);
            ageTv = (TextView) itemView.findViewById(R.id.ageTv);
            genderView = itemView.findViewById(R.id.genderView);
        }
    }
}
