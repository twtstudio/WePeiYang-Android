package com.twtstudio.service.classroom.view;

import android.databinding.ObservableArrayList;

import com.kelin.mvvmlight.base.ViewModel;
import com.twtstudio.service.classroom.BR;
import com.twtstudio.service.classroom.R;
import com.twtstudio.service.classroom.model.FilterBean;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import me.tatarka.bindingcollectionadapter.itemviews.ItemViewClassSelector;

/**
 * Created by zhangyulong on 7/12/17.
 */

public class PopupWindowViewModel implements ViewModel {
    public final ObservableArrayList<ViewModel> items = new ObservableArrayList<>();
    public final ItemViewSelector itemView = ItemViewClassSelector.builder()
            .put(PopupItemViewModel.class, BR.viewModel, R.layout.classroom_popup_item)
            .build();
    public final ObservableArrayList<ViewModel> items2 = new ObservableArrayList<>();
    public final ItemViewSelector itemView2 = ItemViewClassSelector.builder()
            .put(PopupItemViewModel.class, BR.viewModel, R.layout.classroom_popup_item)
            .build();
    private boolean hasDisplayeds[] = new boolean[3];
    private static boolean changeCampus = true; //储存上一次执行时的校区(true为新校区,false为老校区)
    MainActivityViewModel viewModel;
    private List<FilterBean> filterBeans = new ArrayList<>();
    private List<FilterBean> filterBeans2 = new ArrayList<>();
    private List<FilterBean> filterBeans3 = new ArrayList<>();

    PopupWindowViewModel(MainActivityViewModel viewModel) {
        this.viewModel = viewModel;
        for (int i = 0; i < 3; i++)
            hasDisplayeds[i] = false;
    }

    public void initData(boolean isNewCampus, int selector) {
        if (changeCampus != isNewCampus) {
            for (int i = 0; i < 3; i++)
                hasDisplayeds[i] = false;
            changeCampus = isNewCampus;
        }
        processData(isNewCampus, selector);
        items.clear();
        items2.clear();
        switch (selector) {
            case 1:
                for (FilterBean filterBean : filterBeans) {
                    if (filterBean.isTopLine)
                        items.add(new PopupItemViewModel(viewModel, filterBean, filterBeans, this));
                    else
                        items2.add(new PopupItemViewModel(viewModel, filterBean, filterBeans, this));
                }
                hasDisplayeds[0] = true;

                break;
            case 2:
                for (FilterBean filterBean : filterBeans2) {
                    if (filterBean.isTopLine)
                        items.add(new PopupItemViewModel(viewModel, filterBean, filterBeans2, this));
                    else
                        items2.add(new PopupItemViewModel(viewModel, filterBean, filterBeans2, this));
                }
                hasDisplayeds[1] = true;
                break;
            case 3:
                for (FilterBean filterBean : filterBeans3) {
                    items2.add(new PopupItemViewModel(viewModel, filterBean, filterBeans3, this));
                }
                hasDisplayeds[2] = true;
                break;
        }

    }

    private void processData(boolean isNewCampus, int selector) {
        switch (selector) {
            case 1:
                if (!hasDisplayeds[0]) {
                    filterBeans.clear();
                    filterBeans.add(new FilterBean("推荐", false, 1, false, true, true));
                    filterBeans.add(new FilterBean("所有", false, 1, false, false, true));
                    if (isNewCampus) {
//                        for (int i = 31; i <= 55; i++) {
//                            if (i > 33 && i < 37 ||
//                                    i > 37 && i < 43 || i == 47 || i > 51 && i < 55) continue;
//                            if (i == 44 || i == 50)
//                                filterBeans.add(new FilterBean(" ", false, 2, false, false, false));
//                            if(i==46)  filterBeans.add(new FilterBean(Integer.toString(i) + "楼", true, 1, true, false, false));
//                            else filterBeans.add(new FilterBean(Integer.toString(i) + "楼", false, 1, true, false, false));
////                        Messenger.getDefault().register(viewModel,"getData",MainActivityViewModel.class,(viewModel)->{
////                            viewModel.iniData(building,2,5, CommonPrefUtil.getStudentNumber());
////                        });
//                        }
                        filterBeans.add(new FilterBean("45楼", false, 1, true, true, false));
                        filterBeans.add(new FilterBean("46楼", true, 1, true, true, false));
                        for (int i = 31; i <= 55; i++) {
                            if ((i - 31) % 5 == 0 && i != 31)
                                filterBeans.add(new FilterBean(" ", false, 2, false, false, false));
                            if (i == 46)
                                filterBeans.add(new FilterBean(Integer.toString(i) + "楼", true, 1, true, false, false));
                            else
                                filterBeans.add(new FilterBean(Integer.toString(i) + "楼", false, 1, true, false, false));

                        }
                    } else {
                        filterBeans.add(new FilterBean("23楼", true, 1, true, true, false));
                        filterBeans.add(new FilterBean("26楼", false, 1, true, true, false));
                        for (int i = 1; i <= 30; i++) {
                            if ((i - 1) % 5 == 0 && i != 1)
                                filterBeans.add(new FilterBean(" ", false, 2, false, false, false));
                            if (i == 23)
                                filterBeans.add(new FilterBean(Integer.toString(i) + "楼", true, 1, true, false, false));
                            else
                                filterBeans.add(new FilterBean(Integer.toString(i) + "楼", false, 1, true, false, false));
                        }
                    }
                }
                break;
            case 2:
                if (!hasDisplayeds[1]) {
                    filterBeans2.clear();
                    filterBeans2.add(new FilterBean("现在", true, 2, true, true, false));
                    filterBeans2.add(new FilterBean("全天", false, 2, true, true, false));
                    for (int i = 1; i <= 12; i += 2) {
                        filterBeans2.add(new FilterBean(Integer.toString(i) + "-" + Integer.toString(i + 1) + "节", false, 2, true, false, false));
                    }
                }
                break;
            case 3:
                if (!hasDisplayeds[2]) {
                    filterBeans3.clear();
                    filterBeans3.add(new FilterBean("全部", true, 3, true, false, true));
                    filterBeans3.add(new FilterBean("空闲", false, 3, true, false, false));
                    filterBeans3.add(new FilterBean("电源", false, 3, true, false, false));
                    filterBeans3.add(new FilterBean("饮水机", false, 3, true, false, false));
                    filterBeans3.add(new FilterBean("暖气", false, 3, true, false, false));
                }
                break;
        }

    }

    public void updateData(FilterBean bean, List<FilterBean> filterBeans) {
        int i, j;
        i = -1;
        j = -1;
        for (FilterBean filterBean : filterBeans) {
            if (!filterBean.isTopLine)
                i++;
            else {
                j++;
                items.set(j, new PopupItemViewModel(viewModel, filterBean, filterBeans, this));
                continue;
            }
            ;
            if (filterBean == bean) continue;
            if (filterBean.hasClicked) {
                items2.set(i, new PopupItemViewModel(viewModel, filterBean, filterBeans, this));

            }

        }
    }

}

