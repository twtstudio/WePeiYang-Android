package com.twtstudio.service.classroom.view;

import android.databinding.ObservableField;

import com.kelin.mvvmlight.base.*;

/**
 * Created by zhangyulong on 7/12/17.
 */

public class PopupItemViewModel implements com.kelin.mvvmlight.base.ViewModel{
    public final ObservableField<String> text=new ObservableField<>();
    public final ObservableField<Boolean> changeTextColor=new ObservableField<>(false);
    PopupItemViewModel(String text,boolean changeTextColor) {
            this.changeTextColor.set(changeTextColor);
            this.text.set(text);
    }
}
