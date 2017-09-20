package com.twtstudio.service.classroom.view;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.databinding.ObservableField;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twtstudio.service.classroom.utils.PrefUtil;
import com.twtstudio.service.classroom.utils.StringHelper;
import com.twtstudio.service.classroom.model.FilterBean;
import com.twtstudio.service.classroom.utils.TimeHelper;

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
    private String filterCondition = " ";
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
        viewModel.isLoading.set(true);
        filterBean.changePaddingColor = true;
        filterBean.hasClicked = true;
        if (PrefUtil.getIsNewCampus() && building == 0)
            building = 46;
        else if (building == 0)
            building = 23;
        try {
            switch (tag) {
                case 1:
                    viewModel.condition1.set(text.get());
                    building = StringHelper.getBuildingInt(text.get());
                    break;
                case 2:
                    viewModel.condition2.set(text.get());
                    if (text.get().equals("全天")) {
                        time = -1;
                    } else {
                        if (text.get().equals("现在")) time = TimeHelper.getTimeInt();
                        else
                            time = Integer.parseInt(text.get().length() == 6 ? text.get().substring(0, 2) : text.get().substring(0, 1));
                    }
                    break;
                case 3:
                    if (text.get().equals("全部"))
                        viewModel.condition3.set(text.get());
                    else
                        viewModel.condition3.set(text.get() + "...");
                    if (text.get().equals("暖气"))
                        filterCondition = "heating";
                    else if (text.get().equals("饮水机"))
                        filterCondition = "water_dispenser";
                    else if (text.get().equals("电源"))
                        filterCondition = "power_pack";
                    else if (text.get().equals("空闲"))
                        filterCondition = "empty";
                    else
                        viewModel.resetFilterCondition();
                    if (!viewModel.isFilterConditionRepeated(filterCondition))
                        viewModel.addFilterCondition(filterCondition);
                    else {
                        viewModel.removeFilterCondition(filterCondition);
                        filterBean.changePaddingColor = false;
                        filterBean.hasClicked = false;
                    }
                    break;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (time == -1)
            viewModel.getAllDayRoom(building, filterCondition);
//            for (int i = 1; i <= 12; i += 2)
//                viewModel.iniData(building, TimeHelper.getWeekInt(), i, CommonPrefUtil.getStudentNumber());
        else {
            if (time == 0) time = TimeHelper.getTimeInt();
            viewModel.iniData(building, TimeHelper.getWeekInt(), time, CommonPrefUtil.getStudentNumber());
        }
        if (tag == 3 && !text.get().equals("全部"))
            for (FilterBean filterBean : filterBeans) {
                if (filterBean.text.equals("全部")) {
                    filterBean.changePaddingColor = false;
                }
                if (!this.filterBean.hasClicked && filterBean.hasClicked)
                    if (filterBean.text.equals("全部"))
                        viewModel.condition3.set(filterBean.text);
                    else
                        viewModel.condition3.set(filterBean.text + "...");
            }
        else
            //用于设置单选的显示效果
            for (FilterBean filterBean : filterBeans) {
                if (filterBean == this.filterBean) continue;
                filterBean.changePaddingColor = false;
            }

        changePaddingColor.set(filterBean.changePaddingColor);
        popupViewModel.updateData(filterBean, filterBeans);
    }

    public static void resetBuildingAndTime() {
        building = 0;
        time = 0;
    }

//    @BindingConversion
//    public static ColorDrawable convertColorToDrawable(int color) {
//        return new ColorDrawable(color);
//    }

}
