package com.rex.wepeiyang.interactor;

import com.rex.wepeiyang.api.ApiClient;
import com.rex.wepeiyang.bean.ClassTable;
import com.rex.wepeiyang.ui.schedule.OnGetScheduleCallback;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sunjuntao on 15/12/5.
 */
public class ScheduleInteractorImpl implements ScheduleInteractor {
    @Override
    public void getSchedule(String tjuuname, String tjupasswd, final OnGetScheduleCallback onGetScheduleCallback) {
        ApiClient.getClassTable(tjuuname, tjupasswd, new Callback<ClassTable>() {
            @Override
            public void success(ClassTable classTable, Response response) {
                onGetScheduleCallback.onSuccess(classTable);
            }

            @Override
            public void failure(RetrofitError error) {
                onGetScheduleCallback.onFailure(error.getMessage());
            }
        });
    }
}
