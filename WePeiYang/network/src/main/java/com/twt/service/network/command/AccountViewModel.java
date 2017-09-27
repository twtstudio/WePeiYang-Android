package com.twt.service.network.command;

import android.databinding.ObservableArrayList;
import android.support.v4.app.Fragment;

import com.kelin.mvvmlight.base.ViewModel;
import com.twt.service.network.R;
import com.twt.service.network.BR;

import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import me.tatarka.bindingcollectionadapter.itemviews.ItemViewClassSelector;


/**
 * Created by chen on 2017/8/23.
 */

public class AccountViewModel implements ViewModel {
    private Fragment mFragment;
    public ObservableArrayList<ViewModel> items = new ObservableArrayList<>();
    public ItemViewSelector itemViewSelector = ItemViewClassSelector.builder()
            .put(AccountDetailViewModel.class, BR.viewModel, R.layout.item_account_detail_cv)
            .put(AccountPayViweModel.class, BR.viewModel, R.layout.item_account_pay_cv)
            .build();

    public AccountViewModel(Fragment fragment) {
        this.mFragment = fragment;
        items.clear();
        items.add(0, new AccountDetailViewModel(mFragment));
        items.add(1, new AccountPayViweModel(mFragment));
    }

}
