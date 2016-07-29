package com.twt.service.party.api;

import android.util.Log;

import com.twt.service.party.bean.Status;
import com.twt.service.party.bean.UserInfomation;
import com.twt.service.support.PrefUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dell on 2016/7/19.
 */
public class ApiClient {

    private static Api api;
    static {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://www.twt.edu.cn/")
                .build();
        api = retrofit.create(Api.class);
    }
    public static void loadUserInfomation(Callback<UserInfomation> callback){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://open.twtstudio.com/api/v2/")
                .build();
        Api api = retrofit.create(Api.class);
        String token = PrefUtils.getToken().split("\\{")[1];
        token = token.split("}")[0];
        Call<UserInfomation> call = api.getInfomation(token);
        call.enqueue(callback);
    }

    public static void loadStatus(String doWhat, String sno, Callback<Status> callback){
        Call<Status> call= api.getStatus("api",doWhat,sno);
        call.enqueue(callback);
    }
}
