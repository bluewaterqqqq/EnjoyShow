package com.zmdx.enjoyshow.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by zhangyan on 15/10/26.
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            toolbar.setTitle(getTitle());
        }
    }

    protected abstract String getTitle();

    protected abstract Toolbar getToolbar();

}
