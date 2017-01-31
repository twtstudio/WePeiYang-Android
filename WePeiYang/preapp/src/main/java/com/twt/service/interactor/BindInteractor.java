package com.twt.service.interactor;

import com.twt.service.ui.bind.OnBindCallback;

/**
 * Created by sunjuntao on 16/1/1.
 */
public interface BindInteractor {
    void bind(String authorization,String tjuname, String tjupwd, OnBindCallback onBindCallback);
}
