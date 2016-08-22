package com.twt.service.party.interactor;

import com.twt.service.party.ui.submit.detail.OnSubmitCallBack;

/**
 * Created by tjliqy on 2016/8/22.
 */
public interface SubmitInteractor {
    void submit(String title, String content, String type, OnSubmitCallBack callBack);
}
