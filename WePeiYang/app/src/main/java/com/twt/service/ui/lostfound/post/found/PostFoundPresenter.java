package com.twt.service.ui.lostfound.post.found;

import java.io.File;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/3/14.
 */
public interface PostFoundPresenter {
    void postFound(String authorization, String title, String name, String time, String place, String phone, String content, File uploadImage);

    void uploadImage(File uploadImage);

    void postFinally(String found_pic);

    void getFoundDetails(int id);

    void onSuccess();

    void onFailure(RetrofitError error);
}
