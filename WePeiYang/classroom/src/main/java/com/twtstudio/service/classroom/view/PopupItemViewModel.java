package com.twtstudio.service.classroom.view;

import android.databinding.BindingConversion;
import android.databinding.ObservableField;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twtstudio.service.classroom.model.FilterBean;
import com.twtstudio.service.classroom.model.TimeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyulong on 7/12/17.
 */

public class PopupItemViewModel implements com.kelin.mvvmlight.base.ViewModel {
    MainActivityViewModel viewModel;
    PopupWindowViewModel popupViewModel;
    public final ObservableField<String> text = new ObservableField<>();
    public final ObservableField<Boolean> changeTextColor = new ObservableField<>(false);
    public final ObservableField<Boolean> changePaddingColor = new ObservableField<>(false);
    public final ObservableField<Boolean> isOnClickable = new ObservableField<>();
    private int tag = 0;
    private static int building = 0;
    private static int time = 0;
    List<FilterBean> filterBeans = new ArrayList<>();
    FilterBean filterBean;

    PopupItemViewModel(MainActivityViewModel viewModel, FilterBean filterBean, List<FilterBean> filterBeans, PopupWindowViewModel popupWindowViewModel) {
        this.viewModel = viewModel;
        this.filterBean = filterBean;
        this.changeTextColor.set(filterBean.changeTextColor);
        this.changePaddingColor.set(filterBean.changePaddingColor);
        this.text.set(filterBean.text);
        this.tag = filterBean.tag;
        this.isOnClickable.set(filterBean.isOnClickable);
        this.filterBeans = filterBeans;
        this.popupViewModel = popupWindowViewModel;
    }

    public void onClick(View view) {
        switch (tag) {
            case 1:
                viewModel.condition1.set(text.get());
                building = Integer.parseInt(text.get().length() < 3 ? text.get().substring(0, 1) : text.get().substring(0, 2));
                break;
            case 2:
                if (CommonPrefUtil.getIsNewCampus() && building == 0)
                    building = 46;
                else if (building == 0)
                    building = 23;
                viewModel.condition2.set(text.get());
                if (text.get().equals("全天")) {
//                    for (int i = 1; i <= 12; i += 2)
//                        viewModel.iniData(building, TimeHelper.getWeekInt(), i, CommonPrefUtil.getStudentNumber());
                    time = -1;
                } else {
                    if (text.get().equals("现在")) time = TimeHelper.getTimeInt();
                    else
                        time = Integer.parseInt(text.get().length() == 6 ? text.get().substring(0, 2) : text.get().substring(0, 1));
                }
                break;
        }
        if (time == -1)
            viewModel.getAllDayRoom(building);
//            for (int i = 1; i <= 12; i += 2)
//                viewModel.iniData(building, TimeHelper.getWeekInt(), i, CommonPrefUtil.getStudentNumber());
        else {
            if (time == 0) time = TimeHelper.getTimeInt();
            viewModel.iniData(building, TimeHelper.getWeekInt(), time, CommonPrefUtil.getStudentNumber());
        }
        for (FilterBean filterBean : filterBeans) {
            if (filterBean == this.filterBean) continue;
            filterBean.changePaddingColor = false;
        }

        filterBean.changePaddingColor = true;
        filterBean.hasClicked = true;
        changePaddingColor.set(filterBean.changePaddingColor);
        popupViewModel.updateData(filterBean, filterBeans);
    }

    public static void resetBuildingAndTime() {
        building = 0;
        time = 0;
    }

    @BindingConversion
    public static ColorDrawable convertColorToDrawable(int color) {
        return new ColorDrawable(color);
    }
}
