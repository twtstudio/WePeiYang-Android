package com.bdpqchen.yellowpagesmodule.yellowpages.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.bdpqchen.yellowpagesmodule.yellowpages.R;
import com.bdpqchen.yellowpagesmodule.yellowpages.R2;
import com.bdpqchen.yellowpagesmodule.yellowpages.base.BaseActivity;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.Feedback;
import com.bdpqchen.yellowpagesmodule.yellowpages.network.NetworkClient;
import com.bdpqchen.yellowpagesmodule.yellowpages.utils.AppActivityManager;
import com.bdpqchen.yellowpagesmodule.yellowpages.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by bdpqchen on 17-3-10.
 */

public class FeedbackActivity extends BaseActivity {
    public static final String INTENT_FEEDBACK_PHONE_NAME = "phone_name";
    public static final String INTENT_FEEDBACK_PHONE_NUM = "phone_num";

    @BindView(R2.id.toolbar)
    Toolbar mToolbar;
    @BindView(R2.id.et_name)
    EditText mEtName;
    @BindView(R2.id.et_phone)
    EditText mEtPhone;
    @BindView(R2.id.spinner)
    AppCompatSpinner mSpinner;
    @BindView(R2.id.btn_submit)
    Button mSubmit;

    private String mName;
    private String mPhone;
    private int mFeedbackType = 1;
    private Context mContext;


    @Override
    public int getLayout() {
        return R.layout.yp_activity_feedback;
    }

    @Override
    protected Toolbar getToolbar() {
        mToolbar.setTitle("号码反馈");
        return mToolbar;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        Bundle bundle = getIntent().getExtras();
        mName = bundle.getString(INTENT_FEEDBACK_PHONE_NAME);
        mPhone = bundle.getString(INTENT_FEEDBACK_PHONE_NUM);
        mEtName.setText(mName);
        mEtPhone.setText(mPhone);
        mSubmit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.show((Activity) mContext, "感谢您的反馈，我们会尽快核实信息的有效性");
                        submitFeedback();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                AppActivityManager.getActivityManager().finishCurrentActivity();
                            }
                        }, 400);
                    }
                }
        );
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Logger.i(String.valueOf(position));
                mFeedbackType = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void submitFeedback() {
        Subscriber subscriber = new Subscriber<Feedback>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.i("onError()---submit");
            }

            @Override
            public void onNext(Feedback f) {
                Logger.i("onNext---->submitted");

            }
        };
        NetworkClient.getInstance().submitFeedback(subscriber, mFeedbackType, mPhone, mName);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);

    }


}
