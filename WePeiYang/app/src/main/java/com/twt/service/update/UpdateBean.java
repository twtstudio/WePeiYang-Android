package com.twt.service.update;

/**
 * Created by retrox on 31/10/2017.
 */

public class UpdateBean {


    /**
     * success : 1
     * info : {"release":{"id":5,"production":{"id":2,"name":"微北洋","description":"集新闻阅读、GPA 查询、自习室查询、校园公告、失物招领等功能为一体的功能 APP","slogan":"学在北洋，一手掌握","picId":"6"},"pid":"2","content":"1. 修复了几个 Feature； 2. 添加了数个较为严重的 Bug","time":"2017-10-31 16:24:38","version":"3.1.3","versionCode":"13","type":"android","path":"https://mobile-api.twtstudio.com/storage/android_apk/CtrqreeZt0Zkm2PLdfOwDeAkO7b7IPFIuOHts7Um.apk"},"beta":{"id":1,"production":{"id":2,"name":"微北洋","description":"集新闻阅读、GPA 查询、自习室查询、校园公告、失物招领等功能为一体的功能 APP","slogan":"学在北洋，一手掌握","picId":"6"},"pid":"2","content":"[fix]修复主页崩溃和图书馆续借","time":"2017-10-30 21:21:28","version":"3.1.3","versionCode":"13","path":"https://mobile-api.twtstudio.com/storage//storage/android_apk_beta/ZEZjdcemjybQONVvqfc0d07JHuRVhjx50uCqSvTI.apk"}}
     */

    public int success;
    public InfoBean info;

    public static class InfoBean {
        /**
         * release : {"id":5,"production":{"id":2,"name":"微北洋","description":"集新闻阅读、GPA 查询、自习室查询、校园公告、失物招领等功能为一体的功能 APP","slogan":"学在北洋，一手掌握","picId":"6"},"pid":"2","content":"1. 修复了几个 Feature； 2. 添加了数个较为严重的 Bug","time":"2017-10-31 16:24:38","version":"3.1.3","versionCode":"13","type":"android","path":"https://mobile-api.twtstudio.com/storage/android_apk/CtrqreeZt0Zkm2PLdfOwDeAkO7b7IPFIuOHts7Um.apk"}
         * beta : {"id":1,"production":{"id":2,"name":"微北洋","description":"集新闻阅读、GPA 查询、自习室查询、校园公告、失物招领等功能为一体的功能 APP","slogan":"学在北洋，一手掌握","picId":"6"},"pid":"2","content":"[fix]修复主页崩溃和图书馆续借","time":"2017-10-30 21:21:28","version":"3.1.3","versionCode":"13","path":"https://mobile-api.twtstudio.com/storage//storage/android_apk_beta/ZEZjdcemjybQONVvqfc0d07JHuRVhjx50uCqSvTI.apk"}
         */

        public ReleaseBean release;
        public BetaBean beta;

        public static class ReleaseBean {
            /**
             * id : 5
             * production : {"id":2,"name":"微北洋","description":"集新闻阅读、GPA 查询、自习室查询、校园公告、失物招领等功能为一体的功能 APP","slogan":"学在北洋，一手掌握","picId":"6"}
             * pid : 2
             * content : 1. 修复了几个 Feature； 2. 添加了数个较为严重的 Bug
             * time : 2017-10-31 16:24:38
             * version : 3.1.3
             * versionCode : 13
             * type : android
             * path : https://mobile-api.twtstudio.com/storage/android_apk/CtrqreeZt0Zkm2PLdfOwDeAkO7b7IPFIuOHts7Um.apk
             */

            public int id;
            public ProductionBean production;
            public String pid;
            public String content;
            public String time;
            public String version;
            public String versionCode;
            public String type;
            public String path;

            public static class ProductionBean {
                /**
                 * id : 2
                 * name : 微北洋
                 * description : 集新闻阅读、GPA 查询、自习室查询、校园公告、失物招领等功能为一体的功能 APP
                 * slogan : 学在北洋，一手掌握
                 * picId : 6
                 */

                public int id;
                public String name;
                public String description;
                public String slogan;
                public String picId;
            }
        }

        public static class BetaBean {
            /**
             * id : 1
             * production : {"id":2,"name":"微北洋","description":"集新闻阅读、GPA 查询、自习室查询、校园公告、失物招领等功能为一体的功能 APP","slogan":"学在北洋，一手掌握","picId":"6"}
             * pid : 2
             * content : [fix]修复主页崩溃和图书馆续借
             * time : 2017-10-30 21:21:28
             * version : 3.1.3
             * versionCode : 13
             * path : https://mobile-api.twtstudio.com/storage//storage/android_apk_beta/ZEZjdcemjybQONVvqfc0d07JHuRVhjx50uCqSvTI.apk
             */

            public int id;
            public ProductionBeanX production;
            public String pid;
            public String content;
            public String time;
            public String version;
            public String versionCode;
            public String path;

            public static class ProductionBeanX {
                /**
                 * id : 2
                 * name : 微北洋
                 * description : 集新闻阅读、GPA 查询、自习室查询、校园公告、失物招领等功能为一体的功能 APP
                 * slogan : 学在北洋，一手掌握
                 * picId : 6
                 */

                public int id;
                public String name;
                public String description;
                public String slogan;
                public String picId;
            }
        }
    }
}
