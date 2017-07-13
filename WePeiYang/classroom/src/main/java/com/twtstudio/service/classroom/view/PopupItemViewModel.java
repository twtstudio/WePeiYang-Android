package com.twtstudio.service.classroom.view;

import android.databinding.ObservableField;
import android.view.View;

import com.kelin.mvvmlight.base.*;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.kelin.mvvmlight.messenger.Messenger;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twtstudio.service.classroom.model.TimeHelper;

/**
 * Created by zhangyulong on 7/12/17.
 */

public class PopupItemViewModel implements com.kelin.mvvmlight.base.ViewModel{
    RxAppCompatActivity mRxActivity;
    public final ObservableField<String> text=new ObservableField<>();
    public final ObservableField<Boolean> changeTextColor=new ObservableField<>(false);
    private int tag;
    private static int building=0;
    private static int time=0;
    PopupItemViewModel(String text,boolean changeTextColor,RxAppCompatActivity mRxActivity,int tag) {
            this.mRxActivity=mRxActivity;
            this.changeTextColor.set(changeTextColor);
            this.text.set(text);
        building=Integer.parseInt("49");
//            switch (tag){
//                case 1:building=Integer.parseInt("47");break;
//                case 2:time=Integer.parseInt(text);break;
////                case 3:building=Integer.getInteger(text);break;
//            }
    }
    public void onClick(View view){
        if(time!=0)
        Messenger.getDefault().register(mRxActivity,"setData",MainActivityViewModel.class,(viewModel)->{
            viewModel.iniData(building, TimeHelper.getWeekInt(),time, CommonPrefUtil.getStudentNumber(),false);
        });
        else
            Messenger.getDefault().register(mRxActivity,"setData",MainActivityViewModel.class,(viewModel)->{
                viewModel.iniData(building, TimeHelper.getWeekInt(),TimeHelper.getTimeInt(), CommonPrefUtil.getStudentNumber(),false);
            });
    }
}
