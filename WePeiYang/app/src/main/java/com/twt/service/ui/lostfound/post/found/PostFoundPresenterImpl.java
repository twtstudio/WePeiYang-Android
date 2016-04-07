package com.twt.service.ui.lostfound.post.found;

import android.util.Log;

import com.twt.service.bean.RestError;
import com.twt.service.interactor.FoundInteractor;
import com.twt.service.interactor.TokenRefreshInteractor;
import com.twt.service.interactor.TokenRefreshInteractorImpl;
import com.twt.service.support.PrefUtils;

import java.io.File;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/3/14.
 */
public class PostFoundPresenterImpl implements PostFoundPresenter {
    private PostFoundView view;
    private FoundInteractor interactor;
    private String title;
    private String name;
    private String time;
    private String place;
    private String phone;
    private String content;
    private String found_pic;

    public PostFoundPresenterImpl(PostFoundView view, FoundInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void postFound(String authorization, String title, String name, String time, String place, String phone, String content, File uploadImage) {
        this.title = title;
        this.name = name;
        this.time = time;
        this.place = place;
        this.phone = phone;
        this.content = content;
        view.setSubmitClickable(false);
        if (found_pic == null || found_pic.isEmpty()) {
            uploadImage(uploadImage);
        } else {
            interactor.postFound(authorization, title, name, time, place, phone, content, found_pic);
        }
    }


    @Override
    public void uploadImage(File uploadImage) {
        view.showProgress();
        interactor.uploadImage(uploadImage);
    }

    @Override
    public void postFinally(String found_pic) {
        interactor.postFound(PrefUtils.getToken(), title, name, time, place, phone, content, found_pic);
    }

    @Override
    public void getFoundDetails(int id) {
        interactor.getFoundDetails(id);
    }

    @Override
    public void onSuccess() {
        view.hideProgress();
        view.toastMessage("发布成功");
        view.finishActivity();
    }

    @Override
    public void onFailure(RetrofitError error) {
        view.hideProgress();
        view.setSubmitClickable(true);
        switch (error.getKind()) {
            case HTTP:
                RestError restError = (RestError) error.getBodyAs(RestError.class);
                if (restError != null) {
                    switch (restError.error_code) {
                        case 10000:
                            view.toastMessage("请重新登录");
                            PrefUtils.setLogin(false);
                            PrefUtils.removeToken();
                            view.startLoginActivity();
                            break;
                        case 10001:
                            view.toastMessage("请重新登录");
                            PrefUtils.setLogin(false);
                            PrefUtils.removeToken();
                            view.startLoginActivity();
                            break;
                        case 10002:
                            view.toastMessage("请重新登录");
                            PrefUtils.setLogin(false);
                            PrefUtils.removeToken();
                            view.startLoginActivity();
                            break;
                        case 10003:
                            Log.e("refresh", "refresh");
                            TokenRefreshInteractor tokenRefreshInteractor = new TokenRefreshInteractorImpl();
                            tokenRefreshInteractor.refreshToken(PrefUtils.getToken());
                            break;
                        default:
                            view.toastMessage(restError.message);
                            break;
                    }
                }
                break;
            case NETWORK:
                view.toastMessage("无法连接到网络");
                break;
            case CONVERSION:
            case UNEXPECTED:
                throw error;
            default:
                throw new AssertionError("未知的错误类型：" + error.getKind());
        }
    }
}
