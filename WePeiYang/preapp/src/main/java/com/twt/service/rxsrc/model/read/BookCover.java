package com.twt.service.rxsrc.model.read;

import java.util.List;

/**
 * Created by jcy on 2016/11/3.
 */

public class BookCover {

    /**
     * metaResID : null
     * isbn : 9787540458027
     * coverlink : http://img1.doubanio.com/lpic/s27102925.jpg
     * handleTime : 1478170179107
     * fromRes : null
     * status : 0
     */

    public List<ResultBean> result;

    public static class ResultBean {
        public Object metaResID;
        public String isbn;
        public String coverlink;
        public long handleTime;
        public Object fromRes;
        public int status;
    }

}
