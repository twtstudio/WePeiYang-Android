package com.example.caokun.fellowsearch.presenter;

import android.content.Context;

import com.example.caokun.fellowsearch.api.OnNextListener;
import com.example.caokun.fellowsearch.common.Presenter;
import com.example.caokun.fellowsearch.model.FellowApiClient;
import com.example.caokun.fellowsearch.model.FellowApiSubscriber;
import com.example.caokun.fellowsearch.model.FellowController;
import com.example.caokun.fellowsearch.model.FellowfindController;
import com.example.caokun.fellowsearch.model.Institute;
import com.example.caokun.fellowsearch.model.Major;
import com.example.caokun.fellowsearch.model.Province;
import com.example.caokun.fellowsearch.model.Senior;
import com.example.caokun.fellowsearch.model.Student;

import java.util.List;

/**
 * Created by caokun on 2017/2/21.
 */

public class FellowPresenter extends Presenter {
    FellowController mcontroller;
    FellowfindController fellowfindController;
    public FellowPresenter(Context context,FellowController controller){
        super(context);
        mcontroller=controller;
    }
    public FellowPresenter(Context context,FellowfindController controller){
        super(context);
        fellowfindController=controller;
    }
    public void getStudent(String province,String institute,String major,String senior){
        FellowApiClient.getInstance().getStudent(mContext,new FellowApiSubscriber(mContext,mStudentOnNextListener),province,institute,major,senior);
    }
    public void getProvince(){
        FellowApiClient.getInstance().getProvience(mContext,new FellowApiSubscriber(mContext,mProvinceOnNextListener));
    }
    public void getInstitute(String province){
        FellowApiClient.getInstance().getInstitute(mContext,new FellowApiSubscriber(mContext,mInstituteOnNextListener),province);
    }
    public void getMajor(String province,String institute){
        FellowApiClient.getInstance().getMajor(mContext,new FellowApiSubscriber(mContext,mMajorOnNextListener),province,institute);
    }
    public void getSenior(String province){
        FellowApiClient.getInstance().getSenior(mContext,new FellowApiSubscriber(mContext,mSeniorOnNextListener),province);
    }




    private OnNextListener<List<Student>> mStudentOnNextListener=new OnNextListener<List<Student>>() {
        @Override
        public void onNext(List<Student> students) {
            mcontroller.bindStudentData(students);
        }
    };
    private OnNextListener<List<Province>> mProvinceOnNextListener=new OnNextListener<List<Province>>() {
        @Override
        public void onNext(List<Province> provinces) {
            fellowfindController.bindAllProvince(provinces);
        }
    };
    private OnNextListener<List<Institute>> mInstituteOnNextListener=new OnNextListener<List<Institute>>() {
        @Override
        public void onNext(List<Institute> institutes) {
            fellowfindController.bindAllInstitute(institutes);
        }
    };
    private OnNextListener<List<Major>> mMajorOnNextListener=new OnNextListener<List<Major>>() {
        @Override
        public void onNext(List<Major> majors) {
            fellowfindController.bindAllMajor(majors);
        }
    };
    private OnNextListener<List<Senior>> mSeniorOnNextListener=new OnNextListener<List<Senior>>() {
        @Override
        public void onNext(List<Senior> seniors) {
            fellowfindController.bindAllSenior(seniors);
        }
    };
}
