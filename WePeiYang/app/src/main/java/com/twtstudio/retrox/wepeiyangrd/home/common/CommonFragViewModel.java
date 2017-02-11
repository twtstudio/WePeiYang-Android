package com.twtstudio.retrox.wepeiyangrd.home.common;

import android.databinding.ObservableArrayList;

import com.kelin.mvvmlight.base.ViewModel;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twtstudio.retrox.wepeiyangrd.BR;
import com.twtstudio.retrox.wepeiyangrd.R;
import com.twtstudio.retrox.wepeiyangrd.base.BaseActivity;
import com.twtstudio.retrox.wepeiyangrd.base.BaseFragment;
import com.twtstudio.retrox.wepeiyangrd.home.common.gpaItem.GpaItemViewModel;
import com.twtstudio.retrox.wepeiyangrd.home.common.oneItem.OneInfoViewModel;
import com.twtstudio.retrox.wepeiyangrd.home.common.schedule.ScheduleViewModel;

import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import me.tatarka.bindingcollectionadapter.itemviews.ItemViewClassSelector;

/**
 * Created by retrox on 2017/1/16.
 */

public class CommonFragViewModel implements ViewModel {

    private BaseFragment mFragment;

//    public final ObservableArrayMap mObservableArrayMap;

    public final ObservableArrayList<ViewModel> viewModelList = new ObservableArrayList<>();

    private OneInfoViewModel mOneInfoViewModel;
    private GpaItemViewModel mGpaItemViewModel;
    private ScheduleViewModel mScheduleViewModel;

    public final ItemViewSelector itemView = ItemViewClassSelector.builder()
            .put(OneInfoViewModel.class,BR.viewModel,R.layout.item_common_one)
            .put(GpaItemViewModel.class,BR.viewModel,R.layout.item_common_gpa)
            .put(ScheduleViewModel.class,BR.viewModel,R.layout.item_common_schedule)
            .build();

    public CommonFragViewModel(BaseFragment fragment) {
        mFragment = fragment;
        initList();
    }

    private void initList(){
        mOneInfoViewModel = new OneInfoViewModel(mFragment);
        //viewModelList.add(mOneInfoViewModel);

        mGpaItemViewModel = new GpaItemViewModel((BaseActivity) mFragment.getActivity());
        viewModelList.add(mGpaItemViewModel);

        mScheduleViewModel = new ScheduleViewModel((RxAppCompatActivity) mFragment.getActivity());
        viewModelList.add(mScheduleViewModel);
    }

    private void refresh(){
        mGpaItemViewModel.getData();
    }
}
