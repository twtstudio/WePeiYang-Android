package com.twtstudio.retrox.auth.login;

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
    public int dropout; //0=未操作，1=已退学，2=已复学

    public static class AccountsBean {
        /**
         * tju : true
         * lib : true
         */

        public boolean tju;
        public boolean lib;
    }
}
