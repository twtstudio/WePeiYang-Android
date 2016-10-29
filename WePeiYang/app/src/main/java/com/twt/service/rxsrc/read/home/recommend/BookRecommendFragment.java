package com.twt.service.rxsrc.read.home.recommend;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twt.service.R;
import com.twt.service.rxsrc.common.ui.PFragment;
import com.twt.service.rxsrc.model.read.Recommended;
import com.twt.service.rxsrc.model.read.Review;
import com.twt.service.rxsrc.model.read.User;
import com.twt.service.rxsrc.read.home.BookReviewAdapter;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by tjliqy on 2016/10/28.
 */

public class BookRecommendFragment extends PFragment<BookRecommendPresenter> implements BookRecommendController {


    @InjectView(R.id.rv_recommend)
    RecyclerView mRvRecommend;
    @InjectView(R.id.rv_review)
    RecyclerView mRvReview;
    @InjectView(R.id.rv_star)
    RecyclerView mRvStar;
    @InjectView(R.id.banner)
    Banner mBanner;


    BookRecommendAdapter mRecommendAdapter;
    BookReviewAdapter mReviewAdapter;
    BookStarAdapter mStarAdapter;
    @Override
    protected int getLayout() {
        return R.layout.fragment_book_recommend;
    }

    @Override
    protected void initView() {
        List<Integer> images = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            images.add(R.mipmap.test_banner);
        }
        mBanner.setImages(images)
                .setImageLoader(new BannerLoader())
                .setDelayTime(4000)
                .start();

        mRecommendAdapter = new BookRecommendAdapter(getContext());
        mRvRecommend.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        mRecommendAdapter.hideFooter();
        mRvRecommend.setAdapter(mRecommendAdapter);


        mReviewAdapter = new BookReviewAdapter(getContext());
        mRvReview.setLayoutManager(new LinearLayoutManager(getContext()));
        mReviewAdapter.hideFooter();
        mRvReview.setAdapter(mReviewAdapter);

        mStarAdapter = new BookStarAdapter(getContext());
        mRvStar.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        mStarAdapter.hideFooter();
        mRvStar.setAdapter(mStarAdapter);

        // TODO: 2016/10/28 测试代码，后台接口有了就删掉！
        List<Recommended> recommendeds = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            recommendeds.add(new Recommended());
        }
        mRecommendAdapter.addItems(recommendeds);

        List<Review> reviews = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            reviews.add(new Review());
        }
        mReviewAdapter.addItems(reviews);

        List<User> users = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            users.add(new User());
        }
        mStarAdapter.addItems(users);
    }

    @Override
    protected BookRecommendPresenter getPresenter() {
        return new BookRecommendPresenter(getContext(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
