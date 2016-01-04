package com.rex.wepeiyang.interactor;

import com.rex.wepeiyang.ui.bind.OnBindCallback;

/**
 * Created by sunjuntao on 16/1/1.
 */
public interface BindInteractor {
    void bind(String authorization,String tjuname, String tjupwd, OnBindCallback onBindCallback);
}
