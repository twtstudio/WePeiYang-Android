package com.twt.wepeiyang.commons.network;

import android.content.Context;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.twt.wepeiyang.commons.utils.App;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by retrox on 2017/2/4.
 */

public class RxErrorHandler implements Action1<Throwable> {

    private Context mContext;

    public RxErrorHandler(Context context) {
        mContext = context;
    }

    public RxErrorHandler() {
        mContext = App.getApplicationContext();
    }

    @Override
    public void call(Throwable throwable) {
        if (throwable instanceof IOException) {
            IOException error = (IOException) throwable;
            Toast.makeText(mContext, "网络错误", Toast.LENGTH_SHORT).show();
            Logger.e(error, "error");
        } else if (throwable instanceof HttpException) {
            HttpException exception = (HttpException) throwable;
            Logger.e(exception, "http_error");
            try {

                String errorJson = exception.response().errorBody().string();
                Logger.e(errorJson);
                JSONObject errJsonObject = new JSONObject(errorJson);
                int errcode = errJsonObject.getInt("error_code");
                String message = errJsonObject.getString("message");
                Logger.e("错误码：" + errcode + "  message:" + message);
                Toast.makeText(mContext, "错误：" + message, Toast.LENGTH_SHORT).show();
                handleApiError(errcode);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        } else {
            throwable.printStackTrace();
        }
    }

    private void handleApiError(int err_code) {
        switch (err_code) {
            case 10000:
            case 10001:
            case 10002:
                break;
            case 10004:
                Toast.makeText(mContext, "请重新登录", Toast.LENGTH_SHORT).show();
                Logger.e("请重新登录");
                break;
            case 10003:
                Logger.e("refresh Token!");
                RetrofitProvider.getRetrofit().create(CommonApi.class)
                        .refreshToken().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(body -> Toast.makeText(mContext, "正在自动重试...", Toast.LENGTH_SHORT).show())
                        .subscribe(body -> {
                            try {
                                String s = body.string();
                                JSONObject jsonObject = new JSONObject(s);
                                String token = jsonObject.getString("data");
                                CommonPrefUtil.setToken(token);
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(mContext, "刷新试试哦", Toast.LENGTH_SHORT).show();
                        }, this);
                break;
            case 20001:
                Logger.e("绑定办公网啊哥");
                break;
            default:

                break;
        }
    }
}