package com.twt.service.ui.news.comments;

import com.twt.service.bean.Comment;
import com.twt.service.bean.CommentCallback;
import com.twt.service.bean.RestError;
import com.twt.service.interactor.NewsDetailsInteractor;
import com.twt.service.support.PrefUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/2/14.
 */
public class NewsCommentsPresenterImpl implements NewsCommentsPresenter, OnPostNewsCommentsCallback {

    private NewsCommentsView view;
    private NewsDetailsInteractor interactor;

    public NewsCommentsPresenterImpl(NewsCommentsView view, NewsDetailsInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void postComment(int id, String content, String ip) {
        view.setSendBtnClickable(false);
        view.showProgress();
        String authorization = PrefUtils.getToken();
        interactor.postComment(authorization, id, content, ip);
    }

    @Override
    public void onSuccess(CommentCallback callback) {
        view.hideProgress();
        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comment.ccontent = callback.data.ccontent;
        comment.ctime = callback.data.ctime;
        comment.cuser = callback.data.cuser;
        comments.add(comment);
        view.addComments(comments);
        view.toastMessage("评论成功！");
        view.setSendBtnClickable(true);
        view.clearEditText();
    }

    @Override
    public void onFailure(RetrofitError error) {
        view.hideProgress();
        view.setSendBtnClickable(true);
        switch (error.getKind()) {
            case HTTP:
                RestError restError = (RestError)error.getBodyAs(RestError.class);
                if (restError!=null){
                    view.toastMessage(restError.message);
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
