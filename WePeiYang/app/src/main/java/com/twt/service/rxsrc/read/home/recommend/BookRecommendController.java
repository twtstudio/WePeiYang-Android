package com.twt.service.rxsrc.read.home.recommend;

import com.twt.service.rxsrc.common.IViewController;
import com.twt.service.rxsrc.model.read.HomeBanner;
import com.twt.service.rxsrc.model.read.Recommended;
import com.twt.service.rxsrc.model.read.Review;
import com.twt.service.rxsrc.model.read.User;

import java.util.List;


/**
 * Created by tjliqy on 2016/10/27.
 */

public interface BookRecommendController extends IViewController{
        void bindBannerData(List<HomeBanner> banners);
        void bindRecommendedData(List<Recommended> recommends);
        void bindReviewData(List<Review> reviews);
        void bindStarReaderData(List<User> users);
}
