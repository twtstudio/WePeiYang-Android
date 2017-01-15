package com.twt.service.ui.gpa.evalution;

import com.google.gson.JsonElement;
import com.twt.service.api.ApiClient;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by tjliqy on 2017/1/15.
 */

public class EvaluateIntercepterImpl implements EvaluateIntercepter{
    @Override
    public void postGpaEvaluate(String authorization,String token, String lessonId, String unionId, String courseId, String term, int[] fiveQ, String note, onPostEvaluateCallBack onPostEvaluateCallBack) {
        ApiClient.postGpaEvaluate(authorization,token, lessonId, unionId, courseId, term, fiveQ, note, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                onPostEvaluateCallBack.onSuccess(jsonElement.toString());
            }

            @Override
            public void failure(RetrofitError error) {
                onPostEvaluateCallBack.onFailure(error);
            }
        });
    }
}
