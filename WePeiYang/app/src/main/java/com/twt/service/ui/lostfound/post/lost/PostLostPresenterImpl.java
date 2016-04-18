package com.twt.service.ui.lostfound.post.lost;

import android.util.Log;

import com.twt.service.bean.RestError;
import com.twt.service.interactor.LostInteractor;
import com.twt.service.interactor.TokenRefreshInteractor;
import com.twt.service.interactor.TokenRefreshInteractorImpl;
import com.twt.service.support.PrefUtils;

import retrofit.RetrofitError;

/**
 * Created by Rex on 2015/8/10.
 */
public class PostLostPresenterImpl implements PostLostPresenter {

    private PostLostView view;
    private LostInteractor interactor;


    public PostLostPresenterImpl(PostLostView view, LostInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void postLost(String authorization, String title, String name, String time, String place, String phone, String content, String lost_type) {
        view.showProgress();
        view.setSubmitClickable(false);
        interactor.postLost(authorization, title, name, time, place, phone, content, lost_type, "");
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

    @Override
    public void getLostDetails(int id) {
        interactor.getLostDetails(id);
    }

    @Override
    public void editLost(String authorization, int id,String title, String name, String time, String place, String phone, String content, int lost_type) {
        view.setSubmitClickable(false);
        view.setChangeClickable(false);
        view.showProgress();
        interactor.editLost(authorization,id,title,name,time,place,phone,content,lost_type,"");
    }
}
