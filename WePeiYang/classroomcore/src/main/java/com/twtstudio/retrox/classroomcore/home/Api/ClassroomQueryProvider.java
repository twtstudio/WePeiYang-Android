package com.twtstudio.retrox.classroomcore.home.Api;

import android.content.Context;

import com.twt.wepeiyang.commons.network.DefaultRetrofitBuilder;

/**
 * Created by retrox on 2017/2/23.
 */

public class ClassroomQueryProvider {

    private ClassroomApi api;

    private Context mContext;

    public ClassroomQueryProvider(Context mContext) {
        this.mContext = mContext;
        api = DefaultRetrofitBuilder.getBuilder()
                .baseUrl("http://120.27.115.59/test_laravel/public/index.php/api/")
                .build().create(ClassroomApi.class);
    }

    public void getClassRoom(){

    }
}
