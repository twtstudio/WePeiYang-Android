package com.twtstudio.service.classroom.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twtstudio.service.classroom.R;
//@Route(path = "/classroom/main")
public class ClassroomQueryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom_query);
//        viewModel.iniData(46,21,3, CommonPrefUtil.getStudentNumber());
//        mainBinding.setViewModel(viewModel);
    }
}
