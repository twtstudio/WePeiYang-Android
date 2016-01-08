package com.rex.wepeiyang.ui.main;

import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rex.wepeiyang.R;
import com.rex.wepeiyang.bean.Update;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sunjuntao on 16/1/8.
 */
public class UpdateDialogFragment extends DialogFragment implements View.OnClickListener {
    @InjectView(R.id.tv_update_version)
    TextView tvUpdateVersion;
    @InjectView(R.id.tv_change_log)
    TextView tvChangeLog;
    @InjectView(R.id.tv_cancle)
    TextView tvCancle;
    @InjectView(R.id.tv_update)
    TextView tvUpdate;
    private Update update;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_dialog, container, false);
        ButterKnife.inject(this, view);
        update = (Update) getArguments().getSerializable("update");
        if (update != null) {
            tvUpdateVersion.setText(update.versionShort);
            tvChangeLog.setText(update.changelog);
        }
        tvCancle.setOnClickListener(this);
        tvUpdate.setOnClickListener(this);
        getDialog().setTitle("更新");
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_update:
                Uri uri = Uri.parse(update.install_url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                dismiss();
                break;
            case R.id.tv_cancle:
                dismiss();
                break;
        }
    }
}
