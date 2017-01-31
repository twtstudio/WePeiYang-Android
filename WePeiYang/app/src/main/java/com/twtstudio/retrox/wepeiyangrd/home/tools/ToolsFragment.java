package com.twtstudio.retrox.wepeiyangrd.home.tools;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.twtstudio.retrox.wepeiyangrd.R;
import com.twtstudio.retrox.wepeiyangrd.base.BaseFragment;
import com.twtstudio.retrox.wepeiyangrd.databinding.FragmentToolsBinding;

/**
 * Created by retrox on 2016/12/12.
 */

public class ToolsFragment extends BaseFragment {

    public static ToolsFragment newInstance() {

        Bundle args = new Bundle();

        ToolsFragment fragment = new ToolsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentToolsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tools, container, false);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));

        binding.setViewModel(new ToolsFragViewModel(getContext()));
        return binding.getRoot();
    }
}
