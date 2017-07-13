package com.twtstudio.service.classroom.view;

import android.databinding.ObservableArrayList;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.messenger.Messenger;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twtstudio.service.classroom.BR;
import com.twtstudio.service.classroom.R;

import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import me.tatarka.bindingcollectionadapter.itemviews.ItemViewClassSelector;

/**
 * Created by zhangyulong on 7/12/17.
 */

public class PopupWindowViewModel implements ViewModel {
    public final ObservableArrayList<ViewModel> items = new ObservableArrayList<>();
    public final ItemViewSelector itemView = ItemViewClassSelector.builder()
            .put(PopupItemViewModel.class, BR.viewModel, R.layout.popup_item)
            .build();
    public final ObservableArrayList<ViewModel> items2 = new ObservableArrayList<>();
    public final ItemViewSelector itemView2 = ItemViewClassSelector.builder()
            .put(PopupItemViewModel.class, BR.viewModel, R.layout.popup_item)
            .build();
    RxAppCompatActivity mRxActivity;
    PopupWindowViewModel(RxAppCompatActivity mRxActivity) {
        this.mRxActivity=mRxActivity;

    }

    public void initData(boolean isNewCampus, int selector) {
        items.clear();
        items2.clear();
        switch (selector) {
            case 1:
                items.add(new PopupItemViewModel("推荐", true,mRxActivity,1));
                items2.add(new PopupItemViewModel("所有", true,mRxActivity,1));
                if (isNewCampus)
                    for (int i = 31; i <= 55; i++) {
                        final  int building=i;
                        if (i > 33 && i < 37 ||
                                i > 37 && i < 43 || i == 47 || i > 51 && i < 55) continue;
                        if (i == 44 || i == 50)
                            items2.add(new PopupItemViewModel(" ", false,mRxActivity,2));
                        items2.add(new PopupItemViewModel(Integer.toString(i) + "楼", false,mRxActivity,2));
//                        Messenger.getDefault().register(mRxActivity,"getData",MainActivityViewModel.class,(viewModel)->{
//                            viewModel.iniData(building,2,5, CommonPrefUtil.getStudentNumber());
//                        });
                    }
                break;
        }
    }
}

