package com.twtstudio.tjliqy.party;

import android.content.Context;

import com.twt.wepeiyang.commons.experimental.CommonContext;

/**
 * Created by sunjuntao on 15/11/15.
 */
public class WePeiYangAppOld {

    public static Context getContext() {
        return CommonContext.INSTANCE.getApplication().getApplicationContext();
    }

}
