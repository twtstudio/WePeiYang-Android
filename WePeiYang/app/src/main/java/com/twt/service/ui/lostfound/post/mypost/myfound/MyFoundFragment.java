package com.twt.service.ui.lostfound.post.mypost.myfound;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twt.service.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sunjuntao on 16/4/5.
 */
public class MyFoundFragment extends Fragment {
    @InjectView(R.id.rv_found)
    RecyclerView rvFound;
    @InjectView(R.id.srl_found)
    SwipeRefreshLayout srlFound;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_found, container, false);

        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
