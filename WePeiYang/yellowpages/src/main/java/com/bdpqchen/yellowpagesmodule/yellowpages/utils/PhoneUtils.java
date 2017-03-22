package com.bdpqchen.yellowpagesmodule.yellowpages.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import com.bdpqchen.yellowpagesmodule.yellowpages.activity.FeedbackActivity;
import com.orhanobut.logger.Logger;

import static com.bdpqchen.yellowpagesmodule.yellowpages.activity.FeedbackActivity.INTENT_FEEDBACK_PHONE_NAME;
import static com.bdpqchen.yellowpagesmodule.yellowpages.activity.FeedbackActivity.INTENT_FEEDBACK_PHONE_NUM;


/**
 * Created by bdpqchen on 17-3-3.
 */

public class PhoneUtils {

    public static void ringUp(Context context, String phoneNum){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNum));
        context.startActivity(intent);
    }

    public static void permissionCheck(Context context, String phoneNum, int requestCode, Fragment fragment) {
        Activity activity = (Activity) context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int checkCallPhonePermission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.CALL_PHONE);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                //When using the Support library, you have to use the correct method calls.
                //http://stackoverflow.com/questions/32714787/android-m-permissions-onrequestpermissionsresult-not-being-called
                if (null == fragment){
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, requestCode);
                }else{
                    fragment.requestPermissions(new String[]{android.Manifest.permission.CALL_PHONE}, requestCode);
                }
            }else{
                ringUp(context, phoneNum);
            }
        }else{
            PhoneUtils.ringUp(context, phoneNum);
        }
    }

    public static void permissionCheck(Context context, String phoneNum, String phoneName, int requestCode, Fragment fragment) {
        Activity activity = (Activity) context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int checkWritePhonePermission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_CONTACTS);
            if (checkWritePhonePermission != PackageManager.PERMISSION_GRANTED){
                //When using the Support library, you have to use the correct method calls.
                //http://stackoverflow.com/questions/32714787/android-m-permissions-onrequestpermissionsresult-not-being-called
                if (null == fragment){
                    ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.WRITE_CONTACTS}, requestCode);
                }else{
                    fragment.requestPermissions(new String[]{android.Manifest.permission.WRITE_CONTACTS}, requestCode);
                }
            }else{
                Logger.i("utils to write the phone");
                insertContact(context, phoneName, phoneNum);
            }
        }else{
            insertContact(context, phoneName, phoneNum);
            Logger.i("version is lower enough to write the phone");
        }
    }

    public static void insertContact(Context context, String name, String phone){
        Logger.i("begin to insert the data");
        name += "_微北洋黄页";
        ContentValues values = new ContentValues();
        // 首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获取系统返回的rawContactId
        Uri rawContactUri = context.getContentResolver().insert(
                ContactsContract.RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContactUri);

        // 往data表入姓名数据
        values.put(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
        context.getContentResolver().insert(
                android.provider.ContactsContract.Data.CONTENT_URI, values);
        values.put(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone);
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE);
        context.getContentResolver().insert(
                android.provider.ContactsContract.Data.CONTENT_URI,
                values);
        ToastUtils.show((Activity) context, "添加成功");
    }

    public static void copyToClipboard(Context mContext, String phoneStr){
        ClipboardManager cmb = (ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setPrimaryClip(ClipData.newPlainText(null, phoneStr));
        ToastUtils.show((Activity) mContext, "已复制到剪切板");
    }

    public static void feedbackPhone(Context mContext, String name, String phone) {
        Intent intent = new Intent(mContext, FeedbackActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(INTENT_FEEDBACK_PHONE_NAME, name);
        bundle.putString(INTENT_FEEDBACK_PHONE_NUM, phone);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }
}
