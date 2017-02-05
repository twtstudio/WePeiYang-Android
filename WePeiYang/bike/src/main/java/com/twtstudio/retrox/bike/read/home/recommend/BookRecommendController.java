package com.twtstudio.retrox.bike.read.home.recommend;


import com.twtstudio.retrox.bike.common.IViewController;
import com.twtstudio.retrox.bike.model.read.HomeBanner;
import com.twtstudio.retrox.bike.model.read.Recommended;
import com.twtstudio.retrox.bike.model.read.Review;
import com.twtstudio.retrox.bike.model.read.User;

import java.util.List;


/**
 * Created by tjliqy on 2016/10/27.
 */

public interface BookRecommendController extends IViewController {
        void bindBannerData(List<HomeBanner> banners);
        void bindRecommendedData(List<Recommended> recommends);
        void bindReviewData(List<Review> reviews);
        void bindStarReaderData(List<User> users);
        void onGetTokenSuccess();
}
