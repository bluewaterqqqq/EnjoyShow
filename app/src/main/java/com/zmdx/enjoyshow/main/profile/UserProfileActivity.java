package com.zmdx.enjoyshow.main.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.zmdx.enjoyshow.R;
import com.zmdx.enjoyshow.entity.ESUser;
import com.zmdx.enjoyshow.main.Fragment5;

/**
 * Created by zhangyan on 15/11/22.
 */
public class UserProfileActivity extends AppCompatActivity {
    private String mUserId;

    public static void start(Context context, String userId) {
        Intent in = new Intent(context, UserProfileActivity.class);
        in.putExtra("userId", userId);
        context.startActivity(in);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent in = getIntent();
        if (in != null) {
            mUserId = in.getStringExtra("userId");
        }

        setContentView(R.layout.profile_container);

        Fragment frag = new TAProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userId", mUserId);
        bundle.putBoolean("back", true);
        frag.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.profile_container, frag).commit();
    }

    public static class TAProfileFragment extends Fragment5 {

    }
}
