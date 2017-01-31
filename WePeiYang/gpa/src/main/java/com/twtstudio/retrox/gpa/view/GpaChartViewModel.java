package com.twtstudio.retrox.gpa.view;

import android.databinding.ObservableField;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.twtstudio.retrox.gpa.GpaBean;

/**
 * Created by retrox on 2017/1/28.
 */

public class GpaChartViewModel implements ViewModel {

    public final ObservableField<GpaBean> observableGpaBean = new ObservableField<>();

    public final ReplyCommand<Integer> valueSelectCommand = new ReplyCommand<Integer>(this::execute);

    private ReplyCommand<Integer> proxy = null;

    public GpaChartViewModel(GpaBean gpaBean) {
        observableGpaBean.set(gpaBean);
    }

    public GpaChartViewModel() {
    }

    public void setProxy(ReplyCommand<Integer> proxy) {
        this.proxy = proxy;
    }

    private void execute(int index){
        if (proxy != null){
            proxy.execute(index);
        }
    }
}
