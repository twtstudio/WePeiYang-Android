package com.twt.wepeiyang.commons.network;

import android.content.Context;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import rx.functions.Action1;

/**
 * Created by retrox on 2017/2/4.
 */

public class RxErrorHandler implements Action1<Throwable> {

    private Context mContext;

    public RxErrorHandler(Context context) {
        mContext = context;
    }

    @Override
    public void call(Throwable throwable) {
        if (throwable instanceof IOException) {
            IOException error = (IOException) throwable;
            Toast.makeText(mContext, "网络错误", Toast.LENGTH_SHORT).show();
            Logger.e(error, "error");
        } else if (throwable instanceof ApiException) {
            ApiException apiException = (ApiException) throwable;
            /**
             * test code
             */
//            Toast.makeText(mContext, e.error_code, Toast.LENGTH_SHORT).show();
            Logger.e(apiException, apiException.message);
            switch (apiException.error_code) {
                case 10000:
                case 10001:
                case 10002:
                case 10004:
//                            view.toastMessage("请重新登录");
//                            PrefUtils.setLogin(false);
//                            PrefUtils.removeToken();
//                            view.startLoginActivity();
                    Logger.e("请重新登录");
                    break;
                case 10003:
                    Logger.e("refresh Token!");
                    break;
                case 20001:
                    Logger.e("绑定办公网啊哥");
                    break;
                default:
                    Logger.e(apiException.message);
                    break;
            }
        } else {
            throwable.printStackTrace();
        }
    }
}