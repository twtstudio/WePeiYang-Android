package com.twt.service.api;

import com.twt.service.bean.User;

/**
 * Created by huangyong on 16/5/18.
 */
public class AuthManager {

    private static final Object LOCK = new Object();

    private static AuthManager sInstance;

    protected User mUser;

    protected boolean isLogin = false;

    private AuthManager() {

    }

    public static AuthManager getInstance() {
        synchronized (LOCK) {
            if (sInstance == null) {
                sInstance = new AuthManager();
            }
            return sInstance;
        }
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void login(User user) {

    }

    public void update(User user) {

    }

    public void logout() {

    }

    public User getUser() {
        return mUser;
    }

}
