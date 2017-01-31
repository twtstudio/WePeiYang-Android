package com.twtstudio.retrox.wepeiyangrd.home.common;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.twtstudio.retrox.wepeiyangrd.R;
import com.twtstudio.retrox.wepeiyangrd.base.BaseFragment;
import com.twtstudio.retrox.wepeiyangrd.databinding.FragmentCommonsBinding;

/**
 * Created by retrox on 2016/12/12.
 */

public class CommonFragment extends BaseFragment {

    public static CommonFragment newInstance() {

        Bundle args = new Bundle();

        CommonFragment fragment = new CommonFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentCommonsBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_commons,container,false);
        binding.setViewModel(new CommonFragViewModel(this));

        return binding.getRoot();
    }
}
