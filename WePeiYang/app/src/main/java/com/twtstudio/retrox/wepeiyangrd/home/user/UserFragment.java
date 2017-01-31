package com.twtstudio.retrox.wepeiyangrd.home.user;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twtstudio.retrox.wepeiyangrd.R;
import com.twtstudio.retrox.wepeiyangrd.base.BaseActivity;
import com.twtstudio.retrox.wepeiyangrd.base.BaseFragment;
import com.twtstudio.retrox.wepeiyangrd.databinding.FragmentUserBinding;
import com.twt.wepeiyang.commons.view.RecyclerViewDivider;

/**
 * Created by retrox on 2016/12/12.
 */

public class UserFragment extends BaseFragment {

    public static UserFragment newInstance() {

        Bundle args = new Bundle();

        UserFragment fragment = new UserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentUserBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false);

        binding.setViewmodel(new UserFragViewModel((BaseActivity)getActivity()));

        RecyclerView recyclerView = binding.recyclerView;
        RecyclerViewDivider divider = new RecyclerViewDivider.Builder(this.getContext())
                .setStartSkipCount(1)
                .setSize(2f)
                .setColorRes(R.color.background_gray)
                .build();

        recyclerView.addItemDecoration(divider);
        return binding.getRoot();
    }
}
