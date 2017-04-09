package com.twtstudio.retrox.news.api.bean;

import com.twt.wepeiyang.commons.network.ApiException;

import java.util.List;

/**
 * Created by retrox on 26/02/2017.
 */

public class HomeNewsBean {

    /**
     * error_code : -1
     * message :
     * data : {"carousel":[{"index":53349,"pic":"http://news.twt.edu.cn/public/news/wyynews/420_2017_02_25_20_01_24_cover_298.jpg","subject":"诺奖在天大：聆听诺奖大师 感悟科技人生"},{"index":53345,"pic":"http://news.twt.edu.cn/public/news/wyynews/420_2017_02_23_13_36_44_cover_283.jpg","subject":"【对话诺奖大师 体验科学人生】诺奖大师与高中生面对面"},{"index":53332,"pic":"http://news.twt.edu.cn/public/news/wyynews/420_2017_01_15_01_00_50_cover_280.jpg","subject":"北洋骊歌：2017年研究生毕业典礼"},{"index":53331,"pic":"http://news.twt.edu.cn/public/news/wyynews/420_2017_01_16_12_04_37_cover_277.jpg","subject":"【图集】天津大学2017届研究生毕业典礼及暨学位授予仪式"},{"index":53325,"pic":"http://news.twt.edu.cn/public/news/wyynews/420_2016_12_28_20_30_23_cover_307.jpg","subject":"【图集】自信时代 燃电花火：自动化学院2016级迎新晚会"}],"news":{"campus":[{"index":53349,"subject":"诺奖在天大：聆听诺奖大师 感悟科技人生","pic":"http://news.twt.edu.cn/public/news/wyynews/420_2017_02_25_20_01_24_cover_298.jpg","addat":"02/25","visitcount":316},{"index":53345,"subject":"【对话诺奖大师 体验科学人生】诺奖大师与高中生面对面","pic":"http://news.twt.edu.cn/public/news/wyynews/420_2017_02_23_13_36_44_cover_283.jpg","addat":"02/23","visitcount":436},{"index":53332,"subject":"北洋骊歌：2017年研究生毕业典礼","pic":"http://news.twt.edu.cn/public/news/wyynews/420_2017_01_15_01_00_50_cover_280.jpg","addat":"01/15","visitcount":3088}],"annoucements":[{"index":53348,"subject":"【卫津路校区讲座预告】中国路上的轰鸣与奔腾","addat":"02/24","gonggao":"天津大学青年文化促进会","brief":"【讲座主题】中国路上的轰鸣与奔腾【讲座时间】2017年2月28日 14:30【讲"},{"index":53344,"subject":"天津大学2016-2017学年第二批兼职辅导员选聘工作通知","addat":"02/20","gonggao":"学工部","brief":"各位同学：根据学校相关工作安排和实际需求，现面向全校同学公开招募天津大学2016"},{"index":53343,"subject":"关于新校区游泳馆恢复开放的通知","addat":"02/19","gonggao":"场馆中心","brief":"各位师生：游泳馆供热设备已经维修完毕，水温已达到正常，新校区游泳馆将从2017年"}],"jobs":[{"id":46676,"title":"天津水泥工业设计研究院有限公司2017年校园招聘","date":"02/24"},{"id":46675,"title":"中国建筑股份有限公司2017届校园双选会启事","date":"02/24"},{"id":46674,"title":"中铁五局集团海外分公司招聘简章","date":"02/24"}]},"service":{"lost":[{"id":883,"name":"白n","title":"钥匙","place":"图书馆或桃园餐厅","time":"2017/1/5","phone":"13312053310","lost_type":3},{"id":882,"name":"白n","title":"钥匙","place":"图书馆或桃园餐厅","time":"2017/1/5","phone":"13312053310","lost_type":3},{"id":881,"name":"白n","title":"钥匙","place":"图书馆或桃园餐厅","time":"2017/1/5","phone":"13312053310","lost_type":3}],"found":[{"id":876,"name":"钟元旦","title":"自行车卡","place":"诚园七斋东侧出口处","time":"1487721002","phone":"13102232590","found_pic":""},{"id":875,"name":"李同学","title":"在天大老校区捡到身份证一张","place":"卫津路校区建筑学院南路边","time":"1487684758","phone":"13752730365","found_pic":""},{"id":869,"name":"李梦屿","title":"捡到银行卡","place":"诚园旁建行自助存取款机","time":"1487479223","phone":"13302107976","found_pic":""}]}}
     */

    public int error_code;
    public String message;
    public DataBean data;

    public static class DataBean {
        /**
         * carousel : [{"index":53349,"pic":"http://news.twt.edu.cn/public/news/wyynews/420_2017_02_25_20_01_24_cover_298.jpg","subject":"诺奖在天大：聆听诺奖大师 感悟科技人生"},{"index":53345,"pic":"http://news.twt.edu.cn/public/news/wyynews/420_2017_02_23_13_36_44_cover_283.jpg","subject":"【对话诺奖大师 体验科学人生】诺奖大师与高中生面对面"},{"index":53332,"pic":"http://news.twt.edu.cn/public/news/wyynews/420_2017_01_15_01_00_50_cover_280.jpg","subject":"北洋骊歌：2017年研究生毕业典礼"},{"index":53331,"pic":"http://news.twt.edu.cn/public/news/wyynews/420_2017_01_16_12_04_37_cover_277.jpg","subject":"【图集】天津大学2017届研究生毕业典礼及暨学位授予仪式"},{"index":53325,"pic":"http://news.twt.edu.cn/public/news/wyynews/420_2016_12_28_20_30_23_cover_307.jpg","subject":"【图集】自信时代 燃电花火：自动化学院2016级迎新晚会"}]
         * news : {"campus":[{"index":53349,"subject":"诺奖在天大：聆听诺奖大师 感悟科技人生","pic":"http://news.twt.edu.cn/public/news/wyynews/420_2017_02_25_20_01_24_cover_298.jpg","addat":"02/25","visitcount":316},{"index":53345,"subject":"【对话诺奖大师 体验科学人生】诺奖大师与高中生面对面","pic":"http://news.twt.edu.cn/public/news/wyynews/420_2017_02_23_13_36_44_cover_283.jpg","addat":"02/23","visitcount":436},{"index":53332,"subject":"北洋骊歌：2017年研究生毕业典礼","pic":"http://news.twt.edu.cn/public/news/wyynews/420_2017_01_15_01_00_50_cover_280.jpg","addat":"01/15","visitcount":3088}],"annoucements":[{"index":53348,"subject":"【卫津路校区讲座预告】中国路上的轰鸣与奔腾","addat":"02/24","gonggao":"天津大学青年文化促进会","brief":"【讲座主题】中国路上的轰鸣与奔腾【讲座时间】2017年2月28日 14:30【讲"},{"index":53344,"subject":"天津大学2016-2017学年第二批兼职辅导员选聘工作通知","addat":"02/20","gonggao":"学工部","brief":"各位同学：根据学校相关工作安排和实际需求，现面向全校同学公开招募天津大学2016"},{"index":53343,"subject":"关于新校区游泳馆恢复开放的通知","addat":"02/19","gonggao":"场馆中心","brief":"各位师生：游泳馆供热设备已经维修完毕，水温已达到正常，新校区游泳馆将从2017年"}],"jobs":[{"id":46676,"title":"天津水泥工业设计研究院有限公司2017年校园招聘","date":"02/24"},{"id":46675,"title":"中国建筑股份有限公司2017届校园双选会启事","date":"02/24"},{"id":46674,"title":"中铁五局集团海外分公司招聘简章","date":"02/24"}]}
         * service : {"lost":[{"id":883,"name":"白n","title":"钥匙","place":"图书馆或桃园餐厅","time":"2017/1/5","phone":"13312053310","lost_type":3},{"id":882,"name":"白n","title":"钥匙","place":"图书馆或桃园餐厅","time":"2017/1/5","phone":"13312053310","lost_type":3},{"id":881,"name":"白n","title":"钥匙","place":"图书馆或桃园餐厅","time":"2017/1/5","phone":"13312053310","lost_type":3}],"found":[{"id":876,"name":"钟元旦","title":"自行车卡","place":"诚园七斋东侧出口处","time":"1487721002","phone":"13102232590","found_pic":""},{"id":875,"name":"李同学","title":"在天大老校区捡到身份证一张","place":"卫津路校区建筑学院南路边","time":"1487684758","phone":"13752730365","found_pic":""},{"id":869,"name":"李梦屿","title":"捡到银行卡","place":"诚园旁建行自助存取款机","time":"1487479223","phone":"13302107976","found_pic":""}]}
         */

        public NewsBean news;
        public ServiceBean service;
        public List<CarouselBean> carousel;

        public static class NewsBean {
            public List<CampusBean> campus;
            public List<AnnoucementsBean> annoucements;
            public List<JobsBean> jobs;

            public static class CampusBean {
                /**
                 * index : 53349
                 * subject : 诺奖在天大：聆听诺奖大师 感悟科技人生
                 * pic : http://news.twt.edu.cn/public/news/wyynews/420_2017_02_25_20_01_24_cover_298.jpg
                 * addat : 02/25
                 * visitcount : 316
                 */

                public int index;
                public String subject;
                public String pic;
                public String addat;
                public int visitcount;
            }

            public static class AnnoucementsBean {
                /**
                 * index : 53348
                 * subject : 【卫津路校区讲座预告】中国路上的轰鸣与奔腾
                 * addat : 02/24
                 * gonggao : 天津大学青年文化促进会
                 * brief : 【讲座主题】中国路上的轰鸣与奔腾【讲座时间】2017年2月28日 14:30【讲
                 */

                public int index;
                public String subject;
                public String addat;
                public String gonggao;
                public String brief;
            }

            public static class JobsBean {
                /**
                 * id : 46676
                 * title : 天津水泥工业设计研究院有限公司2017年校园招聘
                 * date : 02/24
                 */

                public int id;
                public String title;
                public String date;
            }
        }

        public static class ServiceBean {
            public List<LostBean> lost;
            public List<FoundBean> found;

            public static class LostBean {
                /**
                 * id : 883
                 * name : 白n
                 * title : 钥匙
                 * place : 图书馆或桃园餐厅
                 * time : 2017/1/5
                 * phone : 13312053310
                 * lost_type : 3
                 */

                public int id;
                public String name;
                public String title;
                public String place;
                public String time;
                public String phone;
                public int lost_type;
            }

            public static class FoundBean {
                /**
                 * id : 876
                 * name : 钟元旦
                 * title : 自行车卡
                 * place : 诚园七斋东侧出口处
                 * time : 1487721002
                 * phone : 13102232590
                 * found_pic :
                 */

                public int id;
                public String name;
                public String title;
                public String place;
                public String time;
                public String phone;
                public String found_pic;
            }
        }

        public static class CarouselBean {
            /**
             * index : 53349
             * pic : http://news.twt.edu.cn/public/news/wyynews/420_2017_02_25_20_01_24_cover_298.jpg
             * subject : 诺奖在天大：聆听诺奖大师 感悟科技人生
             */

            public int index;
            public String pic;
            public String subject;
        }
    }

    public DataBean getData() {
        if (error_code==-1){
            return data;
        }else {
            throw new ApiException(error_code,message);
        }

    }
}
