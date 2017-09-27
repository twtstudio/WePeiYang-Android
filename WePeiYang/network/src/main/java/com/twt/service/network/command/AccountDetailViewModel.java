package com.twt.service.network.command;

import android.support.v4.app.Fragment;

import com.kelin.mvvmlight.base.ViewModel;

/**
 * Created by chen on 2017/7/14.
 */

public class AccountDetailViewModel implements ViewModel {
    private Fragment mFragment;
    public String sum ;
    public String used ;
    public String rest ;

    public AccountDetailViewModel(Fragment fragment) {
        this.mFragment=fragment;
    }

}
