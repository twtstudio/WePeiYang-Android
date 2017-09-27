package com.twt.service.network.command;

import android.app.Dialog;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.twt.service.network.R;
import com.twt.service.network.dialog.AccountPayDialog;
import com.twt.service.network.dialog.SpyVertifyDialog;

/**
 * Created by chen on 2017/7/14.
 */

public class AccountPayViweModel implements ViewModel {
    private android.support.v4.app.Fragment mFragment;
    public String restRMB ;
    public final ReplyCommand onBuyCommand = new ReplyCommand(() ->
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
            }).show());
    public final ReplyCommand onPayCommand = new ReplyCommand(() ->
            Toast.makeText(mFragment.getContext(), "典哥说这个先不做", Toast.LENGTH_SHORT).show());

    public AccountPayViweModel(android.support.v4.app.Fragment fragment) {
        this.mFragment = fragment;
    }


}
