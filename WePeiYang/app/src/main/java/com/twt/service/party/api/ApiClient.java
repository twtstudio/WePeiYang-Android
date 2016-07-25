package com.twt.service.party.api;

import com.twt.service.party.bean.Status;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dell on 2016/7/19.
 */
public class ApiClient {

    public static void loadStatus(String doWhat, String sno, Callback<Status> callback){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://www.twt.edu.cn/")
                .build();
        Api api = retrofit.create(Api.class);
        Call<Status> call= api.getStatus("api",doWhat,sno);
        call.enqueue(callback);
    }


}
