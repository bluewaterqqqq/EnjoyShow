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
    public static void start(Context context, ESUser user) {
        Intent in = new Intent(context, UserProfileActivity.class);
        in.putExtra("user", user);
        context.startActivity(in);
    }

    private ESUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent in = getIntent();
        if (in != null) {
            mUser = (ESUser) in.getSerializableExtra("user");
        }

        setContentView(R.layout.profile_container);

        Fragment frag = new TAProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", mUser);
        frag.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.profile_container, frag).commit();
    }

    public static class TAProfileFragment extends Fragment5 {

    }
}
