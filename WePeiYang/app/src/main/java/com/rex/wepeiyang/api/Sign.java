package com.rex.wepeiyang.api;

import android.util.Log;

import com.facebook.common.util.Hex;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by sunjuntao on 15/11/5.
 */
public class Sign {

    public String generate(HashMap<String, String> params) {
        String paramsString = dictSort(params);//获取字典排序后的参数列表
        paramsString = Constant.APPKEY + paramsString + Constant.APPSECRET;
        Log.e("paramsString", paramsString);
        String result = Hex.encodeHex(DigestUtils.sha1(paramsString), true);
        return result.toUpperCase();
    }

    /*
    对除app_key外的参数进行字典升序排列
     */
    private static String dictSort(HashMap<String, String> map) {
        Collection<String> keySet = map.keySet();
        ArrayList<String> list = new ArrayList<>(keySet);
        Collections.sort(list);
        String result = "";
        for (String item : list) {
            result = result + item + map.get(item);
        }
        return result;
    }

}