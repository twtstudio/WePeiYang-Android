package com.twt.service.network.command;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.twt.service.network.R;
import com.twt.service.network.dialog.AccountPayDialog;
import com.twt.service.network.dialog.SpyVertifyDialog;

/**
 * Created by chen on 2017/7/14.
 */

public class AccountPayCommand implements ViewModel {
    private String restRMb;
    private Button mBuyCommand;
    private Fragment mFragment;

    public AccountPayCommand(android.support.v4.app.Fragment fragment) {
        this.mFragment = fragment;
    }

    public void onBuyCommand() {
        new SpyVertifyDialog(mFragment.getContext(), R.style.Dialog, new SpyVertifyDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    dialog.dismiss();
                    new AccountPayDialog(mFragment.getContext(), R.style.Dialog, (dialog1, confirm1) -> {
                        if (confirm1) {
                            Toast.makeText(mFragment.getContext(), "ღ( ´･ᴗ･` )", Toast.LENGTH_SHORT).show();
                        }
                        if (!confirm1) {
                            dialog1.dismiss();
                        }
                    }).show();
                }
                if (!confirm) {
                    dialog.dismiss();
                }
            }
        }).show();
    }


}
