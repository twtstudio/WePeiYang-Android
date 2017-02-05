package com.twtstudio.retrox.bike.model;

/**
 * Created by jcy on 16-11-2.
 *
 * @TwtStudio Mobile Develope Team
 */

public class LatestVersion {

    /**
     * version : 2.2.1
     * timestamp : 2000000000
     * url : http://mobile.twt.edu.cn/wpy
     */

    public LatestBean latest;

    public static class LatestBean {
        public String version;
        public String timestamp;
        public String url;
        public String message;
    }
}
