package com.twt.service.network.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.twt.service.network.R;
import com.twt.service.network.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lenovo on 2017/8/16.
 */

public class AccountPayDialog extends Dialog {
    private Context mContext;
    private OnCloseListener onCloseListener;
    private Unbinder mUnbinder;
    private static int mNum=1;
    private int m=0;

    @BindView(R2.id.dialog_account_rest)
    TextView restTv;
    @BindView(R2.id.dialog_account_arrow_left)
    Button arrowLeft;
    @BindView(R2.id.dialog_account_arrow_right)
    Button arrowRight;
    @BindView(R2.id.dialog_account_num)
    TextView num;
    @BindView(R2.id.dialog_account_ok)
    Button positiveButton;
    @BindView(R2.id.dialog_account_cancel)
    Button negativeButton;

    public AccountPayDialog(Context context) {
        super(context);
        this.mContext=context;
    }

    public AccountPayDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext=context;
    }
    public AccountPayDialog(Context context,int themeResId,OnCloseListener listener){
        super(context,themeResId);
        this.mContext=context;
        this.onCloseListener=listener;
    }
    protected AccountPayDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_account_pay);
        mUnbinder= ButterKnife.bind(this);
        arrowLeft.setOnClickListener(v -> {
            if (mNum>1){
                mNum--;
                num.setText(String.valueOf(mNum));
            }
        });
        arrowRight.setOnClickListener(v -> {
            mNum++;
            num.setText(String.valueOf(mNum));
        });
        positiveButton.setOnClickListener(v -> onCloseListener.onClick(AccountPayDialog.this, true));
        negativeButton.setOnClickListener(v -> onCloseListener.onClick(AccountPayDialog.this, false));

    }

    public int getmNum(){
        return mNum;
    }

    public interface OnCloseListener{
        void onClick(Dialog dialog, boolean confirm);
    }

}
