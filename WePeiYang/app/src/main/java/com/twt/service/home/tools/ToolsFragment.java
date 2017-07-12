package com.twt.service.home.tools;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twt.service.R;
import com.twt.service.base.BaseFragment;
import com.twt.service.databinding.FragmentToolsBinding;

/**
 * Created by retrox on 2016/12/12.
 */

public class ToolsFragment extends BaseFragment {

    private FragmentToolsBinding binding;

    public static ToolsFragment newInstance() {

        Bundle args = new Bundle();

        ToolsFragment fragment = new ToolsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tools, container, false);
        binding.setViewModel(new ToolsFragViewModel(getContext()));
        return binding.getRoot();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));

    }
}
