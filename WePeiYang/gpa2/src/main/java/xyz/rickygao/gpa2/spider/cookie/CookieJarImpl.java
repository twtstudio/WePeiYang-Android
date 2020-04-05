package xyz.rickygao.gpa2.spider.cookie;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * CookieJarImpl
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @blog https://blog.csdn.net/u012527802
 * @time 2018/7/20
 * @desc
 */
class CookieJarImpl implements CookieJar {

    private CookieStore cookieStore;
    public CookieJarImpl(CookieStore cookieStore) {
        if(cookieStore == null) {
            throw new IllegalArgumentException("cookieStore can not be null.");
        }
        this.cookieStore = cookieStore;
    }

    @Override
    public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        this.cookieStore.add(url, cookies);
    }

    @Override
    public synchronized List<Cookie> loadForRequest(HttpUrl url) {
        return this.cookieStore.getCookies();
    }

    public CookieStore getCookieStore() {
        return this.cookieStore;
    }
}