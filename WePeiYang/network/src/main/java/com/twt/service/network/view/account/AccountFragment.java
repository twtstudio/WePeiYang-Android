package com.twt.service.network.view.account;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twt.service.network.command.AccountViewModel;
import com.twt.service.network.R;
import com.twt.service.network.databinding.FragmentAccountMainBinding;


/**
 * Created by chen on 2017/7/7.
 */

public class AccountFragment extends Fragment {

    private View view;
    private AccountViewModel mAccountViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentAccountMainBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_main, container, false);
        mAccountViewModel=new AccountViewModel(this);
        binding.setViewModel(mAccountViewModel);
        view = binding.getRoot();
        return view;
    }
}
