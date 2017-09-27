package com.twt.service.network.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.twt.service.network.R;
import com.twt.service.network.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by chen on 2017/7/14.
 */

public class SpyVertifyDialog extends Dialog{
    @BindView(R2.id.dialog_wifi_title)
    TextView title;
    @BindView(R2.id.dialog_wifi_studentid)
    TextView studentId;
    @BindView(R2.id.dialog_wifi_vertify_iv)
    ImageView vertifyIv;
    @BindView(R2.id.dialog_wifi_vertify_et)
    EditText vertifyEv;
    @BindView(R2.id.dialog_wifi_cancel)
    Button negativeButton;
    @BindView(R2.id.dialog_wifi_ok)
    Button positiveButton;
    private Context mContext;
    private Unbinder unbinder;
    private OnCloseListener onCloseListener;

    public SpyVertifyDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public SpyVertifyDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public SpyVertifyDialog(Context context, int themeResId, OnCloseListener closeListener) {
        super(context, themeResId);
        this.mContext = context;
        this.onCloseListener=closeListener;
    }

    protected SpyVertifyDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_spy_verify);
        setCanceledOnTouchOutside(true);
        unbinder = ButterKnife.bind(this);
        positiveButton.setOnClickListener(v -> onCloseListener.onClick(SpyVertifyDialog.this, true));
        negativeButton.setOnClickListener(v -> onCloseListener.onClick(SpyVertifyDialog.this, false));
    }

    public interface OnCloseListener {
        void onClick(Dialog dialog, boolean confirm);
    }

}
