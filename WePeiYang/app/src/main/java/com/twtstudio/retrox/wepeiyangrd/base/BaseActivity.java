package com.twtstudio.retrox.wepeiyangrd.base;

import com.kelin.mvvmlight.messenger.Messenger;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by retrox on 2016/11/27.
 */

public class BaseActivity extends SupportActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Messenger.getDefault().unregister(this);
    }
}
