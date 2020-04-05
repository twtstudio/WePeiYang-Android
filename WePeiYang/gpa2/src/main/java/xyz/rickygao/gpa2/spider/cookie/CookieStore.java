package xyz.rickygao.gpa2.spider.cookie;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Cookie缓存接口
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @blog https://blog.csdn.net/u012527802
 * @desc
 */
public interface CookieStore {

    /**  添加cookie */
    void add(HttpUrl httpUrl, Cookie cookie);

    /** 添加指定httpurl cookie集合 */
    void add(HttpUrl httpUrl, List<Cookie> cookies);

    /** 根据HttpUrl从缓存中读取cookie集合 */
    List<Cookie> get(HttpUrl httpUrl);

    /** 获取全部缓存cookie */
    List<Cookie> getCookies();

    /**  移除指定httpurl cookie集合 */
    boolean remove(HttpUrl httpUrl, Cookie cookie);

    /** 移除所有cookie */
    boolean removeAll();
}