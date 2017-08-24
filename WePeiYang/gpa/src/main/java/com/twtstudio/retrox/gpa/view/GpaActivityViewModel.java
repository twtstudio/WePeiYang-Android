package com.twtstudio.retrox.gpa.view;

import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.kelin.mvvmlight.messenger.Messenger;
import com.tencent.bugly.crashreport.CrashReport;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twtstudio.retrox.gpa.BR;
import com.twtstudio.retrox.gpa.GpaBean;
import com.twtstudio.retrox.gpa.GpaProvider;
import com.twtstudio.retrox.gpa.R;

import java.util.ArrayList;

import me.tatarka.bindingcollectionadapter.ItemViewSelector;

import me.tatarka.bindingcollectionadapter.itemviews.ItemViewClassSelector;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;
import me.tatarka.bindingcollectionadapter2.collections.MergeObservableList;

/**
 * Created by retrox on 2017/1/28.
 */

public class GpaActivityViewModel implements ViewModel {

    public static final String TOKEN = "refresh_gpa";
    private RxAppCompatActivity mRxActivity;

    public final ObservableField<GpaBean> obGpaBean = new ObservableField<>();

    /**
     * 非header的item结合
     */
    public final ObservableArrayList<ViewModel> mViewModels = new ObservableArrayList<>();

    public final GpaChartViewModel headerViewModel = new GpaChartViewModel();

    public final MergeObservableList<ViewModel> mMergeObservableList = new MergeObservableList<>();

    public GpaActivityViewModel(RxAppCompatActivity rxActivity) {
        mRxActivity = rxActivity;

        Messenger.getDefault().register(rxActivity,TOKEN,()->{
            getGpaData(true);
        });
    }

    public final ReplyCommand<Integer> valueSelectCommand = new ReplyCommand<>(this::setTermIndex);

    public final ObservableBoolean isNeedEvaluate = new ObservableBoolean(false);

    public final ReplyCommand evaluateClick = new ReplyCommand(this::onEvaluateClick);

//    public final ItemBinding itemBinding = ItemBinding.of();

    public final OnItemBind<ViewModel> onItemBind = new OnItemBind<ViewModel>() {
        @Override
        public void onItemBind(ItemBinding itemBinding, int position, ViewModel item) {
            if (item instanceof GpaChartViewModel){
                itemBinding.set(BR.viewModel,R.layout.gpa_item_chart);
            }else if (item instanceof TermBriefViewModel){
                itemBinding.set(BR.viewModel,R.layout.gpa_item_term_brief);
            }else if (item instanceof TermDetailViewModel){
                itemBinding.set(BR.viewModel,R.layout.gpa_item_term);
            }
//            itemBinding.set(BR.item, position == 0 ? R.layout.item_header : R.layout.item);
        }
    };

//    public final ItemViewSelector itemView = ItemViewClassSelector.builder()
//            .put(GpaChartViewModel.class, BR.viewModel, R.layout.gpa_item_chart)
//            .put(TermBriefViewModel.class,BR.viewModel,R.layout.gpa_item_term_brief)
//            .put(TermDetailViewModel.class,BR.viewModel,R.layout.gpa_item_term)
//            .build();

    ArrayList<GpaBean.Term.Course> unEvaluatedCourses;

    public void getGpaData(boolean update){
        /**
         * for animation use
         * shared elements
         */


        GpaProvider.init(mRxActivity)
                .registerAction(gpaBean -> {

                    CommonPrefUtil.setGpaToken(gpaBean.session);
                    if (mMergeObservableList.size()!=0){
                        mMergeObservableList.removeItem(headerViewModel);
                        mMergeObservableList.removeList(mViewModels);
                    }

                    mMergeObservableList.insertItem(headerViewModel);


                    unEvaluatedCourses = new ArrayList<>();
                    for (GpaBean.Term term:gpaBean.data) {
                        for(GpaBean.Term.Course course: term.data){
                            if(course.score == -1){
                                unEvaluatedCourses.add(course);
                            }
                        }
                    }

                    if (unEvaluatedCourses.size() != 0){
                        isNeedEvaluate.set(true);
                    }else {
                        isNeedEvaluate.set(false);
                    }

                    headerViewModel.observableGpaBean.set(gpaBean);
                    headerViewModel.setProxy(valueSelectCommand);
                    obGpaBean.set(gpaBean);
//                    mMergeObservableList.insertItem(headerViewModel);
                    mMergeObservableList.insertList(mViewModels);
                    // TODO: 07/04/2017 也许明年大一的处理？ 不知道为什么后台返回空数据
                    try {
                        setTermIndex(gpaBean.data.size()-1);
                    }catch (IndexOutOfBoundsException e){
                        CrashReport.postCatchedException(e);
                    }
                })
                .getData(update);
    }

    public void getGpaData(){
        getGpaData(true);
    }

    public void setTermIndex(int index){
        GpaBean gpaBean = obGpaBean.get();
        mViewModels.clear();
        mViewModels.add(new TermBriefViewModel(gpaBean.data.get(index)));
        mViewModels.add(new TermDetailViewModel(gpaBean.data.get(index)));
    }

    public void onEvaluateClick(){
        Intent intent = new Intent(mRxActivity,EvaluateListActivity.class);
        intent.putExtra("key",unEvaluatedCourses);
        mRxActivity.startActivity(intent);
    }

}
