package com.twt.service.api;

import com.twt.service.JniUtils;

import org.apache.commons.codec.binary.Hex;
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
        JniUtils jniUtils = JniUtils.getInstance();
        paramsString = jniUtils.getAppKey() + paramsString + jniUtils.getAppSecret();
        String result = new String(Hex.encodeHex(DigestUtils.sha1(paramsString)));
        return result.toUpperCase();
    }

    /*
    对除app_key外的参数进行字典升序排列
     */
    private String dictSort(HashMap<String, String> map) {
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