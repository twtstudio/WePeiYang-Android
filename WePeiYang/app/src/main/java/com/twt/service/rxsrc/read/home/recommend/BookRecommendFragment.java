package com.twt.service.rxsrc.read.home.recommend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twt.service.R;
import com.twt.service.rxsrc.common.ui.PFragment;
import com.twt.service.rxsrc.model.read.HomeBanner;
import com.twt.service.rxsrc.model.read.Recommended;
import com.twt.service.rxsrc.model.read.Review;
import com.twt.service.rxsrc.model.read.User;
import com.twt.service.rxsrc.read.bookdetail.BookDetailActivity;
import com.twt.service.rxsrc.read.home.BookReviewAdapter;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerClickListener;

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

        mPresenter.getBanner();

        mRecommendAdapter = new BookRecommendAdapter(getContext());
        mRvRecommend.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRecommendAdapter.hideFooter();
        mRvRecommend.setAdapter(mRecommendAdapter);
        mPresenter.getRecommendedList("5");

        mReviewAdapter = new BookReviewAdapter(getContext());
        mRvReview.setLayoutManager(new LinearLayoutManager(getContext()));
        mReviewAdapter.hideFooter();
        mRvReview.setAdapter(mReviewAdapter);
        mPresenter.getReviewList("2");

        mStarAdapter = new BookStarAdapter(getContext());
        mRvStar.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        mStarAdapter.hideFooter();
        mRvStar.setAdapter(mStarAdapter);
        mPresenter.getStarReaderList("3");
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

    @Override
    public void bindBannerData(List<HomeBanner> banners) {

        List<String> imgs = new ArrayList<>();
        for (HomeBanner banner : banners) {
            imgs.add(banner.img);
        }
        mBanner.setImages(imgs)
                .setImageLoader(new BannerLoader())
                .setDelayTime(4000)
                .start()
                .setOnBannerClickListener(position -> {
                    if (banners.get(position).id != null) {
                        Intent intent = new Intent(getContext(), BookDetailActivity.class);
                        intent.putExtra("id", banners.get(position).id);
                        startActivity(intent);
                    }
                });
    }

    @Override
    public void bindRecommendedData(List<Recommended> recommendeds) {
        mRecommendAdapter.addItems(recommendeds);
    }

    @Override
    public void bindReviewData(List<Review> reviews) {
        mReviewAdapter.addItems(reviews);
    }

    @Override
    public void bindStarReaderData(List<User> users) {
        mStarAdapter.addItems(users);
    }
}
