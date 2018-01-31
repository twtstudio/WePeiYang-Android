package com.twt.wepeiyang.commons.network;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;
import com.twt.wepeiyang.commons.utils.App;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import es.dmoral.toasty.Toasty;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by retrox on 2017/2/4.
 */

public class RxErrorHandler implements Action1<Throwable> {

    private static Toast toast;
    private Context mContext;

    public RxErrorHandler(Context context) {
        mContext = context;
    }

    public RxErrorHandler() {
        mContext = App.getApplicationContext();
    }

    public static void showToast(Context context, String desc) {
        if (toast == null) {
            toast = Toast.makeText(context, desc, Toast.LENGTH_SHORT);
        } else {
            toast.setText(desc);
        }
        toast.show();
    }

    @Override
    public void call(Throwable throwable) {

        if (throwable instanceof IOException) {
            IOException error = (IOException) throwable;
//            Toasty.error(mContext, "网络错误", Toast.LENGTH_SHORT).show();
            showToast(mContext,"网络错误");
            postThrowable(error);
            Logger.e(error, "error");
        } else if (throwable instanceof HttpException) {
            HttpException exception = (HttpException) throwable;
            postThrowable(exception);
            Logger.e(exception, "http_error");
//            Toasty.error(mContext, "错误码：" + exception.response().code(), Toast.LENGTH_SHORT).show();
            showToast(mContext,"错误码：" + exception.response().code());
            try {
                String errorJson = exception.response().errorBody().string();
                Logger.e(errorJson);
                JSONObject errJsonObject = new JSONObject(errorJson);
                int errcode = errJsonObject.getInt("error_code");
                String message = errJsonObject.getString("message");
                Logger.e("错误码：" + errcode + "  message:" + message);
                postThrowable(new ApiException(errcode, message));
//                Toasty.error(mContext, "错误：" + message, Toast.LENGTH_SHORT).show();
                showToast(mContext,"错误：" + message);
                handleApiError(errcode);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        } else {
            postThrowable(throwable);
            throwable.printStackTrace();
        }
    }

    /**
     * post throwable to server
     *
     * @param throwable
     */
    private void postThrowable(Throwable throwable) {
//        BuglyLog.e("捕获的异常",throwable.getMessage(),throwable);
        CrashReport.postCatchedException(throwable);
    }

    private void handleApiError(int err_code) {
        switch (err_code) {
            case 10000:
            case 10001:
            case 10002:
                break;
            case 10004:
//                Toasty.warning(mContext, "请重新登录", Toast.LENGTH_SHORT).show();
                showToast(mContext,"请重新登录");
                Class clazz = null;
                try {
                    clazz = Class.forName("com.twtstudio.retrox.auth.login.LoginActivity");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (mContext instanceof Activity) {
                    ((Activity) mContext).finish();
                }
                Intent intent = new Intent(mContext, clazz);

                /**
                 * try to solve the problem :
                 * android.util.AndroidRuntimeException:Calling startActivity() from outside of an Activity context requires the FLAG_ACTIVITY_NEW_TASK flag.
                 * Is this really what you want?
                 */
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                Logger.e("请重新登录");
                break;
            case 10003:
                Logger.e("refresh Token!");
                RetrofitProvider.getRetrofit().create(CommonApi.class)
                        .refreshToken().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(body -> Toasty.info(mContext, "正在自动重试...", Toast.LENGTH_SHORT).show())
                        .subscribe(body -> {
                            try {
                                String s = body.string();
                                JSONObject jsonObject = new JSONObject(s);
                                String token = jsonObject.getString("data");
                                CommonPrefUtil.setToken(token);
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
//                            Toasty.info(mContext, "刷新试试哦", Toast.LENGTH_SHORT).show();
                            showToast(mContext,"刷新试试哦");
                        }, this);
                break;
            case 20001:
//                Toasty.error(mContext, "绑定办公网啊哥", Toast.LENGTH_SHORT).show();
                showToast(mContext,"绑定办公网啊哥");
                break;
            default:

                break;
        }
    }
}