package com.twt.wepeiyang.commons.network;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * @deprecated 然而并没有什么卵用
 * Created by retrox on 11/03/2017.
 */

public class CustomResponseBodyConverter<T> implements Converter<ResponseBody,T> {

    private final Gson gson;
    private final TypeAdapter<T> adapter;

    CustomResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String body = value.string();
        try {
            JSONObject jsonObject = new JSONObject(body);
            int errcode = jsonObject.getInt("error_code");
            String message = jsonObject.getString("message");
            if (errcode != -1){
                throw new ApiException(errcode,message);
            }
            return convertDelegate(value);
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            value.close();
        }
        return convertDelegate(value);
    }

    private T convertDelegate(ResponseBody value) throws IOException {
        JsonReader jsonReader = gson.newJsonReader(value.charStream());
        try {
            return adapter.read(jsonReader);
        } finally {
            value.close();
        }
    }
}
