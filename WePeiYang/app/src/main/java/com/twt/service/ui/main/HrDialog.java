package com.twt.service.ui.main;

import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.twt.service.R;
import com.twt.service.support.PrefUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by tjliqy on 2016/8/21.
 */
public class HrDialog extends DialogFragment {

    @InjectView(R.id.know_more)
    Button knowMore;
    @InjectView(R.id.notice_later)
    Button noticeLater;
    @InjectView(R.id.notice_never)
    Button noticeNever;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_main, container);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.know_more, R.id.notice_later, R.id.notice_never})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.know_more:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://coder.twtstudio.com/hr/"));
                startActivity(intent);
                break;
            case R.id.notice_later:
                PrefUtils.setShowDialog(true);
                break;
            case R.id.notice_never:
                PrefUtils.setShowDialog(false);
                break;
        }
        dismiss();
    }
}
