package com.rex.wepeiyang.api;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sunjuntao on 15/11/5.
 */
public class Sign {
    public static String generate(HashMap<String, String> params) {
        HashMap<String, String> sortedParams = dictSort(params);//获取字典排序后的参数列表
        String paramsString = "";
        for (HashMap.Entry<String, String> entry : sortedParams.entrySet()) {
            paramsString += entry.getKey() + entry.getValue();
        }
        paramsString = Constant.APPKEY + paramsString + Constant.APPSECRET;
        return DigestUtils.sha1Hex(paramsString).toUpperCase();
    }

    /*
    对参数进行字典排序的方法
     */
    private static HashMap<String, String> dictSort(HashMap<String, String> map) {
        Collection<String> keySet = map.keySet();
        List<String> list = new ArrayList<>(keySet);
        Collections.sort(list);
        HashMap<String, String> result = new HashMap<>();
        for (String item : list) {
            result.put(item, map.get(item));
        }
        return result;
    }
}
