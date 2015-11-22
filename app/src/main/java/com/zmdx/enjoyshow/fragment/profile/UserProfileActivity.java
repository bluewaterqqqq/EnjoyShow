package com.zmdx.enjoyshow.fragment.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by zhangyan on 15/11/22.
 */
public class UserProfileActivity extends AppCompatActivity {
    public static void start(Context context, String userId) {
        Intent in = new Intent(context, UserProfileActivity.class);
        in.putExtra("userId", userId);
        context.startActivity(in);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
