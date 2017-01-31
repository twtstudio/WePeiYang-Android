package com.twtstudio.retrox.wepeiyangrd.home.common.gpaItem;

import android.databinding.ObservableField;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.messenger.Messenger;
import com.twtstudio.retrox.gpa.GpaBean;
import com.twtstudio.retrox.gpa.GpaProvider;
import com.twtstudio.retrox.wepeiyangrd.base.BaseActivity;


/**
 * Created by retrox on 2017/1/21.
 */

public class GpaItemViewModel implements ViewModel {

    private BaseActivity mContext;

    public final ObservableField<GpaBean> observableGpa = new ObservableField<>();

    public GpaItemViewModel(BaseActivity context) {
        mContext = context;
        getData();
    }

    public void getData() {
        GpaProvider.init(mContext)
                .registerAction(observableGpa::set)
                .getData();
    }


}
