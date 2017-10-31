package com.twt.service.network.command;

import android.support.v4.app.Fragment;

/**
 * Created by chen on 2017/7/14.
 */

public class AccountDetailCommand{
    private Fragment mFragment;
    private String sum ;
    private String used ;
    private String rest ;

    public AccountDetailCommand(Fragment fragment) {
        this.mFragment=fragment;
    }

}
