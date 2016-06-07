package com.twt.service.ui.auth;

import android.content.Context;
import android.text.TextUtils;

import com.twt.service.R;
import com.twt.service.api.ApiSubscriber;
import com.twt.service.api.OnNextListener;
import com.twt.service.api.WePeiYangClient;
import com.twt.service.common.Presenter;
import com.twt.service.model.Token;
import com.twt.service.support.ResourceHelper;

/**
 * Created by huangyong on 16/5/18.
 */
public class AuthPresenter extends Presenter {

    protected AuthViewController mViewController;

    public AuthPresenter(Context context, AuthViewController controller) {
        super(context);
        mViewController = controller;
    }

    public boolean validate(String username, String password) {
        if (TextUtils.isEmpty(username)) {
            mViewController.showUsernameError(ResourceHelper.getString(R.string.empty_error));
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            mViewController.showPasswordError(ResourceHelper.getString(R.string.empty_error));
            return false;
        }
        return true;
    }

    public void submit(String username, String password) {
        String type = ((AuthActivity) mContext).getType();
        if (AuthActivity.AUTH_TWT.equals(type)) {
            login(username, password);
        } else if (AuthActivity.AUTH_TJU.equals(type)) {
            // TODO
        } else if (AuthActivity.ATUH_LIB.equals(type)) {
            // TODO
        }
    }

    protected void login(String username, String password) {
        WePeiYangClient.getInstance().login(mContext, new ApiSubscriber<>(mContext, mOnTokenListener), username, password);
    }

    protected OnNextListener<Token> mOnTokenListener = new OnNextListener<Token>() {
        @Override
        public void onNext(Token token) {

        }
    };
}
