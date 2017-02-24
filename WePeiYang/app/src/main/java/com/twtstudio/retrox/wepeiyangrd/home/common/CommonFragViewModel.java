package com.twtstudio.retrox.wepeiyangrd.home.common;

import android.content.SharedPreferences;
import android.databinding.ObservableArrayList;
import android.preference.PreferenceManager;

import com.kelin.mvvmlight.base.ViewModel;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twtstudio.retrox.bike.homeitem.BikeHomeItemViewModel;
import com.twtstudio.retrox.classroomcore.home.ClassroomViewModel;
import com.twtstudio.retrox.tjulibrary.home.HomeLibItemViewModel;
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
            .put(HomeLibItemViewModel.class,BR.viewModel, com.twtstudio.retrox.tjulibrary.R.layout.item_common_lib)
            .put(ClassroomViewModel.class,BR.viewModel,com.twtstudio.retrox.classroomcore.R.layout.item_common_classroom_query)
            .put(BikeHomeItemViewModel.class,BR.viewModel,com.twtstudio.retrox.bike.R.layout.item_common_bike_card)
            .build();

    public CommonFragViewModel(BaseFragment fragment) {
        mFragment = fragment;
        initList();
    }

    private void initList(){
        mOneInfoViewModel = new OneInfoViewModel(mFragment);
        //viewModelList.add(mOneInfoViewModel);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mFragment.getContext());
        boolean isDisplayGpa = sharedPreferences.getBoolean(mFragment.getString(R.string.pref_is_display_gpa),false);

        if (isDisplayGpa){
            mGpaItemViewModel = new GpaItemViewModel((BaseActivity) mFragment.getActivity());
            viewModelList.add(mGpaItemViewModel);
        }
        
        mScheduleViewModel = new ScheduleViewModel((RxAppCompatActivity) mFragment.getActivity());
        viewModelList.add(mScheduleViewModel);
        viewModelList.add(new HomeLibItemViewModel(mFragment.getContext()));
        viewModelList.add(new ClassroomViewModel());
        viewModelList.add(new BikeHomeItemViewModel());
    }

    private void refresh(){
        mGpaItemViewModel.getData();
    }
}
