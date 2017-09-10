package com.twt.service.home.news;

import android.databinding.ObservableArrayList;

import com.kelin.mvvmlight.base.ViewModel;
import com.twtstudio.retrox.one.OneApiProvider;
import com.twt.service.R;
import com.twt.service.BR;


import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by retrox on 2017/2/11.
 */

public class OneListViewModel implements ViewModel {

    public final ObservableArrayList<ViewModel> obDetailViewModelList = new ObservableArrayList<>();

    public final ItemView itemView = ItemView.of(BR.viewModel, R.layout.item_common_one);

    public OneListViewModel() {
        getData();
    }

    public void getData() {
        OneApiProvider.getInstance()
                .getIdlist(dataBean -> obDetailViewModelList.add(new OneDetailViewModel(dataBean)));
    }
}
