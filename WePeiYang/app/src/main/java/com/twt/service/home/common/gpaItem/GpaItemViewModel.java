package com.twt.service.home.common.gpaItem;

import android.app.ActivityOptions;
import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Build;
import android.view.View;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.twtstudio.retrox.gpa.GpaBean;
import com.twtstudio.retrox.gpa.GpaProvider;
import com.twtstudio.retrox.gpa.view.GpaActivity;
import com.twt.service.base.BaseActivity;


/**
 * Created by retrox on 2017/1/21.
 */

public class GpaItemViewModel implements ViewModel {

    private BaseActivity mContext;

    public final ObservableField<GpaBean> observableGpa = new ObservableField<>();

    public final ReplyCommand<View> replyCommand = new ReplyCommand<View>(this::jumpToDetail);

    public GpaItemViewModel(BaseActivity context) {
        mContext = context;
        getData();
    }

    public void getData() {
        GpaProvider.init(mContext)
                .registerAction(observableGpa::set)
                .getData();
    }

    private void jumpToDetail(View view) {
        Intent intent = new Intent(mContext, GpaActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(mContext, view,
                    mContext.getResources().getString(com.twtstudio.retrox.gpa.R.string.gpa_transition_name));
            mContext.startActivity(intent, activityOptions.toBundle());
        } else {
            mContext.startActivity(intent);
        }

    }

}
