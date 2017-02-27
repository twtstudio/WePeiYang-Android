package com.twt.wepeiyang.commons.auth.self;

/**
 * Created by retrox on 2017/2/20.
 */

public class AuthSelfBean {

    /**
     * twtid : 73963
     * twtuname :
     * realname :
     * studentid : 3015204348
     * avatar : https://i.twtstudio.com/img/avatar.png
     * accounts : {"tju":true,"lib":true}
     */

    public int twtid;
    public String twtuname;
    public String realname;
    public String studentid;
    public String avatar;
    public AccountsBean accounts;

    public static class AccountsBean {
        /**
         * tju : true
         * lib : true
         */

        public boolean tju;
        public boolean lib;
    }
}
