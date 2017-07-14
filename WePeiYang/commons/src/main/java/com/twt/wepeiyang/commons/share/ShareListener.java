package com.twt.wepeiyang.commons.share;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import es.dmoral.toasty.Toasty;

/**
 * Created by tjliqy on 2017/7/3.
 */

public class ShareListener implements UMShareListener {
    private Context mContext;
    private UMShareListener umShareListener;
    public ShareListener(Context context) {
        mContext = context;
    }

    public ShareListener(Context context,UMShareListener umShareListener){
        this(context);
        this.umShareListener = umShareListener;
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {
        if(umShareListener != null){
            umShareListener.onStart(share_media);
        }
    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
        Toasty.success(mContext, share_media + " 分享成功啦").show();
        if(umShareListener != null){
            umShareListener.onResult(share_media);
        }
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        try {
            String[] split = throwable.getMessage().split(" ");
            split = split[0].split("：");
            if (split[1].equals("2003")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int checkWritePhonePermission = ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (checkWritePhonePermission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) mContext, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Toasty.error(mContext, share_media + " 分享失败啦 \n" + throwable.getMessage()).show();
        }

        if (umShareListener != null){
            umShareListener.onError(share_media,throwable);
        }
    }


    @Override
    public void onCancel(SHARE_MEDIA share_media) {
        Toasty.info(mContext, share_media + " 分享取消啦").show();
        if(umShareListener != null){
            umShareListener.onCancel(share_media);
        }
    }

}
