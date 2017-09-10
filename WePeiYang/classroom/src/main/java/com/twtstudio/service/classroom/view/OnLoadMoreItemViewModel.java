package com.twtstudio.service.classroom.view;

import android.databinding.ObservableField;

import com.kelin.mvvmlight.base.*;

/**
 * Created by DefaultAccount on 2017/8/25.
 */

public class OnLoadMoreItemViewModel implements com.kelin.mvvmlight.base.ViewModel {
    public final ObservableField<Boolean> showProgressBar=new ObservableField<>(true);
    public final ObservableField<Boolean> showError=new ObservableField<>(false);
    public OnLoadMoreItemViewModel(boolean showProgressBar,boolean showError){
        this.showProgressBar.set(showProgressBar);
        this.showError.set(showError);
    }

}
