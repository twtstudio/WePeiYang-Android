package com.twtstudio.retrox.gpa.view;

import com.kelin.mvvmlight.base.ViewModel;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by tjliqy on 2017/6/2.
 */

public class EvaluateDetailViewModel implements ViewModel {

    private RxAppCompatActivity mRxActivity;


    public EvaluateDetailViewModel(RxAppCompatActivity rxActivity) {
        mRxActivity = rxActivity;
    }
}
