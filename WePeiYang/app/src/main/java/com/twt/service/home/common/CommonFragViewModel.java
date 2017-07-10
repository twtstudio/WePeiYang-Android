package com.twt.service.home.common;

import android.content.SharedPreferences;
import android.databinding.ObservableArrayList;
import android.preference.PreferenceManager;

import com.kelin.mvvmlight.base.ViewModel;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twtstudio.retrox.bike.homeitem.BikeHomeItemViewModel;
import com.twtstudio.retrox.classroomcore.home.ClassroomViewModel;
import com.twtstudio.retrox.tjulibrary.home.HomeLibItemViewModel;
import com.twt.service.BR;
import com.twt.service.R;
import com.twt.service.base.BaseActivity;
import com.twt.service.base.BaseFragment;
import com.twt.service.home.common.gpaItem.GpaItemViewModel;
import com.twt.service.home.common.oneItem.OneInfoViewModel;
import com.twt.service.home.common.schedule.ScheduleViewModel;

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

    public final ItemViewSelector itemView = ItemViewClassSelector.builder()
//            .put(OneDetailViewModel.class,BR.viewModel,R.layout.item_common_one)
            .put(GpaItemViewModel.class, BR.viewModel, R.layout.item_common_gpa)
            .put(ScheduleViewModel.class, BR.viewModel, R.layout.item_common_schedule)
            .put(HomeLibItemViewModel.class, BR.viewModel, com.twtstudio.retrox.tjulibrary.R.layout.item_common_lib)
            .put(ClassroomViewModel.class, BR.viewModel, com.twtstudio.retrox.classroomcore.R.layout.item_common_classroom_query)
            .put(BikeHomeItemViewModel.class, BR.viewModel, com.twtstudio.retrox.bike.R.layout.item_common_bike_card)
            .build();

    public CommonFragViewModel(BaseFragment fragment) {
        mFragment = fragment;
        initList();
    }

    public void initList() {
//        mOneInfoViewModel = new OneDetailViewModel();
//        viewModelList.add(mOneInfoViewModel);
        if (viewModelList.size() != 0) {
            viewModelList.clear();
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mFragment.getContext());

        ScheduleViewModel mScheduleViewModel = new ScheduleViewModel((RxAppCompatActivity) mFragment.getActivity());
        viewModelList.add(mScheduleViewModel);

        boolean isDisplayGpa = sharedPreferences.getBoolean(mFragment.getString(R.string.pref_is_display_gpa), false);
        if (isDisplayGpa) {
            mGpaItemViewModel = new GpaItemViewModel((BaseActivity) mFragment.getActivity());
            viewModelList.add(mGpaItemViewModel);
        }

        if (CommonPrefUtil.getIsBindLibrary()) {
            viewModelList.add(new HomeLibItemViewModel(mFragment.getContext()));
        }
        viewModelList.add(new ClassroomViewModel(mFragment.getContext()));
        boolean isDisplayBike = sharedPreferences.getBoolean(mFragment.getString(R.string.pref_is_display_bike), false);
        if (isDisplayBike && CommonPrefUtil.getIsBindBike()) {
            viewModelList.add(new BikeHomeItemViewModel((RxAppCompatActivity) mFragment.getActivity()));
        }
    }

    private void refresh() {
        mGpaItemViewModel.getData();
    }
}
