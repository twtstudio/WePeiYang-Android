package com.twt.service.ui.gpa.evalution;

import com.google.gson.JsonElement;

/**
 * Created by tjliqy on 2017/1/15.
 */

public interface EvaluateIntercepter {
    void postGpaEvaluate(String authorization, String token, String lessonId, String unionId, String courseId, String term, int[] fiveQ, String note, onPostEvaluateCallBack onPostEvaluateCallBack);
}
